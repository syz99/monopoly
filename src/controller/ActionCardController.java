package controller;

import backend.assetholder.AbstractAssetHolder;
import backend.assetholder.AbstractPlayer;
import backend.board.AbstractBoard;
import backend.card.action_cards.HoldableCard;
import backend.card.property_cards.BuildingCard;
import backend.tile.*;
import exceptions.BuildingDoesNotExistException;
import exceptions.IncorrectUseOfHoldableException;
import exceptions.NotEnoughMoneyException;
import frontend.views.game.AbstractGameView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionCardController{

    AbstractBoard myBoard;
    Turn turn;
    AbstractGameView myGameView;
    GameController myGameController;

    public ActionCardController(AbstractBoard board, Turn turn, AbstractGameView myGameView, GameController myGameController) {
        this.myBoard = board;
        this.turn = turn;
        this.myGameView = myGameView;
        this.myGameController = myGameController;
    }

    public void handlePay(List<Object> parameters){
        List<AbstractAssetHolder> payers = (List<AbstractAssetHolder>) parameters.get( 0 );
        List<AbstractAssetHolder> payees = (List<AbstractAssetHolder>) parameters.get( 1 );
        double amount = (Double) parameters.get( 2 );
        payHelper( payers, payees, amount );
    }

    public void handleMove(List<Object> parameters){
        myBoard.movePlayer( turn.getMyCurrPlayer(), (Tile) parameters.get( 0 ) );
        myGameView.updateIconDisplay(turn.getMyCurrPlayer(), (Tile) parameters.get(0));
        myGameController.handleTileLanding((Tile) parameters.get(0));
        myGameView.updateAssetDisplay(myBoard.getMyPlayerList(),null);
    }

    public void handleMoveBackwards(List<Object> parameters) {
        int index = (int) parameters.get(1);
        int newIndex = myBoard.getPlayerTile(turn.getMyCurrPlayer()).getTileIndex() - index;
        if (newIndex < 0) {
            newIndex = myBoard.getBoardSize() + newIndex;
        }
        Tile newTile = myBoard.getTilesIndex(newIndex);
        myBoard.movePlayer(turn.getMyCurrPlayer(), newTile);
        myGameView.updateIconDisplay(turn.getMyCurrPlayer(), newTile);
        myGameController.handleTileLanding(newTile);
        myGameView.updateAssetDisplay(myBoard.getMyPlayerList(),null);
    }

    public void handleGetOutOfJail(List<Object> parameters) {
        AbstractPlayer cardUser = (AbstractPlayer)parameters.get(0);
        HoldableCard card = (HoldableCard)parameters.get(1);
        //TODO: can buy get out of jail card?
        if (!cardUser.isInJail()) {
            new IncorrectUseOfHoldableException("You are not in jail so you cannot use this card at this time!").popUp();
        }
        else {
            cardUser.getOutOfJail();
            cardUser.getCards().remove(card);
            myGameView.displayActionInfo( "You've used your get out of jail card. You're free now!" );
        }
    }

    public void handleSave(List<Object> parameters){
        turn.getMyCurrPlayer().getCards().add( (HoldableCard) parameters.get( 0 ) );
        myGameView.updateAssetDisplay(myBoard.getMyPlayerList(), null);
        myGameView.displayActionInfo( "You now own " + ((HoldableCard) parameters.get( 0 )).getName() + ". You can use this card at any time." );
    }

    public void handleMoveAndPay(List<Object> parameters){
        Tile tile = (Tile) parameters.get(0);
        List<AbstractAssetHolder> payers = (List<AbstractAssetHolder>) parameters.get( 1 );
        List<AbstractAssetHolder> payees = (List<AbstractAssetHolder>) parameters.get( 2 );
        double amount = (Double) parameters.get( 3 );
        myBoard.movePlayer(turn.getMyCurrPlayer(),tile);
        myGameView.updateIconDisplay(turn.getMyCurrPlayer(),tile);
        if (payees.size()!= 1 || !(payees.get(0).equals(myBoard.getBank()))) {
            payHelper( payers, payees, amount * ((AbstractPropertyTile) tile).getCard().getRentPriceLookupTable().get(((AbstractPropertyTile) tile).getCurrentInUpgradeOrder()));
        }
    }

    public void handlePayBuilding(List<Object> parameters) throws BuildingDoesNotExistException {
        List<AbstractAssetHolder> payers = (List<AbstractAssetHolder>) parameters.get( 0 );
        List<AbstractAssetHolder> payees = (List<AbstractAssetHolder>) parameters.get( 1 );
        Map<String, Double> baseToMultiplier = (Map<String, Double>) parameters.get( 2 );
        double total = 0;
        Map<String, Integer> totalBuildings = new HashMap<>();
        for(AbstractPropertyTile property: turn.getMyCurrPlayer().getProperties()){
            if(property.isBuildingTile()){
                BuildingCard card = ((BuildingCard) property.getCard());
                String base = card.getBasePropertyType(property.getCurrentInUpgradeOrder());
                if(baseToMultiplier.containsKey( base )){
                    int number = card.getNumericValueOfPropertyType( property.getCurrentInUpgradeOrder() );
                    System.out.println(total);
                    total += baseToMultiplier.get( base ) * number;
                } else{
                    throw new BuildingDoesNotExistException( "Building does not exist" );
                }
            }
        }
        payHelper( payers, payees, total );
    }

    private void payHelper(List<AbstractAssetHolder> payers, List<AbstractAssetHolder> payees, Double amount) {
        for (AbstractAssetHolder payer : payers) {
            for (AbstractAssetHolder payee : payees) {
                try {
                    payer.payFullAmountTo( payee, amount );
                } catch (NotEnoughMoneyException e) {
                    e.popUp();
                }
            }
        }
        myGameView.updateAssetDisplay(myBoard.getMyPlayerList(), null);
    }
}