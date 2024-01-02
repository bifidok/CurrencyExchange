package com.example.currencyexchange.servlets;

import com.example.currencyexchange.DTO.ExchangeRateDTO;
import com.example.currencyexchange.models.ExchangeRate;
import com.example.currencyexchange.services.ExchangeRateService;
import com.example.currencyexchange.utils.BodyReader;
import com.example.currencyexchange.utils.ExchangeRateParser;
import com.example.currencyexchange.utils.ResponseWriter;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

@WebServlet(name = "exchangeRatesServlet", value = "/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService;
    private ExchangeRateParser parser;

    @Override
    public void init(ServletConfig config) throws ServletException {
        exchangeRateService = (ExchangeRateService) config.getServletContext().getAttribute("exchangeRateService");
        parser = (ExchangeRateParser) config.getServletContext().getAttribute("exchangeRateParser");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        List<ExchangeRate> currencies = exchangeRateService.findAll();
        response.setContentType("application/json");

        ResponseWriter.write(response, parser, currencies);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            var parameterMap = request.getParameterMap();
            ExchangeRateDTO exchangeRateDTO = parser.mapMapToObject(parameterMap);
            exchangeRateService.create(exchangeRateDTO);
            ResponseWriter.write(response, parser, exchangeRateDTO);
        } catch (ParseException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
        } catch (SQLException exception) {
            response.sendError(HttpServletResponse.SC_CONFLICT, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, exception.getMessage());
        }
    }
}
