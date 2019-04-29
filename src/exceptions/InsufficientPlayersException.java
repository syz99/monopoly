package exceptions;

/**
 * Custom exceptions for when not enough players are entered into
 * the TextField of FormView
 *
 * @author Sam
 */
public class InsufficientPlayersException extends FormInputException {
    public InsufficientPlayersException(String s) {
        super(s);
    }

    /**
     * Default chained constructor
     */
    public InsufficientPlayersException() {
        this("Not enough players have signed up!");
    }
}
