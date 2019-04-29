package exceptions;

public class NotInJailException extends MonopolyException {

    public NotInJailException(String message) {
        super( message );
    }
}
