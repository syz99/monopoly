package testing.backendtests;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.Bank;
import backend.assetholder.HumanPlayer;
import backend.board.AbstractBoard;
import backend.board.StandardBoard;
import backend.tile.BuildingTile;
import configuration.XMLData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankTest {

    private Bank bank;
    private BuildingTile building;
    private List<AbstractPlayer> playerList;
    private AbstractBoard board;

    @BeforeEach
    void setUp(){
        XMLData data = new XMLData("TestMonopoly.xml");
        bank = data.getBank();
        playerList = new ArrayList<>();
        playerList.add(new HumanPlayer("TestPlayer", "Icon1", 0.0));
        board = new StandardBoard(playerList, data);
        building = (BuildingTile) data.getTiles().get(1);
    }

    @Test
    void addProperty() {
        bank.addProperty(building);
        assertTrue(bank.getProperties().contains(building));
    }

    @Test
    void payFullAmountTo() {
        bank.payFullAmountTo(playerList.get(0), 300.0);
        assertEquals(300.0, playerList.get(0).getMoney());
    }

}
