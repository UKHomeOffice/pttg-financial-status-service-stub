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

    private String accountId;
    private String sortCode;
    private String accountNumber;
    private Result result;

    public ConsentResponse(String accountId, String sortCode, String accountNumber, String status, String description) {
        this.accountId = accountId;
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
        this.result = new Result(status, description);
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
