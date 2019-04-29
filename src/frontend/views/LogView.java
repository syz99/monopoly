package frontend.views;

import configuration.XMLData;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

/**
 * This class represents the View for all history
 * messages that are logged in the view area
 *
 * @author Luis
 * @author Sam
 *              [updated constructor & log to append not replace
 */
public class LogView {

    private XMLData myData;
    private TextArea myGameLog;
    private StringBuilder myLogContent;

    /**
     * LogView main constructor
     * @param data
     */
    public LogView(XMLData data) {
        myData = data;
        myLogContent = new StringBuilder("Welcome to " + myData.getMonopolyType() + ".");
        myGameLog = new TextArea(myLogContent.toString());
        myGameLog.setEditable(false);
        myGameLog.setStyle("-fx-max-width: 400; -fx-max-height: 100");
    }

    /**
     * Updates the LogView area with the given message
     * that is appended to the LogView area
     * @param message       the text to be logged
     */
    public void updateLogDisplay(final String message) {
        myLogContent.append("\n");
        myLogContent.append(message);
        myGameLog.setText(myLogContent.toString());
    }

    /**
     * Getter for the Node of a LogView
     * @return Node         TextArea
     */
    public Node getNode() {
        return myGameLog;
    }

}
