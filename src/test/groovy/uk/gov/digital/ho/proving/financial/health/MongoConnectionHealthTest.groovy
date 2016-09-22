package uk.gov.digital.ho.proving.financial.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import spock.lang.Specification
import uk.gov.digital.ho.proving.financial.dao.BalanceSummaryRepository;


class MongoConnectionHealthTest extends Specification {

    /*def "should report DOWN when mongo unreachable"() {

        given:
        MongoConnectionHealth healthCheck = new MongoConnectionHealth()
        healthCheck.apiRoot = ''
        healthCheck.apiEndpoint = ''

        when:
        Health result = healthCheck.health()

        then:
        result.getStatus() == Status.DOWN
    }*/

    def "should report UP when mongo is reachable and is populated with data"() {

        given:
        MongoConnectionHealth healthCheck = new MongoConnectionHealth()
        def repository = Mock(BalanceSummaryRepository)
        healthCheck.repository = repository

        and:
        repository.count >> 200

        when:
        Health result = healthCheck.health()

        then:
        result.getStatus() == Status.UP
        result.getDetails().containsKey("Mongo is available but not populated with test data")
    }

    def "should report UP when mongo is reachable but empty"() {

        given:
        MongoConnectionHealth healthCheck = new MongoConnectionHealth()
        def repository = Mock(BalanceSummaryRepository)
        healthCheck.repository = repository

        and:
        repository.count >> 0

        when:
        Health result = healthCheck.health()

        then:
        result.getStatus() == Status.UP
        result.getDetails().containsKey("Mongo is available but not populated with test data")
    }
}