package exceptions;

public class UpgradeMinimumException extends MonopolyException {
    public UpgradeMinimumException(String message) {
        super( message );
    }
}
