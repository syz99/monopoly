package testing.backendtests;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.HumanPlayer;
import backend.board.StandardBoard;
import backend.card.action_cards.ActionCard;
import backend.card.action_cards.MoveCard;
import backend.deck.DeckInterface;
import backend.tile.AbstractDrawCardTile;
import backend.tile.CommunityChestTile;
import backend.tile.Tile;
import configuration.XMLData;
import exceptions.MultiplePathException;
import exceptions.TileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DrawCardTileTest {

    private StandardBoard board;
    private List<AbstractPlayer> playerList;
    private XMLData data;
    private AbstractPlayer currentPlayer;
    private CommunityChestTile tile;


    @BeforeEach
    void setUp(){
        playerList = new ArrayList<>();
        playerList.add(new HumanPlayer("TestPlayer", "Icon1", 1000.0));
        data = new XMLData("TestMonopoly.xml");
        board = new StandardBoard(playerList, data);
        currentPlayer = playerList.get(0);
        try {
            for (int i = 0; i < 2; i++) {
                board.movePlayerByOne(currentPlayer);
            }
        } catch (MultiplePathException e) { }
        tile = (CommunityChestTile)board.getPlayerTile(currentPlayer);
    }

    @Test
    void testReturnedActions(){
        List<String> expected = new ArrayList<>();
        expected.add("DrawCard");
        assertEquals(expected, tile.applyLandedOnAction(currentPlayer));
    }

//    @Test
//    void testDrawCard() {
//        ActionCard expected = (board.getMyDecks().get(0)).drawCard();
//        assertEquals(expected, ((CommunityChestTile)board.getPlayerTile(currentPlayer)).drawCard());
//    }

    @Test
    void testGetName() {
        String expected = "Community Chest";
        assertEquals(expected, (tile.getName()));
    }
}