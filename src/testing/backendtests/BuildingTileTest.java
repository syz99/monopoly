package testing.backendtests;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.Bank;
import backend.assetholder.HumanPlayer;
import backend.board.AbstractBoard;
import backend.board.StandardBoard;
import backend.tile.AbstractPropertyTile;
import backend.tile.BuildingTile;
import backend.tile.GoTile;
import configuration.XMLData;
import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTileTest {
    private Bank bank;
    private BuildingTile buildingTile;
    private List<AbstractPlayer> playerList;
    private AbstractBoard board;

    @BeforeEach
    void setUp(){
        XMLData data = new XMLData("TestMonopoly.xml");
        bank = data.getBank();
        playerList = new ArrayList<>();
        playerList.add(new HumanPlayer("TestPlayer", "Icon1", 1000.0));
        board = new StandardBoard(playerList, data);
        buildingTile = (BuildingTile) data.getTiles().get(1);
    }


    @Test
    void calculateRentPrice() {
        double expected = 2;
        double actual = buildingTile.calculateRentPrice( 5 );
        assertEquals( expected, actual );
    }

//    @Test
//    void sellAllBuildingsOnTile() {
//    }
//
//    @Test
//    void sellOneBuilding() {
//    }
//
//    @Test
//    void sellTo() {
//    }

    @Test
    void upgrade() {
        List<AbstractPropertyTile> sameSet = new ArrayList<>();
        sameSet.add( buildingTile );
        try {
            buildingTile.sellTo( playerList.get( 0 ), 0, sameSet);
        } catch (IllegalActionOnImprovedPropertyException e) {
        } catch (IllegalInputTypeException e) {
        } catch (OutOfBuildingStructureException e) {
        } catch (NotEnoughMoneyException e) {
        } catch (UpgradeMaxedOutException e) {
        }

        try {
            buildingTile.upgrade( playerList.get( 0 ), sameSet );
        } catch (IllegalInputTypeException e) {
        } catch (OutOfBuildingStructureException e) {
        } catch (NotEnoughMoneyException e) {
        } catch (UpgradeMaxedOutException e) {
        }
        String expected = "1House";
        String actual = buildingTile.getCurrentInUpgradeOrder();
        assertEquals( expected, actual );
    }


    @Test
    void checkIfImprovedProperty() {
        assertFalse( buildingTile.checkIfImprovedProperty() );
    }

    @Test
    void mortgageProperty() {
        try {
            buildingTile.mortgageProperty();
        } catch (MortgagePropertyException e) {
        } catch (IllegalActionOnImprovedPropertyException e) {
        }
        assertTrue( buildingTile.isMortgaged() );
    }

//    @Test
//    void getTilecolor() {
//        String expected = "Brown";
//        String actual = buildingTile.getTilecolor();
//        assertEquals( expected, actual );
//    }

//    @Test
//    void recalculateTotalPropertiesLeftOneBuildingUpdate() {
//    }
//
//    @Test
//    void recalculateTotalPropertiesLeftAfterWholeSale() {
//    }

    @Test
    void isBuildingTile() {
        assertTrue( buildingTile.isBuildingTile() );
    }
}