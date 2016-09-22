package uk.gov.digital.ho.proving.financial.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import spock.lang.Specification
import uk.gov.digital.ho.proving.financial.dao.BalanceSummaryRepository;


class MongoTestDataHealthIndicatorSpec extends Specification {

    def "should report DOWN when mongo unreachable"() {

        given:
        MongoTestDataHealthIndicator healthCheck = new MongoTestDataHealthIndicator()
        def repository = Mock(BalanceSummaryRepository)
        healthCheck.repository = repository

        and:
        repository.count() >> {throw new RuntimeException ("Something went wrong when accessing mongo")}

        when:
        Health result = healthCheck.health()

        then:
        notThrown(Exception)
        result.getStatus() == Status.DOWN
    }

    def "should report UP when mongo is reachable and is populated with data"() {

        given:
        MongoTestDataHealthIndicator healthCheck = new MongoTestDataHealthIndicator()
        def repository = Mock(BalanceSummaryRepository)
        healthCheck.repository = repository

        and:
        repository.count() >> 200

        when:
        Health result = healthCheck.health()

        then:
        repository.count() * 1
        result.getStatus() == Status.UP
        result.getDetails().containsKey("Mongo is available and populated with test data")
    }

    def "should report DOWN when mongo is reachable but empty"() {

        given:
        MongoTestDataHealthIndicator healthCheck = new MongoTestDataHealthIndicator()
        def repository = Mock(BalanceSummaryRepository)
        healthCheck.repository = repository

        and:
        repository.count() >> 0

        when:
        Health result = healthCheck.health()

        then:
        result.getStatus() == Status.DOWN
        result.getDetails().containsKey("Mongo is available but not populated with test data")
    }
}