package uk.gov.digital.ho.proving.financial.dao;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import uk.gov.digital.ho.proving.financial.TestConfiguration;
import uk.gov.digital.ho.proving.financial.domain.Statement;
import uk.gov.digital.ho.proving.financial.domain.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@WebAppConfiguration
public class StatementRepositoryTest {

    public static final LocalDate TRANSACTION_DATE= LocalDate.parse("2018-03-25");
    public static final String SORT_CODE = "343434";
    public static final String ACCOUNT_NUMBER = "54545454";

    @Autowired
    private StatementRepository repo;

    @After
    public void tearDown() {
        List<Statement> loadedStatement = repo.findByAccountNumberAndSortCode(ACCOUNT_NUMBER, SORT_CODE);
        loadedStatement.stream().forEach(st -> repo.delete(st));
    }

    @Test
    public void shouldPersistAndDeleteStatement() {
        Statement statement = createStatement();

        repo.save(statement);

        List<Statement> loadedStatement = repo.findByAccountNumberAndSortCode(ACCOUNT_NUMBER, SORT_CODE);

        Assertions.assertThat(loadedStatement).hasSize(1);

        repo.delete(loadedStatement);

        loadedStatement = repo.findByAccountNumberAndSortCode(ACCOUNT_NUMBER, SORT_CODE);

        Assertions.assertThat(loadedStatement).hasSize(0);
    }

    @Test
    public void findByAccountNumberAndSortCode() throws Exception {

        final List<Statement> statement = repo.findByAccountNumberAndSortCode("12345678", "601234");

        assertEquals(statement.size(), 1);
        assertEquals(statement.get(0).getAccountNumber(), "12345678");
        assertEquals(statement.get(0).getSortCode(), "601234");
    }

    private Statement createStatement() {
        Statement statement = new Statement("Jane", "Brown", SORT_CODE, ACCOUNT_NUMBER);
        Transaction tr = new Transaction(TRANSACTION_DATE,"2000");
        final ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(tr);
        statement.setTransactions(transactions);
        return statement;
    }

}