package uk.gov.digital.ho.proving.financial.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.gov.digital.ho.proving.financial.domain.BalanceSummary;

import java.util.List;

public interface BalanceSummaryRepository extends MongoRepository<BalanceSummary, String>{

    List<BalanceSummary> findByAccountNumberAndSortCode(String accountNumber, String sortCode);
}
