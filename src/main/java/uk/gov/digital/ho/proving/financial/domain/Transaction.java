package uk.gov.digital.ho.proving.financial.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Transaction {

    //@JsonFormat(shape=JsonFormat.Shape.STRING)
    private LocalDate date;
    private String description;
    private String amount;
    private String balance;

    public Transaction() {
    }

    public Transaction(LocalDate date, String description, String amount, String balance) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.balance = balance;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
    }

    public String getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "date=" + date +
                ", description='" + description + '\'' +
                ", amount='" + amount + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (!date.equals(that.date)) return false;
        if (!description.equals(that.description)) return false;
        if (!amount.equals(that.amount)) return false;
        return balance.equals(that.balance);

    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + amount.hashCode();
        result = 31 * result + balance.hashCode();
        return result;
    }
}
