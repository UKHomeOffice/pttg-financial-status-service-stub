package uk.gov.digital.ho.proving.financial;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class ServiceConfiguration extends WebMvcConfigurerAdapter {

    private static Logger LOGGER = LoggerFactory.getLogger(ServiceConfiguration.class);

    @Value("${mongodb.ssl}")
    private boolean ssl;

    @Value("${mongodb.service}")
    private String mongodbService;

    @Value("${mongodb.connect.timeout.millis}")
    private int mongodbConnectTimeout = 30000;

    @Value("${response.delay:0}")
    private long responseDelayInSeconds;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TimeoutHandlerInterceptor(responseDelayInSeconds));
    }

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
        b.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return b;
    }

    @Bean
    public ObjectMapper getMapper() {
        ObjectMapper m = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-M-d")));
        m.registerModule(javaTimeModule);
        m.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        m.enable(SerializationFeature.INDENT_OUTPUT);
        return m;
    }


    public @Bean
    MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(getMongoClient(), "test");
    }

    public @Bean
    MongoTemplate mongoTemplate() throws Exception {

        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        //required as unique compound indexes have to be removed and recreated
        mongoTemplate.getDb().dropDatabase();
        return mongoTemplate;
    }

    private MongoClient getMongoClient() {
        boolean useHost = (mongodbService != null && !mongodbService.isEmpty());
        MongoClient client;

        if (useHost) {
            final int port = ssl ? 443 : 27017;

            client = new MongoClient(
                    new ServerAddress(mongodbService, port),
                    MongoClientOptions.builder()
                            .connectTimeout(mongodbConnectTimeout)
                            .serverSelectionTimeout(mongodbConnectTimeout)
                            .sslEnabled(ssl)
                            .build());

            LOGGER.info("MongoClient invoked using [" + mongodbService + "] and port [" + port + "]");
        } else {
            LOGGER.info("MongoClient invoked using default host and port");
            client = new MongoClient();
        }
        return client;
    }

}
