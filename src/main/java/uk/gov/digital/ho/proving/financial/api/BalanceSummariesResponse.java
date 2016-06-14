package uk.gov.digital.ho.proving.financial.api;

import uk.gov.digital.ho.proving.financial.domain.BalanceSummary;

import java.util.List;

/*container for a list of balance summaries (records and account information for multiple accounts) */
public class BalanceSummariesResponse extends BaseResponse {

    private List<BalanceSummary> balanceSummaries;


    public BalanceSummariesResponse() {
    }

    public BalanceSummariesResponse(List<BalanceSummary> balanceSummaries) {
        this.balanceSummaries = balanceSummaries;
    }

    public List<BalanceSummary> getBalanceSummaries() {
        return balanceSummaries;
    }

    public void setBalanceSummaries(List<BalanceSummary> balanceSummaries) {
        this.balanceSummaries = balanceSummaries;
    }
}
