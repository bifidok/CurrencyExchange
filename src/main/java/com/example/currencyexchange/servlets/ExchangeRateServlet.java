package com.example.currencyexchange.servlets;

import com.example.currencyexchange.DTO.ExchangeRateDTO;
import com.example.currencyexchange.models.ExchangeRate;
import com.example.currencyexchange.services.ExchangeRateService;
import com.example.currencyexchange.servlets.abstractServlet.ExchangeRateAbstractServlet;
import com.example.currencyexchange.utils.ExchangeRateParser;
import com.example.currencyexchange.utils.ResponseWriter;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "exchangeRateServlet", value = "/exchangeRate/*")
public class ExchangeRateServlet extends ExchangeRateAbstractServlet {
    private ExchangeRateService exchangeRateService;
    private ExchangeRateParser parser;

    @Override
    public void init(ServletConfig config) throws ServletException {
        exchangeRateService = (ExchangeRateService) config.getServletContext().getAttribute("exchangeRateService");
        parser = (ExchangeRateParser) config.getServletContext().getAttribute("exchangeRateParser");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String exchangeNames = getCodesFromRequest(request);
        String codeBase = exchangeNames.substring(0, 3);
        String codeTarget = exchangeNames.substring(3, 6);
        Optional<ExchangeRate> exchangeOptional = exchangeRateService.findExchangeRate(codeBase, codeTarget);
        if (exchangeOptional.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Exchange rate not found");
            return;
        }
        ExchangeRate exchangeRate = exchangeOptional.get();

        ResponseWriter.write(response, parser, exchangeRate);
    }

    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String exchangeNames = getCodesFromRequest(req);
        String codeBase = exchangeNames.substring(0, 3);
        String codeTarget = exchangeNames.substring(3, 6);
        try {
            var rate = getRateFromRequest(req).substring(5);
            ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO(codeBase, codeTarget,rate);
            exchangeRateService.save(exchangeRateDTO);
            ResponseWriter.write(resp, parser, exchangeRateDTO);
        } catch (IllegalArgumentException exception) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
        } catch (SQLException exception) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    private String getRateFromRequest(HttpServletRequest req) {
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(req.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bufferedReader.lines().collect(Collectors.joining("\n"));
    }
    private String getCodesFromRequest(HttpServletRequest request){
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        String exchangeNames = pathParts[pathParts.length - 1];
        return exchangeNames;
    }
}
