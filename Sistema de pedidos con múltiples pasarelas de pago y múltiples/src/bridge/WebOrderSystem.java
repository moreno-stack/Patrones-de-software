package bridge;

import adapter.PaymentProcessor;
import model.Order;

public class WebOrderSystem extends OrderSystem {
    public WebOrderSystem(PaymentProcessor paymentProcessor) {
        super(paymentProcessor);
    }

    @Override
    protected void buildOrder(Order order) {
        System.out.println("[WebOrderSystem] Construyendo pedido web: " + order.getProduct() + " por " + order.getAmount());
    }

    @Override
    protected String getSystemName() {
        return "Web";
    }
}
