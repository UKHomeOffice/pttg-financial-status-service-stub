package uk.gov.digital.ho.proving.financial.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import uk.gov.digital.ho.proving.financial.dao.BalanceSummaryRepository;
import uk.gov.digital.ho.proving.financial.domain.BalanceSummary;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * @Author Home Office Digital
 */
@Component
public class MongoConnectionHealth implements HealthIndicator {

    @Autowired
    private BalanceSummaryRepository repository;


    @Override
    public Health health() {

        String message = "";
        try {
            final long count = repository.count();
            if (count == 0) {
                return Health.up().withDetail("Mongo is available but not populated with test data", "UP").build();
            }

            if (count > 0) {
                return Health.up().withDetail("Mongo is available and populated with test data: ", "UP").build();
            }
        } catch (Exception e) {
            message = e.getMessage();
        }
        return Health.down().withDetail("Mongo was not available: " + message, "Down").build();
    }

}
