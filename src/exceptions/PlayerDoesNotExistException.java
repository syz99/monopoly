package exceptions;

public class PlayerDoesNotExistException extends MonopolyException{
    public PlayerDoesNotExistException(String message) {
        super(message);
    }

}
