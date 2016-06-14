package uk.gov.digital.ho.proving.financial.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.proving.financial.domain.BalanceRecord;
import uk.gov.digital.ho.proving.financial.exception.AccountNotFoundException;
import uk.gov.digital.ho.proving.financial.domain.BalanceSummary;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static uk.gov.digital.ho.proving.financial.util.DateUtils.parseIsoDate;

@RestController
@ControllerAdvice
public class Service {

    private static Logger LOGGER = LoggerFactory.getLogger(Service.class);

    @Autowired
    private DataService dataService;

    @RequestMapping(value = "/financialstatus/v1/{sortcode}/{account}/balances", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<BalanceRecordResponse> getBalanceRecordsForDateRange(
            @PathVariable(value = "account") String account,
            @PathVariable(value = "sortcode") String sortcode,
            @RequestParam(value = "fromDate") String fromDateAsString,
            @RequestParam(value = "toDate") String toDateAsString) {

        LOGGER.debug(String.format("Financial Status Service STUB invoked for %s sortcode %s account between %s and %s", sortcode, account, fromDateAsString, toDateAsString));

        try {

            Optional<LocalDate> fromDate = parseIsoDate(fromDateAsString);
            if (!fromDate.isPresent()) {
                return buildErrorResponse(new BalanceRecordResponse(), "bankcode 1", "Parameter error: From date is invalid", HttpStatus.BAD_REQUEST);
            }

            Optional<LocalDate> toDate = parseIsoDate(toDateAsString);
            if (!toDate.isPresent()) {
                return buildErrorResponse(new BalanceRecordResponse(), "bankcode 2", "Parameter error: To date is invalid", HttpStatus.BAD_REQUEST);
            }

            //flat map - removes the nested optional (if it exists), map uses value if exists or returns optional with absent - so lookup not called
            Optional<BalanceSummary> statement = fromDate.flatMap(from ->
                    toDate.map(to ->
                            dataService.getStatement(sortcode, account, from, to)
                    )
            );

            return statement.map(ips -> {
                        BalanceRecordResponse incomeRetrievalResponse = new BalanceRecordResponse();
                        incomeRetrievalResponse.setBalanceRecords(ips.getBalanceRecords());
                        return new ResponseEntity<>(incomeRetrievalResponse, HttpStatus.OK);
                    }
            ).orElse(buildErrorResponse(new BalanceRecordResponse(), "bankcode 3", "Error retrieving test data", HttpStatus.NOT_FOUND));

        } catch (AccountNotFoundException e) {
            return buildErrorResponse(new BalanceRecordResponse(), "bankcode 4", "Resource not found", HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            LOGGER.error("Error retrieving test data", e);
            return buildErrorResponse(new BalanceRecordResponse(), "bankcode 5", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /*persist complete Balance summary (account information and balance records) throws FinancialStatusStubException if account already exists*/
    @RequestMapping(value = "/financialstatus/v1", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createTestData(@RequestBody @Valid BalanceSummary testData) {

        LOGGER.info(String.format("Financial Status Service STUB invoked for testdata %s", testData));

        try {
            dataService.saveTestData(testData);
        } catch (RuntimeException e) {
            LOGGER.error("Error persisting test data", e);
            return buildErrorResponse(new BalanceRecordResponse(), "0001", "Error persisting testData test data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().build();
    }


    /*persist balance records for given account  - throws FinancialStatusStubException if account already exists*/
    @RequestMapping(value = "/financialstatus/v1/{sortcode}/{account}/balances", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createTestDataBalances(@PathVariable(value = "account") String account,
                                                    @PathVariable(value = "sortcode") String sortcode,
                                                    @RequestBody @Valid List<BalanceRecord> testData) {

        LOGGER.info(String.format("Financial Status Service STUB invoked for testdata %s", testData));

        try {
            BalanceSummary balanceSummary = new BalanceSummary(sortcode, account);
            balanceSummary.setBalanceRecords(testData);
            dataService.saveTestData(balanceSummary);
        } catch (RuntimeException e) {
            LOGGER.error("Error persisting test data", e);
            return buildErrorResponse(new BalanceRecordResponse(), "0001", "Error persisting testData test data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/financialstatus/v1", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<BalanceRecordResponse> deleteTestData() {

        try {
            dataService.initialiseTestData();
            return new ResponseEntity<>(new BalanceRecordResponse(), HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("Error retrieving test data", e);
            return buildErrorResponse(new BalanceRecordResponse(), "bankcode 5", "Something went horrifically wrong: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/financialstatus/v1", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<BalanceSummariesResponse> getAllTestData() {

        try {

            final List<BalanceSummary> balanceSummaries = dataService.getAllBalanceSummaries();

            return new ResponseEntity<>(new BalanceSummariesResponse(balanceSummaries), HttpStatus.OK);

        } catch (RuntimeException e) {
            LOGGER.error("Error retrieving test data", e);
            return buildErrorResponse(new BalanceSummariesResponse(), "bankcode 5", "Something went horrifically wrong: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    protected <U extends BaseResponse> ResponseEntity<U> buildErrorResponse(U response, String statusCode, String statusMessage, HttpStatus status) {
        ResponseStatus error = new ResponseStatus(statusCode, statusMessage);
        response.setStatus(error);
        return ResponseEntity.status(status)
                .contentType(
                        MediaType.APPLICATION_JSON)
                .body(response);
    }

}
