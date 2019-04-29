package frontend.views.player_stats;

import backend.assetholder.AbstractPlayer;
import backend.tile.AbstractPropertyTile;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

import java.util.List;

public class PlayerPropertiesView implements StatsView{
    private TabPane tabPane;

    public PlayerPropertiesView(List<AbstractPlayer> playerList){
        this.tabPane = new TabPane(  );
        for(AbstractPlayer p: playerList){
            Tab tab = new Tab(p.getMyPlayerName());
            tab.setId( p.getMyPlayerName() );
            writeText( p, tab );
            tabPane.getTabs().add( tab );
        }
        tabPane.setMaxHeight( 200 );
        tabPane.setMaxWidth( 200 );
        tabPane.setTabClosingPolicy( TabPane.TabClosingPolicy.UNAVAILABLE);
    }

    @Override
    public void update(List<AbstractPlayer> playerList, AbstractPlayer forfeiter) {
        if(forfeiter != null){
            for(Tab tab: tabPane.getTabs()){
                if(tab.getText().equalsIgnoreCase( forfeiter.getMyPlayerName() )){
                    tabPane.getTabs().remove(tab);
                    break;
                }
            }
        } else{
            for(Tab tab: tabPane.getTabs()){
                AbstractPlayer player = getPlayerFromName( playerList, tab.getText() );
                writeText(player, tab);
            }
        }

    }

    public AbstractPlayer getPlayerFromName(List<AbstractPlayer> playerList, String name){
        for(AbstractPlayer p: playerList){
            if (p.getMyPlayerName().equalsIgnoreCase( name )){
                return p;
            }
        }
        return null;
    }

    public Node getNode(){
        return tabPane;
    }

    public void writeText(AbstractPlayer player, Tab tab){
        TextArea properties = new TextArea();
        String text = "Properties\n";

        for(AbstractPropertyTile prop: player.getProperties()){
            if(prop.isMortgaged()){
                text += prop.getTitleDeed() + " (Mortgaged)\n";
            } else {
                text += prop.getTitleDeed() + "\n";
            }
        }
        properties.setText( text );
        tab.setContent( properties );
    }

}
