package testing.backendtests;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.HumanPlayer;
import backend.board.StandardBoard;
import backend.card.property_cards.PropertyCard;
import backend.tile.AbstractNonBuildingPropertyTile;
import backend.tile.AbstractPropertyTile;
import configuration.XMLData;
import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NonBuildingPropertyTileTest {

    private StandardBoard board;
    private List<AbstractPlayer> playerList;
    private XMLData data;
    private AbstractPlayer currentPlayer;
    private AbstractNonBuildingPropertyTile tile;


    @BeforeEach
    void setUp(){
        playerList = new ArrayList<>();
        playerList.add(new HumanPlayer("TestPlayer", "Icon1", 1000.0));
        data = new XMLData("TestMonopoly.xml");
        board = new StandardBoard(playerList, data);
        currentPlayer = playerList.get(0);
        try {
            for (int i = 0; i < 4; i++) {
                board.movePlayerByOne(currentPlayer);
            }
        } catch (MultiplePathException e) { }
        tile =  ((AbstractNonBuildingPropertyTile)board.getPlayerTile(currentPlayer));
    }

    @Test
    void testReturnedActions(){
        List<String> expected = new ArrayList<>();
        expected.add("Buy");
        expected.add("Auction");
        assertEquals(expected, tile.applyLandedOnAction(currentPlayer));
    }

    @Test
    void testSwitchOwner() {
        tile.switchOwner(board.getBank());
        assertEquals(board.getBank(),tile.getOwner());
    }

    @Test
    void testSellTo() {
        List<AbstractPropertyTile> s = new ArrayList<>();
        s.add(tile);
        try {
            tile.sellTo(currentPlayer,tile.getCard().getTilePrice(),s);
        } catch (IllegalActionOnImprovedPropertyException e) {
        } catch (OutOfBuildingStructureException e) {
        } catch (IllegalInputTypeException e) {
        } catch (NotEnoughMoneyException e) {
        } catch (UpgradeMaxedOutException e) {
        }

        List<AbstractPropertyTile> expectedPlayerProperties = new ArrayList<>();
        expectedPlayerProperties.add(tile);
        List<AbstractPropertyTile> expectedBankProperties = new ArrayList<>();
        expectedBankProperties.add((AbstractPropertyTile)board.getTilesIndex(1));
        expectedBankProperties.add((AbstractPropertyTile)board.getTilesIndex(7));
        expectedBankProperties.add((AbstractPropertyTile)board.getTilesIndex(8));

        assertEquals(800, currentPlayer.getMoney());
        assertEquals(100200,board.getBank().getMoney());
        assertEquals(currentPlayer.getProperties(),tile);
        assertEquals(board.getBank().getProperties(),expectedBankProperties);
    }

    @Test
    void testGetName() {
        String expected = "Community Chest";
        assertEquals(expected, (board.getPlayerTile(currentPlayer).getName()));
    }
}
