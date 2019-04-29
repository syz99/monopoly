package testing.backendtests;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.Bank;
import backend.assetholder.HumanPlayer;
import backend.board.AbstractBoard;
import backend.board.StandardBoard;
import backend.tile.GoToJailTile;
import backend.tile.IncomeTaxTile;
import configuration.XMLData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoToJailTileTest {
    private Bank bank;
    private GoToJailTile goToJailTile;
    private List<AbstractPlayer> playerList;
    private AbstractBoard board;

    @BeforeEach
    void setUp(){
        XMLData data = new XMLData("TestMonopoly.xml");
        bank = data.getBank();
        playerList = new ArrayList<>();
        playerList.add(new HumanPlayer("TestPlayer", "Icon1", 1000.0));
        board = new StandardBoard(playerList, data);
        goToJailTile = (GoToJailTile) data.getTiles().get(9);
    }

    @Test
    void applyLandedOnAction() {
        List<String> expected = new ArrayList<>(  );
        expected.add("GoToJail");
        List<String> actual = goToJailTile.applyLandedOnAction( playerList.get( 0 ) );
        assertEquals( expected, actual );
    }


    @Test
    void isGoToJailTile() {
        assertTrue( goToJailTile.isGoToJailTile() );
    }
}