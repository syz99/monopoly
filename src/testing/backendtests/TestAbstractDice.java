package testing.backendtests;

import backend.dice.AbstractDice;
import backend.dice.NDice;
import backend.dice.SixDice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TestAbstractDice {

    AbstractDice sixDice;
    AbstractDice nDice;

    @BeforeEach
    void setup() {
        sixDice = new SixDice();
        nDice = new NDice(12);
    }

    @Test
    void testSixDiceRoll() {
        int actual = sixDice.roll();
        assertTrue(actual > 0 && actual <=6 );

    }

    @Test
    void testTwelveDiceRoll(){
        int actual = nDice.roll();
        assertTrue(actual > 0 && actual <=12 );
    }
}
