package uk.gov.digital.ho.proving.financial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import uk.gov.digital.ho.proving.financial.api.DataService;

@Component
public class DataInitialiser implements ApplicationRunner{
    @Autowired
    private DataService dataService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dataService.initialiseTestData();
    }
}
