package net.affliction.karos;

import com.google.gson.Gson;
import net.affliction.karos.enums.Datum;
import net.affliction.karos.enums.OrderData;
import net.affliction.karos.enums.OrderItem;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static net.affliction.karos.MainAPI.connection;
import static net.affliction.karos.MainAPI.removeNonAlphanumeric;

public class Database {

    public static String apiURL = System.getProperty("APIUrl");

    public static String username = URLEncoder.encode(System.getProperty("APIUser"));
    public static String password = URLEncoder.encode(System.getProperty("APIPassword"));
    public static void createDB()
    {
        if(connection != null)
        {
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.setQueryTimeout(30);  // set timeout to 30 sec.
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS products (productId TEXT, maxActivations INTEGER);");
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS activations (orderId TEXT, productId TEXT, currentActivations INTEGER);");
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS activations_data (orderId TEXT, uuid TEXT, productId TEXT);");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static boolean resetActivations(String productId, String orderId)
    {
        productId = productId.toLowerCase();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet resultSet2 = statement.executeQuery("SELECT count(*) FROM products WHERE productId='" + productId +"'");
            while(resultSet2.next()) {
                int resnum = resultSet2.getInt(1);
                if(resnum < 1)
                {
                    System.out.println("That product id doesn't exist.");
                    return false;
                }
            }
            ResultSet resultSet = statement.executeQuery("SELECT count(*) FROM activations WHERE productId='" + productId +"' AND orderId='" + orderId + "'");
            while(resultSet.next()) {
                int resnum = resultSet.getInt(1);
                if(resnum < 1)
                {
                    System.out.println("That order has not yet activated.");
                    return false;
                } else {
                    statement.executeUpdate("DELETE FROM activations_data WHERE orderid='" + orderId + "' AND productId='" + productId + "'");
                    statement.executeUpdate("DELETE FROM activations WHERE orderid='" + orderId + "' AND productId='" + productId + "'");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    public static boolean insertOrUpdateProduct(String productId, int maxActivations)
    {
        productId = productId.toLowerCase();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet resultSet = statement.executeQuery("SELECT * FROM products WHERE productId='" + productId +"'");
            ResultSet resultSet2 = statement.executeQuery("SELECT count(*) FROM products WHERE productId='" + productId +"'");
            while(resultSet2.next()) {
                int resnum = resultSet2.getInt(1);
                if(resnum < 1)
                {
                    statement.executeUpdate("insert into products values('" + productId + "', '" + maxActivations + "')");
                    return true;
                }
            }
            resultSet.beforeFirst();
            while(resultSet.next())
            {
                statement.executeUpdate("UPDATE products set maxActivations='" + maxActivations + "' WHERE productId='" + productId + "')");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    public static int getMaxActivations(String productId)
    {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet resultSet2 = statement.executeQuery("SELECT count(*) FROM products WHERE productId='" + productId +"'");
            while(resultSet2.next())
            {
               if(resultSet2.getInt(1) == 0)
               {
                   return 0;
               }
            }
            ResultSet resultSet = statement.executeQuery("SELECT * FROM products WHERE productId='" + productId +"'");
            while(resultSet.next())
            {
                return resultSet.getInt("maxActivations");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }
    public static int getActivationCount(String orderId)
    {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet resultSet2 = statement.executeQuery("SELECT count(*) FROM activations WHERE orderId='" + orderId +"'");
            while(resultSet2.next()) {
                return resultSet2.getInt(1);
            }
            return -2;
        } catch (SQLException e) {
            e.printStackTrace();
            return -2;
        }
    }

    public static boolean activationExists(String orderId, String uuid)
    {
        if(getActivationCount(orderId) == 0)
        {
            return false;
        }
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet resultSet = statement.executeQuery("SELECT count(*) FROM activations_data WHERE orderId='" + orderId +"' AND uuid='"+uuid+"'");
            while(resultSet.next()) {
                if(resultSet.getInt(1) == 0) {
                    return false;
                } else {
                    return true;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Default to true since there is some sort of error
        }
    }
    // statement.executeUpdate("CREATE TABLE IF NOT EXISTS activations (orderId TEXT, productId TEXT, currentActivations INTEGER);");
    public static String validateActivation(String productName, String orderId, String uuid)
    {
        productName = removeNonAlphanumeric(productName); // Filter out nonsense

        orderId = removeNonAlphanumeric(orderId); // Filter out nonsense

        OkHttpClient client = new OkHttpClient();
        Request orderRequest1 = new Request.Builder()
                .url(apiURL+"/api/v3/seller/orders/details/"+orderId+"?email="+username+"&password=" + password)
                .get()
                .build();

        try {
            Response orderResponse1 = client.newCall(orderRequest1).execute();
            if(!orderResponse1.isSuccessful())
            {
                return "ERROR";
            }
            String orderJsonData = orderResponse1.body().string();
            OrderData orderDataArray = new Gson().fromJson(orderJsonData, OrderData.class);
            if(orderDataArray.getData().size() == 0)
            {
                return "INVALID_ORDER";
            }
            Datum orderData = orderDataArray.getData().get(0);
            if(!orderData.getOrderCode().equalsIgnoreCase(orderId))
            {
                return "ERROR";
            }
            if(!orderData.getPaymentStatus().equalsIgnoreCase("Paid"))
            {
                return "NOT_PAID";
            }
            boolean foundItem = false;
            for(OrderItem item : orderData.getOrderItems())
            {
                if(item.getName().equalsIgnoreCase(productName))
                {
                    foundItem = true;
                    break;
                }
            }
            if(!foundItem)
            {
                return "INVALID_ORDER_FOR_PRODUCT";
            }
            final int maxActivations = getMaxActivations(productName);
            if(maxActivations == 0)
            {
                return "INVALID_PRODUCT";
            }
            if(maxActivations == -2)
            {
                return "PRODUCT_DISABLED";
            }
            if(activationExists(orderId, uuid)) // Check if they are activated already
            {
                return "SUCCESS";
            }
            final int activationCount = getActivationCount(orderId);
            if(maxActivations != -1 && activationCount >= maxActivations)
            {
                return "NO_MORE_ACTIVATIONS";
            }
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.setQueryTimeout(30);  // set timeout to 30 sec.
                statement.executeUpdate("insert into activations values('" + orderId + "', '" + productName +"', '" + (activationCount + 1) + "')");
                statement.executeUpdate("insert into activations_data values('" + orderId + "', '" + uuid + "', '" + productName + "')");
                return "SUCCESS";
            } catch (SQLException e) {
                e.printStackTrace();
                return "ERROR_ACTIVATING";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}
