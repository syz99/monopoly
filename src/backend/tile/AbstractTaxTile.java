package backend.tile;

import backend.assetholder.Bank;
import org.w3c.dom.Element;

public abstract class AbstractTaxTile extends Tile {

    private double amountToDeduct;
    private Bank bank;
    private String tileType;
    private int index;


    public AbstractTaxTile(int money, Bank bank, String tileType, int index) {
        this.amountToDeduct = money;
        this.bank = bank;
        this.tileType = tileType;
        this.index = index;
    }

    public AbstractTaxTile(Bank bank, Element n){
        this.bank = bank;
        amountToDeduct = Integer.parseInt(getTagValue("TileRent", n));
        setTileType(getTagValue("TileType", n));
        setTileIndex(Integer.parseInt(getTagValue("TileNumber", n)));
        //this.bank = ;
    }

    public Bank getBank() {
        return bank;
    }

    public double getAmountToDeduct() {
        return amountToDeduct;
    }

}
