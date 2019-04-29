
package testing.backendtests;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.Bank;
import backend.assetholder.HumanPlayer;
import backend.tile.BuildingTile;
import configuration.XMLData;
import exceptions.NotEnoughMoneyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerTest {

    Bank bank;
    AbstractPlayer humanPlayer1;
    AbstractPlayer humanPlayer2;

    @BeforeEach
    void setUp(){
        bank = new XMLData("TestMonopoly.xml").getBank();
        humanPlayer1 = new HumanPlayer("TestPlayer1", "Icon1", 1000.0);
        humanPlayer2 = new HumanPlayer("TestPlayer2", "Icon2", 1000.0);
    }

    @Test
    void addTurnsInJail(){
        humanPlayer1.addTurnInJail();
        assertEquals(0, humanPlayer1.getTurnsInJail());
    }

    @Test
    void getOutOfJail(){
        humanPlayer1.addTurnInJail();
        humanPlayer1.getOutOfJail();
        assertEquals(-1, humanPlayer1.getTurnsInJail());
    }

    @Test
    void testBankruptcyBoolean(){
        humanPlayer1.declareBankruptcy(bank);
        assertTrue(humanPlayer1.isBankrupt());
    }

    @Test
    void testMoneyAfterBankruptcy(){
        humanPlayer1.declareBankruptcy(bank);
        assertEquals(humanPlayer1.getMoney(), 0);
    }

    @Test
    void testPropertiesAfterBankruptcy(){
        humanPlayer1.declareBankruptcy(bank);
        assertTrue(humanPlayer1.getProperties().isEmpty());
    }

    @Test
    void testPayTakesFunds(){
        try {
            humanPlayer1.payFullAmountTo(humanPlayer2, 200.0);
        } catch (NotEnoughMoneyException e) {
        }
        assertEquals(800.0, humanPlayer1.getMoney());
    }

    @Test
    void testPayReceiverGetsFunds() {
        try {
            humanPlayer1.payFullAmountTo(humanPlayer2, 200.0);
        } catch (NotEnoughMoneyException e) {
        }
        assertEquals(1200.0, humanPlayer2.getMoney());
    }

    @Test
    void testAddingProperties(){
        BuildingTile tile = (BuildingTile) new XMLData("TestMonopoly.xml").getTiles().get(1);
        humanPlayer1.addProperty(tile);
        assertTrue(humanPlayer1.getProperties().contains(tile));
    }
}

//package backend.assetholder;
//
//import backend.card.property_cards.BuildingCard;
//import backend.card.property_cards.PropertyCard;
//import backend.tile.BuildingTile;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.awt.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class PlayerTest {
//
//    Bank bank;
//    AbstractPlayer autoPlayer;
//    AbstractPlayer humanPlayer;
//
//    @BeforeEach
//    void setUp(){
//        bank = new Bank(10000.0);
//        autoPlayer = new AutomatedPlayer(1500.0, bank);
//        humanPlayer = new HumanPlayer(1500.0, bank);
//    }
//
//    @Test
//    void addTurnsInJail(){
//        autoPlayer.addTurnInJail();
//        assertEquals(0, autoPlayer.getTurnsInJail());
//    }
//
//    @Test
//    void getOutOfJail(){
//        autoPlayer.addTurnInJail();
//        autoPlayer.getOutOfJail();
//        assertEquals(-1, autoPlayer.getTurnsInJail());
//    }
//
//    @Test
//    void testBankruptcyBoolean(){
//        autoPlayer.declareBankruptcyTo(bank);
//        assertTrue(autoPlayer.isBankrupt());
//    }
//
//    @Test
//    void testMoneyAfterBankruptcy(){
//        autoPlayer.declareBankruptcyTo(bank);
//        assertEquals(autoPlayer.getMoney(), 0);
//    }
//
//    @Test
//    void testPropertiesAfterBankruptcy(){
//        autoPlayer.declareBankruptcyTo(bank);
//        assertTrue(autoPlayer.getProperties().isEmpty());
//    }
//
//    @Test
//    void testPayTakesFunds(){
//        autoPlayer.payFullAmountTo(humanPlayer, 200.0);
//        assertEquals(1300.0, autoPlayer.getMoney());
//    }
//
//    @Test
//    void testPayReceiverGetsFunds(){
//        autoPlayer.payFullAmountTo(humanPlayer, 200.0);
//        assertEquals(1700.0, humanPlayer.getMoney());
//    }
//
//    @Test
//    void testAddingProperties(){
//        BuildingTile tile =new BuildingTile(bank, new BuildingCard("Property", 0.0,0.0, java.awt.Color.BLUE,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0), "Property", 200, Color.BLUE);
//        autoPlayer.addProperty(tile);
//        assertTrue(autoPlayer.getProperties().contains(tile));
//    }
//
//}