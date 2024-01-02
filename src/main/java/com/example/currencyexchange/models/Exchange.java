package com.example.currencyexchange.models;

public class Exchange {
    private Currency base;
    private Currency target;
    private double rate;
    private double amount;
    private String convertedAmount;

    public Exchange(Currency base, Currency target, double rate, double amount, String convertedAmount) {
        this.base = base;
        this.target = target;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public Currency getBase() {
        return base;
    }

    public Currency getTarget() {
        return target;
    }

    public double getRate() {
        return rate;
    }

    public double getAmount() {
        return amount;
    }

    public String getConvertedAmount() {
        return convertedAmount;
    }
}
