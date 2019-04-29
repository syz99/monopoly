package backend.tile;

import backend.assetholder.AbstractPlayer;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class FreeParkingTile extends Tile {

    private int index;
    private String tileType;
    private double landedOnMoney;

    public FreeParkingTile(double landedOnMoney, String tileType, int index){
        //this.landedOnMoney = landedOnMoney;
        this.tileType = tileType;
        this.index = index;
    }

    //might need to calculate money?
    public FreeParkingTile(Element n){
        setTileIndex(Integer.parseInt(getTagValue("TileNumber", n)));
        setTileType(getTagValue("TileType", n));
    }

    public List<String> applyLandedOnAction(AbstractPlayer player) {
        List<String> possibleActions = new ArrayList<>();
//        possibleActions.add("collect"); ORIGINAL MONOPOLY U DON'T GET ANYTHING
        return possibleActions;
    }

}
