package bridge;

import adapter.PaymentProcessor;
import model.Order;

public class MobileOrderSystem extends OrderSystem {
    public MobileOrderSystem(PaymentProcessor paymentProcessor) {
        super(paymentProcessor);
    }

    @Override
    protected void buildOrder(Order order) {
        System.out.println("[MobileOrderSystem] Generando UI de pedido móvil para " + order.getProduct());
    }

    @Override
    protected String getSystemName() {
        return "Móvil";
    }
}
