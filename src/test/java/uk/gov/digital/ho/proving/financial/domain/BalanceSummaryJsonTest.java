package uk.gov.digital.ho.proving.financial.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.digital.ho.proving.financial.ServiceConfiguration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BalanceSummaryJsonTest {

    private static Logger LOGGER = LoggerFactory.getLogger(BalanceSummaryJsonTest.class);

    @Test
    public void toJson() throws Exception {
        ObjectMapper mapper = new ServiceConfiguration().getMapper();
        BalanceSummary st = getStatement();
        String jsonInString = mapper.writeValueAsString(st);

        assertThat(jsonInString).isEqualToIgnoringWhitespace(getJsonResource("statementTest.json"));
    }


    @Test
    public void fromJson() throws Exception {
        ObjectMapper mapper = new ServiceConfiguration().getMapper();
        BalanceSummary tr = mapper.readValue(getJsonResource("statementTest.json"), BalanceSummary.class);
        BalanceSummary trToMatch = getStatement();

        assertThat(tr).isEqualTo(trToMatch);
    }


    private String getJsonResource(String file) {
        String data = null;
        try {
            URI uri;
            uri = this.getClass().getClassLoader().getResource(file).toURI();
            data = new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("utf-8"));
        } catch (URISyntaxException | IOException e) {
            LOGGER.error("Problem reading resource file", e);
        }
        return data;
    }

    private BalanceSummary getStatement() {
        BalanceSummary st = new BalanceSummary("Ray", "Purchase", "601234", "12345678");
        List trs = new ArrayList<>();
        trs.add(new BalanceRecord(LocalDate.parse("2016-10-08"), "3000"));
        trs.add(new BalanceRecord(LocalDate.parse("2016-10-07"), "3000"));
        st.setBalanceRecords(trs);
        return st;
    }

}