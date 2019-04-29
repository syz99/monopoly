package exceptions;

/**
 * Custom exceptions to handle when a user has inputted a name but
 * has not chosen an icon or vice versa during the pre-game form
 */
public class InputIconMismatchException extends FormInputException {
    public InputIconMismatchException(String s) {
        super(s);
    }

    /**
     * Default chained constructor
     */
    public InputIconMismatchException() {
        this("Icon missing!");
    }
}
