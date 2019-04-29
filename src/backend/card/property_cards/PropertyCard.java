package backend.card.property_cards;

import backend.assetholder.AbstractPlayer;
import backend.card.AbstractCard;
import exceptions.UpgradeMaxedOutException;
import exceptions.UpgradeMinimumException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyCard extends AbstractCard {

    private double propertyMortgageValue;
    private Map<String, Double> rentPriceLookupTable;
    private Map<String, Integer> specificToNumeric;
    private List<String> upgradeOrder;
    private String titleDeed;
    private String category;
    private double tilePrice;

    public PropertyCard(Node node){
        Element element = (Element) node;
        tilePrice = Double.parseDouble(getTagValue("TilePrice", element));
        propertyMortgageValue = Double.parseDouble(getTagValue("TileMortgageValue", element));
        titleDeed = getTagValue("TitleDeed", element);
        category = getTagValue("TileColor", element);
        rentPriceLookupTable = new HashMap<>();
        upgradeOrder = new ArrayList<>();
        specificToNumeric = new HashMap<>();

        NodeList upgrades = element.getElementsByTagName("Upgrades");
        for(int i = 0; i<upgrades.getLength();i++){
            //get tag values from properties file//do we need to split the array
            String property = getTagValue("Upgrade", (Element) upgrades.item(i));
            String[] entry = property.split(",");
            String upgrade = entry[0];
            int numeric = Integer.parseInt(entry[1]);
            Double rentPrice = Double.parseDouble(entry[3]);
            upgradeOrder.add(upgrade);
            rentPriceLookupTable.put(upgrade, rentPrice);
            specificToNumeric.put(upgrade, numeric);
        }
    }

    public double getMortgageValue(){
        return propertyMortgageValue;
    }

    public double lookupPrice (String key){
        return rentPriceLookupTable.get(key);
    }

    public Map<String, Double> getRentPriceLookupTable(){
        return rentPriceLookupTable;
    }

    public String nextInUpgradeOrder(String current) throws UpgradeMaxedOutException {
        try {
            return upgradeOrder.get(getUpgradeOrderIndexOf(current)+1);
        }
        catch (IndexOutOfBoundsException i) {
             throw new UpgradeMaxedOutException( "You cannot upgrade anymore, this is the maximum upgrade potential." );
        }
    }

    public String previousInUpgradeOrder(String current) throws UpgradeMinimumException {
        try {
            return upgradeOrder.get(getUpgradeOrderIndexOf(current)-1);
        }
        catch (IndexOutOfBoundsException i) {
            throw new UpgradeMinimumException( "You cannot upgrade anymore, this is the minimum upgrade." );
        }
    }
    public String getUpgradeOrderAtIndex(int index){
        return upgradeOrder.get(index);
    }

    public int getUpgradeOrderIndexOf(String current) {
        return upgradeOrder.indexOf(current);
    }

    public Integer getNumericValueOfPropertyType(String specificPropertyType) {
        return specificToNumeric.get(specificPropertyType);
    }

    public String getSpecificFromNumeric (int numeric) {
        //THROW EXCEPTION FOR WHEN NUMERIC DOESN'T EXIST
        for(String key: specificToNumeric.keySet()){
            if(specificToNumeric.get( key ) == numeric){
                return key;
            }
        }
        return "";
    }

    public double getTilePrice(){
        return this.tilePrice;
    }

    public String getCategory(){
        return this.category;
    }

    public String getTitleDeed() {
        return titleDeed;
    }
}