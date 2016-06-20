package uk.gov.digital.ho.proving.financial.api

import com.mongodb.MongoException
import groovy.json.JsonSlurper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import uk.gov.digital.ho.proving.financial.ServiceConfiguration
import uk.gov.digital.ho.proving.financial.domain.BalanceRecord
import uk.gov.digital.ho.proving.financial.domain.BalanceSummary
import uk.gov.digital.ho.proving.financial.exception.AccountNotFoundException

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class ServiceSpec extends Specification {


    public static final String MIN_BALANCE = "2000"
    DataService dataMock = Mock(DataService)

    Service service = new Service()
    MockMvc mockMvc = standaloneSetup(service).build()

    final String PATH = "/financialstatus/v1/accounts"

    final String PATH_ACCOUNT = "/financialstatus/v1/{sortcode}/{account}/balances"

    static final String SORT_CODE = "123456"
    static final String ACCOUNT_NUMBER = "12345678"
    static final LocalDate TR_DATE = LocalDate.parse("2014-03-25")

    def setup() {
        service.dataService = dataMock
    }


    def "valid account number and sortcode is queried"() {
        given:
        1 * dataMock.getStatement(_, _, _ , _) >> createBalanceSummary()

        when:
        def response = mockMvc.perform(get(PATH_ACCOUNT, SORT_CODE, ACCOUNT_NUMBER).param("fromDate", "2015-10-10").param("toDate", "2016-05-10"))

        then:
        def jsonContent = new JsonSlurper().parseText(response.andReturn().response.getContentAsString())
        response.andExpect(status().isOk())
        jsonContent.balanceRecords[0].balance == "2000"
    }

    def "bank balance test data is persisted"() {
        given:
        1 * dataMock.saveTestData(createBalanceSummaryNoNameDetails())

        when:
        def json = ServiceConfiguration.newInstance().mapper.writeValueAsString(createBalanceSummaryNoNameDetails().getBalanceRecords())
        def response = mockMvc.perform(post(PATH_ACCOUNT, SORT_CODE, ACCOUNT_NUMBER).contentType(MediaType.APPLICATION_JSON)
                .content(json))


        then:
        response.andExpect(status().isOk())
    }

    def "bank summary test data is persisted  (includes account data in request body)"() {
        given:
        1 * dataMock.saveTestData(createBalanceSummary())

        when:
        def json = ServiceConfiguration.newInstance().mapper.writeValueAsString(createBalanceSummary())
        def response = mockMvc.perform(post(PATH).contentType(MediaType.APPLICATION_JSON)
                .content(json))

        then:
        response.andExpect(status().isOk())
    }

    def "non-existant account is queried - not found"() {
        given:
        1 * dataMock.getStatement(_, _, _, _) >> { throw new AccountNotFoundException() }

        when:
        def response = mockMvc.perform(get(PATH_ACCOUNT, SORT_CODE, ACCOUNT_NUMBER).param("fromDate", "2015-10-10").param("toDate", "2016-05-10"))

        then:
        response.andExpect(status().isNotFound())
    }

    def "invalid to date is rejected"() {
        when:
        def response = mockMvc.perform(get(PATH_ACCOUNT, SORT_CODE, ACCOUNT_NUMBER).param("fromDate", "2015-10-10").param("toDate", "2016-05-1000"))

        then:
        def jsonContent = new JsonSlurper().parseText(response.andReturn().response.getContentAsString())
        response.andExpect(status().isBadRequest())
        jsonContent.status.message == "Parameter error: To date is invalid"
    }

    def "invalid from date is rejected"() {
        when:
        def response = mockMvc.perform(get(PATH_ACCOUNT, SORT_CODE, ACCOUNT_NUMBER).param("fromDate", "20155-10-10").param("toDate", "2016-05-10"))

        then:
        def jsonContent = new JsonSlurper().parseText(response.andReturn().response.getContentAsString())
        response.andExpect(status().isBadRequest())
        jsonContent.status.message == "Parameter error: From date is invalid"
    }

    def "error writing to database"() {
        given:
        1 * dataMock.saveTestData(createBalanceSummaryNoNameDetails()) >> { throw new MongoException("a message") }

        when:
        def json = ServiceConfiguration.newInstance().mapper.writeValueAsString(createBalanceSummaryNoNameDetails().getBalanceRecords())
        def response = mockMvc.perform(post(PATH_ACCOUNT, SORT_CODE, ACCOUNT_NUMBER).contentType(MediaType.APPLICATION_JSON)
                .content(json))

        then:
        response.andExpect(status().isInternalServerError())
    }

    def "reject invalid test data - bad request"() {
        given:
        0 * dataMock.saveTestData(createBalanceSummaryNoNameDetails())

        when:
        def invalidjson = "some rubbish at the top" + ServiceConfiguration.newInstance().mapper.writeValueAsString(createBalanceSummaryNoNameDetails().getBalanceRecords())

        def response = mockMvc.perform(post(PATH_ACCOUNT, SORT_CODE, ACCOUNT_NUMBER).contentType(MediaType.APPLICATION_JSON)
                .content(invalidjson))

        then:
        response.andExpect(status().isBadRequest())
    }


    private static BalanceSummary createBalanceSummary() {
        BalanceSummary balanceSummary = new BalanceSummary("Jane", "Brown", SORT_CODE, ACCOUNT_NUMBER)
        balanceSummary.setBalanceRecords([new BalanceRecord(TR_DATE, MIN_BALANCE)])
        balanceSummary
    }

    private static BalanceSummary createBalanceSummaryNoNameDetails() {
        BalanceSummary balanceSummary = new BalanceSummary(SORT_CODE, ACCOUNT_NUMBER)
        balanceSummary.setBalanceRecords([new BalanceRecord(TR_DATE, MIN_BALANCE)])
        balanceSummary
    }

}
