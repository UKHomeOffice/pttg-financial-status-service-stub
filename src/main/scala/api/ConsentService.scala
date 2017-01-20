package uk.gov.digital.ho.proving.financial.api

import java.time.LocalDate
import java.util.Optional

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.{HttpStatus, MediaType, ResponseEntity}
import org.springframework.web.bind.annotation._

@RestController
@PropertySource(value = Array("classpath:application.properties"))
@RequestMapping(value = Array("/financialstatus/v1/ods/accounts/"))
@ControllerAdvice
class ConsentService @Autowired()(dataService: DataService, mapper: ObjectMapper) {

  private val LOGGER = LoggerFactory.getLogger(classOf[ConsentService])

  @RequestMapping(value = Array("{accountId}/consent"), method = Array(RequestMethod.GET), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def getBalanceRecordsForDateRange(@PathVariable(value = "accountId") accountId: String,
                                    @RequestParam(value = "dateOfBirth") @DateTimeFormat(pattern = "d-MMM-yyyy") dob: Optional[LocalDate],
                                    @RequestHeader(value = "userId") userId: String,
                                    @RequestHeader(value = "requestId") requestId: String,
                                    @RequestHeader(value = "consumerId") consumerId: String): ResponseEntity[String] = {

    if (accountId.length == 14) {

      val sortCode = accountId.substring(0, 6)
      val account = accountId.substring(6)

      LOGGER.debug("{} {} {} {} {} {}", sortCode, account, dob, userId, requestId, consumerId)

      val statements = dataService.getStatement(accountId.substring(0, 6), accountId.substring(6))
      val consent = if (statements.getConsent == null) "FAILURE" else statements.getConsent
      val response = new ConsentResponse(accountId, sortCode, account, consent, getConsentMessage(consent))
      new ResponseEntity(mapper.writeValueAsString(response), HttpStatus.OK)
    } else {
      buildErrorResponse()
    }

  }

  def getConsentMessage(status: String) = status match {
    case "INITIATED" => "Consent request has been initiated to Account-Holder"
    case "PENDING" => "Awaiting response from Account-Holder"
    case "SUCCESS" => "Consent received from Account-Holder"
    case "FAILURE" => "Account-Holder refused consent"
    case "INVALID" => "Invalid response from Account-Holder"
  }

  private def buildErrorResponse(): ResponseEntity[String] =
    new ResponseEntity("{\"errorCode\":1,\"errorDescription\":\"sample errorDescription\"}", HttpStatus.BAD_REQUEST)

}
