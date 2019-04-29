package frontend.views.player_options;

import backend.assetholder.AbstractPlayer;
import backend.board.AbstractBoard;
import controller.Turn;
import frontend.views.game.AbstractGameView;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.util.List;
import java.util.Map;

/**
 * The Abstract class for a View consisting of
 * Player options in a game (i.e. buttons, sliders, etc.)
 *
 * @author Edward
 */
abstract public class AbstractOptionsView {

    /**
     * Default constructo
     * @param gameView
     */
    public AbstractOptionsView(AbstractGameView gameView, AbstractBoard board) {}

    /**
     * Abstract method to create all buttons required from
     * a map for the AbstractOptionsView object
     * @param actionMap
     */
    abstract public void createButtons(Map<String, EventHandler<ActionEvent>> actionMap);

    /**
     * Abstract methods for the disabling or enabling
     * of a control element (e.g. a button)
     * @param control
     */
    abstract public void disableControl(String control);
    abstract public void enableControl(String control);
    /**
     * Getter for some AbstractOptionsView object's Node
     * @return
     */
    abstract public Node getOptionsViewNode();

    public abstract void updateDice(Turn turn);

    abstract public void updateCurrPlayerDisplay(AbstractPlayer currPlayer);

    abstract public void updateAssetDisplay(List<AbstractPlayer> myPlayerList, AbstractPlayer forfeiter);

    abstract public void updateLogDisplay(String s);

    abstract public int getCheatMoves();
    //
//    abstract public void requestFocus(String control);
}
