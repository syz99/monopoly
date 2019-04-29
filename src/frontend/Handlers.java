package frontend;

import engine.MonopolyDriver;
import javafx.stage.Stage;

/**
 * This class contains any handlers the front end screens require
 *
 * @author Sam
 */
public class Handlers {

    protected void handleBackToMainButton(Stage stage) {
        stage.close();
        MonopolyDriver md = new MonopolyDriver();
        md.start(stage);
    }
}
