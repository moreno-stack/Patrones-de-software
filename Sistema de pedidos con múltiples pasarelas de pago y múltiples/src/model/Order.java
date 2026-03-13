package model;

public class Order {
    private final int orderId;
    private final String product;
    private final double amount;
    private final String currency;

    public Order(int orderId, String product, double amount, String currency) {
        this.orderId = orderId;
        this.product = product;
        this.amount = amount;
        this.currency = currency;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getProduct() {
        return product;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
