package uk.gov.digital.ho.proving.financial.api;

import uk.gov.digital.ho.proving.financial.domain.BalanceRecord;

import java.util.List;

/*container for daily balances for a given account*/
public class BalanceRecordResponse extends BaseResponse {

    private List<BalanceRecord> balanceRecords;


    public BalanceRecordResponse() {
    }

    public List<BalanceRecord> getBalanceRecords() {
        return balanceRecords;
    }

    public void setBalanceRecords(List<BalanceRecord> balanceRecords) {
        this.balanceRecords = balanceRecords;
    }
}
