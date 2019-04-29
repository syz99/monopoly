package frontend.views;

import backend.assetholder.AbstractPlayer;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import org.w3c.dom.Text;

import java.util.List;

public class FundsView {
    private TextArea myNode;

    public FundsView(){
        createPlayerFundsDisplay();
    }
    private void createPlayerFundsDisplay(){
        myNode = new TextArea( );
        updatePlayerFundsDisplay(null);
        myNode.setMaxHeight( 150 );
        myNode.setMaxWidth( 200 );
        myNode.setStyle( "-fx-background-color: white" );
    }

    public void updatePlayerFundsDisplay(List<AbstractPlayer> playerList){
        String text = "Player Funds \n";
        if(playerList!=null) {
            for (AbstractPlayer p : playerList) {
                text = text + p.getMyPlayerName() + ": " + p.getMoney() + "\n";
            }
        }
        myNode.setText( text );
    }

    public Node getNode() {
        return myNode;
    }
}
