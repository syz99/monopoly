package backend.board;

/**
 * This class extends AbstractBoard and represents the traditional
 * format of a Monopoly board
 */

import backend.assetholder.AbstractPlayer;
import backend.assetholder.Bank;
import exceptions.MultiplePathException;
import backend.tile.AbstractPropertyTile;
import backend.tile.Tile;
import configuration.XMLData;
import exceptions.TileNotFoundException;

import java.util.*;

public class StandardBoard extends AbstractBoard {

    public StandardBoard(List<AbstractPlayer> playerList,
                         Map<Tile, List<Tile>> adjacencyMap,
                         Map<String, List<AbstractPropertyTile>> colorListMap,
                         Tile go,
                         int nDie,
                         Bank bank) {
        super(playerList, adjacencyMap, colorListMap, go, nDie, bank);
    }

    public StandardBoard(List<AbstractPlayer> playerList, XMLData data){
        super(playerList, data);
    }

    public Tile movePlayerByOne(AbstractPlayer p) throws MultiplePathException{
        Tile passedTile = null;
        Tile tile = getPlayerTile(p);
        Tile next;
        //this needs to change for a non-standard board, could be informed by property file
        if(getAdjacentTiles(tile).size() == 1) {
            next = getAdjacentTiles(tile).get(0);
            tile = next;
            if (tile.applyPassedAction(p).size() > 0) {
                passedTile = tile;
            }
        } else {
            throw new MultiplePathException( "There are multiple paths, please choose one" );
        }
        getPlayerTileMap().put(p, tile);
        return next;
    }

    @Override
    public void movePlayer(AbstractPlayer p, Tile tile) {
        getPlayerTileMap().put(p, tile);
    }

    @Override
    public Tile findNearest(AbstractPlayer p, String tileType) throws TileNotFoundException {
        if(containsTileType(tileType)){
            Tile current = getPlayerTile(p);
            Tile next = getAdjacentTiles(current).get(0);
            //this needs to change for a non-standard board, could be informed by property file
            while(!next.getTileType().equals( tileType)){
                next = getAdjacentTiles(next).get(0);
            }
            return next;
        } else {
            throw new TileNotFoundException( "Tile does not exist" );
        }
    }
}