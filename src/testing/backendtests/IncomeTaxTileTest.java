package testing.backendtests;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.Bank;
import backend.assetholder.HumanPlayer;
import backend.board.AbstractBoard;
import backend.board.StandardBoard;
import backend.tile.IncomeTaxTile;
import backend.tile.JailTile;
import configuration.XMLData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IncomeTaxTileTest {
    private Bank bank;
    private IncomeTaxTile taxTile;
    private List<AbstractPlayer> playerList;
    private AbstractBoard board;

    @BeforeEach
    void setUp(){
        XMLData data = new XMLData("TestMonopoly.xml");
        bank = data.getBank();
        playerList = new ArrayList<>();
        playerList.add(new HumanPlayer("TestPlayer", "Icon1", 1000.0));
        board = new StandardBoard(playerList, data);
        taxTile = (IncomeTaxTile) data.getTiles().get(3);
    }

    @Test
    void applyLandedOnAction() {
        List<String> expected = new ArrayList<>(  );
        expected.add("PayTaxFixed");
        expected.add("PayTaxPercentage");
        List<String> actual = taxTile.applyLandedOnAction( playerList.get( 0 ) );
        assertEquals( expected, actual );
    }

    @Test
    void getTaxMultiplier() {
        double expected = 0.1;
        double actual = taxTile.getTaxMultiplier();
        assertEquals( expected, actual );
    }
}