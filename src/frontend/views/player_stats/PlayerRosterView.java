package frontend.views.player_stats;

import backend.assetholder.AbstractPlayer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.Node;

import java.util.List;

/**
 * View area for all players and their icons
 * as well as way to indicate current player
 *
 * @author Sam
 */
public class PlayerRosterView {

    private TextFlow myRosterDisplay;

    /**
     * PlayerRosterView main constructor
     * @param playerList
     */
    public PlayerRosterView(List<AbstractPlayer> playerList) {
        update(playerList, null);
        myRosterDisplay.setMinHeight( 150 );
        myRosterDisplay.setMaxWidth( 200 );
        myRosterDisplay.setStyle( "-fx-background-color: white" );
    }

    /**
     * Method to update the view / current player
     * @param playerList
     */
    public void update(List<AbstractPlayer> playerList, AbstractPlayer forfeiter) {
        myRosterDisplay = new TextFlow();
        Text title = new Text("Joined Players: \n");
        myRosterDisplay.getChildren().add( title );
        setPlayerNameAndIcon(playerList, forfeiter);
    }

    private void setPlayerNameAndIcon(List<AbstractPlayer> playerList, AbstractPlayer forfeiter) {
        for (AbstractPlayer p : playerList) {
            if (p.equals( forfeiter )) {
                //TODO: SAM ADD THIS
            } else {
                Text playerName = new Text( p.getMyPlayerName() );
                ImageView icon = makeIcon( p.getMyIconPath() );
                playerName.setFont( Font.font( "Verdana", FontWeight.NORMAL, 16 ) );
//            if (p.equals(currPlayer)) playerName.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 14));
//            else                      playerName.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));

                myRosterDisplay.getChildren().addAll( new Text( "\n" ), icon, playerName );
            }
        }
    }

    private ImageView makeIcon(String iconPath) {
        Image image = new Image(iconPath + ".png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(30);
        imageView.setFitWidth (30);
        return imageView;
    }

    public Node getNode() {
        return myRosterDisplay;
    }
}