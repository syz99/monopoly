//package frontend.views.player_stats;
//
//import backend.assetholder.AbstractPlayer;
//import javafx.scene.Node;
//import javafx.scene.control.Tab;
//import javafx.scene.control.TabPane;
//
//import java.util.List;
//
//public abstract class AbstractPlayerTabView implements StatsView {
//    private TabPane tabPane;
//
//    public AbstractPlayerTabView(List<AbstractPlayer> playerList, TabPane tabPane){
//        this.tabPane = new TabPane();
//        for(AbstractPlayer p: playerList){
//            Tab tab = new Tab(p.getMyPlayerName());
//            tab.setId( p.getMyPlayerName() );
//            writeText( p, tab );
//            tabPane.getTabs().add( tab );
//        }
//        tabPane.setMaxHeight( 200 );
//        tabPane.setMaxWidth( 200 );
//        tabPane.setTabClosingPolicy( TabPane.TabClosingPolicy.UNAVAILABLE);
//    }
//
//    public AbstractPlayer getPlayerFromName(List<AbstractPlayer> playerList, String name){
//        for(AbstractPlayer p: playerList){
//            if (p.getMyPlayerName().equalsIgnoreCase( name )){
//                return p;
//            }
//        }
//        return null;
//    }
//
//
//    public abstract void update(List<AbstractPlayer> playerList, AbstractPlayer forfeiter);
//
//    public abstract void writeText(AbstractPlayer player, Tab tab);
//
//    public List<Tab> getTabs(){
//        return tabPane.getTabs();
//    }
//
//    public Node getNode(){
//        return tabPane;
//    }
//}