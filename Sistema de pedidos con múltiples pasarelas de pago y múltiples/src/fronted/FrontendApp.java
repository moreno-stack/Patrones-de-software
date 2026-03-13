package fronted;

import adapter.LegacyBankAdapter;
import adapter.PaymentProcessor;
import adapter.PaypalAdapter;
import adapter.StripeAdapter;
import bridge.MobileOrderSystem;
import bridge.WebOrderSystem;
import controller.OrderController;
import external.LegacyBankSystem;
import external.PaypalAPI;
import external.StripeService;
import model.Order;
import service.OrderService;

public class FrontendApp {
    public static void main(String[] args) {
        System.out.println("=== Caso de estudio real: Adapter + Bridge con múltiples pasarelas ===");

        // Paso 1: Creamos adaptadores para versiones legacy / APIs diferentes
        PaymentProcessor paypalAdapter = new PaypalAdapter(new PaypalAPI());
        PaymentProcessor stripeAdapter = new StripeAdapter(new StripeService(), "4242424242424242");
        PaymentProcessor legacyBankAdapter = new LegacyBankAdapter(new LegacyBankSystem(), "ACC-ORIGEN-123", "ACC-DESTINO-456");

        // Paso 2: Creamos orders para dos canales (Bridge)
        Order orderWeb = new Order(101, "Combo Pizza", 18000, "USD");
        Order orderMobile = new Order(102, "Ensalada Fit", 150, "USD");

        // Paso 3: Conecta puente (bridge) con implementador de pago
        WebOrderSystem webSystem = new WebOrderSystem(paypalAdapter); // Bridge + implementor adaptado
        MobileOrderSystem mobileSystem = new MobileOrderSystem(stripeAdapter);

        // Paso 4: Servicio y controlador (orfan) con lógica de flujo
        OrderService webOrderService = new OrderService(webSystem);
        OrderController webController = new OrderController(webOrderService);

        System.out.println("\n-- Pedido desde web con Paypal --");
        webController.createOrder(orderWeb.getOrderId(), orderWeb.getProduct(), orderWeb.getAmount(), orderWeb.getCurrency());

        System.out.println("\n-- Pedido desde móvil con Stripe --");
        OrderService mobileOrderService = new OrderService(mobileSystem);
        OrderController mobileController = new OrderController(mobileOrderService);
        mobileController.createOrder(orderMobile.getOrderId(), orderMobile.getProduct(), orderMobile.getAmount(), orderMobile.getCurrency());

        System.out.println("\n-- Pedido grande con gateway legacy por fallback de bridge --");
        // Simular swap de implementador en runtime (puente reutilizable)
        WebOrderSystem webLegacySystem = new WebOrderSystem(legacyBankAdapter);
        OrderService fallbackService = new OrderService(webLegacySystem);
        OrderController fallbackController = new OrderController(fallbackService);
        fallbackController.createOrder(103, "Laptop Pro", 1800, "USD");

        System.out.println("\n=== Fin del caso de estudio ===");
    }
}
