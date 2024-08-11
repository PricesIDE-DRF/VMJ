package paymentgateway.payment.core;

public interface Payment {
    void processPayment();
    String getSenderAccountName();
    String getSenderAccountNumber();
    String getRecipientAccountName();
    String getRecipientAccountNumber();
    int getAmount();
}