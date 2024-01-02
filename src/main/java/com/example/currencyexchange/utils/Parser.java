package com.example.currencyexchange.utils;

import java.text.ParseException;
import java.util.Map;

public interface Parser<T> {
    T mapJsonToObject(String json) throws ParseException;

    T mapMapToObject(Map<String, String[]> params) throws ParseException;

    String mapObjectToJson(Object obj) throws ParseException;
}
