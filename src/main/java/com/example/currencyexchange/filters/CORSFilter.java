package com.example.currencyexchange.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class CORSFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ((HttpServletResponse)servletResponse).addHeader("Access-Control-Allow-Origin", "*");
        ((HttpServletResponse)servletResponse).addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PATCH, HEAD");
        ((HttpServletResponse)servletResponse).addHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With,accept" +
                ",Origin,Access-Control-Request-Method,Access-Control-Request-Headers");
        ((HttpServletResponse)servletResponse).addHeader("Access-Control-Max-Age", "1728000");
        servletResponse.setContentType("application/json");
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
