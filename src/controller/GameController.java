package controller;

import backend.card.action_cards.HoldableCard;
import backend.dice.AbstractDice;
import backend.assetholder.AbstractPlayer;
import backend.board.AbstractBoard;
import backend.tile.AbstractPropertyTile;
import backend.tile.*;

import configuration.XMLData;
import configuration.XMLWriter;
import exceptions.*;

import frontend.bot_manager.AutomatedPlayerManager;
import frontend.views.game.AbstractGameView;
import frontend.views.game.SplitScreenGameView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 *
 */
public class GameController {

    private GameSetUpController mySetUpController;
    //TODO: make all the back-end stuff be managed by a MonopolyModel/myBoard class
    private AbstractBoard myBoard;
    private AbstractDice myDiceType;
    private Turn myTurn;
    private Map<String, EventHandler<ActionEvent>> handlerMap = new LinkedHashMap<>();
    private int numDoubleRolls;
    private AbstractGameView myGameView;
    private TileActionController tileActionController;
    private AutomatedPlayerManager autoManager;

    public GameController(double width, double height, GameSetUpController controller, AbstractBoard board, XMLData data){
        myBoard = board;
        myGameView = new SplitScreenGameView(width, height, data, myBoard);
        mySetUpController = controller;
        addHandlers();
//        myDiceType = new SixDice();
        myDiceType = data.getDiceType();
        myTurn = new Turn(myBoard.getMyPlayerList().get(0), myDiceType, myBoard);
        this.numDoubleRolls = 0;
        tileActionController = new TileActionController( myBoard, myTurn, myGameView, this);
        for(AbstractPlayer player : myBoard.getMyPlayerList()) {
            if (player.isAuto() == true) {
                autoManager = new AutomatedPlayerManager(myGameView.getMyOptionsView(), this);
            }
        }
        myGameView.disableButton( "End Turn" );
    }

    private void addHandlers(){
        handlerMap.put("Roll",event->this.handleRollButton());
        handlerMap.put("End Turn", event->this.handleEndTurnButton());
        handlerMap.put("Upgrade", event->this.handleUpgradeProperty());
        handlerMap.put("Sell to Bank",event->this.handleSellToBank());
        handlerMap.put("Sell to Player",event->this.handleSellToPlayer());
        handlerMap.put("Mortgage", event->this.handleMortgage());
        handlerMap.put("Unmortgage", event->this.handleUnmortgage() );
        handlerMap.put("Trade",event->this.handleTrade());
        handlerMap.put("Use Card",event->this.handleUseCardButton());
        handlerMap.put("Forfeit",event->this.handleForfeit());
        handlerMap.put("Move Cheat", event->this.handleMoveCheat());
        myGameView.createOptions(handlerMap);
        myGameView.addPlayerOptionsView();
    }

    private void handleEndTurnButton() {
        myTurn.skipTurn();
        myGameView.updateCurrPlayerDisplay(myTurn.getMyCurrPlayer());
        numDoubleRolls = 0;
        myGameView.enableButton("Use Card");
        myGameView.disableButton("End Turn");
        myGameView.enableButton("Roll");
        if(myTurn.getMyCurrPlayer().isAuto()==(true)) {
            autoManager.autoPlayerTurn(myTurn.getMyCurrPlayer());
        }
    }

