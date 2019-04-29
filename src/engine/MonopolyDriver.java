package engine;

import frontend.screens.AbstractScreen;
import javafx.application.Application;

import frontend.ViewMaker;

import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Driver class of the Monopoly game, acts as the game and initializes the
 * entire game framework
 *
 * @author Sam
 */
public class MonopolyDriver extends Application {

    private final static String TITLE = "Monopoly";
    private Stage               myStage;
    private Scene               myIntroScene;
    private AbstractScreen myScreen;

    /**
     * Occurs at start of application
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        myStage = stage;
        // TODO: MonopolyDriver.start() refactor for MVC principles
        myIntroScene = new ViewMaker().makeIntroScene(myStage);
        myStage.setScene(myIntroScene);
        myStage.setTitle(TITLE);
        myIntroScene.setOnKeyPressed(f -> handleKeyInput(f.getCode()));
        myStage.show();
    }

    private void handleKeyInput(KeyCode code) {

    }

    public static void main(String[] args) { launch(args); }
}
