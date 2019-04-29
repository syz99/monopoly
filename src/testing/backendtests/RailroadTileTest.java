package testing.backendtests;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.Bank;
import backend.assetholder.HumanPlayer;
import backend.board.AbstractBoard;
import backend.board.StandardBoard;
import backend.tile.BuildingTile;
import backend.tile.RailroadTile;
import configuration.XMLData;
import exceptions.IllegalActionOnImprovedPropertyException;
import exceptions.MortgagePropertyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RailroadTileTest {

    private Bank bank;
    private RailroadTile railroadTile;
    private List<AbstractPlayer> playerList;
    private AbstractBoard board;

    @BeforeEach
    void setUp(){
        XMLData data = new XMLData("TestMonopoly.xml");
        bank = data.getBank();
        playerList = new ArrayList<>();
        playerList.add(new HumanPlayer("TestPlayer", "Icon1", 1000.0));
        board = new StandardBoard(playerList, data);
        railroadTile = (RailroadTile) data.getTiles().get(4);
    }

    @Test
    void calculateRentPriceMortgaged() {
        try {
            railroadTile.mortgageProperty();
        } catch (MortgagePropertyException e) {
        } catch (IllegalActionOnImprovedPropertyException e) {
        }
        double expected = 0.0;
        double actual = railroadTile.calculateRentPrice( 5 );
        assertEquals( expected, actual );

    }

    @Test
    void calculateRentPriceRollOneOwnOne() {
        double expected = 25.0;
        double actual = railroadTile.calculateRentPrice( 1 );
        assertEquals( expected, actual );

    }
}