    private void handleUseCardButton() {
        HoldableCard card = null;
        ObservableList<String> players = getAllPlayerNames();
        String cardUserName = null;
        try {
            cardUserName = myGameView.displayDropDownAndReturnResult( "Use Card", "Select the player who wants to use a card: ", players );
        } catch (CancelledActionException e) {
            e.doNothing();
        } catch (PropertyNotFoundException e) {
            e.popUp();
        }
        AbstractPlayer cardUser = myBoard.getPlayerFromName( cardUserName );

        ObservableList<String> possibleCards = FXCollections.observableArrayList();
        for(HoldableCard c: cardUser.getCards()){
            possibleCards.add( c.getName() );
        }
        if (possibleCards.size()==0){
            myGameView.displayActionInfo( "You have no cards to use at this time." );
        } else {
            String cardToUse = null;
            try {
                cardToUse = myGameView.displayDropDownAndReturnResult( "Use Card", "Select the card to be used", possibleCards);
            } catch (CancelledActionException e) {
                e.doNothing();
            } catch (PropertyNotFoundException e) {
                e.popUp();
            }
            for (HoldableCard c : cardUser.getCards()) {
                if (c.getName().equalsIgnoreCase( cardToUse )) {
                    card = c;
                }
            }
            List<Object> parameters = new ArrayList<>();
            parameters.add(cardUser);
            parameters.add(card);

            ActionCardController actionCardController = new ActionCardController( myBoard, myTurn, myGameView, this );
            Method handle = null;
            try {
                handle = actionCardController.getClass().getMethod( "handle" + card.getHoldableCardAction(), List.class);
            } catch (NoSuchMethodException e) {
                myGameView.displayActionInfo( "No such method exception" );
            }
            try {
                handle.invoke( actionCardController, parameters );
            } catch (IllegalAccessException e) {
                myGameView.displayActionInfo( "Illegal access exception" );
            } catch (InvocationTargetException e) {
                myGameView.displayActionInfo( "Invocation Target Exception" );
                e.printStackTrace();
            }
        }
        myGameView.updateAssetDisplay(myBoard.getMyPlayerList(), null);
    }

    private void handleEndGame() {
        myGameView.displayActionInfo("Player " + myBoard.getMyPlayerList().get(0).getMyPlayerName() + " won the game! Return to main menu.");
        mySetUpController.backToParent();
    }

    private void handleRollButton() {
        myGameView.disableButton("Roll");
        if(myTurn.getMyCurrPlayer().getTurnsInJail() == 0 || myTurn.getMyCurrPlayer().getTurnsInJail() == 1) {
            new IllegalMoveException("You cannot move because you are in jail. Roll a doubles to get out of jail for free!").popUp();
        }
        myTurn.start();
        myTurn.setNumMoves();
        myGameView.updateDice(myTurn);
        if (myTurn.isDoubleRoll(myTurn.getRolls())) {
            if(myTurn.getMyCurrPlayer().isInJail()) {
                myTurn.getMyCurrPlayer().getOutOfJail();
                myGameView.displayActionInfo("You are released from jail because you rolled doubles. You're free now!");
                myGameView.updateAssetDisplay(myBoard.getMyPlayerList(), null);
                handleMove(myTurn.getNumMoves());
                myGameView.enableButton("End Turn");
            } else {
                //TODO: get rid of magic value
                 if (numDoubleRolls < 2) {
                     numDoubleRolls++;
                     handleMove(myTurn.getNumMoves());
                     if (!myTurn.getMyCurrPlayer().isInJail()) {
                         myGameView.displayActionInfo("You rolled doubles. Roll again!");
                         myGameView.disableButton( "End Turn" );
                         myGameView.enableButton("Roll");
                     }
                } else {
                    tileActionController.handleGoToJail();
                     myGameView.enableButton("End Turn");
                 }
            }
        } else {
            if (myTurn.getMyCurrPlayer().getTurnsInJail() == 2) {
                myGameView.disableButton("Use Card");
                myTurn.getMyCurrPlayer().getOutOfJail();
                myGameView.displayActionInfo("You have had three turns in jail! You are free after you pay the fine.");
                tileActionController.handlePayBail();
                handleMove(myTurn.getNumMoves());
                myGameView.enableButton("End Turn");
            }
            else if(myTurn.getMyCurrPlayer().getTurnsInJail() != -1) {
                myGameView.disableButton("Use Card");
                handleTileLanding(myBoard.getPlayerTile(myTurn.getMyCurrPlayer()));
                myGameView.enableButton("End Turn");
            }
            else {
                handleMove(myTurn.getNumMoves());
            }
        }
    }

    public void handleMove(int numMoves) {
        try {
            for (int i = 0; i < numMoves-1; i++) {
                Tile passedTile = myBoard.movePlayerByOne( myTurn.getMyCurrPlayer());
                if (passedTile != null) {
                    handlePassedTiles(passedTile);
                }
                myGameView.updateIconDisplay(myTurn.getMyCurrPlayer(),passedTile);
            }
            Tile passedTile = myBoard.movePlayerByOne( myTurn.getMyCurrPlayer());
            myGameView.updateIconDisplay(myTurn.getMyCurrPlayer(),passedTile);
        } catch (MultiplePathException e) {
            e.popUp();
        }
        //myGameView.updateIconDisplay(myTurn.getMyCurrPlayer(), myTurn.getNumMoves());
        //TODO: slow down options list popup
        myGameView.enableButton("End Turn");
        handleTileLanding(myBoard.getPlayerTile(myTurn.getMyCurrPlayer()));
        myGameView.updateAssetDisplay(myBoard.getMyPlayerList(), null);
    }

