package backend.tile;

import backend.assetholder.AbstractPlayer;
import backend.card.action_cards.ActionCard;
import backend.deck.DeckInterface;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractDrawCardTile extends Tile {

    private DeckInterface myDeck;
    private String tileType;
    private String deckName;
    private int index;

    public AbstractDrawCardTile(DeckInterface deck, String tileType, int index) {
        myDeck = deck;
        this.tileType = tileType;
        this.index = index;
    }

    public AbstractDrawCardTile(Element n) {
        //myDeck = deck;
        deckName = getTagValue("TileName", n);
        setTileType(getTagValue("TileType", n));
        setTileIndex(Integer.parseInt(getTagValue("TileNumber", n)));
        //TODO: Finish this implementation
    }

    @Override
    public List<String> applyLandedOnAction(AbstractPlayer player) {
        List<String> possibleActions = new ArrayList<>();
        possibleActions.add("DrawCard");
        return possibleActions;
    }

    public ActionCard drawCard(){
        ActionCard card = myDeck.drawCard();
        myDeck.putBack(card);
        return card;
    }

    public void setDeck(DeckInterface deck){
        myDeck = deck;
    }



    public String getName(){
        return deckName;
    }
}