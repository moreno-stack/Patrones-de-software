package adapter;

import external.StripeService;
import model.Order;

public class StripeAdapter implements PaymentProcessor {
    private final StripeService stripeService;
    private final String cardNumber;

    public StripeAdapter(StripeService stripeService, String cardNumber) {
        this.stripeService = stripeService;
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean processPayment(Order order) {
        String desc = "Pedido " + order.getOrderId() + " en Stripe";
        System.out.println("[Adapter][Stripe] Adaptando order a StripeService");
        return stripeService.pay(cardNumber, order.getAmount(), desc);
    }

    @Override
    public String getGatewayName() {
        return "Stripe";
    }
}
