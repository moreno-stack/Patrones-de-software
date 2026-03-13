package external;

public class LegacyBankSystem {
    public boolean transfer(double amount, String accountOrigin, String accountDestination) {
        System.out.println("[LegacyBankSystem] Transferencia legacy: " + amount + " de " + accountOrigin + " a " + accountDestination);
        if (amount < 50) {
            System.out.println("[LegacyBankSystem] No se permiten montos menores a 50 en transferencia.");
            return false;
        }
        return true;
    }
}
