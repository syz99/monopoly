package testing.backendtests;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.Bank;
import backend.assetholder.HumanPlayer;
import backend.board.AbstractBoard;
import backend.board.StandardBoard;
import backend.tile.BuildingTile;
import backend.tile.UtilityTile;
import configuration.XMLData;
import exceptions.IllegalActionOnImprovedPropertyException;
import exceptions.MortgagePropertyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTileTest {
    private Bank bank;
    private BuildingTile building;
    private UtilityTile utility;
    private List<AbstractPlayer> playerList;
    private AbstractBoard board;

    @BeforeEach
    void setUp(){
        XMLData data = new XMLData("TestMonopoly.xml");
        bank = data.getBank();
        playerList = new ArrayList<>();
        playerList.add(new HumanPlayer("TestPlayer", "Icon1", 1000.0));
        board = new StandardBoard(playerList, data);
        utility = (UtilityTile) data.getTiles().get(7);
    }

    @Test
    void rentPriceMortgaged(){
        try {
            utility.mortgageProperty();
        } catch (MortgagePropertyException e) {
        } catch (IllegalActionOnImprovedPropertyException e) {
        }
        double expected = 0;
        double actual = utility.calculateRentPrice(5);
        assertEquals( expected, actual );
    }

    @Test
    void rentPriceRollOneOneUtility(){
        double expected = 4;
        double actual = utility.calculateRentPrice( 1 );
        assertEquals( expected, actual );
    }

}