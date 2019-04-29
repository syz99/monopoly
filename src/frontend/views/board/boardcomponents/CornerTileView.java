package frontend.views.board.boardcomponents;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.StrokeType;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.Node;

import javafx.geometry.Pos;

/**
 * This class extends AbstractTileView and represents the
 * View component of a Tile on the corner of an AbstractBoardView
 *
 * @author Edward
 */
public class CornerTileView extends AbstractTileView {

    private StackPane myRoot;
    private double    mySideLength;

    /**
     * CornerTileView main constructor
     * @param name
     * @param description
     * @param color
     */
    public CornerTileView(String name, String description, String color) {
        super(name, description);
        myRoot = new StackPane();
    }

    @Override
    public void makeTileViewNode(double[] dimensions) {
        mySideLength = dimensions[0];
        myRoot.getChildren().add(makeBorder());
        myRoot.getChildren().add(makeText());
    }

    /**
     * Gets the Node of the CornerTileView which is
     * a StackPane
     * @return Node     a StackPane
     */
    @Override
    public Node getNodeOfTileView() {
        return myRoot;
    }

    /**
     * Returns the X coordinate of the StackPane
     * @return double
     */
    @Override
    public double getMyX() {
        return myRoot.getLayoutX();
    }

    /**
     * Returns the Y coordinate of the StackPane
     * @return double
     */
    @Override
    public double getMyY() {
        return myRoot.getLayoutY();
    }

    /**
     * Adds a given node to myRoot
     * @param n     the node to be added
     */
    public void moveTo(Node n){
        myRoot.setAlignment(n,Pos.CENTER);
        myRoot.getChildren().add(n);
    }

    /**
     * Removes a given node from myRoot
     * @param n     the node to be removed
     */
    public void moveFrom(Node n){
        myRoot.getChildren().remove(n);
    }

    private Node makeBorder() {
        Rectangle border = new Rectangle(mySideLength/2, mySideLength/2, mySideLength, mySideLength );
        border.setFill(Color.rgb(197,225,164));
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(2);
        border.setStrokeType(StrokeType.INSIDE);
        myRoot.setAlignment(border, Pos.CENTER);
        return border;
    }

    private Node makeText() {
        Text tileText = new Text(getMyTileName());
        myRoot.setAlignment(tileText,Pos.CENTER);
        return tileText;
    }
}
