package uk.gov.digital.ho.proving.financial.api

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.TestRestTemplate
import org.springframework.context.annotation.PropertySource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Ignore
import spock.lang.Specification
import uk.gov.digital.ho.proving.financial.ServiceRunner
import uk.gov.digital.ho.proving.financial.domain.BalanceRecord

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate

@SpringApplicationConfiguration(classes = ServiceRunner.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@PropertySource("classpath:application.properties")
@Ignore
class ServiceIntegrationSpec extends Specification{

    static final String TR_ONE_BALANCE = "3000"
    private static Logger LOGGER = LoggerFactory.getLogger(ServiceIntegrationSpec.class)

    static final String SORT_CODE = "123456"
    static final String ACCOUNT_NUMBER = "12345678"
    static final String ACCOUNT_NUMBER_NOT_MATCHED = "00000009"

    @Value('${local.server.port}')
    private int port

    final String PATH_ROOT = "/financialstatus/v1"

    final String PATH_ALL_ACCOUNTS = PATH_ROOT + "/accounts"

    final String PATH_ACCOUNT = PATH_ROOT + "/{sortcode}/{account}/balances"
    static final LocalDate TR_FROM_DATE = LocalDate.parse("2014-03-25")
    static final LocalDate TR_TO_DATE = LocalDate.parse("2016-03-25")
    static final LocalDate TR_ONE_DATE = LocalDate.parse("2015-10-08")

    private String getBaseUrl() {
        return "http://localhost:" + port
    }

    private RestTemplate restTemplate

    void setup() {
        restTemplate = new TestRestTemplate()
    }

    void cleanup() {
        clearTestSpecificData()
    }


    def "set up test data"() {

        given:
        def countBeforeTestDataCreated = getNumberOfPersistedBalanceSummaries()

        when:
        createNonDemoTestData()

        then: "test data should include static data plus 1"
        verifyNumberOfPersistedBalanceSummaries(countBeforeTestDataCreated+1)
    }

    def "tear down test data"() {

        given:
        createNonDemoTestData()
        def countAfterTestDataCreated = getNumberOfPersistedBalanceSummaries()

        when:
        clearTestSpecificData()

        then: "only static demo data should remain"
        verifyNumberOfPersistedBalanceSummaries(countAfterTestDataCreated-1)
    }

    def "create test data for existing account should be rejected"() {
        createNonDemoTestData()
        def countAfterTestDataCreated = getNumberOfPersistedBalanceSummaries()

        when:
        ResponseEntity<BalanceSummaryResponse> response =createNonDemoTestData()

        then:
        response.statusCode.value() == 500
        response.getBody().contains("Error persisting testData test data: Unique contraint for sortcode and account number violated. Clear existing test data to allow insert.")
        verifyNumberOfPersistedBalanceSummaries(countAfterTestDataCreated)
    }

    def "create test data with invalid json should result in bad request"() {
        createNonDemoTestData()

        when:
        def response = createInvalidJsonNonDemoTestData()

        then:
        response.statusCode.value() == 400
    }

    def "get balance records by account number and date range"() {
        URI targetUrl = buildStatementSearchByDateUri(ACCOUNT_NUMBER)
        createNonDemoTestData()

        when: "api invoked with valid account number and date range"
        ResponseEntity<BalanceSummaryResponse> data = restTemplate.getForEntity(targetUrl, BalanceSummaryResponse.class)

        then:
        data.getBody().getBalanceRecords().size() == 1
        data.getBody().getBalanceRecords().each {it == new BalanceRecord(TR_ONE_DATE, TR_ONE_BALANCE)}
    }

    def "get balance records  - account doesn't exist"() {
        URI targetUrl = buildStatementSearchByDateUri(ACCOUNT_NUMBER_NOT_MATCHED)
        createNonDemoTestData()

        when: "api invoked with non-existant account number and date range"
        ResponseEntity<BalanceSummaryResponse> response = restTemplate.getForEntity(targetUrl, BalanceSummaryResponse.class)

        then:
        response.statusCode.value() == 404
    }


    private URI buildStatementSearchByDateUri(String accountNumber) {
        return UriComponentsBuilder.fromUriString(getBaseUrl())
                    .path(PATH_ACCOUNT)
                    .queryParam("fromDate", TR_FROM_DATE)
                    .queryParam("toDate", TR_TO_DATE)
                    .buildAndExpand(SORT_CODE, accountNumber)
                    .toUri()
    }


    private void clearTestSpecificData() {
        restTemplate.delete(getBaseUrl()+PATH_ALL_ACCOUNTS,BalanceSummaryResponse.class)
    }

    private void verifyNumberOfPersistedBalanceSummaries(int expected) {
        ResponseEntity<BalanceSummariesResponse> allData = restTemplate.getForEntity(getBaseUrl()+PATH_ALL_ACCOUNTS, BalanceSummariesResponse.class)
        assert allData.getBody().getBalanceSummaries().size() == expected
    }

    private int getNumberOfPersistedBalanceSummaries() {
        restTemplate.getForEntity(getBaseUrl()+PATH_ALL_ACCOUNTS, BalanceSummariesResponse.class).body.balanceSummaries.size()
    }

    def createNonDemoTestData() {
        final HttpHeaders headers = new HttpHeaders()
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        final HttpEntity<String> requestEntity = new HttpEntity<>(getNonDemoData("serviceIntegrationTest.json"), headers)
        restTemplate.postForEntity(getBaseUrl()+PATH_ALL_ACCOUNTS, requestEntity, String.class)
    }

    private ResponseEntity<String> createInvalidJsonNonDemoTestData() {
        final HttpHeaders headers = new HttpHeaders()
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        final HttpEntity<String> requestEntity = new HttpEntity<>(getNonDemoData("serviceIntegrationTest_invalidJson.json"), headers)
        return restTemplate.postForEntity(getBaseUrl() + PATH_ALL_ACCOUNTS, requestEntity, String.class)
    }


    private static String getNonDemoData(String file){
        String data = null
        try {
            URI uri
            uri = ServiceIntegrationSpec.class.getClassLoader().getResource(file).toURI()
            data =  new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("utf-8"))
        } catch (URISyntaxException | IOException e) {
            LOGGER.error("Problem reading resource file",e)
        }
        return data
    }

}
