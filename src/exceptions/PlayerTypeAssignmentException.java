package exceptions;

public class PlayerTypeAssignmentException extends FormInputException {

    public PlayerTypeAssignmentException(String s) {
        super(s);
    }

    /**
     * Default chained constructor
     */
    public PlayerTypeAssignmentException() {
        this("Player Type not assigned!");
    }
}
