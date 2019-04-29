package controller;

import backend.assetholder.AbstractAssetHolder;
import backend.assetholder.AbstractPlayer;

import backend.board.AbstractBoard;
import backend.card.action_cards.ActionCard;
import backend.card.action_cards.MoveAndPayCard;
import backend.card.action_cards.PayBuildingsCard;
import backend.card.action_cards.PayCard;
import backend.tile.*;
import exceptions.*;
import frontend.views.game.AbstractGameView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TileActionController {
    private AbstractBoard myBoard;
    private Turn myTurn;
    private AbstractGameView myGameView;
    private GameController gameController;


    public TileActionController(AbstractBoard board, Turn turn, AbstractGameView gameView, GameController gameController) {
       this.myBoard = board;
       this.myTurn = turn;
       this.myGameView = gameView;
       this.gameController = gameController;
    }

    public void handleStayInJail() {
        myTurn.getMyCurrPlayer().addTurnInJail();
    }

    public void handleCollectMoneyLanded() {
        myBoard.getBank().payFullAmountTo( myTurn.getMyCurrPlayer(), myBoard.getGoTile().getLandedOnMoney() );
        myGameView.displayActionInfo( "You collected " + myBoard.getGoTile().getLandedOnMoney() + " for landing on go." );
    }

    public void handleGoToJail() {
        try {
            JailTile jail = myBoard.getJailTile();
            myBoard.getPlayerTileMap().put( myTurn.getMyCurrPlayer(), jail);
            myTurn.getMyCurrPlayer().addTurnInJail();
            myGameView.displayActionInfo( "Arrested! You're going to Jail." );
            myGameView.updateLogDisplay(myTurn.getMyCurrPlayer().getMyPlayerName() + " has been sent to Jail!");
            myGameView.updateIconDisplay(myTurn.getMyCurrPlayer(),jail);
            myGameView.disableButton("Use Card");
        } catch (TileNotFoundException e){
            e.popUp();
        }
    }

    public void handlePayBail(){
        try {
            myTurn.getMyCurrPlayer().payFullAmountTo(myBoard.getBank(), myBoard.getJailTile().getBailAmount());
            myTurn.getMyCurrPlayer().getOutOfJail();
            myGameView.displayActionInfo("You've successfully paid the fine. You're free now!");
            myGameView.updateAssetDisplay(myBoard.getMyPlayerList(), null);
            myGameView.updateLogDisplay(myTurn.getMyCurrPlayer().getMyPlayerName() + " has paid the fine and can move!");
            gameController.handleMove(myTurn.getNumMoves());
        } catch(TileNotFoundException e) {
            e.popUp();
        } catch (NotEnoughMoneyException e) {
            e.popUp();
        }
    }

    public void handlePayRent() {
        AbstractPropertyTile property = (AbstractPropertyTile) myBoard.getPlayerTile( myTurn.getMyCurrPlayer());
        double rent = property.calculateRentPrice( myTurn.getNumMoves());
        try {
            myTurn.getMyCurrPlayer().payFullAmountTo(property.getOwner(), rent);
            myGameView.updateAssetDisplay(myBoard.getMyPlayerList(), null);
            myGameView.displayActionInfo( "You paid " + property.calculateRentPrice( myTurn.getNumMoves()) + " to " + ( (AbstractPlayer) property.getOwner()).getMyPlayerName() + ".");
            myGameView.updateLogDisplay(myTurn.getMyCurrPlayer().getMyPlayerName() + " has paid " + property.calculateRentPrice( myTurn.getNumMoves()) + " of rent to " + ( (AbstractPlayer) property.getOwner()).getMyPlayerName() + ".");
        } catch (NotEnoughMoneyException e) {
            e.popUp();
            payOrForfeit( rent, "handlePayRent" );
        }
    }

    public void handlePayTaxFixed() {
        double tax = ((AbstractTaxTile)myTurn.currPlayerTile()).getAmountToDeduct();
        try {
            myTurn.getMyCurrPlayer().payFullAmountTo( myBoard.getBank(), tax);
            myGameView.updateAssetDisplay(myBoard.getMyPlayerList(), null);
            myGameView.displayActionInfo( "It's tax season! You've paid " + tax + " in taxes.");
            myGameView.updateLogDisplay( myTurn.getMyCurrPlayer().getMyPlayerName() + " payed " + tax + " in taxes.");
        } catch (NotEnoughMoneyException e) {
            e.popUp();
            payOrForfeit( tax, "handlePayTaxFixed" );
        }
    }

    private void payOrForfeit(double debt, String method) {
        myGameView.disableButton( "End Turn" );
        while(myTurn.getMyCurrPlayer().getMoney() < debt) {
            List<String> options = new ArrayList<>();
            if (myTurn.getMyCurrPlayer().getProperties().size() != 0) {
                options.add( "Sell To Player" );
                options.add( "Sell To Bank" );
                options.add( "Mortgage" );
            }
            options.add( "Forfeit" );
            String desiredAction = myGameView.displayOptionsPopup( options, "Pay", "Pay " + debt + " monopoly dollars", "You must pay or forfeit. Here are your options." );
            if (desiredAction.equals( "Forfeit" )) {
                gameController.handleForfeitFor(myTurn.getMyCurrPlayer());
                break;
            } else {
                gameController.translateReadable( desiredAction );
                desiredAction = desiredAction.replaceAll( "\\s+", "" );
                Method handle = null;
                try {
                    handle = gameController.getClass().getMethod( "handle" + desiredAction + "For", AbstractPlayer.class);
                    handle.invoke( gameController , myTurn.getMyCurrPlayer());
                    if (myTurn.getMyCurrPlayer().getMoney() >= debt) {
                        Method redo = null;
                        redo = this.getClass().getMethod(method);
                        redo.invoke(this);
                        break;
                    }
                } catch (IllegalAccessException e1) {
                    myGameView.displayActionInfo( "Illegal Access Exception" );
                } catch (NoSuchMethodException e){
                    myGameView.displayActionInfo( "There is no such method" );
                } catch (InvocationTargetException e1) {
                    myGameView.displayActionInfo( "Invocation Target Exception" );
                }
            }
        }
    }

    public void handleDrawCard(){
        try {
            ActionCard actionCard = ((AbstractDrawCardTile) myBoard.getPlayerTile(myTurn.getMyCurrPlayer())).drawCard();
            if(actionCard.getActionType().contains("Pay")) {
                getClass().getMethod("reinitialize"+ actionCard.getActionType(), ActionCard.class).invoke(this, actionCard);
            }
            myGameView.displayActionInfo( actionCard.getText() );
            ActionCardController actionCardController = new ActionCardController(myBoard, myTurn, myGameView, gameController);
            Method handle = actionCardController.getClass().getMethod("handle" + actionCard.getActionType(), List.class);
            handle.invoke(actionCardController, actionCard.getParameters());
        } catch (NoSuchMethodException e) {
            myGameView.displayActionInfo( "There is no such method" );
        } catch (SecurityException e) {
            myGameView.displayActionInfo( "Security exception" );
        } catch (IllegalAccessException e) {
            myGameView.displayActionInfo( "Illegal access exception" );
        } catch (IllegalArgumentException e) {
            myGameView.displayActionInfo( "Illegal argument exception" );
        } catch (InvocationTargetException e) {
            myGameView.displayActionInfo( "Invocation target exception" );
        }
    }

//    get the class itself so that you don't need three separate methods to do the same thing
//    private void reinitializePayCard(ActionCard actionCard){
//        List<AbstractAssetHolder> players = new ArrayList<>();
//        for(AbstractPlayer p: myBoard.getMyPlayerList()) players.add(p);
//        List<AbstractAssetHolder> bank = new ArrayList<>();
//        bank.add(myBoard.getBank());
//        List<AbstractAssetHolder> currPlayer = new ArrayList<>();
//        currPlayer.add(myTurn.getMyCurrPlayer());
//        Class cast = actionCard.getClass();
//        if (((cast) actionCard).getPayeeString().equalsIgnoreCase("Everyone")) {
//            ((cast) actionCard).setPayees(players);
//        }
//        else if (((cast) actionCard).getPayeeString().equalsIgnoreCase("Bank")) {
//            ((cast) actionCard).setPayees(bank);
//        }
//        else if(((cast) actionCard).getPayeeString().equalsIgnoreCase("CurrentPlayer")) {
//            ((cast) actionCard).setPayees(currPlayer);
//        }
//        if (((cast) actionCard).getPayerString().equalsIgnoreCase("Everyone")) {
//            ((cast) actionCard).setPayers(players);
//        }
//        else if (((cast) actionCard).getPayerString().equalsIgnoreCase("Bank")) {
//            ((cast) actionCard).setPayers(bank);
//        }
//        else if(((cast) actionCard).getPayerString().equalsIgnoreCase("CurrentPlayer")) {
//            ((cast) actionCard).setPayers(currPlayer);
//        }
//    }

    public void reinitializePay(ActionCard actionCard){
        List<AbstractAssetHolder> players = new ArrayList<>();
        for(AbstractPlayer p: myBoard.getMyPlayerList()) players.add(p);
        List<AbstractAssetHolder> bank = new ArrayList<>();
        bank.add(myBoard.getBank());
        List<AbstractAssetHolder> currPlayer = new ArrayList<>();
        currPlayer.add(myTurn.getMyCurrPlayer());
        if (((PayCard) actionCard).getPayeeString().equalsIgnoreCase("Everyone")) {
            ((PayCard) actionCard).setPayees(players);
        }
        else if (((PayCard) actionCard).getPayeeString().equalsIgnoreCase("Bank")) {
            ((PayCard) actionCard).setPayees(bank);
        }
        else if(((PayCard) actionCard).getPayeeString().equalsIgnoreCase("CurrentPlayer")) {
            ((PayCard) actionCard).setPayees(currPlayer);
        }
        if (((PayCard) actionCard).getPayerString().equalsIgnoreCase("Everyone")) {
            ((PayCard) actionCard).setPayers(players);
        }
        else if (((PayCard) actionCard).getPayerString().equalsIgnoreCase("Bank")) {
            ((PayCard) actionCard).setPayers(bank);
        }
        else if(((PayCard) actionCard).getPayerString().equalsIgnoreCase("CurrentPlayer")) {
            ((PayCard) actionCard).setPayers(currPlayer);
        }
    }

    public void reinitializeMoveAndPay(ActionCard actionCard){
        List<AbstractAssetHolder> players = new ArrayList<>();
        for(AbstractPlayer p: myBoard.getMyPlayerList()) players.add(p);
        List<AbstractAssetHolder> bank = new ArrayList<>();
        bank.add(myBoard.getBank());
        List<AbstractAssetHolder> currPlayer = new ArrayList<>();
        currPlayer.add(myTurn.getMyCurrPlayer());
        Tile tile = null;
        try {
            tile = myBoard.findNearest(myTurn.getMyCurrPlayer(), ((MoveAndPayCard) actionCard).getTargetTileType());
        } catch (TileNotFoundException e) {
            e.popUp();
        }
        ((MoveAndPayCard)actionCard).setTile(tile);
        if (((MoveAndPayCard) actionCard).getPayeeString().equalsIgnoreCase("Everyone")) {
            ((MoveAndPayCard) actionCard).setPayees(players);
        }
        else if (((MoveAndPayCard) actionCard).getPayeeString().equalsIgnoreCase("Bank")) {
            ((MoveAndPayCard) actionCard).setPayees(bank);
        }
        else if(((MoveAndPayCard) actionCard).getPayeeString().equalsIgnoreCase("CurrentPlayer")) {
            ((MoveAndPayCard) actionCard).setPayees(currPlayer);
        }
        else if (((MoveAndPayCard) actionCard).getPayeeString().equalsIgnoreCase("Owner")) {
            List<AbstractAssetHolder> payees = new ArrayList<>();
            payees.add(((AbstractPropertyTile)tile).getOwner());
            ((MoveAndPayCard) actionCard).setPayees(payees);
        }
        if (((MoveAndPayCard) actionCard).getPayerString().equalsIgnoreCase("Everyone")) {
            ((MoveAndPayCard) actionCard).setPayers(players);
        }
        else if (((MoveAndPayCard) actionCard).getPayerString().equalsIgnoreCase("Bank")) {
            ((MoveAndPayCard) actionCard).setPayers(bank);
        }
        else if(((MoveAndPayCard) actionCard).getPayerString().equalsIgnoreCase("CurrentPlayer")) {
            ((MoveAndPayCard) actionCard).setPayers(currPlayer);
        }
    }

    public void reinitializePayBuilding(ActionCard actionCard) {
        List<AbstractAssetHolder> players = new ArrayList<>();
        for (AbstractPlayer p : myBoard.getMyPlayerList()) players.add(p);
        List<AbstractAssetHolder> bank = new ArrayList<>();
        bank.add(myBoard.getBank());
        List<AbstractAssetHolder> currPlayer = new ArrayList<>();
        currPlayer.add(myTurn.getMyCurrPlayer());
        if (((PayBuildingsCard) actionCard).getPayeeString().equalsIgnoreCase("Everyone")) {
            ((PayBuildingsCard) actionCard).setPayees(players);
        } else if (((PayBuildingsCard) actionCard).getPayeeString().equalsIgnoreCase("Bank")) {
            ((PayBuildingsCard) actionCard).setPayees(bank);
        } else if (((PayBuildingsCard) actionCard).getPayeeString().equalsIgnoreCase("CurrentPlayer")) {
            ((PayBuildingsCard) actionCard).setPayees(currPlayer);
        }
        if (((PayBuildingsCard) actionCard).getPayerString().equalsIgnoreCase("Everyone")) {
            ((PayBuildingsCard) actionCard).setPayers(players);
        } else if (((PayBuildingsCard) actionCard).getPayerString().equalsIgnoreCase("Bank")) {
            ((PayBuildingsCard) actionCard).setPayers(bank);
        } else if (((PayBuildingsCard) actionCard).getPayerString().equalsIgnoreCase("CurrentPlayer")) {
            ((PayBuildingsCard) actionCard).setPayers(currPlayer);
        }
    }
    public void handleAuction() {
        Map<AbstractPlayer,Double> auctionAmount = new HashMap<>();
        for (int i = 0; i < myBoard.getMyPlayerList().size(); i++) {
            AbstractPlayer key = getPlayerAtIndex(i);
            String value = myGameView.showInputTextDialog("Auction Amount for player " + getPlayerNameAtIndex(i),
                    "Enter your auction amount:",
                    "Amount:");
            try {
                if (Double.parseDouble((value)) > key.getMoney()){
                    new NotEnoughMoneyException("You don't have enough money! Please enter again.").popUp();
                    i--;
                } else if (Double.parseDouble((value)) < 0.0){
                    new IllegalInputTypeException( "Input must be a positive number!" ).popUp();
                    i--;
                }else {
                    auctionAmount.put(key, Double.parseDouble((value)));
                }
            } catch (NumberFormatException n) {
                new IllegalInputTypeException("Input must be a number!").popUp();
                i--;
            }
        }
        AbstractPropertyTile property = (AbstractPropertyTile) myTurn.currPlayerTile();
        Map.Entry<AbstractPlayer, Double> winner = property.determineAuctionResults(auctionAmount);
        String info = winner.getKey().getMyPlayerName() + " wins " + myTurn.getTileNameforPlayer(myTurn.getMyCurrPlayer()) + " for " + winner.getValue() + " Monopoly Dollars!";
        myGameView.displayActionInfo(info);
        myGameView.updateLogDisplay(info);
        Map<AbstractPlayer, Double> playerValue = convertEntrytoMap(winner);
        try {
            buyHelper(playerValue);
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
        }
        myGameView.updateAssetDisplay(myBoard.getMyPlayerList(),null);
    }


    public void handlePayTaxPercentage() {
        double tax = myTurn.getMyCurrPlayer().getMoney() * ((IncomeTaxTile) myTurn.currPlayerTile()).getTaxMultiplier();
        try {
            myTurn.getMyCurrPlayer().payFullAmountTo( myBoard.getBank(),tax);
            myGameView.updateAssetDisplay(myBoard.getMyPlayerList(), null);
            myGameView.displayActionInfo( "You've paid " + tax + " in taxes.");
        } catch (NotEnoughMoneyException e) {
            e.popUp();
            myGameView.disableButton( "End Turn" );
        }
        myGameView.updateLogDisplay( myTurn.getMyCurrPlayer().getMyPlayerName() + " payed " + tax + " in taxes.");
    }

    public void handleBuy(){
        try {
            Map.Entry<AbstractPlayer, Double> playerValue = buyHelper(null);
            String info = playerValue.getKey().getMyPlayerName() + " bought " + this.myTurn.getTileNameforPlayer(playerValue.getKey()) + " for " + playerValue.getValue() + " Monopoly Dollars!";
            myGameView.displayActionInfo(info);
        } catch (IllegalActionOnImprovedPropertyException e) {
            e.popUp();
        } catch (IllegalInputTypeException e) {
            e.popUp();
        } catch (OutOfBuildingStructureException e) {
            e.popUp();
        } catch (NotEnoughMoneyException e){
            e.popUp();
            myGameView.displayActionInfo( "This property must go to auction." );
            handleAuction();
        } catch (UpgradeMaxedOutException e) {
            e.popUp();
        }
    }

    private Map.Entry<AbstractPlayer, Double> buyHelper(Map<AbstractPlayer,Double> paramMap) throws IllegalActionOnImprovedPropertyException, IllegalInputTypeException, OutOfBuildingStructureException, NotEnoughMoneyException, UpgradeMaxedOutException {
        AbstractPlayer player = null;
        double value = 0;
        if (paramMap != null) {
            if (paramMap.keySet().size()==1) {
                for (AbstractPlayer p : paramMap.keySet()) {
                    player = p;
                }
            }
            value = paramMap.get(player);
        }
        else {
            player = myTurn.getMyCurrPlayer();
            value = ((AbstractPropertyTile)myTurn.currPlayerTile()).getTilePrice();
        }
        buyProperty(player, value);
        Map.Entry<AbstractPlayer,Double> ret = new AbstractMap.SimpleEntry<>(player, value);
        return ret;
    }

    private void buyProperty(AbstractPlayer player, Double value) throws IllegalActionOnImprovedPropertyException, IllegalInputTypeException, OutOfBuildingStructureException, NotEnoughMoneyException, UpgradeMaxedOutException {
        AbstractPropertyTile property;
        property = (AbstractPropertyTile) myTurn.currPlayerTile();
        List<AbstractPropertyTile> sameSetProperties = myBoard.getColorListMap().get( property.getCard().getCategory());
        property.sellTo( player, value, sameSetProperties );

    }

    private ObservableList<String> getAllPlayerNames() {
        ObservableList<String> players = FXCollections.observableArrayList();
        for (AbstractPlayer p : myBoard.getMyPlayerList()) {
            players.add( p.getMyPlayerName() );
        }
        return players;
    }

    private AbstractPlayer getPlayerAtIndex(int i) {
        return myBoard.getMyPlayerList().get(i);
    }

    private Map<AbstractPlayer, Double> convertEntrytoMap(Map.Entry<AbstractPlayer, Double> param) {
        Map<AbstractPlayer, Double> mapFromSet = new HashMap<>();
        mapFromSet.put(param.getKey(), param.getValue());
        return mapFromSet;
    }

    private String getPlayerNameAtIndex(int i) {
        return getPlayerAtIndex(i).getMyPlayerName();
    }
}
