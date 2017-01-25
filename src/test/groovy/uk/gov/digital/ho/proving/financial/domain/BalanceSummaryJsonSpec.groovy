package uk.gov.digital.ho.proving.financial.domain

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification
import uk.gov.digital.ho.proving.financial.ServiceConfiguration

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate

import static org.codehaus.groovy.runtime.StringGroovyMethods.replaceAll

class BalanceSummaryJsonSpec extends Specification{
    ObjectMapper mapper = new ServiceConfiguration().getMapper()


    def "Balance Record should serialize to expected json format"() {

        given:
        def balanceSummary = getBalanceSummary()

        when: "balance record is serialized"
        def jsonInString = mapper.writeValueAsString(balanceSummary)

        then: "the output matches the expected json (once whitespace removed)"
        jsonInString.replaceAll("\\s","") ==  getJsonResource("balanceSummaryJsonTest.json").replaceAll("\\s","")
    }

    def "Balance Record in json format should deserialize to expected object"() {

        given:
        def jsonString = getJsonResource("balanceSummaryJsonTest.json")

        when: "json string is deserialized"
        BalanceSummary balanceSummary = mapper.readValue(jsonString, BalanceSummary.class)

        then:
        balanceSummary == getBalanceSummary()
    }

    def getJsonResource(String file){
        try {
            URI uri = this.getClass().getClassLoader().getResource(file).toURI()
            new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("utf-8"))
        } catch (URISyntaxException | IOException e) {
            println("Problem reading resource file",e)
        }
    }

    def BalanceSummary getBalanceSummary() {
        BalanceSummary st = new BalanceSummary("Ray Purchase", "601234", "12345678", "Y", "07777777777")
        st.setBalanceRecords([new BalanceRecord(LocalDate.parse("2016-10-08"), "3000"),new BalanceRecord(LocalDate.parse("2016-10-07"), "3000")])
        st
    }

}