package paymentgateway.payment;

import paymentgateway.payment.core.Payment;

import java.lang.reflect.Constructor;
import java.util.logging.Logger;

public class PaymentFactory {
    private static final Logger LOGGER = Logger.getLogger(PaymentFactory.class.getName());

    private PaymentFactory() {}

    public static Payment createPayment(String fullyQualifiedName, Object ... base) {
        Payment record = null;

        if (checkConfig(fullyQualifiedName, base[0])) {
            try {
                Class<?> clz = Class.forName(fullyQualifiedName);
                Constructor<?> constructor = clz.getConstructors()[0];
                record = (Payment) constructor.newInstance(base);
            } catch (IllegalArgumentException e) {
                LOGGER.severe("Failed to create instance of Payment.");
                LOGGER.severe("Given FQN: " + fullyQualifiedName);
                LOGGER.severe("Failed to run: Check your constructor argument");
                System.exit(20);
            } catch (ClassCastException e) {
                LOGGER.severe("Failed to create instance of Payment.");
                LOGGER.severe("Given FQN: " + fullyQualifiedName);
                LOGGER.severe("Failed to cast the object");
                System.exit(30);
            } catch (ClassNotFoundException e) {
                LOGGER.severe("Failed to create instance of Payment.");
                LOGGER.severe("Given FQN: " + fullyQualifiedName);
                LOGGER.severe("Decorator can't be applied to the object");
                System.exit(40);
            } catch (Exception e) {
                LOGGER.severe("Failed to create instance of Payment.");
                LOGGER.severe("Given FQN: " + fullyQualifiedName);
                System.exit(50);
            }
        } else {
            System.out.println("Config Fail");
            System.exit(10);
        }

        return record;
    }

    public static boolean checkConfig(String fullyQualifiedName, Object base) {
        boolean a = true;
        if (fullyQualifiedName.equals("paymentgateway.payment.localbank.PaymentImpl")) {
            String baseku = base.getClass().getCanonicalName();
            a = baseku.equals("paymentgateway.payment.multicurrencysupport.PaymentImpl");
        }

        return a;
    }
}