    public void handleTileLanding(Tile tile) {
        try {
            List<String> actions = tile.applyLandedOnAction( myTurn.getMyCurrPlayer() );
            if (actions.size() != 0) {
                String desiredAction = determineDesiredActionForReflection(actions);
                TileActionController tileActionController = new TileActionController(myBoard, myTurn, myGameView, this);
                Method handle = tileActionController.getClass().getMethod("handle" + desiredAction);
                handle.invoke(tileActionController);
            }
        } catch (NoSuchMethodException e) {
            myGameView.displayActionInfo("There is no such method");
        } catch (SecurityException e) {
            myGameView.displayActionInfo("Security exception");
        } catch (IllegalAccessException e) {
            myGameView.displayActionInfo("Illegal access exception");
        } catch (IllegalArgumentException e) {
            myGameView.displayActionInfo("Illegal argument");
        } catch (InvocationTargetException e) {
            myGameView.displayActionInfo("Invocation target exception");
            e.printStackTrace();
        }
    }

    private void handlePassedTiles(Tile passedTile) {
        try {
            List<String> actions = passedTile.applyPassedAction(myTurn.getMyCurrPlayer());
            if (actions.size() != 0) {
                String desiredAction = determineDesiredActionForReflection(actions);
                PassedTileActionController passedTileActionController = new PassedTileActionController( myBoard, myTurn, myGameView);
                Method handle = passedTileActionController.getClass().getMethod("handle" + desiredAction);
                handle.invoke(passedTileActionController);
            }
        } catch (IllegalAccessException e) {
            myGameView.displayActionInfo("Illegal access exception");
        } catch (InvocationTargetException e) {
            myGameView.displayActionInfo("Invocation target exception");
            e.printStackTrace();
        }  catch (NoSuchMethodException e) {
            myGameView.displayActionInfo("No such method exception");
        }
    }

    public String determineDesiredActionForReflection(List<String> actions) {
        String desiredAction;
        if (actions.size() > 1) {
            List<String> readableActions = new ArrayList<>();
            for (String each : actions) {
                readableActions.add(makeReadable(each));
            }
            String pickedOption = myGameView.displayOptionsPopup(readableActions, "Options", "Tile Actions", "Choose One");
            desiredAction = translateReadable(pickedOption);
        } else {
            desiredAction = actions.get(0);
        }
        return desiredAction;
    }

    public void handleMoveCheat() {
        myGameView.disableButton("Roll");
        int moves = myGameView.getCheatMoves();
        myTurn.setNumMoves( moves );
        this.handleMove( moves );
        myGameView.updateAssetDisplay( myBoard.getMyPlayerList(), null );
        myGameView.updateLogDisplay(myTurn.getMyCurrPlayer().getMyPlayerName() + " moved " + moves + " spaces.");
    }

    public void handleUpgradeProperty() {
        //this line below is for the demo, comment out later
        XMLWriter.writeXML("saved_xml.xml", myBoard);
        try {
            ObservableList<String> players = getAllOptionNames(myBoard.getPlayerNamesAsStrings());
            AbstractPlayer owner = getSelectedPlayer("Upgrade Property", "Choose who is upgrading their property ", players);

            ObservableList<String> tiles = getAllOptionNames(myBoard.getBuildingTileNamesAsStrings(owner));
            BuildingTile tile = (BuildingTile) getSelectedPropertyTile("Upgrade Property", "Choose which property to upgrade ", tiles);
            tile.upgrade(owner, myBoard.getSameSetProperties(tile));
            myGameView.displayActionInfo( "You successfully upgraded to " + makeReadable(tile.getCurrentInUpgradeOrder()) );
            myGameView.updateAssetDisplay(myBoard.getMyPlayerList(), null);
            myGameView.updateLogDisplay(owner.getMyPlayerName() + " upgraded " + tile.getTitleDeed() + " to " + makeReadable(tile.getCurrentInUpgradeOrder()) + "." );
        } catch (IllegalInputTypeException e) {
            e.popUp();
        } catch (OutOfBuildingStructureException e) {
            e.popUp();
        } catch (CancelledActionException e) {
            e.doNothing();
        } catch (PropertyNotFoundException e) {
            e.popUp();
        } catch (NotEnoughMoneyException e) {
            e.popUp();
        } catch (UpgradeMaxedOutException e) {
            e.popUp();
        }
    }

