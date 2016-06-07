package uk.gov.digital.ho.proving.financial.api;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.digital.ho.proving.financial.ServiceRunner;
import uk.gov.digital.ho.proving.financial.domain.Transaction;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServiceRunner.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@PropertySource("classpath:application.properties")
public class ServiceIntegrationTest {

    public static final String TR_ONE_DESC = "employer payment";
    public static final String TR_ONE_AMOUNT = "2000";
    public static final String TR_ONE_BALANCE = "3000";
    private static Logger LOGGER = LoggerFactory.getLogger(ServiceIntegrationTest.class);

    public static final String SORT_CODE = "123456";
    public static final String ACCOUNT_NUMBER = "12345678";
    public static final String ACCOUNT_NUMBER_NOT_MATCHED = "00000009";
    @Value("${local.server.port}")
    private int port;

    final String PATH = "/financialstatus/v1/transactions";

    final String PATH_ACCOUNT = "/financialstatus/v1/transactions/{sortcode}/{account}";
    public static final LocalDate TR_FROM_DATE = LocalDate.parse("2014-03-25");
    public static final LocalDate TR_TO_DATE = LocalDate.parse("2016-03-25");
    public static final LocalDate TR_ONE_DATE = LocalDate.parse("2015-10-08");

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        restTemplate = new TestRestTemplate();
        createNonDemoTestData();
    }

    @After
    public void tearDown() {
        clearTestSpecificData();
    }


    @Test
    public void setupAndTearDownTestData() throws Exception {
        verifyNumberOfPersistedStatements(3);

        clearTestSpecificData();

        verifyNumberOfPersistedStatements(2);
    }

    @Test
    public void createTestData_invalidJson() throws Exception {
        clearTestSpecificData();

        final ResponseEntity<String> response = createInvalidJsonNonDemoTestData();

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    public void getTransactionsByAccountAndDateRange() throws Exception {

        URI targetUrl = buildStatementSearchByDateUri(ACCOUNT_NUMBER);

        ResponseEntity<StatementResponse> testStatement = restTemplate.getForEntity(targetUrl, StatementResponse.class);

        assertThat(testStatement.getBody().getTransactions()).hasSize(1).contains(new Transaction(TR_ONE_DATE, TR_ONE_DESC, TR_ONE_AMOUNT, TR_ONE_BALANCE));
    }

    @Test
    public void getTransactionsByAccountAndDateRange_accountNotFound() throws Exception {

        URI targetUrl = buildStatementSearchByDateUri(ACCOUNT_NUMBER_NOT_MATCHED);

        ResponseEntity<StatementResponse> response = restTemplate.getForEntity(targetUrl, StatementResponse.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
    }

    //todo if required
    public void getTransactionsByAccount() throws Exception {

        URI targetUrl = buildStatementSearchByDateUri(ACCOUNT_NUMBER);

        ResponseEntity<StatementResponse> testStatement = restTemplate.getForEntity(targetUrl, StatementResponse.class);

        assertThat(testStatement.getBody().getTransactions()).hasSize(4).contains(new Transaction(TR_ONE_DATE, TR_ONE_DESC, TR_ONE_AMOUNT, TR_ONE_BALANCE));
    }

    private URI buildStatementSearchByDateUri(String accountNumber) {
        return UriComponentsBuilder.fromUriString(getBaseUrl())
                    .path(PATH_ACCOUNT)
                    .queryParam("fromDate", TR_FROM_DATE)
                    .queryParam("toDate", TR_TO_DATE)
                    .buildAndExpand(SORT_CODE, accountNumber)
                    .toUri();
    }


    private void clearTestSpecificData() {
        restTemplate.delete(getBaseUrl()+PATH,StatementResponse.class);
    }

    private void verifyNumberOfPersistedStatements(int expected) {
        ResponseEntity<StatementListResponse> allData = restTemplate.getForEntity(getBaseUrl()+PATH, StatementListResponse.class);
        assertThat(allData.getBody().getStatements()).hasSize(expected);
    }

    private void createNonDemoTestData() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        final HttpEntity<String> requestEntity = new HttpEntity<>(getNonDemoData("serviceIntegrationTest.json"), headers);
        restTemplate.postForEntity(getBaseUrl()+PATH, requestEntity, String.class);
    }

    private ResponseEntity<String> createInvalidJsonNonDemoTestData() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        final HttpEntity<String> requestEntity = new HttpEntity<>(getNonDemoData("serviceIntegrationTest_invalidJson.json"), headers);
        return restTemplate.postForEntity(getBaseUrl() + PATH, requestEntity, String.class);
    }


    private String getNonDemoData(String file){
        String data = null;
        try {
            URI uri;
            uri = ServiceIntegrationTest.class.getClassLoader().getResource(file).toURI();
            data =  new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("utf-8"));
        } catch (URISyntaxException | IOException e) {
            LOGGER.error("Problem reading resource file",e);
        }
        return data;
    }

}
