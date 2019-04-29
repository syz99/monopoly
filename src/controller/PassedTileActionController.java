package controller;

import backend.assetholder.Bank;
import backend.board.AbstractBoard;
import frontend.views.game.AbstractGameView;

public class PassedTileActionController {

    AbstractBoard myBoard;
    Turn myTurn;
    AbstractGameView myGameView;
    Bank myBank;

    public PassedTileActionController(AbstractBoard board, Turn turn, AbstractGameView gameView) {
        this.myBoard = board;
        this.myTurn = turn;
        this.myGameView = gameView;
    }

    public void handleCollectMoneyPassed() {
        myBoard.getBank().payFullAmountTo( myTurn.getMyCurrPlayer(), myBoard.getGoTile().getPassedMoney() );
        myGameView.displayActionInfo( "You collected " + myBoard.getGoTile().getPassedMoney() + " for passing go." );
        myGameView.updateLogDisplay(myTurn.getMyCurrPlayer().getMyPlayerName() + " collected " + myBoard.getGoTile().getPassedMoney() + " for passing go.");
    }
}
