package paymentgateway.payment.core;

public abstract class PaymentComponent implements Payment {
    protected String senderAccountName;
    protected String senderAccountNumber;
    protected String recipientAccountName;
    protected String recipientAccountNumber;
    protected int amount;

    public PaymentComponent(){ }

    public PaymentComponent(
        String senderAccountName,
        String senderAccountNumber,
        String recipientAccountName,
        String recipientAccountNumber,
        int amount
    ){
        this.senderAccountName = senderAccountName;
        this.senderAccountNumber = senderAccountNumber;
        this.recipientAccountName = recipientAccountName;
        this.recipientAccountNumber = recipientAccountNumber;
        this.amount = amount;
    }

    public String getSenderAccountName() {
        return this.senderAccountName;
    }

    public String getSenderAccountNumber() {
        return this.senderAccountNumber;
    }

    public String getRecipientAccountName() {
        return this.recipientAccountName;
    }

    public String getRecipientAccountNumber() {
        return this.recipientAccountNumber;
    }

    public int getAmount() {
        return this.amount;
    }

    public abstract void processPayment();

    public String toString() {
        return String.format(
            "Payment of %d from %s with account number %s to %s with account number %s", 
            amount, senderAccountName, senderAccountNumber, recipientAccountName, recipientAccountNumber
        );
    }
}
