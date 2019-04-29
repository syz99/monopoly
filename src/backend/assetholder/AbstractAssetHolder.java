package backend.assetholder;

import backend.tile.AbstractPropertyTile;
import exceptions.NotEnoughMoneyException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAssetHolder{
    private List<AbstractPropertyTile> properties  = new ArrayList<>();
    private Double money;

    public AbstractAssetHolder(Double money) {
        this.money = money;
    }

    public double getMoney() {
        return this.money;
    }

    public void setMoney(double newValue) {
        this.money = newValue;
    }

    public abstract void addProperty(AbstractPropertyTile property);

    public void addAllProperties(List<AbstractPropertyTile> propertyList){
        for(AbstractPropertyTile property: propertyList){
            addProperty( property );
        }
    }

    public List<AbstractPropertyTile> getProperties() {
        return properties;
    }

    public abstract void payFullAmountTo(AbstractAssetHolder receiver, Double debt) throws NotEnoughMoneyException;

    public abstract boolean checkIfOwnsAllOf(List<AbstractPropertyTile> properties);

    public List<AbstractPropertyTile> ownsSublistOfPropertiesIn(List<AbstractPropertyTile> properties){
        List<AbstractPropertyTile> propertiesOwnedBy = new ArrayList<>();
        for (AbstractPropertyTile tile : properties) {
            if (tile.getOwner().equals(this)) {
                //throw exceptions: YOU CANNOT UPGRADE WITHOUT A MONOPOLY ON COLOR
                propertiesOwnedBy.add( tile );
            }
        }
        return propertiesOwnedBy;
    }

    @Override
    public boolean equals (Object o) {
        if (o == this) { return true; }
        if (!(o instanceof AbstractAssetHolder)) {
            return false;
        }
        AbstractAssetHolder player2 = (AbstractAssetHolder) o;
        return ((player2.getProperties().equals(this.getProperties())) && (player2.getMoney() == this.getMoney()));
    }
}
