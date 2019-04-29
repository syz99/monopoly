package frontend.screens;

import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Extends AbstractScreen. Represents the menu screen rendered
 * for custom mode play
 *
 * @author Sam
 */
public class CustomModeScreen extends AbstractScreen {
    public CustomModeScreen(double sWidth, double sHeight, Stage stage, AbstractScreen parent) {
        super (sWidth, sHeight, stage, parent);
    }

    @Override
    public void changeDisplayNode(Node n) {

    }
}
