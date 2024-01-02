package com.example.currencyexchange.utils;

import com.example.currencyexchange.models.Exchange;
import com.example.currencyexchange.models.ExchangeRate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.util.Map;

public class ExchangeParser implements Parser<Exchange> {
    private static final String DEFAULT_MESSAGE = "Not correct exchange representation";

    private final ObjectMapper mapper;

    public ExchangeParser() {
        mapper = new ObjectMapper();
    }

    @Override
    public Exchange mapJsonToObject(String json) throws ParseException {
        try {
            Exchange exchange = mapper.readValue(json, Exchange.class);
            return exchange;
        } catch (JsonProcessingException exception) {
            throw new ParseException(DEFAULT_MESSAGE, 0);
        }
    }

    @Override
    public Exchange mapMapToObject(Map<String, String[]> params) throws ParseException {
        return null;
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
