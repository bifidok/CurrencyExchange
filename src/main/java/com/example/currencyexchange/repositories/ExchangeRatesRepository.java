package com.example.currencyexchange.repositories;

import com.example.currencyexchange.models.Currency;
import com.example.currencyexchange.models.ExchangeRate;
import com.example.currencyexchange.services.CurrencyService;
import com.example.currencyexchange.utils.DatabaseConnector;

import java.sql.*;
import java.util.*;

public class ExchangeRatesRepository implements CrudRepository<ExchangeRate> {
    private final Connection connection;
    private final CurrencyService currencyService;

    public ExchangeRatesRepository() {
        connection = DatabaseConnector.getConnection();
        currencyService = new CurrencyService();
    }

    @Override
    public List<ExchangeRate> findAll() {
        List<ExchangeRate> rates = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            String sql = "select * from exchange_rates";
            statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                rates.add(parseResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement(statement);
        }
        return rates;
    }
    @Override
    public Optional<ExchangeRate> findById(int id){
        ExchangeRate exchangeRate = null;
        PreparedStatement statement = null;
        try {
            String sql = "select * from exchange_rates where exchange_rates.id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                exchangeRate = parseResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement(statement);
        }
        return Optional.ofNullable(exchangeRate);
    }

    public Optional<ExchangeRate> findByBaseToTargetId(int baseId, int targetId) {
        ExchangeRate exchangeRate = null;
        PreparedStatement statement = null;
        try {
            String sql = "select * from exchange_rates where exchange_rates.baseCurrency = ? " +
                    "and exchange_rates.targetCurrency = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, baseId);
            statement.setInt(2, targetId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                exchangeRate = parseResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement(statement);
        }
        return Optional.ofNullable(exchangeRate);
    }
    @Override
    public void create(ExchangeRate obj) throws SQLException, IllegalArgumentException {
        PreparedStatement statement = null;
        Optional<Currency> base = currencyService.findByCode(obj.getBaseCurrency().getCode());
        if(base.isEmpty()){
            throw new IllegalArgumentException("Base currency doesnt exist");
        }
        Optional<Currency> target = currencyService.findByCode(obj.getTargetCurrency().getCode());
        if(target.isEmpty()){
            throw new IllegalArgumentException("Target currency doesnt exist");
        }
        try {
            String sql = "insert into exchange_rates(baseCurrency,targetCurrency,rate) values(?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, base.get().getId());
            statement.setInt(2, target.get().getId());
            statement.setDouble(3, obj.getRate());
            statement.executeUpdate();
        } finally {
            closeStatement(statement);
        }
    }
    public void update(ExchangeRate exchangeRate) throws SQLException{
        PreparedStatement statement = null;
        try {
            String sql = "update exchange_rates set rate = ? where exchange_rates.id = ?";
            statement = connection.prepareStatement(sql);
            statement.setDouble(1, exchangeRate.getRate());
            statement.setInt(2, exchangeRate.getId());
            statement.executeUpdate();
        } finally {
            closeStatement(statement);
        }
    }

    private ExchangeRate parseResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int baseId = resultSet.getInt("baseCurrency");
        Currency base = currencyService.findById(baseId).orElse(null);
        int targetId = resultSet.getInt("targetCurrency");
        Currency target = currencyService.findById(targetId).orElse(null);
        double rate = resultSet.getDouble("rate");
        return new ExchangeRate(id,base,target,rate);
    }

    private void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
