package uk.gov.digital.ho.proving.financial.api;

import uk.gov.digital.ho.proving.financial.domain.BalanceRecord;

import java.util.List;

/*container for daily balances for a given account*/
public class BalanceSummaryResponse extends BaseResponse {

    private String accountHolderName;
    private List<BalanceRecord> balanceRecords;


    public BalanceSummaryResponse(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public BalanceSummaryResponse() {
    }


    public List<BalanceRecord> getBalanceRecords() {
        return balanceRecords;
    }

    public void setBalanceRecords(List<BalanceRecord> balanceRecords) {
        this.balanceRecords = balanceRecords;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }
}
