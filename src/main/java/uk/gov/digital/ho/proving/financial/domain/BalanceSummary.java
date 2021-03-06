package uk.gov.digital.ho.proving.financial.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Document
@CompoundIndexes({
        @CompoundIndex(name = "acc_idx", unique = true, def = "{'sortCode' : 1, 'accountNumber': 1}")
})
public class BalanceSummary {

    @Id
    private String id;

    private String accountHolderName;
    @NotNull
    private String sortCode;
    @NotNull
    private String accountNumber;

    private String consent;

    private String mobileNumber;

    private List<BalanceRecord> balanceRecords;

    public BalanceSummary() {
    }


    public BalanceSummary(String accountHolderName, String sortCode, String accountNumber,String consent, String mobileNumber ) {
        this.accountHolderName = accountHolderName;
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
        this.mobileNumber = mobileNumber;
        this.consent = consent;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getConsent() {
        return consent;
    }

    public void setConsent(String consent) {
        this.consent = consent;
    }

    public List<BalanceRecord> getBalanceRecords() {
        return balanceRecords;
    }

    public void setBalanceRecords(List<BalanceRecord> balanceRecords) {
        this.balanceRecords = balanceRecords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BalanceSummary that = (BalanceSummary) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (accountHolderName != null ? !accountHolderName.equals(that.accountHolderName) : that.accountHolderName != null)
            return false;
        if (sortCode != null ? !sortCode.equals(that.sortCode) : that.sortCode != null) return false;
        if (accountNumber != null ? !accountNumber.equals(that.accountNumber) : that.accountNumber != null)
            return false;
        if (mobileNumber != null ? !mobileNumber.equals(that.mobileNumber) : that.mobileNumber != null) return false;
        if (consent != null ? !consent.equals(that.consent) : that.consent != null) return false;
        return balanceRecords != null ? balanceRecords.equals(that.balanceRecords) : that.balanceRecords == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (accountHolderName != null ? accountHolderName.hashCode() : 0);
        result = 31 * result + (sortCode != null ? sortCode.hashCode() : 0);
        result = 31 * result + (accountNumber != null ? accountNumber.hashCode() : 0);
        result = 31 * result + (mobileNumber != null ? mobileNumber.hashCode() : 0);
        result = 31 * result + (consent != null ? consent.hashCode() : 0);
        result = 31 * result + (balanceRecords != null ? balanceRecords.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BalanceSummary{" +
                "id='" + id + '\'' +
                ", accountHolderName='" + accountHolderName + '\'' +
                ", sortCode='" + sortCode + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", consent='" + consent + '\'' +
                ", balanceRecords=" + balanceRecords +
                '}';
    }
}
