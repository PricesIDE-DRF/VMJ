package paymentgateway.payment.localbank;

import paymentgateway.payment.core.Payment;
import paymentgateway.payment.localbank.DLocalBank;

public class PaymentImpl extends DLocalBank {
    public PaymentImpl(Payment record) {
        super(record);
    }
}
