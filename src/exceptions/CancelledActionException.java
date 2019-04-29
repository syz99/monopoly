package exceptions;

public class CancelledActionException extends MonopolyException {

    public CancelledActionException(String message) {
        super(message);
    }


    public void doNothing() {
        return;
    }
}
