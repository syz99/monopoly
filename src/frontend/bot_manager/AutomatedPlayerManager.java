package frontend.bot_manager;

import backend.assetholder.AbstractPlayer;
import controller.GameController;
import frontend.views.player_options.BPaneOptionsView;
import controller.TileActionController;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import org.testfx.util.WaitForAsyncUtils;
import org.testfx.util.WaitForAsyncUtils.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Map;
import java.util.Set;

public class AutomatedPlayerManager extends ApplicationTest {
    private  Button ROLL_BUTTON;
    private  Button END_TURN_BUTTON;
    private  Button TRADE_BUTTON;
    private  Button MORTGAGE_BUTTON;
    private  Button MOVE_BUTTON;
    private  Button COLLECT_BUTTON;
    private  Button PAY_BAIL_BUTTON;
    private  Button FORFEIT_BUTTON;
    private  Button MOVE_HANDLER_BUTTON;
    private  Button UNMORTGAGE_BUTTON;
    private  Button SELL_TO_BANK;
    private  Button UPGRADE;
    private  VBox   buttons;

    private AbstractPlayer myBot;
    private BPaneOptionsView myBPane;
    private Map<String, Control> buttonMap;

    private Button rollButton;
    private Button endTurnButton;

    private GameController myCtrl;

    public AutomatedPlayerManager(BPaneOptionsView bPane, GameController ctrl) {
        myCtrl=ctrl;
        myBPane = bPane;
        buttonMap = bPane.getControls();
//        buttons=TestingScreen.getVBox(playerOptionsModal);
//         ROLL_BUTTON = TestingScreen.getVBox();
        initializeButtons();

    }

    public void initializeButtons(){
        String roll = "ROLL_BUTTON";
        String endturn = "END_TURN_BUTTON";
        rollButton = (Button) buttonMap.get(roll);
        endTurnButton = (Button) buttonMap.get(endturn);


    }

    public void autoPlayerTurn(AbstractPlayer player) {
        myBot = player;
        System.setProperty("testfx.robot", "glass");
        Button okButton = lookup("OK").queryButton();
//        Set nodes = lookup("OK").queryAll();
//        for (Object node : nodes ) {
//            if (node instanceof ButtonType){
//                ButtonType button = ((ButtonType) node);
//                clickOn(button);
//            }
//        }
        clickOn(okButton);
        botRoll();
        WaitForAsyncUtils.waitForFxEvents();
        clickOn(okButton);
        botEndTurn();
        try {
            unclick();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void clearPopUp(Alert popup, Map<String, Control> buttonMap) {
        Button okButton = lookup("OK").queryButton();
        Button buyButton = lookup("Buy").queryButton();
        Button auctionButton = lookup("Auction").queryButton();
//        Button

//        if(popup instanceof TileActionController.class)

    }

    public void click(Button button){
        clickOn(button);
        simulateUIAction (button , () -> button.fire());
    }

    private void simulateUIAction(Node n, Runnable action) {
        moveTo(n);
        WaitForAsyncUtils.waitForFxEvents();
        Platform.runLater(action);

    }

    private void unclick() throws Exception {
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    public void botRoll() {
        clickOn(rollButton);
    }

    public void botEndTurn() {
        clickOn(endTurnButton);
    }



    // extra utility methods for different UI components
    protected void clickOn (Button b) {
        simulateAction(b, () -> b.fire());
    }

    private void simulateAction (Node n, Runnable action) {
        // simulate robot motion
        //moveTo(n);  - FIX THIS
        // fire event using given action on the given node
        Platform.runLater(action);
        // make it "later" so the requested event has time to run
        WaitForAsyncUtils.waitForFxEvents();
    }
}