    private void handleTrade() {
    }

    public void handleSellToPlayer() {
        try {
            ObservableList<String> players = getAllOptionNames(myBoard.getPlayerNamesAsStrings());
            AbstractPlayer owner = getSelectedPlayer("Sell Property", "Choose who is selling their property ", players);
            handleSellToPlayerFor( owner );
        } catch (NullPointerException e) {
            new CancelledActionException("").doNothing();
        } catch (CancelledActionException e) {
            e.doNothing();
        } catch (PropertyNotFoundException e) {
            e.popUp();
        }
    }

    public void handleSellToPlayerFor(AbstractPlayer owner)  {
        try {
            ObservableList<String> players = getAllOptionNames(myBoard.getPlayerNamesAsStrings());
            players.remove(owner.getMyPlayerName());
            AbstractPlayer buyer = getSelectedPlayer("Sell Property", "Choose who to sell property to ", players);
            ObservableList<String> tiles = getAllOptionNames(myBoard.getPropertyTileNamesAsStrings(owner));
            AbstractPropertyTile tile = getSelectedPropertyTile("Sell Property", "Choose which property to sell ", tiles);

            double amount = 0;
            String value = "";
            boolean sellAmountDetermined = false, properformat = false;
            while (!sellAmountDetermined && !properformat) {
                while (!properformat) {
                    value = myGameView.showInputTextDialog("Amount to sell to player " + buyer.getMyPlayerName(),
                            "Enter your proposed amount:",
                            "Amount:");
                    try {
                        amount = Double.parseDouble(value);
                        if (Double.parseDouble((value)) > owner.getMoney()){
                            new NotEnoughMoneyException("You don't have enough money! Please enter again.").popUp();
                        } else if (Double.parseDouble((value)) < 0.0){
                            new IllegalInputTypeException( "Input must be a positive number!" ).popUp();
                        }
                        else {
                            properformat = true;
                        }
                    } catch (NumberFormatException n) {
                        new IllegalInputTypeException("Input must be a number!").popUp();
                    }
                }
                List<String> options = createListOf2OptionsAsStrings("Yes", "No");
                String result = myGameView.displayOptionsPopup(options, "Proposed Amount", "Do you accept the proposed amount below?", value + " Monopoly dollars");
                if (result.equalsIgnoreCase(options.get(0))) {
                    sellAmountDetermined = true;
                    tile.sellTo(buyer, amount, myBoard.getSameSetProperties(tile));
                    if (tile.isMortgaged()) {
                        result = myGameView.displayOptionsPopup(options, "Property is mortgaged", "Would you like to lift the mortgage? ", "Choose an option");
                        if (result.equals("Yes")) {
                            tile.unmortgageProperty();
                        } else {
                            tile.soldMortgagedPropertyLaterUnmortgages();
                        }
                    }
                }
                else {
                    properformat = false;
                }
            }
            myGameView.updateAssetDisplay(myBoard.getMyPlayerList(), null);
            myGameView.updateLogDisplay(owner.getMyPlayerName() + " sold " + tile.getTitleDeed() + " to " + buyer.getMyPlayerName() + " for " + amount + ".");
        } catch (CancelledActionException e) {
            e.doNothing();
        } catch (PropertyNotFoundException e) {
            e.popUp();
        } catch (IllegalActionOnImprovedPropertyException e) {
            e.popUp();
        } catch (IllegalInputTypeException e) {
            e.popUp();
        } catch (OutOfBuildingStructureException e) {
            e.popUp();
        } catch (NotEnoughMoneyException e) {
            e.popUp();
        } catch (UpgradeMaxedOutException e) {
            e.popUp();
        } catch (MortgagePropertyException e) {
            e.popUp();
        }
    }

    public void handleSellToBank() {
        try {
            ObservableList<String> players = getAllOptionNames(myBoard.getPlayerNamesAsStrings());
            AbstractPlayer owner = getSelectedPlayer("Sell Buildings", "Choose who is selling a building", players);
            handleSellToBankFor(owner);
        } catch (PropertyNotFoundException e) {
            e.popUp();
        } catch (CancelledActionException e) {
            e.doNothing();
        }
    }

