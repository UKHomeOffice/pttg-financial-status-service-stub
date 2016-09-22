package uk.gov.digital.ho.proving.financial.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import uk.gov.digital.ho.proving.financial.dao.BalanceSummaryRepository;

/**
 * @Author Home Office Digital
 * There is a default springboot healthcheck for the mongotemplate - this healthcheck indicates if the mongo instance is returning test data
 */
@Component
public class MongoTestDataHealthIndicator implements HealthIndicator {

    @Autowired
    private BalanceSummaryRepository repository;


    @Override
    public Health health() {

        String message = "";
        try {
            final long count = repository.count();
            if (count == 0) {
                return Health.down().withDetail("Mongo is available but not populated with test data", "DOWN").build();
            }

            if (count > 0) {
                return Health.up().withDetail("Mongo is available and populated with test data", "UP").build();
            }
        } catch (Exception e) {
            message = e.getMessage();
        }
        return Health.down().withDetail("Mongo was not available: " + message, "DOWN").build();
    }

}
