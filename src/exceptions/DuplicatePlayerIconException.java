package exceptions;

public class DuplicatePlayerIconException extends FormInputException {

    public DuplicatePlayerIconException(String s) {
        super(s);
    }

    /**
     * Default chain constructor
     */
    public DuplicatePlayerIconException() {
        this("Players cannot have the same icon!");
    }

}
