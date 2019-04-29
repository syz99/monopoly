package exceptions;

import exceptions.MonopolyException;

/**
 * Custom exceptions that is a general representation
 * of some error that occurs with player input during
 * the pre-game signup
 *
 * @author Sam
 */
public class FormInputException extends MonopolyException {

    public FormInputException(String s) {
        super(s);
    }

    /**
     * Default chain constructor
     */
    public FormInputException() {
        this("Error occurred with user input. Re-check values!");
    }
}
