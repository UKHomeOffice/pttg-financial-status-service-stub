package uk.gov.digital.ho.proving.financial.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Document
public class Applicants {

    @Id
    private String id;

    @NotNull
    private Individual individual;

    @NotNull
    private String payFreq;

    //@NotNull
    private String accountNumber;

    private List<Income> incomes;

    public Applicants() {
    }

    public Applicants(String id, Individual individual, String payFreq, String accountNumber, List<Income> incomes) {
        this.id = id;
        this.individual = individual;
        this.payFreq = payFreq;
        this.accountNumber = accountNumber;
        this.incomes = incomes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public String getPayFreq() {
        return payFreq;
    }

    public void setPayFreq(String payFreq) {
        this.payFreq = payFreq;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<Income> getIncomes() {
        return incomes;
    }

    public void setIncomes(List<Income> incomes) {
        this.incomes = incomes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Applicants that = (Applicants) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (individual != null ? !individual.equals(that.individual) : that.individual != null) return false;
        if (payFreq != null ? !payFreq.equals(that.payFreq) : that.payFreq != null) return false;
        if (accountNumber != null ? !accountNumber.equals(that.accountNumber) : that.accountNumber != null)
            return false;
        return incomes != null ? incomes.equals(that.incomes) : that.incomes == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (individual != null ? individual.hashCode() : 0);
        result = 31 * result + (payFreq != null ? payFreq.hashCode() : 0);
        result = 31 * result + (accountNumber != null ? accountNumber.hashCode() : 0);
        result = 31 * result + (incomes != null ? incomes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Applicants{" +
                "id='" + id + '\'' +
                ", individual=" + individual +
                ", payFreq='" + payFreq + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", incomes=" + incomes +
                '}';
    }
}
