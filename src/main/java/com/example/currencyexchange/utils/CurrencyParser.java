package com.example.currencyexchange.utils;

import com.example.currencyexchange.models.Currency;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.util.Map;

public class CurrencyParser implements Parser<Currency>{
    private static final String DEFAULT_MESSAGE = "Not correct currency representation";

    private final ObjectMapper mapper;

    public CurrencyParser() {
        mapper = new ObjectMapper();
    }
    @Override
    public Currency mapJsonToObject(String json) throws ParseException {
        Currency currency = null;
        try {
            currency = mapper.readValue(json, Currency.class);
        } catch (JsonProcessingException exception) {
            throw new ParseException(DEFAULT_MESSAGE, 0);
        }
        return currency;
    }

    @Override
    public Currency mapMapToObject(Map<String, String[]> params) throws ParseException {
        String code = params.get("code")[0];
        String name = params.get("name")[0];
        String sign = params.get("sign")[0];
        if(code == null || name == null || sign == null){
            throw new ParseException(DEFAULT_MESSAGE,0);
        }
        return new Currency(code,name,sign);
    }

    @Override
    public String mapObjectToJson(Object obj) throws ParseException {
        String json = null;
        try {
            json = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException exception) {
            throw new ParseException(DEFAULT_MESSAGE, 0);
        }
        return json;
    }
}
