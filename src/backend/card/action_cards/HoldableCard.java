package backend.card.action_cards;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public abstract class HoldableCard extends ActionCard {
    String name;

    public HoldableCard(String text, String name) {
        super( "Save", text );
        this.name = name;
    }

    public HoldableCard(Element n){
        super("Save", "");
        setText(getTagValue("Message", n));
        name = getTagValue("Type", n);
    }

    @Override
    public List<Object> getParameters() {
        List<Object> parameters =  new ArrayList<>(  );
        parameters.add( this );
        return parameters;
    }

    public String getName(){
        return name;
    }

    public String getActionType() {
        return "Save";
    }

    public abstract String getHoldableCardAction();
}
