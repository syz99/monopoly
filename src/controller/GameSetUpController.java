package controller;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.AutomatedPlayer;
import backend.assetholder.HumanPlayer;
import backend.board.AbstractBoard;
import backend.board.StandardBoard;

import configuration.XMLData;
import engine.MonopolyDriver;
import frontend.screens.AbstractScreen;
import frontend.views.GameConfigView;
import frontend.views.FormView;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is a controller that manages all "set-up" before
 * the game starts (i.e. creation of myBoard, etc.)
 *
 * @author Edward
 * @author Sam
 */
public class GameSetUpController {

    private static final String CONFIG_FILE = "OriginalMonopoly.xml";

    private Node myNode;

    private GameController myGameController;
    private FormView myFormView;
    private AbstractScreen myScreen;
    private double screenWidth, screenHeight;
    private XMLData myData;
    private AbstractBoard myBoard;
    private BorderPane myLayoutPane;
    private GameConfigView myGameConfigView;

    public GameSetUpController(double sWidth, double sHeight, AbstractScreen screen) {
        screenWidth = sWidth;
        screenHeight = sHeight;
        myScreen = screen;
        myGameConfigView = new GameConfigView(this);

        try {
            myData = new XMLData(CONFIG_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        myFormView = new FormView(this);
        myLayoutPane = new BorderPane();
        myLayoutPane.setCenter(myFormView.getNode());
        myLayoutPane.setTop(myGameConfigView.getNode());
        myNode = myLayoutPane;
        //makeSetUpScreen();
    }

    private void makeSetUpScreen(Map<TextField, ComboBox> playerToIcon, Map<TextField, ComboBox> playerToType) {
        myBoard = makeBoard(playerToIcon, playerToType);
        myGameController = new GameController(
                screenWidth, screenHeight,
                this, myBoard, myData
        );
        myLayoutPane.setCenter(myGameController.getGameNode());
    }

    public Node getNode() {
        return myNode;
    }

    public void startGame(Map<TextField, ComboBox> playerToIcon, Map<TextField, ComboBox> playerToType) {
        makeSetUpScreen(playerToIcon, playerToType);
        myScreen.changeDisplayNode(myNode);
    }

    private AbstractBoard makeBoard(Map<TextField, ComboBox> playerToIcon, Map<TextField, ComboBox> playerToType) {
        AbstractBoard board = new StandardBoard(
                makePlayerList(playerToIcon, playerToType), myData.getAdjacencyList(),
                myData.getPropertyCategoryMap(), myData.getFirstTile(),
                myData.getNumDie(), myData.getBank()
        );
        //reinitializeDecks(myData.getDecks(), );
        return board;
    }

    private List<AbstractPlayer> makePlayerList(Map<TextField, ComboBox> playerToIcon, Map<TextField, ComboBox> playerToType) {
        List<AbstractPlayer> playerList = new ArrayList<>();

        for (TextField pName : playerToIcon.keySet()) {
            String name = pName.getText();

            if (!name.equals("")) {
                String playerType = playerToType.get(pName).getValue().toString();
                if (playerType.equals("human")) {
                    playerList.add(new HumanPlayer(
                            name,
                            (String) playerToIcon.get(pName).getValue(),
                            myData.getInitialFunds()
                    ));
                }
                if (playerType.equals("bot")) {
                    playerList.add(new AutomatedPlayer(
                            name,
                            (String) playerToIcon.get(pName).getValue(),
                            myData.getInitialFunds()
                    ));
                }
            }
        }

        return playerList;
    }

    public int getMaxPlayers() { return myData.getMaxPlayers(); }
    public int getMinPlayers() { return myData.getMinPlayers(); }

    public void backToParent() {
        myScreen.backToParent();
    }

    public void handleNewGame() {
        Stage stage = new Stage();
        MonopolyDriver newDriver = new MonopolyDriver();
        newDriver.start(stage);
    }

    //    private List<NormalDeck> reinitializeDecks(List<NormalDeck> decks, List<AbstractAssetHolder> playerList){
//        for(NormalDeck deck: decks){
//            for(ActionCard card: deck.getCards()){
//                //this is very hardcoded at the moment
//                if(card.getActionType().equalsIgnoreCase("Pay")){
//                    if(((PayCard) card).getPayeeString().equalsIgnoreCase("Everyone")){
//                        ((PayCard) card).setPayees(playerList);
//                    }
//                    //else if()
//                }
//                else if(card.getActionType().equalsIgnoreCase("PayBuildings")){
//                    if(((PayCard) card).getPayeeString().equalsIgnoreCase("Everyone")){
//                        //((PayCard) card).setPayers();
//                        ((PayCard) card).setPayees(playerList);
//                    }
//                }
//                else if(card.getActionType().equalsIgnoreCase("MoveAndPay")){
//
//                }
//            }
//        }
//    }
    public void handleSave() {
        myGameConfigView.getSavePath("Choose Folder to save in","/src/resources");
    }

    public void handleLoad(){
        myGameConfigView.generateLoadDialog();
    }

    public String getBackround() {
        return myData.getBackground();
    }

    public String getBoxColor() {
        return myData.getBoxColor();
    }

}
