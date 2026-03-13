package service;

import bridge.OrderSystem;
import model.Order;

public class OrderService {
    private final OrderSystem orderSystem;

    public OrderService(OrderSystem orderSystem) {
        this.orderSystem = orderSystem;
    }

    public boolean placeOrder(Order order) {
        System.out.println("[OrderService] Iniciando colocación de pedido " + order.getOrderId());
        try {
            orderSystem.createOrder(order);
            return true;
        } catch (Exception ex) {
            System.out.println("[OrderService] Error al procesar pedido: " + ex.getMessage());
            return false;
        }
    }
}
