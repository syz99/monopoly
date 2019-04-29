package backend.tile;

import backend.assetholder.AbstractPlayer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public abstract class Tile {

    private String tileType;
    private int index;

    public abstract List<String> applyLandedOnAction(AbstractPlayer p);

    public String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    public int getTileIndex(){
        return index;
    }

    public String getTileType(){
        return tileType;
    }

    public void setTileIndex(int i){
        index = i;
    }


    public void setTileType(String s){ tileType = s; }

    public String getName(){
        return "temp";
        //ONLY FOR TESTING PURPOSES
    }

    public boolean individualUpdateEvenCheck(boolean upgrade, int thresholdForUpdate, BuildingTile tile){
        return true;
    };

    public boolean isJailTile(){
        return false;
    }

    public boolean isGoToJailTile(){
        return false;
    }

    public boolean isGoTile(){
        return false;
    }

    public boolean isPropertyTile() {
        return false;
    }

    public boolean isBuildingTile() {
        return false;
    }

    public List<String> applyPassedAction(AbstractPlayer player){
        //return empty list
        return new ArrayList<String>();
    }
}
