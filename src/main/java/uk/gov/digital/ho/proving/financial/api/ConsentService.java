package uk.gov.digital.ho.proving.financial.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.proving.financial.domain.BalanceSummary;
import uk.gov.digital.ho.proving.financial.exception.AccountNotFoundException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@PropertySource(value = {"classpath:application.properties"})
@RequestMapping(value = {"/accounts/"})
@ControllerAdvice
public class ConsentService {

    @Autowired
    private DataService dataService;
    @Autowired
    private ObjectMapper mapper;

    private static Logger LOGGER = LoggerFactory.getLogger(ConsentService.class);

    @RequestMapping(value = {"{accountId}/consent"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getBalanceRecordsForDateRange(@PathVariable(value = "accountId") String accountId,
                                                                @RequestParam(value = "fromBalanceDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromBalanceDate,
                                                                @RequestParam(value = "toBalanceDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toBalanceDate,
                                                                @RequestParam(value = "dateOfBirth") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
                                                                @RequestHeader(value = "userId") String userId,
                                                                @RequestHeader(value = "requestId") String requestId,
                                                                @RequestHeader(value = "consumerId") String consumerId) {

        try {
            if (accountId.length() == 14) {
                String sortCode = accountId.substring(0, 6);
                String account = accountId.substring(6);
                LOGGER.debug("{} {} {} {} {} {} {} {}", sortCode, account, fromBalanceDate, toBalanceDate, dob, userId, requestId, consumerId);
                BalanceSummary statements = dataService.getStatement(accountId.substring(0, 6), accountId.substring(6));
                String consent = (statements.getConsent() == null) ? "FAILURE" : statements.getConsent();

                if (statements.getMobileNumber() == null || statements.getMobileNumber().trim().equals("")) {
                    return buildErrorResponse(446, "Mobile Number is Invalid", HttpStatus.BAD_REQUEST);
                } else {
                    BarclaysAccountConsentResponse response = new BarclaysAccountConsentResponse(new ConsentResponse(accountId, sortCode, account, fromBalanceDate.format(DateTimeFormatter.ISO_DATE), toBalanceDate.format(DateTimeFormatter.ISO_DATE), consent, getConsentMessage(consent)));
                    return new ResponseEntity<>(mapper.writeValueAsString(response), HttpStatus.OK);
                }
            } else {
                return buildErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(), HttpStatus.BAD_REQUEST);
            }
        } catch (AccountNotFoundException acn) {
            return buildErrorResponse(455, "No Data Found", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    private String getConsentMessage(String status) {

        String response = "";
        switch (status) {
            case "INITIATED":
                response = "Consent request has been initiated to Account-Holder";
                break;
            case "PENDING":
                response = "Awaiting response from Account-Holder";
                break;
            case "SUCCESS":
                response = "Consent received from Account-Holder";
                break;
            case "FAILURE":
                response = "Account-Holder refused consent";
                break;
            case "INVALID":
                response = "Invalid response from Account-Holder";
                break;
        }
        return response;
    }

    private ResponseEntity<String> buildErrorResponse(Integer errorCode, String errorMessage, HttpStatus httpStatus) {
        return new ResponseEntity<>("{\"errorCode\":" + errorCode + ",\"errorDescription\":\"" + errorMessage + "\"}", httpStatus);
    }

}
