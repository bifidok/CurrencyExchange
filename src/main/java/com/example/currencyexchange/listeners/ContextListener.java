package com.example.currencyexchange.listeners;

import com.example.currencyexchange.models.ExchangeRate;
import com.example.currencyexchange.repositories.CurrencyRepository;
import com.example.currencyexchange.services.CurrencyService;
import com.example.currencyexchange.services.ExchangeRateService;
import com.example.currencyexchange.services.ExchangeService;
import com.example.currencyexchange.utils.CurrencyParser;
import com.example.currencyexchange.utils.ExchangeParser;
import com.example.currencyexchange.utils.ExchangeRateParser;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        CurrencyService currencyService = new CurrencyService();
        ExchangeRateService exchangeRateService = new ExchangeRateService();
        ExchangeService exchangeService = new ExchangeService();
        CurrencyParser currencyParser = new CurrencyParser();
        ExchangeParser exchangeParser = new ExchangeParser();
        ExchangeRateParser exchangeRateParser = new ExchangeRateParser();

        context.setAttribute("currencyService",currencyService);
        context.setAttribute("exchangeRateService",exchangeRateService);
        context.setAttribute("exchangeService",exchangeService);
        context.setAttribute("currencyParser",currencyParser);
        context.setAttribute("exchangeParser",exchangeParser);
        context.setAttribute("exchangeRateParser",exchangeRateParser);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
