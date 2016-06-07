package uk.gov.digital.ho.proving.financial.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.proving.financial.domain.Statement;

import java.time.LocalDate;
import java.util.List;

public interface StatementRepository extends MongoRepository<Statement, String>{

    List<Statement> findByAccountNumberAndSortCode(String accountNumber, String sortCode);
}
