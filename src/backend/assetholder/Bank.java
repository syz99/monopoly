package backend.assetholder;

import backend.tile.AbstractPropertyTile;

import java.util.List;
import java.util.Map;

public class Bank extends AbstractAssetHolder {
    Map<String,Integer> totalPropertiesLeft;

    public Bank(Double money, Map<String,Integer> totalPropertiesLeft) {
        super(money);
        this.totalPropertiesLeft = totalPropertiesLeft;
    }

    @Override
    public void addProperty(AbstractPropertyTile property) {
        this.getProperties().add( property );
        recalculateTotalPropertiesLeftAfterWholeSale(property);
    }


    public void recalculateTotalPropertiesLeftAfterWholeSale(AbstractPropertyTile property) {
        //when would this ever happen? when not downgrading evenly
        property.recalculateTotalPropertiesLeftAfterWholeSale( totalPropertiesLeft );
    }

    public void recalculateTotalPropertiesLeftOneBuildingUpdate(AbstractPropertyTile property, boolean upgrade) {
        property.recalculateTotalPropertiesLeftOneBuildingUpdate(totalPropertiesLeft, upgrade );
    }

    public Boolean buildingsRemain(String building){
        return totalPropertiesLeft.get( building ) != 0;
        //assume building isn't faulty string (it exists in map)
    }

    //money is supposed to be unlimited in standard version
    @Override
    public void payFullAmountTo(AbstractAssetHolder receiver, Double debt) {
        receiver.setMoney( receiver.getMoney() + debt );
        //TODO: limited money?
        //this.setMoney(this.getMoney()-debt);
    }

    @Override
    public boolean checkIfOwnsAllOf(List<AbstractPropertyTile> properties) {
        return false;
        //return false because this function is used to check if player owns all of the same category for upgrade purposes
        //therefore should exclude bank
    }

    public int getNumberOfBuildingsLeft(String s) {
        return totalPropertiesLeft.get(s);
    }


}