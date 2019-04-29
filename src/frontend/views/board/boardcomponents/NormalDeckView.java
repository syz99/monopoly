package frontend.views.board.boardcomponents;

import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.StrokeType;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Node;

import javafx.geometry.Pos;

/**
 * This class extends AbstractTileView and represents the
 * View of the card deck of an AbstractBoardView
 *
 * @author Edward
 */
public class NormalDeckView extends AbstractTileView {

    private StackPane myRoot;
    private double    myWidth;
    private double    myHeight;

    /**
     * NormalDeckView main constructor
     * @param name
     * @param description
     */
    public NormalDeckView(String name, String description) {
        super(name, description);
        myRoot = new StackPane();
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

    @Override
    public Node getNodeOfTileView() {
        return myRoot;
    }

    @Override
    public void makeTileViewNode(double[] dimensions) {

    }

    public void makeTileViewNode(double[] dimensions, String imgPath) {
        myWidth = dimensions[0];
        myHeight = dimensions[1];
        myRoot.setMaxSize(myWidth, myHeight);
        myRoot.getChildren().add(makeText());
        myRoot.getChildren().add(makeBorder());
        ImageView img = new ImageView(imgPath);
        img.setFitHeight(90);
        img.setPreserveRatio(true);
        myRoot.getChildren().add(img);
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

    private Node makeBorder() {
        Rectangle border = new Rectangle(myWidth/2, myHeight/2, myWidth, myHeight );
        border.setFill(Color.TRANSPARENT);
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
        myRoot.setAlignment(tileText,Pos.CENTER);
        return tileText;
    }
}

