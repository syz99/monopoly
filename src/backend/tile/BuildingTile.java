package backend.tile;

import backend.assetholder.AbstractAssetHolder;
import backend.assetholder.AbstractPlayer;
import backend.assetholder.Bank;
import backend.card.property_cards.BuildingCard;
import backend.card.property_cards.PropertyCard;
import exceptions.*;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Map;

public class BuildingTile extends backend.tile.AbstractPropertyTile {
    private String tilecolor;
    private BuildingCard card;


    public BuildingTile(Bank bank, BuildingCard card, String tiletype, String tilecolor, int index) {
        super(bank, card, tiletype, index);
        this.card = (BuildingCard) this.getCard();
        this.tilecolor = tilecolor;
    }

    public BuildingTile(Bank bank, Element n){
        super(bank, n);
        setCard(new BuildingCard(n.getElementsByTagName("Card").item(0)));
        card = (BuildingCard)this.getCard();
    }

    //store these as strings and make a hashmap of price lookup
    public double calculateRentPrice(int roll) {
        if (isMortgaged()) {
            return 0.0;
        }
        else {
            //might need to debug, why must card be cast to property to use this method
            return card.lookupPrice(getCurrentInUpgradeOrder());
        }
    }


    /**
     * Houses and Hotels may be sold back to the Bank at any time for one-half the price paid for them.
     * All houses on one colour-group may be sold at once,
     * or they may be sold one house at a time (one hotel equals five houses),
     * evenly, in reverse of the manner in which they were erected.
     */
    public void sellAllBuildingsOnTile(List<AbstractPropertyTile> sameSetProperties) throws IllegalActionOnImprovedPropertyException {
        if (checkIfImprovedProperty()) {
            for (AbstractPropertyTile p : sameSetProperties) {
                BuildingTile b = (BuildingTile) p;
                getBank().recalculateTotalPropertiesLeftAfterWholeSale(b);
                getBank().payFullAmountTo(b.getOwner(), b.sellBuildingToBankPriceWholeSale());
                //TODO: replace magic value
                b.setCurrentInUpgradeOrder(b.getCard().getUpgradeOrderAtIndex(1));
            }
        }
        else {
            throw new IllegalActionOnImprovedPropertyException("This property does not have any structures on it!");
        }
    }

    /**
     * or they may be sold one house at a time (one hotel equals five houses),
     * evenly, in reverse of the manner in which they were erected.
     */
    public void sellOneBuilding(List<AbstractPropertyTile> sameSetProperties) throws IllegalActionOnImprovedPropertyException, UpgradeMinimumException {
        if(checkIfImprovedProperty() && checkIfUpdatingEvenly(sameSetProperties,false)) {
            getBank().recalculateTotalPropertiesLeftOneBuildingUpdate(this, false);
            getBank().payFullAmountTo( getOwner(), sellBuildingToBankPrice() );
            setCurrentInUpgradeOrder(card.previousInUpgradeOrder(getCurrentInUpgradeOrder()));
        }
        else {
            if (!checkIfImprovedProperty()) {
                throw new IllegalActionOnImprovedPropertyException("This property does not have any structures on it!");
            }
            else {
                throw new IllegalActionOnImprovedPropertyException("You can not downgrade unevenly!");
            }
        }
    }

    @Override
    public void sellTo(AbstractAssetHolder assetHolder, double price, List<AbstractPropertyTile> sameColorProperties) throws IllegalActionOnImprovedPropertyException, IllegalInputTypeException, OutOfBuildingStructureException, NotEnoughMoneyException, UpgradeMaxedOutException {
        if (!checkIfImprovedProperty()) {
            super.sellTo(assetHolder,price, sameColorProperties);
            if (assetHolder.checkIfOwnsAllOf(sameColorProperties) && card.getUpgradeOrderIndexOf(getCurrentInUpgradeOrder()) == 0){
                //assume upgrade order is as so: no house not all of same color properties, no house all of same color properties, etc.
                for (AbstractPropertyTile t : sameColorProperties) {
                    ((BuildingTile)t).upgrade((AbstractPlayer) assetHolder, sameColorProperties);
                }
            }
        }
        else {
            throw new IllegalActionOnImprovedPropertyException("You cannot sell properties with structures on them!");
        }
    }

    //CHECK IF WE NEED UPGRADE FOR RAILROAD AND UTILITY
    public void upgrade(AbstractPlayer player, List<AbstractPropertyTile> sameCategoryProperties) throws IllegalInputTypeException, OutOfBuildingStructureException, NotEnoughMoneyException, UpgradeMaxedOutException {
        BuildingCard card = (BuildingCard) this.getCard();
        String building = card.getBasePropertyType(card.nextInUpgradeOrder(getCurrentInUpgradeOrder()));
        if (player.checkIfOwnsAllOf(sameCategoryProperties) && checkIfUpdatingEvenly(sameCategoryProperties, true) && getBank().buildingsRemain( building )) {
            //throw exceptions if not caught in nextInUpgradeOrder
            double payment = card.getPriceNeededToUpgradeLookupTable(getCurrentInUpgradeOrder());
            player.payFullAmountTo(getBank(), payment);
            setCurrentInUpgradeOrder(card.nextInUpgradeOrder(getCurrentInUpgradeOrder()));
            getBank().recalculateTotalPropertiesLeftOneBuildingUpdate(this,true);
        }
        else {
            if (!player.checkIfOwnsAllOf(sameCategoryProperties)) {
                throw new IllegalInputTypeException("You cannot upgrade this property without owning all properties of the same group");
            }
            else if (!checkIfUpdatingEvenly(sameCategoryProperties, true)) {
                throw new IllegalInputTypeException("You cannot upgrade this property unevenly");
            }
            else if (!getBank().buildingsRemain( building )) {
                throw new OutOfBuildingStructureException("The bank is out of the building structure that you need to upgrade!");
            }
        }
    }

