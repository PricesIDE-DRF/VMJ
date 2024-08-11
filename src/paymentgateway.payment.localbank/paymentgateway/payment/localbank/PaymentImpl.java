package paymentgateway.payment.localbank;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.core.PaymentDecorator;

public class PaymentImpl extends PaymentDecorator {
    public PaymentImpl(Payment record) {
        super(record);
    }

    public void processPayment() {
        super.record.processPayment();
        System.out.println("Processing payment with Local Bank...");
    }

    public String toString() {
        return super.toString() + " processing with local bank";
    }
}
