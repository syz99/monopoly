package exceptions;

public class UpgradeMaxedOutException extends MonopolyException {
    public UpgradeMaxedOutException(String message) {
        super( message );
    }
}
