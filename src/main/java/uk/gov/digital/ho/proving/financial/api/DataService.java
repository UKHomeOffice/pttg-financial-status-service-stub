package uk.gov.digital.ho.proving.financial.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bson.Document;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import uk.gov.digital.ho.proving.financial.AccountNotFoundException;
import uk.gov.digital.ho.proving.financial.FinancialStatusStubException;
import uk.gov.digital.ho.proving.financial.MongoException;
import uk.gov.digital.ho.proving.financial.dao.StatementRepository;
import uk.gov.digital.ho.proving.financial.domain.Statement;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * This class retrieves data from an external sources and converts it to Home Office domain classes. When the HMRC web
 * api is available this class will call the api via a delegate and then convert the response to Home Office
 * domain classes.
 */
@org.springframework.stereotype.Service
public class DataService {
    @Autowired
    @Qualifier("transactionSummary")
    DBCollection transactionSummaryCollection;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private StatementRepository repository;

    private static Logger LOGGER = LoggerFactory.getLogger(DataService.class);

//    // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public Statement lookup(String account, LocalDate applicationFromDate, LocalDate applicationToDate) {
        DBObject query = new QueryBuilder().start().put("individual.nino").is(account).get();
        DBCursor cursor = transactionSummaryCollection.find(query);

        if (1 == cursor.size()) {
            JSONObject jsonResponse = new JSONObject(cursor.next().toString());
            jsonResponse.remove("_id");

            try {
                Statement transactions = mapper.readValue(jsonResponse.toString(), Statement.class);
                //    incomeProvingResponse.setIncomes(incomeProvingResponse.getIncomes().stream().filter( income ->
                //        !(income.getPayDate().isBefore(applicationFromDate)) && !(income.getPayDate().isAfter(applicationToDate))
                //    ).collect(Collectors.toList()));
                LOGGER.info(transactions.toString());
                return transactions;
            } catch (Exception e) {
                LOGGER.error("Could not map JSON from mongodb to Application domain class", e);
                throw new FinancialStatusStubException("Error reading test data", e);
            }

        } else {
            LOGGER.error("Could not retrieve a unique document from mongodb for account [" + account + "]");
            throw new AccountNotFoundException();
        }
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
}
