package backend.board;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.Bank;
import backend.deck.DeckInterface;
import exceptions.TileNotFoundException;
import exceptions.MultiplePathException;
import backend.tile.GoTile;
import backend.tile.JailTile;
import backend.tile.AbstractPropertyTile;
import backend.tile.Tile;
import configuration.XMLData;

import java.util.*;

/**
 * This class is an abstraction of the game board which contains
 * fundamental pieces to the game itself
 *
 * @author Matt
 * @author Sam
 *             updated Constructor for PlayerList
 */
public abstract class AbstractBoard {
    private Map<AbstractPlayer, Tile>               playerPositionMap;
    private Map<Tile, List<Tile>>                   adjacencyMap;
    private Map<String, List<AbstractPropertyTile>> propertyCategoryToSpecificListMap;
    private List<AbstractPlayer>                    myPlayerList;
    private int                                     numDie;
    private Bank                                    bank;
    private List<DeckInterface>                     myDecks;
    private int                                     myNumDice;

    /**
     * Constructor that takes in the list of players, tiles, and an adjacency list for the graph of tiles
     */
    public AbstractBoard(List<AbstractPlayer> playerList, Map<Tile, List<Tile>> adjMap, Map<String, List<AbstractPropertyTile>> colorListMap, Tile go, int nDie, Bank bnk) {
        myPlayerList = playerList;
        adjacencyMap = adjMap;
        propertyCategoryToSpecificListMap = colorListMap;
        numDie = nDie;
        bank = bnk;
        playerPositionMap = new HashMap<>();
        for (AbstractPlayer p : playerList) playerPositionMap.put(p, go);
    }

    public AbstractBoard(List<AbstractPlayer> playerList, XMLData data){
        myPlayerList = playerList;
        adjacencyMap = data.getAdjacencyList();
        propertyCategoryToSpecificListMap = data.getPropertyCategoryMap();
        numDie = data.getNumDie();
        bank = data.getBank();
        playerPositionMap = new HashMap<>();
        for (AbstractPlayer p : playerList) playerPositionMap.put(p, data.getFirstTile());
        myDecks = data.getDecks();
        myNumDice = data.getNumDie();
    }

    /**
     * gets the tile that the player is currently on
     */
    public Tile getPlayerTile(AbstractPlayer p) {
        return playerPositionMap.get(p);
    }

    /**
     * Moves the player on the board by reassigning its tile mapping
     */

    public abstract void movePlayer(AbstractPlayer p, Tile tile);

    public abstract Tile findNearest(AbstractPlayer p, String tileType) throws TileNotFoundException;

    public abstract Tile movePlayerByOne(AbstractPlayer p) throws MultiplePathException;

    public Map<AbstractPlayer, Tile> getPlayerTileMap() {
        return playerPositionMap;
    }

    public List<Tile> getAdjacentTiles(Tile tile) {
        return adjacencyMap.get(tile);
    }

    public Boolean containsTileType(String tileType) {
        for(Tile t: adjacencyMap.keySet()) {
            if (t.getTileType().equalsIgnoreCase( tileType )){
                return true;
            }
        }
        return false;
    }

    public JailTile getJailTile() throws TileNotFoundException {
        String methodName = String.valueOf(new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getReturnType());

        //TODO: should not catch exceptions that were just thrown!!! fix
        for (Tile key : adjacencyMap.keySet()) {
            if (key.isJailTile()) {
                return (JailTile) key;
            }
        }
        throw new TileNotFoundException("Could not find Tile of type " + methodName);
    }

    public GoTile getGoTile(){
        for (Tile key: adjacencyMap.keySet()) {
            if(key.isGoTile()){
                return (GoTile) key;
            }
        }
        return null;
    }

    public List<AbstractPlayer> getMyPlayerList() { return myPlayerList; }

    public Map<String, List<AbstractPropertyTile>> getColorListMap() {
        return propertyCategoryToSpecificListMap;
    }

    public int getNumDie() { return numDie; }

    public Bank getBank(){
        return bank;
    }

    public Tile getTilesIndex(int i){
        for(Tile t: adjacencyMap.keySet()){
            if(t.getTileIndex() == i) return t;
        }
        return null; //change this !!!
    }

    public AbstractPlayer getPlayerFromName(String name) {
        for(AbstractPlayer p: myPlayerList){
            if (p.getMyPlayerName().equalsIgnoreCase( name )){
                return p;
            }
        }
        return null;
    }

    public List<String> getPlayerNamesAsStrings() {
        List<String> playerStrings = new ArrayList<>();
        for (AbstractPlayer p : myPlayerList) {
            playerStrings.add(p.getMyPlayerName());
        }
        return playerStrings;
    }

    public List<String> getPropertyTileNamesAsStrings(AbstractPlayer owner) {
        List<String> tileStrings = new ArrayList<>();
        for (AbstractPropertyTile t : owner.getProperties()) {
            tileStrings.add(t.getTitleDeed());
        }
        return tileStrings;
    }

    public List<String> getBuildingTileNamesAsStrings(AbstractPlayer owner) {
        List<String> tileStrings = new ArrayList<>();
        for (AbstractPropertyTile t : owner.getProperties()) {
            if (t.isBuildingTile()) {
                tileStrings.add(t.getTitleDeed());
            }
        }
        return tileStrings;
    }

    public Tile getPropertyTileFromName(String name) {
        for (Tile t : adjacencyMap.keySet()) {
            if (t.isPropertyTile()) {
                AbstractPropertyTile property = (AbstractPropertyTile)t;
                if (property.getTitleDeed().equalsIgnoreCase(name)) {
                    return property;
                }
            }
        }
        return null;
    }

    public int getBoardSize() {
        return adjacencyMap.keySet().size();
    }

    public List<AbstractPropertyTile> getSameSetProperties(AbstractPropertyTile property) {
        return getColorListMap().get( property.getCard().getCategory());
    }

    public List<DeckInterface> getMyDecks() {
        return myDecks;
    }

    public int getMyNumDice() {
        return myNumDice;
    }
}
