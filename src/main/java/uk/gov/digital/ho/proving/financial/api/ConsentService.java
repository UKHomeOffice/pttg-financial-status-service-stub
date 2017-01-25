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

import javax.servlet.http.HttpServletResponse;

@RestController
@PropertySource(value = {"classpath:application.properties"})
@RequestMapping(value = {"/financialstatus/v1/ods/accounts/"})
@ControllerAdvice
public class ConsentService {

    @Autowired
    private DataService dataService;
    @Autowired
    private ObjectMapper mapper;

    private static Logger LOGGER = LoggerFactory.getLogger(ConsentService.class);

    // Due to Barclays using non standard HTTP status code e.g. 446 and Spring not allowing them to be
    // put inside a ResponseEntity we are building the response as a string and explicitly returning it

    @RequestMapping(value = {"{accountId}/consent"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getBalanceRecordsForDateRange(@PathVariable(value = "accountId") String accountId,
                                                @RequestParam(value = "dateOfBirth") @DateTimeFormat(pattern = "d-MMM-yyyy") Optional<LocalDate> dob,
                                                @RequestHeader(value = "userId") String userId,
                                                @RequestHeader(value = "requestId") String requestId,
                                                @RequestHeader(value = "consumerId") String consumerId,
                                                HttpServletResponse httpResponse) {

        try {
            if (accountId.length() == 14) {
                String sortCode = accountId.substring(0, 6);
                String account = accountId.substring(6);
                LOGGER.debug("{} {} {} {} {} {}", sortCode, account, dob, userId, requestId, consumerId);
                BalanceSummary statements = dataService.getStatement(accountId.substring(0, 6), accountId.substring(6));
                String consent = (statements.getConsent() == null) ? "FAILURE" : statements.getConsent();

                if (statements.getMobileNumber() == null || statements.getMobileNumber().trim().equals("")) {
                    httpResponse.setStatus(446);
                    return buildErrorResponse(446, "Mobile Number is Invalid");
                } else {
                    ConsentResponse response = new ConsentResponse(accountId, sortCode, account, consent, getConsentMessage(consent));
                    return mapper.writeValueAsString(response);
                }
            } else {
                return buildErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString());
            }
        } catch (AccountNotFoundException acn) {
            httpResponse.setStatus(455);
            return buildErrorResponse(455, "No Data Found");
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString());
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

    private String buildErrorResponse(Integer errorCode, String errorMessage) {
        return "{\"errorCode\":" + errorCode + ",\"errorDescription\":\"" + errorMessage + "\"}";
    }

}
