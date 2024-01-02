package com.example.currencyexchange.services;

import com.example.currencyexchange.DTO.ExchangeRateDTO;
import com.example.currencyexchange.models.Currency;
import com.example.currencyexchange.models.ExchangeRate;
import com.example.currencyexchange.repositories.CurrencyRepository;
import com.example.currencyexchange.repositories.ExchangeRatesRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {
    private final ExchangeRatesRepository repository;
    private final CurrencyRepository currencyRepository;

    public ExchangeRateService() {
        repository = new ExchangeRatesRepository();
        currencyRepository = new CurrencyRepository();
    }

    public void save(ExchangeRateDTO exchangeRate) throws SQLException {
        Optional<ExchangeRate> exchangeRateOptional =
                findExchangeRate(exchangeRate.getBaseCurrencyCode(), exchangeRate.getTargetCurrencyCode());
        if (exchangeRateOptional.isEmpty()) {
            throw new IllegalArgumentException("Exchange rate not found");
        }
        try {
            var exchangeRateExists = exchangeRateOptional.get();
            repository.update(new ExchangeRate(exchangeRateExists.getId(),
                    exchangeRateExists.getBaseCurrency(),
                    exchangeRateExists.getTargetCurrency(),
                    Double.parseDouble(exchangeRate.getRate())));
        } catch (SQLException exception) {
            throw new SQLException("Database error");
        }
    }

    public List<ExchangeRate> findAll() {
        return repository.findAll();
    }

    public void create(ExchangeRateDTO exchangeRateDTO) throws SQLException {
        Optional<Currency> base = currencyRepository.findByCode(exchangeRateDTO.getBaseCurrencyCode());
        Optional<Currency> target = currencyRepository.findByCode(exchangeRateDTO.getTargetCurrencyCode());
        try {
            ExchangeRate exchangeRate = new ExchangeRate(base.get(), target.get(),
                    Double.parseDouble(exchangeRateDTO.getRate()));
            repository.create(exchangeRate);
        } catch (SQLException e) {
            throw new SQLException("This exchange rate already created");
        }
    }

    public Optional<ExchangeRate> findExchangeRate(String baseCode, String targetCode) {
        Optional<Currency> base = currencyRepository.findByCode(baseCode);
        Optional<Currency> target = currencyRepository.findByCode(targetCode);
        if (base.isEmpty() || target.isEmpty()) {
            return Optional.empty();
        }
        return findExchangeRate(base.get().getId(), target.get().getId());
    }

    public Optional<ExchangeRate> findExchangeRate(int baseId, int targetId) {
        Optional<ExchangeRate> exchangeOptional = repository.findByBaseToTargetId(baseId, targetId);
        if (exchangeOptional.isEmpty()) {
            exchangeOptional = repository.findByBaseToTargetId(targetId, baseId);
            if (exchangeOptional.isPresent()) {
                ExchangeRate exchangeRate = exchangeOptional.get();
                ExchangeRate exchangeWithNewRate = new ExchangeRate(exchangeRate.getId(),
                        exchangeRate.getTargetCurrency(), exchangeRate.getBaseCurrency(), 1 / exchangeRate.getRate());
                exchangeOptional = Optional.of(exchangeWithNewRate);
            }
        }
        return exchangeOptional;
    }
}
