package frontend;

import frontend.screens.MainMenuScreen;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;

/**
 * Makes the Screen (Scene object) on first load of this
 * game app.
 *
 * @author Sam
 * @author Edward
 */
public class ViewMaker {

    private static final double SCREEN_WIDTH  = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    private double FRAMES_PER_SECOND = 3;
    private double MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    private Timeline animation;

    public ViewMaker() {}

    public Scene makeIntroScene(Stage stage) {
        MainMenuScreen mainMenuScreen = new MainMenuScreen(SCREEN_WIDTH, SCREEN_HEIGHT, stage,null);
        mainMenuScreen.makeScreen();
        mainMenuScreen.getMyIntroScene().setOnKeyPressed(e -> handleKeyInput(e.getCode()));

        animateGame();

        return mainMenuScreen.getMyIntroScene();
    }

    private void animateGame() {
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private void step() {

    }

    private void handleKeyInput(KeyCode code) {
    }
}
