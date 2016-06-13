package uk.gov.digital.ho.proving.financial.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.digital.ho.proving.financial.ServiceConfiguration;
import uk.gov.digital.ho.proving.financial.api.ServiceIntegrationTest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

public class TransactionTest {

    private static Logger LOGGER = LoggerFactory.getLogger(TransactionTest.class);

    @Test
    public void toJson() throws Exception {
        ObjectMapper mapper = new ServiceConfiguration().getMapper();
        Transaction tr = new Transaction(LocalDate.parse("2016-10-08"),"3000");
        String jsonInString = mapper.writeValueAsString(tr);

        assertThat(jsonInString).isEqualToIgnoringWhitespace(getJsonResource("transactionTest.json"));
    }

    @Test
    public void fromJson() throws Exception {
        ObjectMapper mapper = new ServiceConfiguration().getMapper();
        Transaction tr = mapper.readValue(getJsonResource("transactionTest.json"), Transaction.class);
        Transaction trToMatch = new Transaction(LocalDate.parse("2016-10-08"),"3000");

        assertThat(tr).isEqualTo(trToMatch);
    }


    private String getJsonResource(String file){
        String data = null;
        try {
            URI uri;
            uri = this.getClass().getClassLoader().getResource(file).toURI();
            data =  new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("utf-8"));
        } catch (URISyntaxException | IOException e) {
            LOGGER.error("Problem reading resource file",e);
        }
        return data;
    }
}