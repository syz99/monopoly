package backend.tile;

import backend.deck.DeckInterface;
import org.w3c.dom.Element;

public class CommunityChestTile extends AbstractDrawCardTile {

    public CommunityChestTile(DeckInterface deck, String tileType, int index) {
        super(deck, tileType, index);
    }

    public CommunityChestTile(Element n){
        super(n);
    }

}
