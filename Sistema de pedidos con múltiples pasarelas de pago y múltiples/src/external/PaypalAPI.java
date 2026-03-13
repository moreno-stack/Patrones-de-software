package external;

import model.Order;

public class PaypalAPI {
    public boolean sendPayment(double amount, String currency, String description) {
        System.out.println("[PaypalAPI] Enviando pago: " + amount + " " + currency + " -> " + description);
        // Simular regla de negocio: Paypal no envía menos de 5
        if (amount < 5) {
            System.out.println("[PaypalAPI] Monto menor al mínimo. Pago rechazado.");
            return false;
        }
        return true;
    }
}
