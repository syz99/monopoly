package testing.backendtests;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.Bank;
import backend.assetholder.HumanPlayer;
import backend.board.AbstractBoard;
import backend.board.StandardBoard;
import backend.tile.BuildingTile;
import backend.tile.JailTile;
import backend.tile.UtilityTile;
import configuration.XMLData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JailTileTest {
    private Bank bank;
    private JailTile jail;
    private List<AbstractPlayer> playerList;
    private AbstractBoard board;

    @BeforeEach
    void setUp(){
        XMLData data = new XMLData("TestMonopoly.xml");
        bank = data.getBank();
        playerList = new ArrayList<>();
        playerList.add(new HumanPlayer("TestPlayer", "Icon1", 1000.0));
        board = new StandardBoard(playerList, data);
        jail = (JailTile) data.getTiles().get(6);
    }

    @Test
    void applyLandedOnActionNotInJail() {
        List<String> expected = new ArrayList<>(  );
        List<String> actual = jail.applyLandedOnAction( playerList.get( 0 ) );
        assertEquals( expected, actual );
    }

    @Test
    void applyLandedOnActionnJail() {
        playerList.get( 0 ).addTurnInJail();
        List<String> expected = new ArrayList<>( );
        expected.add( "PayBail" );
        expected.add( "StayInJail" );
        List<String> actual = jail.applyLandedOnAction( playerList.get( 0 ) );
        assertEquals( expected, actual );
    }

    @Test
    void isJailTile() {
        assertTrue( jail.isJailTile() );
    }

    @Test
    void getBailAmount() {
        double expected = 50;
        double actual = jail.getBailAmount();
        assertEquals( expected,actual );
    }

    @Test
    void getName() {
        String expected = "Jail Tile";
        String actual = jail.getName();
        assertEquals( expected, actual );
    }
}