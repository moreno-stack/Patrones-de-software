package bridge;

import adapter.PaymentProcessor;
import model.Order;

public abstract class OrderSystem {
    protected final PaymentProcessor paymentProcessor;

    protected OrderSystem(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public void createOrder(Order order) {
        validateOrder(order);
        buildOrder(order);
        boolean success = executePayment(order);
        if (success) {
            confirmOrder(order);
        } else {
            failOrder(order);
        }
    }

    protected abstract void buildOrder(Order order);

    private void validateOrder(Order order) {
        System.out.println("[Bridge] Validando pedido " + order.getOrderId());
        if (order.getAmount() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }
        if (order.getProduct() == null || order.getProduct().isEmpty()) {
            throw new IllegalArgumentException("El producto es obligatorio.");
        }
    }

    private boolean executePayment(Order order) {
        System.out.println("[Bridge] Ejecutando pago con " + paymentProcessor.getGatewayName());
        return paymentProcessor.processPayment(order);
    }

    private void confirmOrder(Order order) {
        System.out.println("[Bridge] Pedido " + order.getOrderId() + " confirmado en " + getSystemName() + ".");
    }

    private void failOrder(Order order) {
        System.out.println("[Bridge] Pedido " + order.getOrderId() + " falló. Se notifica al cliente.");
    }

    protected abstract String getSystemName();
}
