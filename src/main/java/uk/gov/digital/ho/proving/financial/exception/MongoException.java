package uk.gov.digital.ho.proving.financial.exception;

public class MongoException extends FinancialStatusStubException{
    public MongoException() {
        super();
    }

    public MongoException(String s, Throwable e) {
        super(s, e);
    }
}
