package com.example.currencyexchange.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    List<T> findAll();
    Optional<T> findById(int id);

    void create(T obj) throws SQLException;
}
