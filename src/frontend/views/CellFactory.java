package frontend.views;

import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

/**
 * Factory to create images in the icons
 * dropdown in FormView
 *
 * @author Stephanie
 */
public class CellFactory extends ListCell<String> {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty)
            setGraphic(null);
        else {
            Image image = new Image(item + ".png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            setGraphic(new HBox(imageView));
        }
        setText("");
    }
}
