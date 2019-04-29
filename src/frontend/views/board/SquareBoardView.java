package frontend.views.board;

import backend.board.AbstractBoard;
import configuration.ImportPropertyFile;

/**
 * This class represents a square-shaped Board View; Essentially
 * an extension of RectangularBoardView with equal-lengthed sides
 *
 * @author Edward
 */
public class SquareBoardView extends RectangularBoardView {
    /**
     * SquareBoardView main constructor
     * @param board
     * @param screenWidth
     * @param screenHeight
     * @param tileHeight
     * @param horizontalTiles
     * @param verticalTiles
     */
    public SquareBoardView(AbstractBoard board, double screenWidth, double screenHeight, double tileHeight, int horizontalTiles, int verticalTiles) {
        super(board, Math.min(screenWidth,screenHeight), Math.min(screenWidth,screenHeight), tileHeight, horizontalTiles, verticalTiles);
    }
}
