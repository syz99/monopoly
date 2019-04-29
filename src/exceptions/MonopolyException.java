package exceptions;

import javafx.scene.control.Alert;

public abstract class MonopolyException extends Exception {
    public MonopolyException(String message) {
        super(message);
    }

    public void popUp() {
        Alert formAlert = new Alert(Alert.AlertType.ERROR);
        formAlert.setContentText(this.getMessage());
        formAlert.showAndWait();
    }

}
