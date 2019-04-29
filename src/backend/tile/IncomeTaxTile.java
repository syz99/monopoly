package backend.tile;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.Bank;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class IncomeTaxTile extends AbstractTaxTile {

    private double taxMultiplier;

    public IncomeTaxTile(int money, Bank bank, String tileType, int index) {
        super(money, bank, tileType, index);
    }

    public IncomeTaxTile(Bank bank, Element n){
        super(bank, n);
        this.taxMultiplier = Double.parseDouble(getTagValue("TaxMultiplier", n));
    }

    @Override
    public List<String> applyLandedOnAction(AbstractPlayer player) {
        //interaction with front-end: pay full or 10%?
        List<String> possibleActions = new ArrayList<>(  );
        possibleActions.add("PayTaxFixed");
        possibleActions.add("PayTaxPercentage");
        return possibleActions;
    }

    public double getTaxMultiplier() {
        return taxMultiplier;
    }
}
