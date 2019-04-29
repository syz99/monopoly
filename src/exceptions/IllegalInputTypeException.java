package exceptions;

public class IllegalInputTypeException extends MonopolyException {

    public IllegalInputTypeException(String message) {
        super(message);
    }

    public void doNothing() {
        return;
    }
}
