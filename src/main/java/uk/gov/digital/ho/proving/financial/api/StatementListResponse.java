package uk.gov.digital.ho.proving.financial.api;

import uk.gov.digital.ho.proving.financial.domain.Statement;
import uk.gov.digital.ho.proving.financial.domain.Transaction;

import java.util.List;


public class StatementListResponse extends BaseResponse {

    private List<Statement> statements;


    public StatementListResponse() {
    }

    public StatementListResponse(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }
}
