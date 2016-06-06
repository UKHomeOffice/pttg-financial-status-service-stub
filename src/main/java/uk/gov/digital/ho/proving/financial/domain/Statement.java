package uk.gov.digital.ho.proving.financial.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Statement{

    private String firstName;
    private String surname;
    private String sortCode;
    private String accountNumber;

    private List<Transaction> transactions;

    public Statement() {
    }

    public Statement(String firstName, String surname, String sortCode, String accountNumber) {
        this.firstName = firstName;
        this.surname = surname;
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "Statement{" +
                "firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", sortCode='" + sortCode + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", transactions=" + transactions +
                '}';
    }
}