    public void handleSellToBankFor(AbstractPlayer owner){
        ObservableList<String> tiles = getAllOptionNames(myBoard.getBuildingTileNamesAsStrings(owner));

        BuildingTile tile = null;
        try {
            tile = (BuildingTile) getSelectedPropertyTile("Sell Buildings", "Choose which property to sell buildings from ", tiles);
            List<String> options = createListOf2OptionsAsStrings("Sell all buildings on all properties of the same group",
                    "Sell one building on selected property");
            String str = myGameView.displayOptionsPopup(options, "Sell Buildings", "Sell buildings options ", "Choose one ");
            if (str.equalsIgnoreCase(options.get(0))) {
                try {
                    tile.sellAllBuildingsOnTile(myBoard.getSameSetProperties(tile));
                } catch (IllegalActionOnImprovedPropertyException e) {
                    e.popUp();
                }
                //TODO: add front-end implementation
            } else {
                try {
                    tile.sellOneBuilding(myBoard.getSameSetProperties(tile));
                } catch (IllegalActionOnImprovedPropertyException e) {
                    e.popUp();
                } catch (UpgradeMinimumException e) {
                    e.popUp();
                }
                //TODO: add front-end implementation
            }
            myGameView.updateAssetDisplay(myBoard.getMyPlayerList(), null);
            myGameView.updateLogDisplay(owner.getMyPlayerName() + " chose to " + str + " (" + tile.getTitleDeed() +").");
        }
        catch (CancelledActionException e) {
            e.doNothing();
        } catch (PropertyNotFoundException e) {
            e.popUp();
        }
    }

    public void handleForfeit() {
        ObservableList<String> players = getAllPlayerNames();
        String player = null;
        try {
            player = myGameView.displayDropDownAndReturnResult("Forfeit", "Select the player who wants to forfeit: ", players);
            AbstractPlayer forfeiter = myBoard.getPlayerFromName(player);
            handleForfeitFor( forfeiter );
        } catch (CancelledActionException e) {
            e.doNothing();
        } catch (PropertyNotFoundException e) {
            e.popUp();
        }
    }

    public void handleForfeitFor(AbstractPlayer forfeiter){
        forfeiter.declareBankruptcy(myBoard.getBank());
        myBoard.getMyPlayerList().remove(forfeiter);
        myGameView.removePlayer(forfeiter, myBoard.getPlayerTile(forfeiter));
        myBoard.getPlayerTileMap().remove(forfeiter);
        myGameView.updateAssetDisplay(myBoard.getMyPlayerList(), forfeiter);
        myGameView.updateLogDisplay(forfeiter.getMyPlayerName() + " has forfeited.");
        myTurn.skipTurn();
        if(myBoard.getMyPlayerList().size() == 1){
            handleEndGame();
        }
    }

    public void handleMortgage(){
        try{
            ObservableList<String> players = getAllPlayerNames();
            String mortgagerName = myGameView.displayDropDownAndReturnResult( "Mortgage", "Select the player who wants to mortgage: ", players );
            AbstractPlayer mortgager = myBoard.getPlayerFromName( mortgagerName );
            handleMortgageFor( mortgager );
        } catch (PropertyNotFoundException e) {
            e.popUp();
        } catch (CancelledActionException e) {
            e.doNothing();
        }
    }

    public void handleMortgageFor(AbstractPlayer mortgager) {
        ObservableList<String> possibleProperties = FXCollections.observableArrayList();
        for (AbstractPropertyTile p : mortgager.getProperties()) {
            possibleProperties.add( p.getTitleDeed() );
        }
        if (possibleProperties.size() == 0) {
            myGameView.displayActionInfo( "You have no properties to mortgage at this time." );
        } else {
            AbstractPropertyTile property = null;
            String propertyToMortgage = null;
            try {
                propertyToMortgage = myGameView.displayDropDownAndReturnResult( "Mortgage", "Select the property to be mortgaged", possibleProperties );
                for (AbstractPropertyTile prop : mortgager.getProperties()) {
                    if (prop.getTitleDeed().equalsIgnoreCase( propertyToMortgage )) {
                        property = prop;
                    }
                }
                property.mortgageProperty();
                myGameView.displayActionInfo( "You've successfully mortgaged " + property.getTitleDeed() );
                myGameView.updateAssetDisplay( myBoard.getMyPlayerList(), null );
                myGameView.updateLogDisplay(mortgager.getMyPlayerName() + " has mortgaged " + property.getTitleDeed() + ".");
            } catch (MortgagePropertyException e) {
                e.popUp();
            } catch (IllegalActionOnImprovedPropertyException e) {
                e.popUp();
            } catch (CancelledActionException e) {
                e.doNothing();
            } catch (PropertyNotFoundException e) {
                e.popUp();
            }
        }
    }

