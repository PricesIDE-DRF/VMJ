package paymentgateway.payment.core;

public class PaymentImpl extends PaymentComponent {
    public PaymentImpl(
        String senderAccountName,
        String senderAccountNumber,
        String recipientAccountName,
        String recipientAccountNumber,
        int amount
    ) {
        super(senderAccountName, senderAccountNumber, recipientAccountName, recipientAccountNumber, amount);
    }

    public void processPayment() {
        System.out.printf(
            "Processing payment of %d from %s with account number %s to %s with account number %s...\n",
            super.getAmount(), super.getSenderAccountName(), super.getRecipientAccountNumber(), 
            super.getRecipientAccountName(), super.getRecipientAccountNumber()
        );
    }
    
    public String toString() {
        return super.toString();
    }
}
