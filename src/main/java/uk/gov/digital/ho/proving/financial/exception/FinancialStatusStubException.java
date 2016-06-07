package uk.gov.digital.ho.proving.financial.exception;

public class FinancialStatusStubException extends RuntimeException{
    public FinancialStatusStubException(String s, Throwable e) {
        super(s, e);
    }

    public FinancialStatusStubException(String s) {
        super(s);
    }

    public FinancialStatusStubException() {
    }
}
