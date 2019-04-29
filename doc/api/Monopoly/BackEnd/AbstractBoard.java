package api.Monopoly.BackEnd;
import java.util.*;

/**
 *
 */
public abstract class AbstractBoard {

    /**
     * Default constructor
     */
    public AbstractBoard() {
    }

    /**
     *
     */
    private HashMap playerToTileMap;

    /**
     *
     */
    private List tilePath;

    /**
     *
     */
    private List decks;

    /**
     *
     */
    private int[][] myTilePath;






    /**
     *
     */
    private void checkPassedGo() {
        // TODO implement here
    }

    /**
     *
     */
    private void movePlayers() {
        // TODO implement here
    }

    /**
     *
     */
    private void checkIfJailed() {
        // TODO implement here
    }

    /**
     * @param config
     */
    public void initialize(Scanner config) {
        // TODO implement here
    }
}