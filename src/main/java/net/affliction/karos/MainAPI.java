package net.affliction.karos;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.*;


public class MainAPI {

    public static Connection connection = null;

    public static String
    removeNonAlphanumeric(String str)
    {
        // replace the given string
        // with empty string
        // except the pattern "[^a-zA-Z0-9]"
        str = str.replaceAll(
                "[^a-zA-Z0-9.\\-]", "");

        // return string
        return str;
    }
    public static void main(String[] args)
    {
        System.out.println("Loading Karos Activation Rest API...");
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Database.createDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            HttpServer authServer = HttpServer.create(new InetSocketAddress("0.0.0.0", 9191), 0);
            authServer.setExecutor(null);
            authServer.createContext("/validate", new ValidatePurchaseContext());
            authServer.start();
        } catch(Exception ex)
        {
            ex.printStackTrace();
        }
        System.out.println("Karos Activation API Started! Waiting for commands...");
        while(true)
        {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));
            try {
                String command = reader.readLine();
                if(command.equalsIgnoreCase("product"))
                {
                    System.out.println("Please enter product name");
                    String productName = reader.readLine();
                    System.out.println("Please enter the max amount of validations allowed. Use -1 for unlimited.");
                    String maxActivations = reader.readLine();
                    try {
                        int max = Integer.parseInt(maxActivations);
                        if(Database.insertOrUpdateProduct(productName, max))
                        {
                            System.out.println("Product added or updated.");
                        } else {
                            System.out.println("Unable to add or update that product for some reason!");
                        }
                    } catch(Exception ex)
                    {
                        ex.printStackTrace();
                        System.out.println("That doesn't look like a number or an error occurred.");
                    }
                }
                if(command.equalsIgnoreCase("reset"))
                {
                    System.out.println("Please enter product name");
                    String productName = reader.readLine();
                    System.out.println("Please enter order id");
                    String orderId = reader.readLine();
                    if(Database.resetActivations(productName, orderId))
                    {
                        System.out.println("Order activations reset!");
                    } else {
                        System.out.println("Unable to reset activations!");
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }
    public static void writeResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    public static Map<String, String> processQuery(String query){
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length>1) {
                result.put(pair[0], pair[1]);
            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }

}
