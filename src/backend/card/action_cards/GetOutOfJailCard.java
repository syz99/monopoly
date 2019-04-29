package backend.card.action_cards;

import backend.assetholder.AbstractPlayer;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class GetOutOfJailCard extends HoldableCard {

    public GetOutOfJailCard(String text) {
        super( "Save", text );

    }

    public GetOutOfJailCard(Element n){
        super("", "Get Out Of Jail");
        setType(getTagValue("Type", n));
        setText( getTagValue( "Message",n ) );
    }

    @Override
    public String getHoldableCardAction() {
        return "GetOutOfJail";
    }
}