package exceptions;

/**
 * Custom exceptions
 */
public class DuplicatePlayeNameException extends FormInputException {

    public DuplicatePlayeNameException(String s) {
        super(s);
    }

    /**
     * Default chain constructor
     */
    public DuplicatePlayeNameException() {
        this("Players cannot have the same name!");
    }
}
