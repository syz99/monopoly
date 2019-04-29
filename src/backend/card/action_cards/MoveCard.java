package backend.card.action_cards;

import backend.tile.Tile;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class MoveCard extends ActionCard {
    private Tile tile;
    private int index;

    public MoveCard(Tile tile, String type, String text) {
        super(type, text);
        this.tile = tile;
        index = tile.getTileIndex();
    }

    public MoveCard(Element n){
        super("", "");
        setType(getTagValue("Type", n));
        setText(getTagValue("Message", n));
        index = Integer.parseInt(getTagValue("TileIndex", n));
    }

    public void setTile(Tile t){
        tile = t;
    }

    public int getIndex(){
        return index;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> parameters = new ArrayList<Object>();
        parameters.add( tile );
        parameters.add(index);
        return parameters;
    }
}
