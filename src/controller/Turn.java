package controller;

import backend.assetholder.AbstractPlayer;
import backend.board.AbstractBoard;
import backend.dice.AbstractDice;
import backend.tile.AbstractPropertyTile;
import backend.tile.Tile;
import frontend.bot_manager.AutomatedPlayerManager;
import frontend.views.player_options.BPaneOptionsView;

import configuration.XMLWriter;

import java.util.*;

/**
 * This class represents a single turn in the game of Monopoly
 * from dice roll to the end of a turn.
 *
 * sub-controller of controller.game
 *
 * @author Matt
 * @author Sam
 */
public class Turn {

    private AbstractPlayer myCurrPlayer;
    private AbstractBoard  myBoard;
    private AbstractDice   myDice;
    private List<String>   myCurrentTileActions;
    private Integer[]      myRolls;
    private AutomatedPlayerManager autoManager;
    private BPaneOptionsView bPane;


    private Integer numMoves;
    public Turn (AbstractPlayer player, AbstractDice dice, AbstractBoard board) {
        myCurrPlayer = player;
//        if(myCurrPlayer.isAuto()) {
//            view = SplitScreenGameView;
//            bPane =
//            autoManager = new AutomatedPlayerManager(bPane);
//        }
        myBoard = board;
        myDice = dice;
        myCurrentTileActions = new ArrayList<>();
        numMoves = 0;
    }

    public void start() {
        myRolls = rollDice(myBoard.getNumDie());
        XMLWriter.writeXML(System.getProperty("user.dir")+"\\data\\saved_xml.xml", myBoard);
    }

    public void skipTurn() {
        myCurrPlayer = getNextPlayer();
    }

    public Tile currPlayerTile(){
        return myBoard.getPlayerTile( myCurrPlayer );
    }

    private AbstractPlayer getNextPlayer() {
        Iterator<AbstractPlayer> iterator = myBoard.getMyPlayerList().iterator();

        while (iterator.hasNext()) {
            AbstractPlayer current = iterator.next();
//            if(current.isAuto()==(true)) {
//                GameController.getMyAutoManager().autoPlayerTurn(current);
//            }
            if (current.equals(myCurrPlayer) && iterator.hasNext())  // employs custom AbstractPlayer.equals()
                return iterator.next(); // get next player if myCurrPlayer not last element
        }
        return myBoard.getMyPlayerList().get(0); // reached end of list thus modulo to beginning
    }

    // TODO: MAKE PRIVATE ONCE GAME.ROLLVALUE() IS DELETED
    public Integer[] rollDice(int numDie) {
        Integer[] rolls = new Integer[numDie];
        for (int i = 0; i < rolls.length; i++) rolls[i] = myDice.roll();
        myRolls = rolls;
        return rolls;
    }

    public void setNumMoves(){
        numMoves = 0;
        for (int roll : myRolls) {
            numMoves += roll;
        }
    }

    public int getNumMoves() {
        return numMoves;
    }

    public void setNumMoves(int moves){
        numMoves = moves;
    }

    //in a turn a player can roll/move, trade, mortgage
    public void setNextPlayer(AbstractPlayer nextPlayer) {
        myCurrPlayer = nextPlayer;
    }

    public boolean isDoubleRoll(Integer[] rolls) {
        if (rolls.length == 1) return false;
        HashSet<Integer> doubles = new HashSet(Arrays.asList(rolls));
        return doubles.size() == 1;
    }

    public AbstractPlayer getMyCurrPlayer() { return myCurrPlayer; }
    public Integer[] getRolls() { return myRolls; }

    public List<String> getMyCurrentTileActions() {
        return myCurrentTileActions;
    }

    public String getTileNameforPlayer(AbstractPlayer p) {
        //TODO: exceptions if current tile is not abstract property tile
        return ((AbstractPropertyTile)myBoard.getPlayerTile(p)).getTitleDeed();
    }
}