package com.example.currencyexchange.utils;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

public class ResponseWriter {
    private ResponseWriter() {

    }

    public static void write(HttpServletResponse response, Parser parser, Object obj) throws IOException {
        PrintWriter out = response.getWriter();
        String json = null;
        try {
            json = parser.mapObjectToJson(obj);
        } catch (ParseException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            return;
        }
        out.print(json);
        out.flush();
    }
}
