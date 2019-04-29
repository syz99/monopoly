package backend;

import backend.tile.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a node used in the graph
 * to represent the network of tiles on the game board
 *
 * @author Sam
 * @author Edward
 */
public class BoardTileNode {
    Tile tileInfo;
    List<BoardTileNode> nexts;

    public BoardTileNode(Tile tInfo, List<BoardTileNode> n) {
        tileInfo = tInfo;
        nexts = n;
    }

    public BoardTileNode(Tile tInfo) {
        this(tInfo, new ArrayList<BoardTileNode>());
    }

    public void addNext(BoardTileNode n) {
        nexts.add(n);
    }


}