package com.example.currencyexchange.services;

import com.example.currencyexchange.models.ExchangeRate;

import java.util.Optional;

public class ExchangeService {
    private final ExchangeRateService exchangeRateService;
    public ExchangeService(){
        exchangeRateService = new ExchangeRateService();
    }
    public Optional<ExchangeRate> findExchangeRate(String baseCode, String targetCode){
        Optional<ExchangeRate> exchangeRate = exchangeRateService.findExchangeRate(baseCode,targetCode);
        if (exchangeRate.isPresent()){
            return exchangeRate;
        }

        exchangeRate = exchangeRateService.findExchangeRate(targetCode,baseCode);
        if(exchangeRate.isPresent()){
            ExchangeRate temp = exchangeRate.get();
            return Optional.of(new ExchangeRate(temp.getId(),temp.getTargetCurrency(),temp.getBaseCurrency(),1 / temp.getRate()));
        }

        Optional<ExchangeRate> usdToBase = exchangeRateService.findExchangeRate("USD",baseCode);
        Optional<ExchangeRate> usdToTarget = exchangeRateService.findExchangeRate("USD",targetCode);
        if(usdToBase.isEmpty() || usdToTarget.isEmpty()){
            return Optional.empty();
        }
        var a = usdToBase.get();
        var b = usdToTarget.get();
        double baseToUSD = 1 / a.getRate();
        double baseToTarget = baseToUSD * b.getRate();
        return Optional.of(new ExchangeRate(a.getTargetCurrency(),b.getTargetCurrency(),baseToTarget));
    }
}
