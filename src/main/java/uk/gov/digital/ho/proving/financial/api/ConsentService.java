package uk.gov.digital.ho.proving.financial.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.digital.ho.proving.financial.domain.BalanceSummary;
import uk.gov.digital.ho.proving.financial.exception.AccountNotFoundException;

@RestController
@PropertySource(value = {"classpath:application.properties"})
@RequestMapping(value = {"/financialstatus/v1/ods/accounts/"})
@ControllerAdvice
public class ConsentService {

    @Autowired
    DataService dataService;
    @Autowired
    ObjectMapper mapper;

    private static Logger LOGGER = LoggerFactory.getLogger(ConsentService.class);

    @RequestMapping(value = {"{accountId}/consent"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getBalanceRecordsForDateRange(@PathVariable(value = "accountId") String accountId,
                                                                @RequestParam(value = "dateOfBirth") @DateTimeFormat(pattern = "d-MMM-yyyy") Optional<LocalDate> dob,
                                                                @RequestHeader(value = "userId") String userId,
                                                                @RequestHeader(value = "requestId") String requestId,
                                                                @RequestHeader(value = "consumerId") String consumerId) {
        try {
            if (accountId.length() == 14) {
                String sortCode = accountId.substring(0, 6);
                String account = accountId.substring(6);
                LOGGER.debug("{} {} {} {} {} {}", sortCode, account, dob, userId, requestId, consumerId);
                BalanceSummary statements = dataService.getStatement(accountId.substring(0, 6), accountId.substring(6));
                String consent = (statements.getConsent() == null) ? "FAILURE" : statements.getConsent();

                ConsentResponse response = new ConsentResponse(accountId, sortCode, account, consent, getConsentMessage(consent));
                return new ResponseEntity(mapper.writeValueAsString(response), HttpStatus.OK);
            } else {
                return buildErrorResponse(HttpStatus.BAD_REQUEST);
            }
        } catch (AccountNotFoundException acn) {
            return buildErrorResponse(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST);
        }

    }

    private String getConsentMessage(String status) {

        String response = "";
        switch (status) {
            case "INITIATED":
                response = "Consent request has been initiated to Account-Holder";
            case "PENDING":
                response = "Awaiting response from Account-Holder";
            case "SUCCESS":
                response = "Consent received from Account-Holder";
            case "FAILURE":
                response = "Account-Holder refused consent";
            case "INVALID":
                response = "Invalid response from Account-Holder";
        }
        return response;
    }

    private ResponseEntity<String> buildErrorResponse(HttpStatus httpStatus) {
        return new ResponseEntity("{\"errorCode\":1,\"errorDescription\":\"sample errorDescription\"}", httpStatus);
    }
}
