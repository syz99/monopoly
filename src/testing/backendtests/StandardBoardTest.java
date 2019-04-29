package testing.backendtests;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.HumanPlayer;
import backend.board.StandardBoard;
import backend.tile.Tile;
import configuration.XMLData;
import exceptions.MultiplePathException;
import exceptions.TileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardBoardTest {

    private StandardBoard board;
    private List<AbstractPlayer> playerList;
    private XMLData data;


    @BeforeEach
    void setUp(){
        playerList = new ArrayList<>();
        playerList.add(new HumanPlayer("TestPlayer", "Icon1", 1000.0));
        data = new XMLData("TestMonopoly.xml");
        board = new StandardBoard(playerList, data);
    }

    @Test
    void movesOneTile(){
        try {
            board.movePlayerByOne(playerList.get(0));
        } catch (MultiplePathException e) { }
        assertEquals(data.getTiles().get(1), board.getPlayerTile(playerList.get(0)));
    }

    @Test
    void movePlayerToNearest() {
        Tile railroad = data.getTiles().get(8);
        try {
            board.findNearest(playerList.get(0), railroad.getTileType());
        } catch (TileNotFoundException e) { }
        assertEquals(data.getTiles().get(4), board.getPlayerTile(playerList.get(0)));
    }

    @Test
    void testGetJailTile(){
        try {
            assertEquals(data.getTiles().get(6), board.getJailTile());
        } catch (TileNotFoundException e) { }
    }

    @Test
    void testGetGoTile(){
        assertEquals(data.getTiles().get(0), board.getGoTile());
    }
}