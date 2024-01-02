package com.example.currencyexchange.servlets;

import com.example.currencyexchange.models.Exchange;
import com.example.currencyexchange.models.ExchangeRate;
import com.example.currencyexchange.services.ExchangeRateService;
import com.example.currencyexchange.services.ExchangeService;
import com.example.currencyexchange.utils.ExchangeParser;
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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Optional;

@WebServlet(name = "exchangeServlet", value = "/exchange")
public class ExchangeServlet extends HttpServlet {
    private ExchangeService exchangeService;
    private ExchangeParser parser;

    @Override
    public void init(ServletConfig config) throws ServletException {
        exchangeService = (ExchangeService) config.getServletContext().getAttribute("exchangeService");
        parser = (ExchangeParser) config.getServletContext().getAttribute("exchangeParser");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amountStr = req.getParameter("amount");
        double amount = Double.parseDouble(amountStr);
        Optional<ExchangeRate> exchangeRateOptional = exchangeService.findExchangeRate(from, to);
        if (exchangeRateOptional.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Currency pair not found");
            return;
        }
        ExchangeRate exchangeRate = exchangeRateOptional.get();
        Exchange exchange = new Exchange(exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),
                exchangeRate.getRate(),
                amount,
                new DecimalFormat("#.##").format(amount * exchangeRate.getRate()));

        ResponseWriter.write(resp,parser,exchange);
    }
}
