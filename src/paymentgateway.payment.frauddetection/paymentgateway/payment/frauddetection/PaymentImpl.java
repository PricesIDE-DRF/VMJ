package paymentgateway.payment.frauddetection;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentDecorator;

import java.util.*;

public class PaymentImpl extends PaymentDecorator {
    public PaymentImpl(Payment record) {
        super(record);
    }

    public void processPayment() {
        super.record.processPayment();
        
        System.out.println("Detecting fraud in the transaction...");
        Random random = new Random();
        int randomInt = random.nextInt();
        if (randomInt %2 == 0) {
            System.out.println("No fraud has been detected, the transaction proceeded smoothly!");
        } else {
            System.out.println("Fraud has been detected, please review the transaction further!");
        }
    }
    
    public String toString() {
        return super.toString() + " with fraud detection";
    }
}
