package frontend.views.board.boardcomponents;

import javafx.scene.Node;

/**
 * This class represents the abstract View class
 * for all Tile views
 *
 * @author Edward
 */
public abstract class AbstractTileView {

    private String myDescription;
    private String myTileName;

    /**
     * AbstractTileView main constructor
     * @param name
     * @param description
     */
    public AbstractTileView(String name, String description){
        myTileName = name;
        myDescription = description;
    }

    /**
     * Returns the name of the tile view
     * @return String       name of the AbstractTileView
     */
    public String getMyTileName(){ return myTileName; }

    /**
     * Returns the description of the AbstractTileView
     * @return String       description of tile
     */
    public String getMyDescription() { return myDescription; }

    /**
     * Getters for the X and Y Layout coordinates of the root
     * @return double       X or Y coordinate
     */
    abstract public double getMyX();
    abstract public double getMyY();

    abstract public Node getNodeOfTileView();
    abstract public void makeTileViewNode(double[] dimensions);
    abstract public void moveTo(Node n);
    abstract public void moveFrom(Node n);
}
