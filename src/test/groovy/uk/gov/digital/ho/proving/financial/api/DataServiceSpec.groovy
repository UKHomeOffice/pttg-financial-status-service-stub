package uk.gov.digital.ho.proving.financial.api;

import org.junit.Before;
import org.junit.Test;
import spock.lang.Specification
import uk.gov.digital.ho.proving.financial.ServiceConfiguration;
import uk.gov.digital.ho.proving.financial.dao.BalanceSummaryRepository
import uk.gov.digital.ho.proving.financial.domain.BalanceRecord
import uk.gov.digital.ho.proving.financial.domain.BalanceSummary

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate;

import static org.junit.Assert.*;


public class DataServiceSpec extends Specification{
    static final String SORT_CODE = "123456"
    static final String ACCOUNT_NUMBER = "12345678"
    static final LocalDate TR_DATE_25_MARCH = LocalDate.parse("2014-03-25")
    static final LocalDate TR_DATE_27_MARCH = LocalDate.parse("2014-03-27")
    static final LocalDate TR_DATE_28_MARCH = LocalDate.parse("2014-03-28")
    static final LocalDate TR_DATE_21_MARCH = LocalDate.parse("2014-03-21")
    public static final String ACCOUNT_HOLDER_NAME = "Ray Purchase"


    BalanceSummaryRepository repo = Mock(BalanceSummaryRepository.class)
    DataService service = new DataService()

    def setup() throws Exception {
        service.repository = repo
    }

    def "data filtering should exclude dates outside the range"() {
        given:
        1 * repo.findByAccountNumberAndSortCode(ACCOUNT_NUMBER, SORT_CODE) >> {createBalanceSummary()}

        when:
        def balanceSummary = service.getStatement(SORT_CODE, ACCOUNT_NUMBER, TR_DATE_25_MARCH, TR_DATE_27_MARCH)

        then:
        balanceSummary.balanceRecords.size() == 2
    }

    def "data filtering should include records on the to and from dates"() {
        given:
        1 * repo.findByAccountNumberAndSortCode(ACCOUNT_NUMBER, SORT_CODE) >> {createBalanceSummary()}

        when:
        def balanceSummary = service.getStatement(SORT_CODE, ACCOUNT_NUMBER, TR_DATE_25_MARCH, TR_DATE_28_MARCH)

        then:
        balanceSummary.balanceRecords.size() == 3
    }

    private static List<BalanceSummary> createBalanceSummary() {
        BalanceSummary balanceSummary = new BalanceSummary(ACCOUNT_HOLDER_NAME, SORT_CODE, ACCOUNT_NUMBER)
        balanceSummary.setBalanceRecords([new BalanceRecord(TR_DATE_21_MARCH, "2000"), new BalanceRecord(TR_DATE_25_MARCH, "2000"), new BalanceRecord(TR_DATE_27_MARCH, "2000"), new BalanceRecord(TR_DATE_28_MARCH, "2000")])
        [balanceSummary]
    }


}