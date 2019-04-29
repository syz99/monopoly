package exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotEnoughMoneyAndMustPayException extends MonopolyException {
    public NotEnoughMoneyAndMustPayException(String message) {
        super( message );
    }


}
