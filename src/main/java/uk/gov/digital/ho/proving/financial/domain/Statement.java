package uk.gov.digital.ho.proving.financial.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Document
public class Statement{

    @Id
    private String id;

    private String firstName;
    private String surname;
    @NotNull
    private String sortCode;
    @NotNull
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Statement)) return false;

        Statement statement = (Statement) o;

        if (id != null ? !id.equals(statement.id) : statement.id != null) return false;
        if (firstName != null ? !firstName.equals(statement.firstName) : statement.firstName != null) return false;
        if (surname != null ? !surname.equals(statement.surname) : statement.surname != null) return false;
        if (!sortCode.equals(statement.sortCode)) return false;
        if (!accountNumber.equals(statement.accountNumber)) return false;
        return transactions != null ? transactions.equals(statement.transactions) : statement.transactions == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (sortCode != null ? sortCode.hashCode() : 0);
        result = 31 * result + (accountNumber != null ? accountNumber.hashCode() : 0);
        result = 31 * result + (transactions != null ? transactions.hashCode() : 0);
        return result;
    }
}
