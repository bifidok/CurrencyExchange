package com.example.currencyexchange.servlets;

import com.example.currencyexchange.models.Currency;
import com.example.currencyexchange.services.CurrencyService;
import com.example.currencyexchange.utils.CurrencyParser;
import com.example.currencyexchange.utils.ResponseWriter;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

@WebServlet(name = "currenciesServlet", value = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    private CurrencyService service;
    private CurrencyParser parser;

    @Override
    public void init(ServletConfig config) throws ServletException {
        service = (CurrencyService) config.getServletContext().getAttribute("currencyService");
        parser = (CurrencyParser) config.getServletContext().getAttribute("currencyParser");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Currency> currencies = service.findAll();
        ResponseWriter.write(response, parser, currencies);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Currency currency = null;
        var a = request.getParameterMap();
        try {
            currency = parser.mapMapToObject(request.getParameterMap());
            service.create(currency);
            ResponseWriter.write(response, parser, currency);
        } catch (ParseException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
        } catch (SQLException exception) {
            response.sendError(HttpServletResponse.SC_CONFLICT, exception.getMessage());
        }
    }
}