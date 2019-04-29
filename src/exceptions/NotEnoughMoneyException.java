package exceptions;

public class NotEnoughMoneyException extends MonopolyException {
    public NotEnoughMoneyException(String message) {
        super( message );
    }
}
