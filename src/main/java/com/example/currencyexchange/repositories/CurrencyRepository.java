package com.example.currencyexchange.repositories;

import com.example.currencyexchange.models.Currency;
import com.example.currencyexchange.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyRepository implements CrudRepository<Currency> {
    private final Connection connection;

    public CurrencyRepository() {
        connection = DatabaseConnector.getConnection();
    }

    public List<Currency> findAll() {
        List<Currency> currencies = new ArrayList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String sql = "select * from currencies";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                currencies.add(parseResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement(statement);
        }
        return currencies;
    }

    public Optional<Currency> findByCode(String name) {
        Currency currency = null;
        PreparedStatement statement = null;
        try {
            String sql = "select * from currencies where currencies.code = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                currency = parseResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement(statement);
        }
        return Optional.ofNullable(currency);
    }

    @Override
    public Optional<Currency> findById(int id) {
        Currency currency = null;
        PreparedStatement statement = null;
        try {
            String sql = "select * from currencies where currencies.id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                currency = parseResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement(statement);
        }
        return Optional.ofNullable(currency);
    }

    @Override
    public void create(Currency obj) throws SQLException {
        PreparedStatement statement = null;
        try {
            String sql = "insert into currencies(code,name,sign) values(?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, obj.getCode());
            statement.setString(2, obj.getName());
            statement.setString(3, obj.getSign());
            statement.executeUpdate();
        } finally {
            closeStatement(statement);
        }
    }
    private Currency parseResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String code = resultSet.getString("code");
        String fullName = resultSet.getString("name");
        String sign = resultSet.getString("sign");
        return new Currency(id,code,fullName,sign);
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
