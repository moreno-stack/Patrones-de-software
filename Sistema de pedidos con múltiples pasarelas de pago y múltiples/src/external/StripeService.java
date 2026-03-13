package external;

import model.Order;

public class StripeService {
    public boolean pay(String cardNumber, double amount, String description) {
        System.out.println("[StripeService] Procesando con tarjeta final " + cardNumber.substring(cardNumber.length()-4) + " para " + amount);
        // Simular validación de tarjeta y límite
        if (cardNumber.length() != 16 || amount <= 0) {
            return false;
        }
        return amount <= 10000; // simulamos límite de transacción
    }
}
