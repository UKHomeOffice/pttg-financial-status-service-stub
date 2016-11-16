package uk.gov.digital.ho.proving.financial.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.gov.digital.ho.proving.financial.domain.Applications;

public interface ApplicationRepository extends MongoRepository<Applications, String>{

}
