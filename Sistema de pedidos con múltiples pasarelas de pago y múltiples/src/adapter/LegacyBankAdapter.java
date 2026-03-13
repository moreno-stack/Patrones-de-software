package adapter;

import external.LegacyBankSystem;
import model.Order;

public class LegacyBankAdapter implements PaymentProcessor {
    private final LegacyBankSystem legacyBankSystem;
    private final String accountOrigin;
    private final String accountDestination;

    public LegacyBankAdapter(LegacyBankSystem legacyBankSystem, String accountOrigin, String accountDestination) {
        this.legacyBankSystem = legacyBankSystem;
        this.accountOrigin = accountOrigin;
        this.accountDestination = accountDestination;
    }

    @Override
    public boolean processPayment(Order order) {
        System.out.println("[Adapter][LegacyBank] Adaptando order a LegacyBankSystem");
        return legacyBankSystem.transfer(order.getAmount(), accountOrigin, accountDestination);
    }

    @Override
    public String getGatewayName() {
        return "LegacyBank";
    }
}
