package uk.gov.digital.ho.proving.financial.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.gov.digital.ho.proving.financial.domain.Statement;

public interface StatementRepository extends MongoRepository<Statement, String>{

}
