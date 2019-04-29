package backend.assetholder;

import backend.card.action_cards.HoldableCard;
import backend.tile.AbstractPropertyTile;
import exceptions.NotEnoughMoneyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract class representing a Player in the game
 *
 * @author Sam [constructor change]
 */
abstract public class AbstractPlayer extends AbstractAssetHolder {

    private List<HoldableCard> cards = new ArrayList<>();
    private String             myPlayerName;
    private String             myIconPath;
    private Boolean            isBankrupt;
    private int                turnsInJail; //-1 not in jail, 0 just got to jail, 1 = 1 turn in jail
    private final static int FREE = -1;

    /**
     * AbstractPlayer main constructor
     * @param name
     * @param iconPath
     * @param money
     */
    public AbstractPlayer(String name, String iconPath, Double money) {
        super( money );
        myPlayerName = name;
        myIconPath = iconPath;
        isBankrupt = false;
        turnsInJail = FREE;
    }

    public void declareBankruptcy(Bank bank){
        this.isBankrupt = true;
        bank.addAllProperties( this.getProperties() );
        this.getProperties().clear();
        this.setMoney( 0 );
    }

    @Override
    public void payFullAmountTo (AbstractAssetHolder receiver, Double debt) throws NotEnoughMoneyException {
        if(this.getMoney() >= debt){
            this.setMoney( this.getMoney()-debt );
            receiver.setMoney( receiver.getMoney() + debt );
        } else {
            throw new NotEnoughMoneyException( "You don't have enough money" );
        }

    }

    public void addProperty(AbstractPropertyTile property){
        this.getProperties().add( property );
    }

//    public void sellPropertiesToBankUntilGoalReached(AbstractAssetHolder receiver, Double goal) {
//        //right now you pay back debt by selling properties until you can pay back, but made separate method cuz this can be changed
//        int i = 0;
//        while(goal > 0){
//            AbstractPropertyTile currentProperty = this.getProperties().get( i );
//            //assume selling in order of buying property, but can change this to own choice
//            goal -= currentProperty.sellBuildingToBankPrice();
//            bank.addProperty( currentProperty );
//            i++;
//        }
//    }

//    private Double getTotalAssetValue() {
//        Double totalAssetValue = 0.0;
//        for(AbstractPropertyTile each: this.getProperties()){
//            totalAssetValue += each.sellBuildingToBankPrice();
//            //assume can only sell to bank, no trading
//        }
//        return totalAssetValue;
//    }

    /**
     * Checks if the player owns all of the properties given
     * @param properties
     * @return boolean      whether player owns all Tiles
     */
    public boolean checkIfOwnsAllOf(List<AbstractPropertyTile> properties) {
        return ownsSublistOfPropertiesIn( properties ).size() == properties.size();
    }

    /**
     * Custom equals method for an AbstractPlayer
     * @param o
     * @return boolean      whether player equals another
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractPlayer)) return false;
        if (!super.equals(o)) return false;
        AbstractPlayer that = (AbstractPlayer) o;

        return getMyIconPath().equals(that.getMyIconPath()) &&
                getMyPlayerName().equals(that.getMyPlayerName());
    }

    /**
     * Custom hashCoding for an AbstractPlayer based on
     * its unique icon and name
     * @return int      hashcode of AbstractPlayer
     */
    @Override
    public int hashCode() {
        return Objects.hash(getMyIconPath(), getMyPlayerName());
    }

    /**
     * Getter for the AbstractPlayer object's name
     * @return String       player name
     */
    public String getMyPlayerName() { return myPlayerName; }

    /**
     * Getter for the AbstractPlayer object's icon image path
     * @return String       player's icon img path
     */
    public String getMyIconPath() { return myIconPath; }

    /**
     * Getter for the number of turns in jail
     * @return int      turns in jail
     */
    public int getTurnsInJail() { return turnsInJail; }

    /**
     * Called when going to jail to set value to 0
     */
    public void addTurnInJail() { turnsInJail += 1; }

    /**
     * Returns whether or not player is in jail
     * @return boolean      whether player in jail
     */
    public boolean isInJail(){
        return turnsInJail != FREE;
    }

    public boolean isAuto() {
        return (this.isAuto());
    }

    /**
     * Changes turnsInJail invariant for player not in jail
     */
    public void getOutOfJail(){
        turnsInJail = -1;
    }

    /**
     * Returns whether player is bankrupt or not
     * @return boolean      whether player bankrupt
     */
    public Boolean isBankrupt(){
        return isBankrupt;
    }

    /**
     * Adds card to player's list of cards
     * @param card
     */
    public void addCard(HoldableCard card){
        cards.add( card );
    }

    /**
     * Gets player's list of cards
     * @return
     */
    public List<HoldableCard> getCards(){
        return cards;
    }

}
