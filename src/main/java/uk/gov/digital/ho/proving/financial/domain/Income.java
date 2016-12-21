package uk.gov.digital.ho.proving.financial.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Document
public class Income {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @NotNull
    private String payDate;
    @NotNull
    private String income;
    @NotNull
    private String employer;


    public Income() {
    }

    public Income(String payDate, String income, String employer) {
        this.payDate = payDate;
        this.income = income;
        this.employer = employer;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Income income1 = (Income) o;

        if (payDate != null ? !payDate.equals(income1.payDate) : income1.payDate != null) return false;
        if (income != null ? !income.equals(income1.income) : income1.income != null) return false;
        return employer != null ? employer.equals(income1.employer) : income1.employer == null;

    }

    @Override
    public int hashCode() {
        int result = payDate != null ? payDate.hashCode() : 0;
        result = 31 * result + (income != null ? income.hashCode() : 0);
        result = 31 * result + (employer != null ? employer.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Income{" +
                "payDate=" + payDate +
                ", income='" + income + '\'' +
                ", employer='" + employer + '\'' +
                '}';
    }
}
