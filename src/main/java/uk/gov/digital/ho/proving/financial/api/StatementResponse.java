package uk.gov.digital.ho.proving.financial.api;

import uk.gov.digital.ho.proving.financial.api.BaseResponse;
import uk.gov.digital.ho.proving.financial.domain.Transaction;

import java.util.List;


public class StatementResponse extends BaseResponse {

    private List<Transaction> transactions;


    public StatementResponse() {
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
