package backend.tile;

import backend.assetholder.AbstractPlayer;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JailTile extends Tile {

    private Set<AbstractPlayer> criminals;
    private int index;
    private double bailAmount = 50;

    public JailTile(Element n){
        criminals = new HashSet<>();
        setTileType(getTagValue("TileType", n));
        setTileIndex(Integer.parseInt(getTagValue("TileNumber", n)));
    }

    @Override
    public List<String> applyLandedOnAction(AbstractPlayer player) {
        List<String> possibleActions = new ArrayList<>(  );
        if (player.isInJail()) {
            possibleActions.add("PayBail");
            possibleActions.add("StayInJail");
        }
        return possibleActions;
    }

    @Override
    public boolean isJailTile(){
        return true;
    }

    public double getBailAmount(){
        return bailAmount;
    }

    @Override
    public String getName(){
        return "Jail Tile";
    }
}
