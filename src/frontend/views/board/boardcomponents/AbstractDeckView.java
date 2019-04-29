package frontend.views.board.boardcomponents;

import javafx.scene.Node;

/**
 * This class represents the abstract View class
 * for all Deck views
 *
 * @author Edward
 */
public abstract class AbstractDeckView {
    private String myTileName;
    private String myDescription;

    public AbstractDeckView(String name, String description){
        myTileName = name;
        myDescription = description;
    }

    public void moveTo(Node n){
        return;
    }

    public void moveFrom(Node n){
        return;
    }

    abstract public Node getNodeOfTileView();
    abstract public void makeTileViewNode(double[] dimensions);
    //abstract public void placeTileView(int x, int y);

    public String getMyTileName(){ return myTileName; }

    public String getMyDescription(){ return myDescription; }

}