    //UPGRADE OR DOWNGRADE
    private boolean checkIfUpdatingEvenly(List<AbstractPropertyTile> properties, boolean upgrade) {
        int thresholdForUpdate = card.getUpgradeOrderIndexOf(this.getCurrentInUpgradeOrder());
        for (AbstractPropertyTile tile : properties) {
                if (individualUpdateEvenCheck( upgrade, thresholdForUpdate, (BuildingTile) tile )) return false;
        }
        return true;
    }

    @Override
    public boolean individualUpdateEvenCheck(boolean upgrade, int thresholdForUpdate, BuildingTile tile) {
        BuildingTile currentTile = tile;
        BuildingCard currentcard = (BuildingCard) currentTile.getCard();
        if (upgrade) {
            if (currentcard.getUpgradeOrderIndexOf(currentTile.getCurrentInUpgradeOrder()) < thresholdForUpdate) {
                //throw exceptions: YOU CANNOT UPGRADE UNEVENLY
                return true;
            }
        }
        else {
            if (currentcard.getUpgradeOrderIndexOf(currentTile.getCurrentInUpgradeOrder()) > thresholdForUpdate) {
                //throw exceptions: YOU CANNOT DOWNGRADE UNEVENLY
                return true;
            }
        }
        return false;
    }

    public double sellBuildingToBankPrice() {
        return card.getOneBuildingSellToBankPrice(getCurrentInUpgradeOrder());
    }

    public double sellBuildingToBankPriceWholeSale() {
        return (card.getNumericValueOfPropertyType(getCurrentInUpgradeOrder()) * card.getOneBuildingSellToBankPrice(getCurrentInUpgradeOrder()));
    }

    // Before an improved property can be mortgaged, all the Houses and Hotels on all the properties of its color-group must be sold back to the Bank at half price.
/** need controller logic: throw exceptions for **/
//    public void mortgageImprovedProperty(AbstractPlayer player, AbstractBoard board) {
//        List<BuildingTile> properties = board.getColorListMap().get(this.getTilecolor());
//        for (BuildingTile building : properties) {
//            getBank().payFullAmountTo(player,building.sellBuildingToBankPrice());
//            getBank().addHouses(this.getNumberOfHouses());
//            getBank().addHotels(this.getNumberOfHotels());
//        }
//        super.mortgageProperty();
//    }

    public boolean checkIfImprovedProperty() {
        //TODO: read from xml
        return (card.getUpgradeOrderIndexOf(getCurrentInUpgradeOrder()) > 1);
    }

    @Override
    public void mortgageProperty() throws MortgagePropertyException, IllegalActionOnImprovedPropertyException {
        if (!isMortgaged() && !checkIfImprovedProperty()) {
            getBank().payFullAmountTo(getOwner(), card.getMortgageValue() );
            setMortgaged(true);
        }
        else {
            if (isMortgaged()) {
                throw new MortgagePropertyException("Property is already mortgaged!");
            }
            else {
                throw new IllegalActionOnImprovedPropertyException("You can not mortgage properties that have structures on them!");
            }
        }
    }

    //private static String getTagValue(String tag, Element element) {
    //    NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
    //    Node node = nodeList.item(0);
    //    return node.getNodeValue();
    //}

    public String getTilecolor() {
        return tilecolor;
    }

    @Override
    public void recalculateTotalPropertiesLeftOneBuildingUpdate(Map<String,Integer> totalPropertiesLeft, boolean upgrade) {
        String current = this.getCurrentInUpgradeOrder();
        String baseKey = card.getBasePropertyType(current);
        Integer baseValue = card.getNumericValueOfPropertyType(current);
        int currentIndex = card.getUpgradeOrderIndexOf(current);
        if(currentIndex > 0){
            String previous = card.getUpgradeOrderAtIndex(currentIndex - 1);
            String previousBaseKey = card.getBasePropertyType(previous);
            Integer previousBaseValue = card.getNumericValueOfPropertyType(previous);
            if (upgrade) {
                totalPropertiesLeft.put(previousBaseKey, totalPropertiesLeft.get(previousBaseKey) + previousBaseValue);
            }
            else {
                totalPropertiesLeft.put(previousBaseKey, totalPropertiesLeft.get(baseKey) + baseValue);
                totalPropertiesLeft.put(baseKey, totalPropertiesLeft.get(previousBaseKey) - previousBaseValue);
            }
        }
        if (upgrade) { totalPropertiesLeft.put(baseKey, totalPropertiesLeft.get(baseKey) - baseValue); }
    }

    @Override
    public void recalculateTotalPropertiesLeftAfterWholeSale(Map<String,Integer> totalPropertiesLeft){
        String current = this.getCurrentInUpgradeOrder();
        String baseKey = card.getBasePropertyType(current);
        Integer baseValue = card.getNumericValueOfPropertyType(current);
        totalPropertiesLeft.put(baseKey, totalPropertiesLeft.get(baseKey) + baseValue);
    }

    @Override
    public boolean isBuildingTile() {
        return true;
    }
}