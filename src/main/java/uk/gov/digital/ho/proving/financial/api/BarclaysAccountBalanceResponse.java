package uk.gov.digital.ho.proving.financial.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import uk.gov.digital.ho.proving.financial.domain.BalanceRecord;
import uk.gov.digital.ho.proving.financial.domain.BalanceSummary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*container for daily balances for a given account*/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BarclaysAccountBalanceResponse extends BaseResponse {
    private final Account account;

    public BarclaysAccountBalanceResponse() {
        account = null;
    }

    public BarclaysAccountBalanceResponse(BalanceSummary balanceSummary) {
        this.account = new Account(
                Long.valueOf(balanceSummary.getSortCode() + balanceSummary.getAccountNumber()),
                Long.valueOf(balanceSummary.getSortCode()),
                Long.valueOf(balanceSummary.getAccountNumber()),
                "GBP", balanceSummary.getAccountHolderName(),
                "",
                "",
                balanceSummary.getBalanceRecords().
                        stream().
                        map(balanceRecord -> new AccountBalance(balanceRecord.getDate(), new BigDecimal(balanceRecord.getBalance()))).
                        collect(Collectors.toList()));
    }

    public Account getAccount() {
        return account;
    }
}
