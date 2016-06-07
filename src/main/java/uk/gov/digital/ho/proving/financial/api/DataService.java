package uk.gov.digital.ho.proving.financial.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import uk.gov.digital.ho.proving.financial.exception.AccountNotFoundException;
import uk.gov.digital.ho.proving.financial.exception.FinancialStatusStubException;
import uk.gov.digital.ho.proving.financial.exception.MongoException;
import uk.gov.digital.ho.proving.financial.dao.StatementRepository;
import uk.gov.digital.ho.proving.financial.domain.Statement;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 */
@org.springframework.stereotype.Service
public class DataService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private StatementRepository repository;

    private static Logger LOGGER = LoggerFactory.getLogger(DataService.class);

    public Statement getStatement(String sortcode, String account, LocalDate applicationFromDate, LocalDate applicationToDate) {
        final List<Statement> statementList = repository.findByAccountNumberAndSortCode(account, sortcode);
        handleNonSingularResult(statementList);
        final Statement statement = statementList.get(0);
        statement.setTransactions(statement.getTransactions().stream().filter(tr -> !(tr.getDate().isBefore(applicationFromDate)) && !(tr.getDate().isAfter(applicationToDate))).collect(Collectors.toList()));

        return statement;
    }

    public Statement getStatement(String sortcode, String account) {
        final List<Statement> statementList = repository.findByAccountNumberAndSortCode(account, sortcode);
        handleNonSingularResult(statementList);
        return statementList.get(0);
    }

    public void saveTestData(Statement testData) {
        try {
            LOGGER.debug("Attempting to persist data [" + testData + "]");
            repository.insert(testData);
        } catch (Exception e) {
            LOGGER.error("An error has occurred while trying to add data", e);
            throw new MongoException("n error has occurred while trying to add data", e);
        }
    }

    public List<Statement> getAllTransactions() {
        return repository.findAll();
    }

    public void initialiseTestData() throws IOException {
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        repository.deleteAll();
        Resource[] mappingLocations = patternResolver.getResources("classpath*:demoData*.json");

        for (Resource mappingLocation : mappingLocations) {
            Statement testDataStatement = null;
            try {
                testDataStatement = mapper.readValue(mappingLocation.getURL(), Statement.class);
            } catch (IOException e) {
                LOGGER.error("Error loading json from classpath: " + e);
            }

            LOGGER.debug("Adding document from: " + mappingLocation.getURI());

            repository.insert(testDataStatement);
        }

    }

    private void handleNonSingularResult(List<Statement> statementList) {
        if (statementList.size() > 1) {
            throw new FinancialStatusStubException("Invalid data retrieved, unique contraint for sortcode and account number violated");
        }else if(statementList.isEmpty()) {
            throw new AccountNotFoundException();
        }
    }
}
