package paymentgateway.payment.multicurrencysupport;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentDecorator;

import java.util.*;
import java.lang.reflect.Type;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.HttpURLConnection;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PaymentImpl extends PaymentDecorator {
    protected static final String BASE_API_URL = "https://api.exchangerate-api.com/v4/latest";
    protected String originCurrency;
    protected String destinationCurrency;

    public PaymentImpl(Payment record, String originCurrency, String destinationCurrency) {
        super(record);
        this.originCurrency = originCurrency;
        this.destinationCurrency = destinationCurrency;
    }

    public void processPayment() {
        try {
            URL url = new URL(String.format("%s/%s", BASE_API_URL, originCurrency));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            connection.disconnect();

            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> decodedData = gson.fromJson(response.toString(), mapType);
            Map<String, Object> rates = (Map<String, Object>) decodedData.get("rates");
            Double originCurrencyRate = ((Number) rates.get(originCurrency)).doubleValue();
            Double destinationCurrencyRate = ((Number) rates.get(destinationCurrency)).doubleValue();
            double destinationCurrencyAmount = super.getAmount() / originCurrencyRate * destinationCurrencyRate;

            System.out.printf(
                "Origin currency amount: %d\nDestination currency amount: %.2f\n",
                super.getAmount(), destinationCurrencyAmount
            );
            System.out.printf(
                "Processing payment from %s with account number %s of %d (%s) to %s with account number %s of %.2f (%s)...\n",
                super.getSenderAccountName(), super.getSenderAccountNumber(), super.getAmount(), originCurrency,
                super.getRecipientAccountName(), super.getRecipientAccountNumber(), destinationCurrencyAmount, destinationCurrency
            );
        } catch (FileNotFoundException e) {
            handleCurrencyNotFound(originCurrency);
        } catch (NullPointerException e) {
            handleCurrencyNotFound(destinationCurrency);
        } catch (Exception e) {
            System.out.print("There is an error in our system, please try again later.");
            System.exit(1);
        }
    }

    private static void handleCurrencyNotFound(String currencyCode) {
        System.out.printf(
            "Currency with the code %s is not yet available in our system.", currencyCode);
        System.exit(1);
    }

    public String toString() {
        return String.format(
            "%s from %s currency to %s currency", 
            super.toString(), originCurrency.toUpperCase(), destinationCurrency.toUpperCase()
        );
    }
}
