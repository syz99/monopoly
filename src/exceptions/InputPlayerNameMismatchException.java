package exceptions;

public class InputPlayerNameMismatchException extends FormInputException {

    public InputPlayerNameMismatchException(String s) {
        super(s);
    }

    /**
     * Default chained constructor
     */
    public InputPlayerNameMismatchException() {
        this("Player name is missing!");
    }
}
