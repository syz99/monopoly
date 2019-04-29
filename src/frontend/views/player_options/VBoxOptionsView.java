package frontend.views.player_options;

import backend.assetholder.AbstractPlayer;
import backend.board.AbstractBoard;
import controller.Turn;
import exceptions.CancelledActionException;
import exceptions.PropertyNotFoundException;
import frontend.views.game.AbstractGameView;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Control;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents some abstract VBox node that offers
 * options and controls to users
 *
 * @author Edward
 * @author Sam
 * @author Stephanie
 */
public class VBoxOptionsView extends AbstractOptionsView {

    private VBox                myOptionsViewNode;
    private AbstractGameView    myGameView;
    private Map<String,Control> myControls;

    /**
     * VBoxOptionsView main constructor
     * @param gameView
     */
    public VBoxOptionsView(AbstractGameView gameView, AbstractBoard board){
        super(gameView,board);
        myGameView = gameView;
        myControls = new HashMap<>();
        makeNode();
    }

    private void makeNode() {
        VBox playerOptionsModal = new VBox();
        playerOptionsModal.setSpacing(10);
        myOptionsViewNode=playerOptionsModal;
    }

    /**
     * Creates all necessary user controls/buttons from the
     * given actionMap
     * @param actionMap     mapping of necessary button handlers to set
     */
    @Override
    public void createButtons(Map<String, EventHandler<ActionEvent>> actionMap) {
        for(String action:actionMap.keySet()){
            makeButton(action,actionMap.get(action));
        }
    }

    private void makeButton(String action, EventHandler<ActionEvent> handler) {
        var buttonView = new Button();
        buttonView.setText(action);
        buttonView.setOnAction(handler);
        myOptionsViewNode.getChildren().add(buttonView);
        myControls.put(action, buttonView);
    }

    /**
     * Disables the user control (grey out)
     * @param control
     */
    @Override
    public void disableControl(String control) {
        Control selected = myControls.get(control);
        selected.setDisable(false);
    }

    /**
     * Enables a grey-out user control
     * @param control
     */
    @Override
    public void enableControl(String control) {
        Control selected = myControls.get(control);
        selected.setDisable(true);
    }

    /**
     * Getter of the Node of the VBoxOptionsView
     * @return Node         the internal VBox
     */
    @Override
    public Node getOptionsViewNode() {
        return myOptionsViewNode;
    }

    @Override
    public void updateDice(Turn turn) {

    }

    @Override
    public void updateCurrPlayerDisplay(AbstractPlayer currPlayer) {

    }

    @Override
    public void updateAssetDisplay(List<AbstractPlayer> myPlayerList, AbstractPlayer forfeiter) {

    }

    @Override
    public void updateLogDisplay(String s) {}

    @Override
    public int getCheatMoves() { return 0; }

//
//    @Override
//    public void requestFocus(String control) {
//
//    }

}
