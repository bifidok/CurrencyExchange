package com.example.currencyexchange.services;

import com.example.currencyexchange.models.Currency;
import com.example.currencyexchange.repositories.CurrencyRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private final CurrencyRepository repository;

    public CurrencyService() {
        repository = new CurrencyRepository();
    }

    public List<Currency> findAll() {
        return repository.findAll();
    }

    public Optional<Currency> findByCode(String name) throws IllegalArgumentException {
        Optional<Currency> currency = repository.findByCode(name);
        if(currency.isEmpty()){
            throw new IllegalArgumentException("Code not found");
        }
        return currency;
    }

    public Optional<Currency> findById(int id) {
        return repository.findById(id);
    }

    public void create(Currency obj) throws SQLException {
        try {
            repository.create(obj);
        }catch (SQLException exception){
            throw new SQLException("This code already created");
        }
    }
}
