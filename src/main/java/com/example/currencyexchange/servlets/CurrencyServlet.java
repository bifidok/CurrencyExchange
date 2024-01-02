package com.example.currencyexchange.servlets;

import com.example.currencyexchange.models.Currency;
import com.example.currencyexchange.repositories.CrudRepository;
import com.example.currencyexchange.repositories.CurrencyRepository;
import com.example.currencyexchange.services.CurrencyService;
import com.example.currencyexchange.utils.CurrencyParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(name = "currencyServlet", value = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyService service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        service = (CurrencyService) config.getServletContext().getAttribute("currencyService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        String code = pathParts[pathParts.length - 1];
        Optional<Currency> currencyOptional = null;
        try {
            currencyOptional = service.findByCode(code);
        }catch (IllegalArgumentException exception){
            response.sendError(HttpServletResponse.SC_NOT_FOUND,exception.getMessage());
            return;
        }
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Currency currency = currencyOptional.get();
        String json = objectMapper.writeValueAsString(currency);
        out.print(json);
        out.flush();
    }
}
