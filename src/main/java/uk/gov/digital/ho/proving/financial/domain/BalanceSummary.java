package uk.gov.digital.ho.proving.financial.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Document
public class BalanceSummary {

    @Id
    private String id;

    private String firstName;
    private String surname;
    @NotNull
    private String sortCode;
    @NotNull
    private String accountNumber;

    private List<BalanceRecord> balanceRecords;

    public BalanceSummary() {
    }

    public BalanceSummary(String firstName, String surname, String sortCode, String accountNumber) {
        this.firstName = firstName;
        this.surname = surname;
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
    }

    public BalanceSummary(String sortCode, String accountNumber) {
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
    }

    public List<BalanceRecord> getBalanceRecords() {
        return balanceRecords;
    }

    public void setBalanceRecords(List<BalanceRecord> balanceRecords) {
        this.balanceRecords = balanceRecords;
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
        return "BalanceSummary{" +
                "firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", sortCode='" + sortCode + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", balanceRecords=" + balanceRecords +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BalanceSummary)) return false;

        BalanceSummary balanceSummary = (BalanceSummary) o;

        if (id != null ? !id.equals(balanceSummary.id) : balanceSummary.id != null) return false;
        if (firstName != null ? !firstName.equals(balanceSummary.firstName) : balanceSummary.firstName != null) return false;
        if (surname != null ? !surname.equals(balanceSummary.surname) : balanceSummary.surname != null) return false;
        if (!sortCode.equals(balanceSummary.sortCode)) return false;
        if (!accountNumber.equals(balanceSummary.accountNumber)) return false;
        return balanceRecords != null ? balanceRecords.equals(balanceSummary.balanceRecords) : balanceSummary.balanceRecords == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (sortCode != null ? sortCode.hashCode() : 0);
        result = 31 * result + (accountNumber != null ? accountNumber.hashCode() : 0);
        result = 31 * result + (balanceRecords != null ? balanceRecords.hashCode() : 0);
        return result;
    }
}
