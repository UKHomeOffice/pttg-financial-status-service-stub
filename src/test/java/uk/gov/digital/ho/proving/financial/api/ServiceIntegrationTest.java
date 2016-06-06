package uk.gov.digital.ho.proving.financial.api;


import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import uk.gov.digital.ho.proving.financial.ServiceRunner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServiceRunner.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@PropertySource("classpath:application.properties")
public class ServiceIntegrationTest {


    @Value("${local.server.port}")
    private int port;

    final String PATH = "/financialstatus/v1/transactions";

    private String getBaseUrl() {
        return "http://localhost:" + port + PATH;
    }


    private MockRestServiceServer mockServer;

    private RestTemplate restTemplate;

    @Autowired
    private Service service = new Service();


    @Before
    public void setUp() {
        restTemplate = new TestRestTemplate();
    }


    @Test
    public void setupAndTearDownTestData() throws Exception {

        createNonDemoTestData();

        verifyNumberOfPersistedStatements(3);

        clearTestSpecificData();

        verifyNumberOfPersistedStatements(2);
    }

    private void clearTestSpecificData() {
        restTemplate.delete(getBaseUrl(),StatementResponse.class);
    }

    private void verifyNumberOfPersistedStatements(int expected) {
        ResponseEntity<StatementListResponse> allData = restTemplate.getForEntity(getBaseUrl(), StatementListResponse.class);
        assertThat(allData.getBody().getStatements()).hasSize(expected);
    }

    private void createNonDemoTestData() throws URISyntaxException, IOException {
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        final HttpEntity<String> requestEntity = new HttpEntity<String>(getNonDemoData(),headers);
        restTemplate.postForEntity(getBaseUrl(), requestEntity, String.class);
    }


    private String getNonDemoData() throws URISyntaxException, IOException {
        URI uri = ServiceIntegrationTest.class.getClassLoader().getResource("serviceIntegrationTest.json").toURI();
        return new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("utf-8"));
    }

}
