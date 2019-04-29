package frontend.views.board.boardcomponents;

import javafx.scene.text.TextAlignment;
import javafx.scene.shape.StrokeType;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Node;

import javafx.geometry.Pos;

/**
 * This class extends AbstractTileView and represents the
 * View component of a PropertyTile of an AbstractBoardView
 *
 * @author Edward
 */
public class PropertyTileView extends AbstractTileView {

    private StackPane myRoot;
    private Paint     myColor;
    private double    myWidth;
    private double    myHeight;

    /**
     * PropertyTileView main constructor
     * @param name
     * @param description
     * @param paint
     */
    public PropertyTileView(String name, String description, Paint paint) {
        super(name, description);
        myColor = paint;
        myRoot = new StackPane();
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

    /**
     * Returns the StackPane of the PropertyTileView
     * @return Node         a StackPane
     */
    @Override
    public Node getNodeOfTileView() {
        return myRoot;
    }

    /**
     * Creates a TileView by adding all the elements of
     * the TileView to the StackPane root including the
     * border of the tile, the label & text of tile
     * @param dimensions
     */
    @Override
    public void makeTileViewNode(double[] dimensions) {
        myWidth = dimensions[0];
        myHeight = dimensions[1];
        myRoot.setMaxSize(myWidth, myHeight);
        myRoot.getChildren().add(makeBorder());
        myRoot.getChildren().add(makeLabel());
        myRoot.getChildren().add(makeText());
    }

    private Node makeBorder() {
        Rectangle border = new Rectangle(myWidth/2, myHeight/2, myWidth, myHeight );
        border.setFill(Color.rgb(197,225,164));
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(2);
        border.setStrokeType(StrokeType.INSIDE);
        myRoot.setAlignment(border,Pos.CENTER);
        return border;
    }

    private Node makeText() {
        Text tileText = new Text(getMyTileName());
        tileText.setTextAlignment(TextAlignment.CENTER);
        tileText.setWrappingWidth(myWidth);
        tileText.setFont(Font.font("Verdana",myWidth/5));
        myRoot.setAlignment(tileText,Pos.BOTTOM_CENTER);
        return tileText;
    }

    private Node makeLabel() {
        Rectangle labelShape = new Rectangle(myWidth, myHeight / 3);
        labelShape.setFill(myColor);
        labelShape.setStrokeWidth(1);
        labelShape.setStroke(Color.BLACK);
        labelShape.setStrokeType(StrokeType.INSIDE);
        myRoot.setAlignment(labelShape,Pos.TOP_CENTER);
        return labelShape;
    }
}


