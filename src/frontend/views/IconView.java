package frontend.views;

import frontend.views.board.boardcomponents.AbstractTileView;
import javafx.scene.image.ImageView;
import javafx.scene.Node;

/**
 * Represents the View of an icon piece (player gamepiece)
 *
 * @author Stephanie
 */
public class IconView {

    private ImageView myImageView;

    /**
     * IconView main constructor
     * @param icon
     */
    public IconView(ImageView icon){
        myImageView = icon;
    }

    /**
     * Gets the internal Node
     * @return Node     an ImageView
     */
    public Node getMyNode() { return myImageView; }

    public void setOn(AbstractTileView tileView){
        myImageView.setX(tileView.getMyX());
        myImageView.setY(tileView.getMyY());
    }


    /**
     * Setters for the height and width of the internal ImageView
     * @param v
     */
    public void setHeight(double v) { myImageView.setFitHeight(v); }
    public void setWidth (double v) { myImageView.setFitWidth(v); }
}
