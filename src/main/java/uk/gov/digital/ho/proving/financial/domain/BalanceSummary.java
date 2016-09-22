package uk.gov.digital.ho.proving.financial.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Document
public class BalanceSummary {

    @Id
    private String id;

    private String accountHolderName;
    @NotNull
    private String sortCode;
    @NotNull
    private String accountNumber;

    private List<BalanceRecord> balanceRecords;

    public BalanceSummary() {
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public BalanceSummary(String accountHolderName, String sortCode, String accountNumber) {
        this.accountHolderName = accountHolderName;
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
    }

    public List<BalanceRecord> getBalanceRecords() {
        return balanceRecords;
    }

    public void setBalanceRecords(List<BalanceRecord> balanceRecords) {
        this.balanceRecords = balanceRecords;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BalanceSummary)) return false;

        BalanceSummary that = (BalanceSummary) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (!accountHolderName.equals(that.accountHolderName)) return false;
        if (!sortCode.equals(that.sortCode)) return false;
        if (!accountNumber.equals(that.accountNumber)) return false;
        return balanceRecords != null ? balanceRecords.equals(that.balanceRecords) : that.balanceRecords == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + accountHolderName.hashCode();
        result = 31 * result + sortCode.hashCode();
        result = 31 * result + accountNumber.hashCode();
        result = 31 * result + (balanceRecords != null ? balanceRecords.hashCode() : 0);
        return result;
    }
}
