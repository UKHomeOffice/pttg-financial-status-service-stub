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
}
