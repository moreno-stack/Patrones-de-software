package adapter;

import external.PaypalAPI;
import model.Order;

public class PaypalAdapter implements PaymentProcessor {
    private final PaypalAPI paypalAPI;

    public PaypalAdapter(PaypalAPI paypalAPI) {
        this.paypalAPI = paypalAPI;
    }

    @Override
    public boolean processPayment(Order order) {
        String description = "Pedido " + order.getOrderId() + " - " + order.getProduct();
        System.out.println("[Adapter][Paypal] Adaptando order a PaypalAPI");
        return paypalAPI.sendPayment(order.getAmount(), order.getCurrency(), description);
    }

    @Override
    public String getGatewayName() {
        return "Paypal";
    }
}
