package web;

import adapter.LegacyBankAdapter;
import adapter.PaymentProcessor;
import adapter.PaypalAdapter;
import adapter.StripeAdapter;
import bridge.MobileOrderSystem;
import bridge.WebOrderSystem;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import external.LegacyBankSystem;
import external.PaypalAPI;
import external.StripeService;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import model.Order;
import service.OrderService;

public class WebServer {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new HtmlHandler());
        server.createContext("/order", new OrderHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Servidor web corriendo en http://localhost:" + port);
    }

    static class HtmlHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String html = "<!DOCTYPE html>"
                    + "<html><head><meta charset='UTF-8'><title>Sistema de pedidos Adapter+Bridge</title></head><body>"
                    + "<h1>Sistema de pedidos (Adapter + Bridge)</h1>"
                    + "<form method='post' action='/order'>"
                    + "Pedido ID: <input name='orderId' value='1001' required><br>"
                    + "Producto: <input name='product' value='Hamburguesa' required><br>"
                    + "Monto: <input name='amount' value='12000' required><br>"
                    + "Moneda: <input name='currency' value='USD' required><br>"
                    + "Canal: <select name='channel'><option value='web'>Web</option><option value='mobile'>Móvil</option></select><br>"
                    + "Pasarela: <select name='gateway'><option value='paypal'>Paypal</option><option value='stripe'>Stripe</option><option value='legacy'>LegacyBank</option></select><br>"
                    + "<button type='submit'>Crear Pedido</button>"
                    + "</form>"
                    + "<p>Estos pedidos usan el patrón Adapter para la pasarela y Bridge para el canal de venta.</p>"
                    + "</body></html>";
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, html.getBytes(StandardCharsets.UTF_8).length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(html.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    static class OrderHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String body = readRequestBody(exchange.getRequestBody());

            Map<String, String> params;
            if (method.equalsIgnoreCase("POST")) {
                params = parseQuery(body);
            } else {
                params = parseQuery(exchange.getRequestURI().getQuery());
            }

            String response;
            try {
                int orderId = Integer.parseInt(params.getOrDefault("orderId", "1"));
                String product = params.getOrDefault("product", "Producto");
                double amount = Double.parseDouble(params.getOrDefault("amount", "100"));
                String currency = params.getOrDefault("currency", "USD");
                String channel = params.getOrDefault("channel", "web");
                String gateway = params.getOrDefault("gateway", "paypal");

                PaymentProcessor processor = createProcessor(gateway);
                Order order = new Order(orderId, product, amount, currency);
                service.OrderService service;
                if (channel.equalsIgnoreCase("mobile")) {
                    service = new OrderService(new MobileOrderSystem(processor));
                } else {
                    service = new OrderService(new WebOrderSystem(processor));
                }

                boolean success = service.placeOrder(order);
                response = "<html><body><h2>Resultado de pedido</h2>" +
                        "<p>Pedido: " + orderId + " - " + product + " - " + amount + " " + currency + "</p>" +
                        "<p>Canal: " + channel + " Gateway: " + gateway + "</p>" +
                        "<p>Estado: " + (success ? "Éxito" : "Fallo") + "</p>" +
                        "<a href='/'>Volver</a></body></html>";
            } catch (Exception ex) {
                response = "<html><body><h2>Error</h2><p>" + ex.getMessage() + "</p><a href='/'>Volver</a></body></html>";
            }
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        }

        private static String readRequestBody(InputStream is) throws IOException {
            byte[] bytes = is.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        }

        private static Map<String, String> parseQuery(String query) throws IOException {
            Map<String, String> result = new HashMap<>();
            if (query == null || query.isEmpty()) {
                return result;
            }
            for (String part : query.split("&")) {
                String[] pair = part.split("=", 2);
                String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
                String value = pair.length > 1 ? URLDecoder.decode(pair[1], StandardCharsets.UTF_8) : "";
                result.put(key, value);
            }
            return result;
        }

        private static PaymentProcessor createProcessor(String gateway) {
            if (gateway.equalsIgnoreCase("stripe")) {
                return new StripeAdapter(new StripeService(), "4242424242424242");
            } else if (gateway.equalsIgnoreCase("legacy")) {
                return new LegacyBankAdapter(new LegacyBankSystem(), "ACC-ORIGEN-123", "ACC-DESTINO-456");
            }
            return new PaypalAdapter(new PaypalAPI());
        }
    }
}
