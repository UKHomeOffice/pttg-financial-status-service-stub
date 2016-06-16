package uk.gov.digital.ho.proving.financial.domain

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification
import uk.gov.digital.ho.proving.financial.ServiceConfiguration

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate

class BalanceRecordJsonSpec extends Specification{
    ObjectMapper mapper = new ServiceConfiguration().getMapper()


    def "Balance Record should serialize to expected json format"() {

        given:
        def balanceRecord = new BalanceRecord(LocalDate.parse("2016-10-08"), "3000")

        when: "balance record is serialized"
        def jsonInString = mapper.writeValueAsString(balanceRecord)

        then:
        jsonInString == getJsonResource("balanceRecordJsonTest.json")
    }


    def "Balance Record in json format should deserialize to expected object"() {

        given:
        def jsonString = getJsonResource("balanceRecordJsonTest.json")

        when: "json string is deserialized"
        BalanceRecord balanceRecord = mapper.readValue(jsonString, BalanceRecord.class)

        then:
        balanceRecord == new BalanceRecord(LocalDate.parse("2016-10-08"),"3000")
    }


    def getJsonResource(String file){

        try {
            URI uri = this.getClass().getClassLoader().getResource(file).toURI()
            new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("utf-8"))
        } catch (URISyntaxException | IOException e) {
            println "Problem reading resource file"+ e
        }
    }

}