package frontend.views.game;

import backend.assetholder.AbstractPlayer;
import backend.board.AbstractBoard;
import backend.tile.Tile;
import configuration.XMLData;

import controller.Turn;
import frontend.views.LogView;
import frontend.views.player_options.AbstractOptionsView;
import frontend.views.player_options.BPaneOptionsView;
import frontend.views.player_options.DiceView;
import frontend.views.board.AbstractBoardView;
import frontend.views.board.SquareBoardView;

import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.util.Optional;
import java.util.List;
import java.util.Map;

/**
 * Extends AbstractGameView; Represents the Game View of
 * a split screen layout (half and half)
 *
 * @author Edward
 * @author Sam [docs + added BoardView]
 */
public class SplitScreenGameView extends AbstractGameView {

    private GridPane myPane;
    private AbstractBoardView myBoardView;
    private AbstractOptionsView myOptionsView;
    private LogView myLogView;
    private DiceView myDiceView;
    private XMLData myData;

    /**
     * SplitScreenGameView main constructor
     * @param screenWidth
     * @param screenHeight
     */
    public SplitScreenGameView(double screenWidth, double screenHeight, XMLData data, AbstractBoard board){
        super(screenWidth,screenHeight, data, board);
        myData = data;
        try {


            myBoardView = new SquareBoardView(board, 0.9*screenWidth, 0.9*screenHeight,90,data.getMyHorizontal(),data.getMyVertical());

        } catch (Exception e) {
            e.printStackTrace(); //change this !!!
        }
        myOptionsView = new BPaneOptionsView(this,board,myData);
        addBoardView();
    }

    /**
     * Sets layout dimensions of the View's internal GridPane node
     * @param screenWidth
     * @param screenHeight
     */
    @Override
    public void setBoundsForEntireGame(double screenWidth, double screenHeight) {
        myPane = new GridPane();
        myPane.setMaxWidth(screenWidth);
        myPane.setMaxHeight(screenHeight);
    }

    /**
     * Splits the internal GridPane node evenly in two columns
     */
    @Override
    public void divideScreen() {
        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setPercentWidth(50);
        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setPercentWidth(50);
        myPane.getColumnConstraints().addAll(leftCol,rightCol);
    }

    /**
     * Adds the PlayerOptionsView on the right hand half of the internal node
     */
    @Override
    public void addPlayerOptionsView() {
        myPane.add(myOptionsView.getOptionsViewNode(),1,0);
    }

    /**
     * Adds the AbstractBoardView on the left hand half of internal node
     */
    @Override
    public void addBoardView() {
        myPane.add(myBoardView.getBoardViewNode(), 0, 0);
    }


    @Override
    public String showInputTextDialog(String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.getDialogPane().lookupButton( ButtonType.CANCEL).setDisable(true);
        dialog.getDialogPane().lookupButton( ButtonType.CANCEL).setVisible(false);

        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        }
        return "";
//        else {
//            throw new CancelledActionException( "Cancelled Action" );
//        }
    }

    @Override
    public BPaneOptionsView getMyOptionsView() {
        return (BPaneOptionsView) myOptionsView;
    }

    /**
     * Gets the internal node of the SplitScreenGameView
     * @return Node         a GridPane
     */
    @Override
    public Node getGameViewNode() {
        return myPane;
    }

    public void setPossibleActions(Map<String, EventHandler<ActionEvent>> actionMap){

    }

    /**
     * Creates the buttons to be displayed on the playerOptionsView from
     * a given mapping of handlers and calls the parent class createButtons method
     * @param handlerMap
     */
    public void createOptions(Map<String, EventHandler<ActionEvent>> handlerMap){
        myOptionsView.createButtons(handlerMap);
    }

    @Override
    public void updateDice(Turn turn) {
        myOptionsView.updateDice(turn);
    }

    @Override
    public void updateAssetDisplay(List<AbstractPlayer> myPlayerList, AbstractPlayer forfeiter) {
        myOptionsView.updateAssetDisplay(myPlayerList, forfeiter);
    }


    public void disableButton(String str) {
        myOptionsView.disableControl(str);
    }

    public void enableButton(String str) {
        myOptionsView.enableControl(str);
    }

    @Override
    public void updateCurrPlayerDisplay(AbstractPlayer currPlayer) {
        myOptionsView.updateCurrPlayerDisplay(currPlayer);
    }

    @Override
    public void updateIconDisplay(AbstractPlayer currPlayer, Tile tile) {
        myBoardView.move(currPlayer, tile);
    }

    @Override
    public void updateLogDisplay(String s) { myOptionsView.updateLogDisplay(s); }

    @Override
    public int getCheatMoves() {
        return myOptionsView.getCheatMoves();
    }

    @Override
    public void removePlayer(AbstractPlayer forfeiter, Tile tile) {
        myBoardView.removePlayer(forfeiter, tile);
    }

//    @Override
//    public void requestFocus(String str) {
//        myOptionsView.requestFocus(str);
//    }
}