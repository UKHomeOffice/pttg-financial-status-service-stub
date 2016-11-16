package uk.gov.digital.ho.proving.financial.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.gov.digital.ho.proving.financial.domain.Applicants;

public interface ApplicantRepository extends MongoRepository<Applicants, String>{}
