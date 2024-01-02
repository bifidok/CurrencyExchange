package com.example.currencyexchange.utils;

import com.example.currencyexchange.DTO.ExchangeRateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.util.Map;

public class ExchangeRateParser implements Parser<ExchangeRateDTO> {
    private static final String DEFAULT_MESSAGE = "Not correct exchange rate representation";

    private final ObjectMapper mapper;

    public ExchangeRateParser() {
        mapper = new ObjectMapper();
    }

    @Override
    public ExchangeRateDTO mapJsonToObject(String json) throws ParseException {
        try {
            ExchangeRateDTO exchangeRateDTO = mapper.readValue(json, ExchangeRateDTO.class);
            return exchangeRateDTO;
        } catch (JsonProcessingException exception) {
            throw new ParseException(DEFAULT_MESSAGE, 0);
        }
    }

    @Override
    public ExchangeRateDTO mapMapToObject(Map<String, String[]> params) throws ParseException {
        try {
            String base = params.get("baseCurrencyCode")[0];
            String target = params.get("targetCurrencyCode")[0];
            String rate = params.get("rate")[0];
            return new ExchangeRateDTO(base, target, rate);
        } catch (Exception exception) {
            throw new ParseException(DEFAULT_MESSAGE, 0);
        }
    }

    @Override
    public String mapObjectToJson(Object obj) throws ParseException {
        try {
            String json = mapper.writeValueAsString(obj);
            return json;
        } catch (JsonProcessingException exception) {
            throw new ParseException(DEFAULT_MESSAGE, 0);
        }
    }
}
