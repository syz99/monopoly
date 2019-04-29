package testing.controllertests;

import controller.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestTurn {

    private Turn turn1;

    @BeforeEach
    void setup() {
//        turn1 = new Turn(new HumanPlayer(), )
    }

    @Test
    void testIsDoubleRollReturnsTrue() {
        int[] doubleRoll = new int[] {3, 3};
        assert equals(5);

    }

}
