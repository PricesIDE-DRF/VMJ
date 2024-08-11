package paymentgateway.product.marketplace;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.PaymentFactory;

import java.io.*;
import java.util.StringTokenizer;

public class Marketplace {
    private static InputReader in;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);

        int process = 1;
        while (process == 1) {
            System.out.print("Sender account name: ");
            String senderAccountName = in.next();
            System.out.print("Sender account number: ");
            String senderAccountNumber = in.next();
            System.out.print("Sender currency code: ");
            String senderCurrencyCode = in.next();
            System.out.print("Recipient account name: ");
            String recipientAccountName = in.next();
            System.out.print("Recipient account number: ");
            String recipientAccountNumber = in.next();
            System.out.print("Recipient currency code: ");
            String recipientCurrencyCode = in.next();
            System.out.print("Amount: ");
            int amount = Integer.parseInt(in.next());
            
            Payment core = PaymentFactory.createPayment(
                "paymentgateway.payment.core.PaymentImpl",
                senderAccountName, senderAccountNumber, recipientAccountName,
                recipientAccountNumber, amount
            );
            Payment multiCurrencySupport = PaymentFactory.createPayment(
                "paymentgateway.payment.multicurrencysupport.PaymentImpl",
                core, senderCurrencyCode.toUpperCase(), recipientCurrencyCode.toUpperCase()
            );
            Payment localBank = PaymentFactory.createPayment(
                "paymentgateway.payment.localbank.PaymentImpl",
                multiCurrencySupport
            );
            Payment fraudDetection = PaymentFactory.createPayment(
                "paymentgateway.payment.frauddetection.PaymentImpl", 
                localBank
            );
            fraudDetection.processPayment();
            
            process = processUserChoice();
        }
    }

    private static int processUserChoice() {
        System.out.println();
        System.out.print("Press 1 if you want to make another payment: ");
        String userChoice = in.next();
        int process = 0;
        try {
            process = Integer.parseInt(userChoice);
        } catch (Exception e) { }

        if (process == 1) System.out.println();
        else System.out.print("Thank you for using our service.");

        return process;
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }
    }
}
