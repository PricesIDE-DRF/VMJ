package paymentgateway.payment.core;

public abstract class PaymentDecorator extends PaymentComponent {
    public Payment record;

    public PaymentDecorator(Payment record) {
        this.record = record;
    }

    public String getSenderAccountName() {
        return record.getSenderAccountName();
    }

    public String getSenderAccountNumber() {
        return record.getSenderAccountNumber();
    }

    public String getRecipientAccountName() {
        return record.getRecipientAccountName();
    }

    public String getRecipientAccountNumber() {
        return record.getRecipientAccountNumber();
    }

    public int getAmount() {
        return record.getAmount();
    }

    public abstract void processPayment();

    public String toString() {
        return super.toString();
    }
}
