package frontend.views.board;

import backend.assetholder.AbstractPlayer;
import backend.board.AbstractBoard;
import backend.card.property_cards.BuildingCard;
import backend.card.property_cards.PropertyCard;
import backend.tile.AbstractPropertyTile;
import backend.tile.BuildingTile;
import backend.tile.Tile;

import frontend.views.board.boardcomponents.*;
import frontend.views.IconView;

import javafx.application.Platform;
import javafx.geometry.Insets;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.layout.*;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the generalized View of a rectangular Monopoly Board
 * consisting of TileViews and Icons.
 *
 * @author Edward
 * @author Sam
 */
public class RectangularBoardView extends AbstractBoardView {

    private AnchorPane             myRoot;
    private double                 myTileHeight;
    private double                 myScreenWidth, myScreenHeight;
    private int                    myHorizontals, myVerticals;

    private Map<AbstractPlayer, IconView> myPlayerIconMap;
    private Map<IconView, Integer> iconToIndexMap;
    private List<AbstractPlayer>   myPlayerList;
    private List<AbstractTileView> myTiles;
    private AbstractBoard          myBoard;
    private int                    myNumMoves;
    private Map<Tile, AbstractTileView> tileToTileView = new HashMap<>(  );

    /**
     * RectangularBoardView main constructor
     * @param board
     * @param screenWidth
     * @param screenHeight
     * @param tileHeight
     * @param horizontalTiles
     * @param verticalTiles
     */
    public RectangularBoardView(AbstractBoard board, double screenWidth, double screenHeight, double tileHeight, int horizontalTiles, int verticalTiles){
        super(screenWidth,screenHeight);
        myBoard = board;
        myTiles = new ArrayList<>();
        myTileHeight = tileHeight;
        myHorizontals = horizontalTiles; //10
        myVerticals = verticalTiles; //10

        makeBoard();
        makeBackground();
        myPlayerList = myBoard.getMyPlayerList();
        myPlayerIconMap = makePlayerIconMap();
        iconToIndexMap = setIconToIndexMap();
    }

    private Map<AbstractPlayer, IconView> makePlayerIconMap() {
        Map<AbstractPlayer, IconView> map = new HashMap<>();
        for (AbstractPlayer p : myPlayerList) {
            map.put(p, makeIcon(p.getMyIconPath()));
        }
        return map;
    }

