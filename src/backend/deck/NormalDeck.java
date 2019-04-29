package backend.deck;

import backend.card.action_cards.ActionCard;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class NormalDeck implements DeckInterface {
    private Queue<ActionCard> myDeck;
    private String name;

    public NormalDeck(Element n){
        myDeck = new LinkedList<>();
        name = getTagValue("Name", n);
    }

    public int getMyDeckSize() {
        //for Testing
        return myDeck.size();
    }

    @Override
    public ActionCard drawCard() {
        if(myDeck != null){
            return myDeck.remove();
        } else{
            return null;
            //EXCEPTION HANDLING
        }
        //draws "top" card
    }

    @Override
    public void putBack(ActionCard card) {
        myDeck.add(card);
        //adds card to "bottom"
    }

    @Override
    public void addCards(List<ActionCard> cardList) {
        for(ActionCard card: cardList){
            myDeck.add( card );
        }
    }

    @Override
    public String getName(){
        return name;
    }

    public String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
}
