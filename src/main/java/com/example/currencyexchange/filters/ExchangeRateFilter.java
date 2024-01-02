package com.example.currencyexchange.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/exchangeRate/*")
public class ExchangeRateFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String pathInfo = ((HttpServletRequest) servletRequest).getPathInfo();
        String[] pathParts = pathInfo.split("/");
        if (pathParts.length == 0) {
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Path does not contains a codes");
            return;
        }
        if (pathParts[pathParts.length - 1].length() != 6) {
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Not correct exchange name, it should contains pair with length equal to 6");
            return;
        }
        servletResponse.setContentType("application/json");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
