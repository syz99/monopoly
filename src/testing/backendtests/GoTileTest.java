package testing.backendtests;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.Bank;
import backend.assetholder.HumanPlayer;
import backend.board.AbstractBoard;
import backend.board.StandardBoard;
import backend.tile.GoTile;
import backend.tile.GoToJailTile;
import configuration.XMLData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoTileTest {

    private Bank bank;
    private GoTile goTile;
    private List<AbstractPlayer> playerList;
    private AbstractBoard board;

    @BeforeEach
    void setUp(){
        XMLData data = new XMLData("TestMonopoly.xml");
        bank = data.getBank();
        playerList = new ArrayList<>();
        playerList.add(new HumanPlayer("TestPlayer", "Icon1", 1000.0));
        board = new StandardBoard(playerList, data);
        goTile = (GoTile) data.getTiles().get(0);
    }

    @Test
    void getLandedOnMoney() {
        double expected = 200;
        double actual = goTile.getLandedOnMoney();
        assertEquals( expected, actual );
    }

    @Test
    void getPassedMoney() {
        double expected = 200;
        double actual = goTile.getPassedMoney();
        assertEquals( expected, actual );
    }

    @Test
    void applyLandedOnAction() {
        List<String> expected = new ArrayList<>( );
        expected.add("CollectMoneyLanded");
        List<String> actual = goTile.applyLandedOnAction( playerList.get( 0 ) );
        assertEquals( expected, actual );

    }

    @Test
    void applyPassedAction() {
        List<String> expected = new ArrayList<>();
        expected.add("CollectMoneyPassed");
        List<String> actual = goTile.applyPassedAction( playerList.get( 0 ) );
        assertEquals( expected, actual );
    }

    @Test
    void isGoTile() {
        assertTrue(goTile.isGoTile());
    }
}