    private IconView makeIcon(String iconPath) {
        Image image = new Image(iconPath + ".png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(myTileHeight / 2);
        imageView.setFitWidth (myTileHeight / 2);

        return new IconView(imageView);
    }

    private Map<IconView, Integer> setIconToIndexMap() {
        Map<IconView, Integer> map = new HashMap<>();
        for (IconView i : myPlayerIconMap.values())
            map.put(i, 0);

        return map;
    }

    private void makeBackground() {
        BackgroundFill boardBackgroundFill = new BackgroundFill(Color.rgb(220,237,200), CornerRadii.EMPTY, Insets.EMPTY);
        Background boardBackGround = new Background(boardBackgroundFill);
        myRoot.setBackground(boardBackGround);

        /* keeps board background color in line with view itself */
        myRoot.maxWidthProperty().bind(myRoot.widthProperty());
        myRoot.maxHeightProperty().bind(myRoot.heightProperty());
    }

    private double calculateTileWidth(double sideLength, double totalTiles){
        return (sideLength-myTileHeight*2)/(totalTiles-2);
    }

    /**
     * Lays out the RectangularBoardView TileViews
     */
    public void makeBoard() {
        makeCorners();
        makeBottomRow();
        makeLeftRow();
        makeTopRow();
        makeRightRow();
        placeDeck("Community Chest","",100,50,0.6);
        placeDeck("Chance","",100,50,0.25);
    }

    /**
     * This method dictates the movement of a particular
     * player icon on the board for the given number of moves
     * @param currPlayer        the AbstractPlayer to move
     * @param nMoves            the number of moves the icon moves
     */
    public void move(AbstractPlayer currPlayer, int nMoves) {
        myNumMoves = nMoves; // update invariant myNumMoves
        Thread updateThread = new Thread(() -> {
            while(myNumMoves>0) {
                try {
                    Thread.sleep(300);
                    Platform.runLater(() -> this.movePiece(
                            myPlayerIconMap.get(currPlayer)
                    ));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        updateThread.setDaemon(true);
        updateThread.start();
    }

    private void movePiece(IconView icon) {
        if(myNumMoves>0) {
            moverHelper(icon);
            myNumMoves--;
        }
    }

    private void movePieceIncrementally(IconView icon) {
        moverHelper(icon);
    }

    private void moverHelper(IconView icon) {
        if (iconToIndexMap.get(icon) >= myTiles.size()) {
            iconToIndexMap.put(icon, 0);
        }
        myTiles.get(iconToIndexMap.get(icon)).moveTo(icon.getMyNode());
        iconToIndexMap.put(icon, iconToIndexMap.get(icon) + 1);
    }

    public void move(AbstractPlayer currPlayer, Tile tile){
//        Thread updateThread = new Thread(() -> {
//            try {
//                Thread.sleep(500);
//                Platform.runLater(() -> this.moveHelper(
//                       currPlayer,tile));
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//            }
//        });
//        updateThread.setDaemon(true);
//        updateThread.start();
        this.moveHelper(currPlayer,tile);
    }

    private void moveHelper(AbstractPlayer currPlayer, Tile tile){
        AbstractTileView target = tileToTileView.get( tile );
        IconView pIcon = myPlayerIconMap.get(currPlayer);
        target.moveTo(pIcon.getMyNode());
        //System.out.println(target.getMyTileName());
    }
    /**
     * Setter of the RectangularBoardView object's AnchorPane
     */
    @Override
    public void setRoot() {
        myRoot = new AnchorPane();
    }

    /**
     * Defines the given Screen dimensions
     * @param screenWidth
     * @param screenHeight
     */
    @Override
    public void setScreenLimits(double screenWidth, double screenHeight) {
        myScreenHeight = screenHeight;
        myScreenWidth = screenWidth;
    }

    /**
     * Returns the AnchorPane of the RectangularBoardView
     * @return Pane, the AnchorPane
     */
    @Override
    public Node getBoardViewNode() {
        return myRoot;
    }

    @Override
    public void removePlayer(AbstractPlayer forfeiter,Tile tile) {
        AbstractTileView removeFrom = tileToTileView.get(tile);
        removeFrom.moveFrom(myPlayerIconMap.get(forfeiter).getMyNode());
    }

    private void makeBottomRow() {
        for(int i = 1; i<myHorizontals-1;i++){
            Tile tile= myBoard.getTilesIndex(i);
            String s = tile.getTileType();
            //change TileType to make BuildingTile, RailroadTile, and UtilityTile all
            if(s.equalsIgnoreCase("BuildingTile")){
                PropertyCard card = ((AbstractPropertyTile) tile).getCard();
                try {
                    tileToTileView.put( tile, placePropertyTile(card.getTitleDeed(), (BuildingTile) tile, "", (Color)Color.class.getField(card.getCategory().toUpperCase()).get(null), i, 1, myHorizontals, myScreenWidth, 0));
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // change this !!!
                } catch (NoSuchFieldException e) {
                    e.printStackTrace(); // change this !!!
                }
            }
            //else if(s.equalsIgnoreCase("RailroadTile")||s.equalsIgnoreCase("UtilityTile")){
            //    placePropertyTile(tile.getTileType(), "", Color.GREEN, i, 1, myHorizontals, myScreenWidth,  0);
            //}
            else {
                tileToTileView.put(tile, placeNonPropertyTile(tile.getTileType(), "", i, 1, myHorizontals, myScreenWidth, 0));
                //this needs to be changed, might need to give names to non-property tiles
                //AbstractCard card = tile;
                //placeNonPropertyTile();
            }
        }
    }

    private void makeLeftRow(){
        int x = 0;
        for(int i = myHorizontals; i<myHorizontals+myVerticals-2;i++) {
            x++;
            Tile tile = myBoard.getTilesIndex(i);
            String s = tile.getTileType();
            //change TileType to make BuildingTile, RailroadTile, and UtilityTile all
            if (s.equalsIgnoreCase("BuildingTile")) {//||s.equalsIgnoreCase("RailroadTile")||s.equalsIgnoreCase("UtilityTile")){
                PropertyCard card = ((AbstractPropertyTile) tile).getCard();
                try {
                    tileToTileView.put(tile, placePropertyTile(card.getTitleDeed(), (BuildingTile) tile, "", (Color) Color.class.getField(card.getCategory().toUpperCase()).get(null), 9, x, myVerticals, myScreenWidth, 90));
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // change this !!!
                } catch (NoSuchFieldException e) {
                    e.printStackTrace(); // change this !!!
                }
            //} else if (s.equalsIgnoreCase("RailroadTile") || s.equalsIgnoreCase("UtilityTile")) {
            //    placePropertyTile(tile.getTileType(), "", Color.GREEN, 9, i-10, myHorizontals, myScreenWidth, 90);
            } else {
                tileToTileView.put(tile, placeNonPropertyTile(tile.getTileType(), "", 9, x, myVerticals, myScreenWidth, 90));
                //this needs to be changed, might need to give names to non-property tiles
                //AbstractCard card = tile;
                //placeNonPropertyTile();
            }
        }
    }

    private void makeTopRow() {
        int x = 0;
        for(int i = myHorizontals+myVerticals-1; i<2*myHorizontals+myVerticals-3;i++) {
            x++;
            Tile tile = myBoard.getTilesIndex(i);
            String s = tile.getTileType();
            //change TileType to make BuildingTile, RailroadTile, and UtilityTile all
            if (s.equalsIgnoreCase("BuildingTile")) {//||s.equalsIgnoreCase("RailroadTile")||s.equalsIgnoreCase("UtilityTile")){
                PropertyCard card = ((AbstractPropertyTile) tile).getCard();
                try {
                    tileToTileView.put(tile, placePropertyTile(card.getTitleDeed(), (BuildingTile) tile,"", (Color) Color.class.getField(card.getCategory().toUpperCase()).get(null), x, 1, myHorizontals, myScreenWidth, 180));
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // change this !!!
                } catch (NoSuchFieldException e) {
                    e.printStackTrace(); // change this !!!
                }
            }
//            else if (s.equalsIgnoreCase("RailroadTile") || s.equalsIgnoreCase("UtilityTile")) {
//                placePropertyTile(tile.getTileType(), "", Color.GREEN, x, 1, myHorizontals, myScreenWidth, 180);
//            }
            else {
                tileToTileView.put(tile, placeNonPropertyTile(tile.getTileType(), "", x, 1, myHorizontals, myScreenWidth, 180));
            }
        }
    }

    private void makeRightRow(){
        int x = 0;
        for(int i = 2*myHorizontals+myVerticals-2; i<2*myHorizontals+2*myVerticals-4;i++) {
            x++;
            Tile tile = myBoard.getTilesIndex(i);
            String s = tile.getTileType();
            //change TileType to make BuildingTile, RailroadTile, and UtilityTile all
            if (s.equalsIgnoreCase("BuildingTile")) {//||s.equalsIgnoreCase("RailroadTile")||s.equalsIgnoreCase("UtilityTile")){
                PropertyCard card = ((AbstractPropertyTile) tile).getCard();
                try {
                    tileToTileView.put( tile, placePropertyTile(card.getTitleDeed(), (BuildingTile) tile,"", (Color) Color.class.getField(card.getCategory().toUpperCase()).get(null), x, x, myHorizontals, myScreenWidth, 270));
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // change this !!!
                } catch (NoSuchFieldException e) {
                    e.printStackTrace(); // change this !!!
                }
                //} else if (s.equalsIgnoreCase("RailroadTile") || s.equalsIgnoreCase("UtilityTile")) {
                //    placePropertyTile(tile.getTileType(), "", Color.GREEN, 9, i-10, myHorizontals, myScreenWidth, 90);
            } else {
                tileToTileView.put(tile, placeNonPropertyTile(tile.getTileType(), "", x, x, myHorizontals, myScreenWidth, 270));
                //this needs to be changed, might need to give names to non-property tiles
                //AbstractCard card = tile;
                //placeNonPropertyTile();
            }
        }
    }


    public void placeDeck(String tileName,
                          String tileDescription,
                          double width, double length, double prop){
        var tile = new NormalDeckView(tileName,tileDescription);
        var height = length;
        tile.makeTileViewNode(new double[]{width,height}, parseTileNamePath(tileName));
        Node tileNode = tile.getNodeOfTileView();
        tileNode.setRotate(-45);
        myRoot.setTopAnchor(tileNode,myScreenHeight*prop);
        myRoot.setLeftAnchor(tileNode,myScreenWidth*prop);
        myRoot.getChildren().add(tileNode);
    }

    private String parseTileNamePath(String tileName) {
        String[] arr = tileName.split(" ");
        return arr[0].toLowerCase() + "Card.png";
    }

    public PropertyTileView placePropertyTile(String tileName, AbstractPropertyTile propTile,
                                  String tileDescription,
                                  Paint tileColor,
                                  int xoffset,
                                  int yoffset,
                                  int totalTiles,
                                  double sideLength,double rotationAngle){
        var tile = new PropertyTileView(tileName, tileDescription,tileColor);
        var height = myTileHeight;
        var width = calculateTileWidth(sideLength,totalTiles);
        tile.makeTileViewNode(new double[]{width,height});
        Node tileNode = tile.getNodeOfTileView();

        //System.out.print("Prop tile Set");
        tileNode.setOnMouseClicked(e -> showBuildingTileClickedAlert(propTile));
        tileNode.setRotate(rotationAngle);
        if(rotationAngle==0) {
            myRoot.setTopAnchor(tileNode, myScreenHeight - height * yoffset);
            myRoot.setLeftAnchor(tileNode, myScreenWidth - width * xoffset - myTileHeight);
        } else if (rotationAngle==90){
            myRoot.setTopAnchor(tileNode, myScreenHeight-myTileHeight*1.5-width*(yoffset-0.5));
            myRoot.setLeftAnchor(tileNode, (height-width)/2);
        } else if (rotationAngle==180){
            myRoot.setTopAnchor(tileNode,0.0);
            myRoot.setLeftAnchor(tileNode,myTileHeight+width*(xoffset-1));
        } else if (rotationAngle==270){
            myRoot.setTopAnchor(tileNode, myTileHeight+width*(yoffset-1)-(height-width)/2);
            myRoot.setLeftAnchor(tileNode,myScreenWidth-myTileHeight/2-width/2);
        }
        tileNode.setRotate(rotationAngle);
        myRoot.getChildren().add(tileNode);
        myTiles.add(tile);
        return tile;
    }

    public RectangularTileView placeNonPropertyTile(String tileName, //AbstractNonBuildingPropertyTile nonPropTile,
                                     String tileDescription,
                                     int xoffset,
                                     int yoffset,
                                     int totalTiles,
                                     double sideLength,double rotationAngle){
        var tile = new RectangularTileView(tileName, tileDescription);
        var height = myTileHeight;
        var width = calculateTileWidth(sideLength,totalTiles);
        tile.makeTileViewNode(new double[]{width,height});
        Node tileNode = tile.getNodeOfTileView();
//        System.out.print("Nonprop tile Set");

        // tileNode.setOnMouseClicked(e -> {showTileClickedAlert(details);});
        tileNode.setRotate(rotationAngle);

        if(rotationAngle==0) {
            myRoot.setTopAnchor(tileNode, myScreenHeight - height * yoffset);
            myRoot.setLeftAnchor(tileNode, myScreenWidth - width * xoffset - myTileHeight);
        } else if (rotationAngle==90){
            myRoot.setTopAnchor(tileNode, myScreenHeight-myTileHeight*1.5-width*(yoffset-0.5));
            myRoot.setLeftAnchor(tileNode, (height-width)/2);
        } else if (rotationAngle==180){
            myRoot.setTopAnchor(tileNode,0.0);
            myRoot.setLeftAnchor(tileNode,myTileHeight+width*(xoffset-1));
        } else if (rotationAngle==270){
            myRoot.setTopAnchor(tileNode, myTileHeight+width*(yoffset-1)-(height-width)/2);
            myRoot.setLeftAnchor(tileNode,myScreenWidth-myTileHeight/2-width/2);
        }
        tileNode.setRotate(rotationAngle);
        myRoot.getChildren().add(tileNode);
        myTiles.add(tile);
        return tile;
    }

    public CornerTileView placeCornerTile(String tileName,
                                String tileDescription,
                                String tileColor,
                                double xDiff,
                                double yDiff){
        var width = myTileHeight;
        var height = myTileHeight;
        var tile = new CornerTileView(tileName, tileDescription, tileColor);
        tile.makeTileViewNode(new double[]{width,height});
        Node tileNode = tile.getNodeOfTileView();
        myRoot.setTopAnchor(tileNode, (myScreenHeight-height)*yDiff);
        myRoot.setLeftAnchor(tileNode, (myScreenWidth-width)*xDiff);
//        ImportPropertyFile deets = new ImportPropertyFile(details);

        // tileNode.setOnMouseClicked(e -> {showTileClickedAlert(deets);});
        myRoot.getChildren().add(tileNode);
        return tile;
    }

    public void makeCorners(){
        //System.out.print(myPropertyFile.getProp("TileOName"));
        //System.out.print(myPropertyFile.getProp("TileOFile®"))
        Tile tileLowerRight = myBoard.getTilesIndex(0);
        Tile tileLowerLeft = myBoard.getTilesIndex(myHorizontals-1);
        Tile tileUpperLeft = myBoard.getTilesIndex(myHorizontals+myVerticals-2);
        Tile tileUpperRight = myBoard.getTilesIndex(myHorizontals*2+myVerticals-3   );

        tileToTileView.put(tileLowerRight, placeCornerTile(tileLowerRight.getTileType(), tileLowerRight.getTileType(), "clear", 1, 1));
        tileToTileView.put(tileLowerLeft, placeCornerTile(tileLowerLeft.getTileType(), tileLowerLeft.getTileType(), "clear", 0, 1));
        tileToTileView.put(tileUpperLeft, placeCornerTile(tileUpperLeft.getTileType(), tileUpperLeft.getTileType(), "clear", 0, 0));
        tileToTileView.put(tileUpperRight, placeCornerTile(tileUpperRight.getTileType(), tileUpperRight.getTileType(), "clear", 1, 0));
    }

/**
 * TO DISPLAY TILE POPUPS ON CLICK OF TILE
 *
 * FIND THIS LINE OF CODE IN PLACEPROP and PLACENONPROP methods
 */
    private void showBuildingTileClickedAlert(AbstractPropertyTile tile) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tile.getTitleDeed());
        alert.setHeaderText("Tile Info:");
        String text = "Tile Price: " + tile.getCard().getTilePrice() + "\n";
        for(int i = 0; i<tile.getCard().getRentPriceLookupTable().size(); i++){
            for(String s: tile.getCard().getRentPriceLookupTable().keySet()){
                if(tile.getCard().getUpgradeOrderIndexOf(s) == i){
                    text += "Tile Rent with " + s + " = " + tile.getCard().getRentPriceLookupTable().get(s) + "\n";
                }
            }
        }
        text+= "Mortgage Value = " + tile.getCard().getMortgageValue() + "\n";
        //this is hardcoded ...
        //text+="Upgrade Price = " + ((BuildingCard) tile.getCard()).getPriceNeededToUpgradeLookupTable("1House") + "\n";
        text += "\n current buildings: " + tile.getCurrentInUpgradeOrder();
        alert.setContentText(text);
        alert.showAndWait();
    }
}