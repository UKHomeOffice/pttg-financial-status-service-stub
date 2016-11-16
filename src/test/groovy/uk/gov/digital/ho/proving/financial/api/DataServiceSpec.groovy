package uk.gov.digital.ho.proving.financial.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.dao.DuplicateKeyException;
import spock.lang.Specification
import uk.gov.digital.ho.proving.financial.dao.ApplicantRepository
import uk.gov.digital.ho.proving.financial.dao.ApplicationRepository
import uk.gov.digital.ho.proving.financial.dao.BalanceSummaryRepository
import uk.gov.digital.ho.proving.financial.domain.Applicants
import uk.gov.digital.ho.proving.financial.domain.Applications
import uk.gov.digital.ho.proving.financial.domain.BalanceRecord
import uk.gov.digital.ho.proving.financial.domain.BalanceSummary
import uk.gov.digital.ho.proving.financial.domain.Income
import uk.gov.digital.ho.proving.financial.domain.Individual

import java.time.LocalDate

public class DataServiceSpec extends Specification{
    static final String SORT_CODE = "123456"
    static final String ACCOUNT_NUMBER = "12345678"
    static final LocalDate TR_DATE_25_MARCH = LocalDate.parse("2014-03-25")
    static final LocalDate TR_DATE_27_MARCH = LocalDate.parse("2014-03-27")
    static final LocalDate TR_DATE_28_MARCH = LocalDate.parse("2014-03-28")
    static final LocalDate TR_DATE_21_MARCH = LocalDate.parse("2014-03-21")
    public static final String ACCOUNT_HOLDER_NAME = "Ray Purchase"


    BalanceSummaryRepository repo = Mock(BalanceSummaryRepository.class)
    ApplicantRepository applicantRepository = Mock(ApplicantRepository.class)
    ApplicationRepository applicationRepository = Mock(ApplicationRepository.class)

    DataService service = new DataService()

    def setup() throws Exception {
        service.mapper = Mock(ObjectMapper.class)
        service.repository = repo
        service.applicationRepository = applicationRepository
        service.applicantRepository = applicantRepository

    }

    def "data filtering should exclude dates outside the range"() {
        given:
        1 * repo.findByAccountNumberAndSortCode(ACCOUNT_NUMBER, SORT_CODE) >> {createBalanceSummaryList()}

        when:
        def balanceSummary = service.getStatement(SORT_CODE, ACCOUNT_NUMBER, TR_DATE_25_MARCH, TR_DATE_27_MARCH)

        then:
        balanceSummary.balanceRecords.size() == 2
    }

    def "data filtering should include records on the to and from dates"() {
        given:
        1 * repo.findByAccountNumberAndSortCode(ACCOUNT_NUMBER, SORT_CODE) >> {createBalanceSummaryList()}

        when:
        def balanceSummary = service.getStatement(SORT_CODE, ACCOUNT_NUMBER, TR_DATE_25_MARCH, TR_DATE_28_MARCH)

        then:
        balanceSummary.balanceRecords.size() == 3
    }

    def "data insert should enforce uniqueness on sortcode and account number, confirm exception handled gracefully and insert continues after failure"() {
        given:
        repo.insert(_) >> {throw new DuplicateKeyException("")}
        service.mapper.readValue(_,BalanceSummary.class) >> {createBalanceSummary()}
        service.mapper.readValue(_,Applicants.class) >> {createApplicants()}
        service.mapper.readValue(_,Applications.class) >> {createApplications()}

        when:
        service.initialiseTestData()

        then:
        noExceptionThrown()
        (2.._) * repo.insert(_)
    }

    private static List<BalanceSummary> createBalanceSummaryList() {
        [createBalanceSummary()]
    }

    private static BalanceSummary createBalanceSummary() {
        BalanceSummary balanceSummary = new BalanceSummary(ACCOUNT_HOLDER_NAME, SORT_CODE, ACCOUNT_NUMBER)
        balanceSummary.setBalanceRecords([new BalanceRecord(TR_DATE_21_MARCH, "2000"), new BalanceRecord(TR_DATE_25_MARCH, "2000"), new BalanceRecord(TR_DATE_27_MARCH, "2000"), new BalanceRecord(TR_DATE_28_MARCH, "2000")])
        balanceSummary
    }

    private static Applicants createApplicants() {
        Income income = new Income("2016-01-01", "1000.00", "Flying Pizza Ltd")
        Individual individual = new Individual("Mr", "Fred", "Flintstone","AA123456A")
        Applicants applicants = new Applicants("0",individual,"M1", ACCOUNT_NUMBER, [income, income] )
        applicants
    }

    private static Applications createApplications() {
//        Income income = new Income("2016-01-01", "1000.00", "Flying Pizza Ltd")
        Individual individual = new Individual("Mr", "Fred", "Flintstone","AA123456A")
        Applications applications = new Applications(individual)
        applications
    }

}