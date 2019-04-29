package backend.dice;

import java.util.Random;

/**
 * This class is an abstraction of some n-sided dice
 *
 * @author Sam
 */
public abstract class AbstractDice {

    // TODO: make enumerator for each subclass and pass set of enum values up? since some dice might not just have #'s as values
    private static int numStates;

    public AbstractDice(int nStates) {
        numStates = nStates;
    }

    /**
     * Mimics the rolling of dice
     * @return an int, some random value within the range of the dice
     */
    public int roll() {
        return new Random().nextInt(numStates) + 1;
    }

    public int getNumStates() { return numStates; }
}
