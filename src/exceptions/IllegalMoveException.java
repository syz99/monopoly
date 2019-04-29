package exceptions;

public class IllegalMoveException extends MonopolyException{
    public IllegalMoveException(String message) {
        super(message);
    }
}
