package uk.gov.digital.ho.proving.financial.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification
import uk.gov.digital.ho.proving.financial.TestConfiguration
import uk.gov.digital.ho.proving.financial.domain.BalanceRecord
import uk.gov.digital.ho.proving.financial.domain.BalanceSummary

import java.time.LocalDate

@ContextConfiguration(classes = TestConfiguration.class)
@WebAppConfiguration
class BalanceSummaryRepositorySpec extends Specification {
    static final LocalDate TRANSACTION_DATE = LocalDate.parse("2018-03-25")
    static final String SORT_CODE = "343434"
    static final String ACCOUNT_NUMBER = "54545454"

    @Autowired
    private BalanceSummaryRepository repo


    void cleanup() {
        List<BalanceSummary> loadedBalanceSummary = repo.findByAccountNumberAndSortCode(ACCOUNT_NUMBER, SORT_CODE)
        loadedBalanceSummary.each { st -> repo.delete(st) }
    }

    def "Get Balance summary by account number and sortcode"() {
        BalanceSummary balanceSummary = createBalanceSummary()

        given: "balance summary exists in database"
        repo.save(balanceSummary)

        when:
        List<BalanceSummary> loadedBalanceSummary = repo.findByAccountNumberAndSortCode(ACCOUNT_NUMBER, SORT_CODE)

        then:
        loadedBalanceSummary.size() == 1
        loadedBalanceSummary.head().accountNumber == ACCOUNT_NUMBER
        loadedBalanceSummary.head().sortCode == SORT_CODE
        balanceSummary == createBalanceSummary()
    }

    private static BalanceSummary createBalanceSummary() {
        BalanceSummary balanceSummary = new BalanceSummary("Jane", "Brown", SORT_CODE, ACCOUNT_NUMBER)
        balanceSummary.setBalanceRecords([new BalanceRecord(TRANSACTION_DATE, "2000")])
        balanceSummary
    }
}
