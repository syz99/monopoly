package frontend.screens;

import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Extends AbstractScreen. Represents the menu screen rendered for normal mode play
 *
 * @author Sam
 */
public class NormalModeScreen extends AbstractScreen {

    public NormalModeScreen(double sWidth, double sHeight, Stage stage, AbstractScreen parent) {
        super(sWidth, sHeight, stage, parent);
        makeScreen();
    }

    @Override
    public void changeDisplayNode(Node n) {

    }
}
