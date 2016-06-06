package uk.gov.digital.ho.proving.financial.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.proving.financial.AccountNotFoundException;
import uk.gov.digital.ho.proving.financial.domain.Statement;

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


    @RequestMapping(value = "/financialstatus/v1/transactions/{account}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatementResponse> getTransactions(
            @PathVariable(value = "account") String account,
            @RequestParam(value = "fromDate") String fromDateAsString,
            @RequestParam(value = "toDate") String toDateAsString) {

        LOGGER.info(String.format("Financial Status Service STUB invoked for %s account between %s and %s", account, fromDateAsString, toDateAsString));

        try {

            Optional<LocalDate> fromDate = parseIsoDate(fromDateAsString);
            if (!fromDate.isPresent()) {
                return buildErrorResponse(new StatementResponse(), "bankcode 1", "Parameter error: From date is invalid", HttpStatus.BAD_REQUEST);
            }

            Optional<LocalDate> toDate = parseIsoDate(toDateAsString);
            if (!toDate.isPresent()) {
                return buildErrorResponse(new StatementResponse(), "bankcode 2", "Parameter error: To date is invalid", HttpStatus.BAD_REQUEST);
            }

            //flat map - removes the nested optional (if it exists), map uses value if exists or returns optional with absent - so lookup not called
            Optional<Statement> statement = fromDate.flatMap(from ->
                    toDate.map(to ->
                            dataService.lookup(account, from, to)
                    )
            );

            return statement.map(ips -> {
                        StatementResponse incomeRetrievalResponse = new StatementResponse();
                        incomeRetrievalResponse.setTransactions(ips.getTransactions());
                        return new ResponseEntity<>(incomeRetrievalResponse, HttpStatus.OK);
                    }
            ).orElse(buildErrorResponse(new StatementResponse(), "bankcode 3", "Error retrieving test data", HttpStatus.NOT_FOUND));

        } catch (AccountNotFoundException e) {
            return buildErrorResponse(new StatementResponse(), "bankcode 4", "Resource not found", HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            LOGGER.error("Error retrieving test data", e);
            return buildErrorResponse(new StatementResponse(), "bankcode 5", "Something went horrifically wrong: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/financialstatus/v1/transactions", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatementListResponse> getAllTransactions() {

        try {

            final List<Statement> allTransactions = dataService.getAllTransactions();

            return new ResponseEntity<>(new StatementListResponse(allTransactions), HttpStatus.OK);

        } catch (RuntimeException e) {
            LOGGER.error("Error retrieving test data", e);
            return buildErrorResponse(new StatementListResponse(), "bankcode 5", "Something went horrifically wrong: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/financialstatus/v1/transactions", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createTestData(@RequestBody Statement testData) {

        LOGGER.info(String.format("Financial Status Service STUB invoked for testdata %s", testData));

        try {
            dataService.saveTestData(testData);
        } catch (RuntimeException e) {
            LOGGER.error("Error persisting test data", e);
            return buildErrorResponse(new StatementResponse(), "0001", "Error persisting testData test data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/financialstatus/v1/transactions", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<StatementResponse> deleteTestData() {

        try {
            dataService.initialiseTestData();
            return new ResponseEntity<>(new StatementResponse(), HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("Error retrieving test data", e);
            return buildErrorResponse(new StatementResponse(), "bankcode 5", "Something went horrifically wrong: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
