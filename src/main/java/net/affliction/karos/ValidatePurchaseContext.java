package net.affliction.karos;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class ValidatePurchaseContext implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestURI().getQuery() == null)
        {
            MainAPI.writeResponse(exchange, "Invalid");
            return;
        }
        Map<String,String> query = MainAPI.processQuery(exchange.getRequestURI().getQuery());
        if(!query.containsKey("productname"))
        {
            MainAPI.writeResponse(exchange, "Invalid");
            return;
        }
        if(!query.containsKey("orderid"))
        {
            MainAPI.writeResponse(exchange, "Invalid");
            return;
        }
        if(!query.containsKey("uuid"))
        {
            MainAPI.writeResponse(exchange, "Invalid");
            return;
        }
        final String productName = query.get("productname").toLowerCase();
        final String orderId = query.get("orderid");
        final String uuid = query.get("uuid");
        MainAPI.writeResponse(exchange, Database.validateActivation(productName, orderId, uuid));
    }
}
