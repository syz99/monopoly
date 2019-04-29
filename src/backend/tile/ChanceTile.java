package backend.tile;

import backend.deck.DeckInterface;
import org.w3c.dom.Element;

public class ChanceTile extends AbstractDrawCardTile {

    public ChanceTile(DeckInterface deck, String tileType, int index) {
        super(deck, tileType, index);
    }

    public ChanceTile(Element n){
        super(n);
    }

}
