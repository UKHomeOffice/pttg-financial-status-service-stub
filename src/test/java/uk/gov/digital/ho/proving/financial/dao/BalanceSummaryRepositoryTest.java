package uk.gov.digital.ho.proving.financial.dao;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import uk.gov.digital.ho.proving.financial.TestConfiguration;
import uk.gov.digital.ho.proving.financial.domain.BalanceSummary;
import uk.gov.digital.ho.proving.financial.domain.BalanceRecord;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@WebAppConfiguration
public class BalanceSummaryRepositoryTest {

    public static final LocalDate TRANSACTION_DATE= LocalDate.parse("2018-03-25");
    public static final String SORT_CODE = "343434";
    public static final String ACCOUNT_NUMBER = "54545454";

    @Autowired
    private BalanceSummaryRepository repo;

    @After
    public void tearDown() {
        List<BalanceSummary> loadedBalanceSummary = repo.findByAccountNumberAndSortCode(ACCOUNT_NUMBER, SORT_CODE);
        loadedBalanceSummary.stream().forEach(st -> repo.delete(st));
    }

    @Test
    public void shouldPersistAndDeleteStatement() {
        BalanceSummary balanceSummary = createBalanceSummary();

        repo.save(balanceSummary);

        List<BalanceSummary> loadedBalanceSummary = repo.findByAccountNumberAndSortCode(ACCOUNT_NUMBER, SORT_CODE);

        Assertions.assertThat(loadedBalanceSummary).hasSize(1);

        repo.delete(loadedBalanceSummary);

        loadedBalanceSummary = repo.findByAccountNumberAndSortCode(ACCOUNT_NUMBER, SORT_CODE);

        Assertions.assertThat(loadedBalanceSummary).hasSize(0);
    }

    @Ignore
    @Test
    public void findByAccountNumberAndSortCode() throws Exception {

        final List<BalanceSummary> balanceSummary = repo.findByAccountNumberAndSortCode("12345678", "601234");

        assertEquals(balanceSummary.size(), 1);
        assertEquals(balanceSummary.get(0).getAccountNumber(), "12345678");
        assertEquals(balanceSummary.get(0).getSortCode(), "601234");
    }

    private BalanceSummary createBalanceSummary() {
        BalanceSummary balanceSummary = new BalanceSummary("Jane", "Brown", SORT_CODE, ACCOUNT_NUMBER);
        BalanceRecord tr = new BalanceRecord(TRANSACTION_DATE,"2000");
        final ArrayList<BalanceRecord> balanceRecords = new ArrayList<>();
        balanceRecords.add(tr);
        balanceSummary.setBalanceRecords(balanceRecords);
        return balanceSummary;
    }

}