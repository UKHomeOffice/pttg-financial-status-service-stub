package uk.gov.digital.ho.proving.financial.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Document
public class BalanceRecord {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @NotNull
    private LocalDate date;
    @NotNull
    private String balance;

    public BalanceRecord() {
    }

    public BalanceRecord(LocalDate date, String balance) {
        this.date = date;
        this.balance = balance;
    }

    public LocalDate getDate() {
        return date;
    }


    public String getBalance() {
        return balance;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "BalanceRecord{" +
                "date=" + date +
                ", balance='" + balance + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BalanceRecord)) return false;

        BalanceRecord that = (BalanceRecord) o;

        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return balance != null ? balance.equals(that.balance) : that.balance == null;

    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }
}
