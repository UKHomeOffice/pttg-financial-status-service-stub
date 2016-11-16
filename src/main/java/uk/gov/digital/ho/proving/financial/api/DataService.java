package uk.gov.digital.ho.proving.financial.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.dao.DuplicateKeyException;
import uk.gov.digital.ho.proving.financial.dao.ApplicationRepository;
import uk.gov.digital.ho.proving.financial.dao.BalanceSummaryRepository;
import uk.gov.digital.ho.proving.financial.dao.ApplicantRepository;
import uk.gov.digital.ho.proving.financial.domain.Applications;
import uk.gov.digital.ho.proving.financial.domain.BalanceSummary;
import uk.gov.digital.ho.proving.financial.domain.Applicants;
import uk.gov.digital.ho.proving.financial.exception.AccountNotFoundException;
import uk.gov.digital.ho.proving.financial.exception.FinancialStatusStubException;
import uk.gov.digital.ho.proving.financial.exception.MongoException;

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
    private BalanceSummaryRepository repository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private ApplicationRepository applicationRepository;


    private static Logger LOGGER = LoggerFactory.getLogger(DataService.class);

    public BalanceSummary getStatement(String sortcode, String account, LocalDate applicationFromDate, LocalDate applicationToDate) {
        final BalanceSummary balanceSummary = getBalanceSummaryUnfiltered(sortcode, account);
        balanceSummary.setBalanceRecords(balanceSummary.getBalanceRecords().stream().filter(tr -> !(tr.getDate().isBefore(applicationFromDate)) && !(tr.getDate().isAfter(applicationToDate))).collect(Collectors.toList()));
        LOGGER.debug("returning balance records: {}", balanceSummary.getBalanceRecords());
        return balanceSummary;
    }

    private BalanceSummary getBalanceSummaryUnfiltered(String sortcode, String account) {
        final List<BalanceSummary> balanceSummaryList = repository.findByAccountNumberAndSortCode(account, sortcode);
        handleNonSingularResult(balanceSummaryList);
        return balanceSummaryList.get(0);
    }

    public BalanceSummary getStatement(String sortcode, String account) {
        final List<BalanceSummary> balanceSummaryList = repository.findByAccountNumberAndSortCode(account, sortcode);
        handleNonSingularResult(balanceSummaryList);
        return balanceSummaryList.get(0);
    }

    public void saveTestData(BalanceSummary testData) {

        LOGGER.debug("Attempting to persist data [" + testData + "]");
        final List<BalanceSummary> balanceSummaryList = repository.findByAccountNumberAndSortCode(testData.getAccountNumber(), testData.getSortCode());
        if (balanceSummaryList.isEmpty()) {
            try {
                repository.insert(testData);
            } catch (Exception e) {
                LOGGER.error("An error has occurred while trying to add data", e);
                throw new MongoException("n error has occurred while trying to add data", e);
            }
        } else {
            throw new FinancialStatusStubException("Unique contraint for sortcode and account number violated. Clear existing test data to allow insert.");
        }
    }

    public List<BalanceSummary> getAllBalanceSummaries() {
        return repository.findAll();
    }

    public void initialiseTestData() throws IOException {

        initialiseBankTestData();
        initialiseApplicationTestData();
        initialiseApplicantTestData();
    }

    private void initialiseApplicationTestData()  throws IOException{
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        applicationRepository.deleteAll();
        Resource[] mappingLocations = patternResolver.getResources("classpath*:application*.json");

        for (Resource mappingLocation : mappingLocations) {
            Applications testApplications = null;
            try {
                testApplications = mapper.readValue(mappingLocation.getURL(), Applications.class);
            } catch (IOException e) {
                LOGGER.error("Error loading json from classpath: " + e);
            }

            LOGGER.debug("Adding document from: " + mappingLocation.getURI());
            try {
                applicationRepository.insert(testApplications);
            } catch (DuplicateKeyException me) {
                LOGGER.info("Error received inserting test data, non-unique key: {} , insert aborted:", testApplications.getIndividual().getNino(), me);
            }
        }

    }

    private void initialiseApplicantTestData()  throws IOException{
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        applicantRepository.deleteAll();
        Resource[] mappingLocations = patternResolver.getResources("classpath*:applicant*.json");

        for (Resource mappingLocation : mappingLocations) {
            Applicants testApplicants = null;
            try {
                testApplicants = mapper.readValue(mappingLocation.getURL(), Applicants.class);
            } catch (IOException e) {
                LOGGER.error("Error loading json from classpath: " + e);
            }

            LOGGER.debug("Adding document from: " + mappingLocation.getURI());
            try {
                applicantRepository.insert(testApplicants);
            } catch (DuplicateKeyException me) {
                LOGGER.info("Error received inserting test data, non-unique key: {} , insert aborted:", testApplicants.getIndividual().getNino(), me);
            }
        }

    }

    private void initialiseBankTestData()  throws IOException{
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        repository.deleteAll();
        Resource[] mappingLocations = patternResolver.getResources("classpath*:demoData*.json");

        for (Resource mappingLocation : mappingLocations) {
            BalanceSummary testDataBalanceSummary = null;
            try {
                testDataBalanceSummary = mapper.readValue(mappingLocation.getURL(), BalanceSummary.class);
            } catch (IOException e) {
                LOGGER.error("Error loading json from classpath: " + e);
            }

            LOGGER.debug("Adding document from: " + mappingLocation.getURI());
            try {
                repository.insert(testDataBalanceSummary);
            } catch (DuplicateKeyException me) {
                LOGGER.info("Error received inserting test data, non-unique key: {} {}, insert aborted:",testDataBalanceSummary.getSortCode(), testDataBalanceSummary.getAccountNumber(), me);
            }
        }


    }

    private void handleNonSingularResult(List<BalanceSummary> balanceSummaryList) {
        if (balanceSummaryList.size() > 1) {
            throw new FinancialStatusStubException("Invalid data retrieved, unique contraint for sortcode and account number violated");
        } else if (balanceSummaryList.isEmpty()) {
            throw new AccountNotFoundException();
        }
    }
}
