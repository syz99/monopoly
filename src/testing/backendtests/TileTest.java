package testing.backendtests;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.Bank;
import backend.assetholder.HumanPlayer;
import backend.board.AbstractBoard;
import backend.board.StandardBoard;
import backend.tile.BuildingTile;
import backend.tile.Tile;
import backend.tile.UtilityTile;
import configuration.XMLData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {
    private Bank bank;
    private BuildingTile building;
    private List<AbstractPlayer> playerList;
    private AbstractBoard board;
    private Tile tile;
    private BuildingTile buildingTile;


    @BeforeEach
    void setUp(){
        XMLData data = new XMLData("TestMonopoly.xml");
        bank = data.getBank();
        playerList = new ArrayList<>();
        playerList.add(new HumanPlayer("TestPlayer", "Icon1", 1000.0));
        board = new StandardBoard(playerList, data);
        tile = data.getTiles().get(0);
        buildingTile = (BuildingTile) data.getTiles().get(1);
    }


    @Test
    void getTileIndex() {
        int expected = 0;
        int actual = tile.getTileIndex();
        assertEquals( expected,actual );
    }


    @Test
    void notJailTileTest() {
        assertFalse( tile.isJailTile() );
    }

    @Test
    void notGoToTileTest() {
        assertFalse( tile.isGoToJailTile() );

    }

    @Test
    void isGoTileTest() {
        assertTrue( tile.isGoTile() );
    }

    @Test
    void notPropertyTile() {
        assertFalse( tile.isPropertyTile());
    }

    @Test
    void notBuildingTile() {
        assertFalse( tile.isBuildingTile() );
    }

    @Test
    void applyPassedAction() {
        int expected = 0;
        int actual = buildingTile.applyPassedAction(playerList.get( 0 )).size();
        assertEquals( expected, actual );
    }
}