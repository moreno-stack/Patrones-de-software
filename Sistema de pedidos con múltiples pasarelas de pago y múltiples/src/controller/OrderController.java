package controller;

import model.Order;
import service.OrderService;

public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public void createOrder(int orderId, String product, double amount, String currency) {
        System.out.println("[OrderController] Recibido pedido: " + orderId + " -> " + product + " " + amount);
        Order order = new Order(orderId, product, amount, currency);

        boolean success = orderService.placeOrder(order);
        if (!success) {
            System.out.println("[OrderController] Intentando gateway alternativo...");
            tryFallback(order);
        }
    }

    private void tryFallback(Order order) {
        // El fallback se configura fuera del controlador en una aplicación real.
        System.out.println("[OrderController] Fallback no configurado en este caso. Cancelando pedido " + order.getOrderId());
    }
}