    public void handleUnmortgage() {
        try {
            AbstractPropertyTile property = null;
            ObservableList<String> players = getAllPlayerNames();
            String mortgagerName = myGameView.displayDropDownAndReturnResult( "Unmortgage", "Select the player who wants to unmortgage: ", players );
            AbstractPlayer mortgager = myBoard.getPlayerFromName( mortgagerName );

            ObservableList<String> possibleProperties = FXCollections.observableArrayList();
            for(AbstractPropertyTile p: mortgager.getProperties()){
                if(p.isMortgaged()){
                    possibleProperties.add( p.getTitleDeed() );
                }
            }

            if (possibleProperties.size()==0){
                myGameView.displayActionInfo( "You have no properties to unmortgage at this time." );
            } else{
                String propertyToUnortgage = myGameView.displayDropDownAndReturnResult( "Unortgage", "Select the property to be unmortgaged", possibleProperties );
                for(AbstractPropertyTile p: mortgager.getProperties()){
                    if(p.getTitleDeed().equalsIgnoreCase( propertyToUnortgage )){
                        property = p;
                    }
                }
            }
            property.unmortgageProperty();
            myGameView.displayActionInfo( "You've successfully unmortgaged " + property.getTitleDeed());
            myGameView.updateAssetDisplay(myBoard.getMyPlayerList(), null);
            myGameView.updateLogDisplay(myTurn.getMyCurrPlayer().getMyPlayerName() + " has unmortgaged " + property.getTitleDeed() + ".");
        } catch (MortgagePropertyException e) {
            e.popUp();
        } catch (PropertyNotFoundException e) {
            e.popUp();
        } catch (CancelledActionException e) {
            e.doNothing();
        }
    }

    public Node getGameNode() {
        return myGameView.getGameViewNode();
    }

    private ObservableList<String> getAllOptionNames(List<String> names) {
        ObservableList<String> options = FXCollections.observableArrayList();
        for (String name : names) {
            options.add(name);
        }
        return options;
    }

    private List<String> createListOf2OptionsAsStrings(String option1, String option2) {
        List<String> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        return options;
    }

    private AbstractPlayer getSelectedPlayer(String title, String prompt, ObservableList<String> players) throws CancelledActionException, PropertyNotFoundException {
        String person = myGameView.displayDropDownAndReturnResult(title, prompt, players);
        AbstractPlayer player = null;
        player = myBoard.getPlayerFromName(person);
        return player;
    }

    private AbstractPropertyTile getSelectedPropertyTile(String title, String prompt, ObservableList<String> properties) throws CancelledActionException, PropertyNotFoundException {
        String tile = myGameView.displayDropDownAndReturnResult(title, prompt, properties);
        AbstractPropertyTile property = null;
        property = (AbstractPropertyTile) myBoard.getPropertyTileFromName(tile);
        return property;
    }

    private ObservableList<String> getAllPlayerNames() {
        ObservableList<String> players = FXCollections.observableArrayList();
        for (AbstractPlayer p : myBoard.getMyPlayerList()) {
            players.add(p.getMyPlayerName());
        }
        return players;
    }

    private String makeReadable(String s){
        String label = s.substring( 0,1 );
        for(int i = 1; i < s.length(); i++){
            //start at 1 so doesn't add a space before the first letter
            if(Character.isUpperCase( s. charAt( i ))){
                label += " " + s.charAt( i );
            } else{
                label += s.charAt( i );
            }
        }
        return label;
    }

    public AbstractGameView getMyButtons() {
        return myGameView;
    }

    public String translateReadable(String s){
        return s.replaceAll("\\s+","");
    }

    public AutomatedPlayerManager getMyAutoManager() { return autoManager; }
}