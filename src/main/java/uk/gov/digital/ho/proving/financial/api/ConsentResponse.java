package uk.gov.digital.ho.proving.financial.api;

public class ConsentResponse {

    private class Result {
        private String status;
        private String description;

        public Result(String status, String description) {
            this.status = status;
            this.description = description;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    private long accountId;
    private long sortCode;
    private long accountNumber;
    private String fromBalanceDate;
    private String toBalanceDate;
    private Result result;

    public ConsentResponse(String accountId, String sortCode, String accountNumber, String fromBalanceDate, String toBalanceDate, String status, String description) {
        this.accountId = Long.valueOf(accountId);
        this.sortCode = Long.valueOf(sortCode);
        this.accountNumber = Long.valueOf(accountNumber);
        this.fromBalanceDate = fromBalanceDate;
        this.toBalanceDate = toBalanceDate;
        this.result = new Result(status, description);
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getSortCode() {
        return sortCode;
    }

    public void setSortCode(long sortCode) {
        this.sortCode = sortCode;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
