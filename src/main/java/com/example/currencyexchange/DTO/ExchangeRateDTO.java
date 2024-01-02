package com.example.currencyexchange.DTO;

public class ExchangeRateDTO {
    private int id;
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private String rate;

    public ExchangeRateDTO() {

    }

    public ExchangeRateDTO(String baseCurrencyCode, String targetCurrencyCode, String rate) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

    public ExchangeRateDTO(int id, String baseCurrencyCode, String targetCurrencyCode, String rate) {
        this.id = id;
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public String getRate() {
        return rate;
    }
}
