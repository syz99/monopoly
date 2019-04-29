package backend.card.action_cards;

import backend.assetholder.AbstractAssetHolder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayBuildingsCard extends ActionCard {
    private List<AbstractAssetHolder> payers;
    private List<AbstractAssetHolder> payees;
    private String payerString;
    private String payeeString;
    private Map<String, Double> baseToMultiplier;

    public PayBuildingsCard(List<AbstractAssetHolder> payers, List<AbstractAssetHolder> payees, Map<String, Double> baseToMultiplier, String type, String text) {
        super(type, text);
        this.payers =  payers;
        this.payees = payees;
        this.baseToMultiplier = baseToMultiplier;
    }
    
    public PayBuildingsCard(Element n){
        super("", "");
        baseToMultiplier = new HashMap<>();
        constructBaseToMultiplier(n.getElementsByTagName("Multiplier"));
        payerString = getTagValue("Payer", n);
        payeeString = getTagValue("Payee", n);
        setType(getTagValue("Type", n));
        setText(getTagValue("Message", n));
    }

    @Override
    public List<Object> getParameters() {
        List<Object> parameters = new ArrayList<Object>();
        parameters.add( payers );
        parameters.add( payees );
        parameters.add( baseToMultiplier );
        return parameters;
    }

    public void constructBaseToMultiplier(NodeList nodeList){
        for(int i = 0; i < nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            baseToMultiplier.put(getTagValue("Property", element), Double.parseDouble(getTagValue("Amount", element)));
        }
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
