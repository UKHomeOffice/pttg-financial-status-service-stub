package uk.gov.digital.ho.proving.financial.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import uk.gov.digital.ho.proving.financial.domain.BalanceRecord;

import java.util.List;

/*container for daily balances for a given account*/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BalanceSummaryResponse extends BaseResponse {


    private String accountHolderName = null;
    private List<BalanceRecord> balanceRecords = null;


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
