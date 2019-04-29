package backend.card.action_cards;

import backend.assetholder.AbstractAssetHolder;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class PayCard extends ActionCard {
    private List<AbstractAssetHolder> payers;
    private List<AbstractAssetHolder> payees;
    private String payerString;
    private String payeeString;
    private double amount;

    public PayCard(List<AbstractAssetHolder> payers, List<AbstractAssetHolder> payees, double amount, String type, String text) {
        super(type,text );
        this.payers =  payers;
        this.payees = payees;
        this.amount = amount;
    }

    public PayCard(Element n){
        super("", "");
        payerString = getTagValue("Payer", n);
        payeeString = getTagValue("Payee", n);
        amount = Double.parseDouble(getTagValue("Amount", n));
        setText(getTagValue("Message", n));
        setType(getTagValue("Type", n));
    }

    @Override
    public List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();
        parameters.add( payers );
        parameters.add( payees );
        parameters.add( amount );
        return parameters;
    }

    public String getPayerString(){
        return payerString;
    }

    public String getPayeeString(){
        return payeeString;
    }

    public void setPayers(List<AbstractAssetHolder> p){
        payers = p;
    }

    public void setPayees(List<AbstractAssetHolder> p){
        payees = p;
    }

}
