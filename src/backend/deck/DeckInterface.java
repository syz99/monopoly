package backend.deck;

import backend.card.action_cards.ActionCard;

import java.util.List;

public interface DeckInterface {

    ActionCard drawCard();

    void putBack(ActionCard card);

    void addCards(List<ActionCard> cardList);

    String getName();

}
