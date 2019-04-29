package configuration;

import backend.assetholder.AbstractPlayer;
import backend.assetholder.HumanPlayer;
import backend.tile.AbstractPropertyTile;
import backend.tile.Tile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavedXMLData {

    List<AbstractPlayer> playerList;
    Map<AbstractPlayer, Tile> playerPositionMap;
    XMLData data;

    public SavedXMLData(String fileName, XMLData data){
        try {
            this.data = data;
            playerPositionMap = new HashMap<>();
            File xmlFile = new File(this.getClass().getClassLoader().getResource(fileName).toURI());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList players = doc.getElementsByTagName("Player");
            playerList = new ArrayList<>();
            for(int i = 0; i<players.getLength(); i++){
                playerList.add(getPlayer(players.item(i)));
            }
        }catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private AbstractPlayer getPlayer(Node node){
        AbstractPlayer player;
        Element element = (Element) node;
        player = new HumanPlayer(getTagValue("Name", element), getTagValue("Icon", element), Double.parseDouble(getTagValue("Money", element)));
        NodeList prop = ((Element) node).getElementsByTagName("Property");
        for(int i = 0; i < prop.getLength(); i++){
            player.addProperty(getProperty(prop.item(i)));
        }
        playerPositionMap.put(player, data.getTiles().get(Integer.parseInt(getTagValue("TileIndex", element))));
        return player;
    }

    private AbstractPropertyTile getProperty(Node node){
        Element element = (Element) node;
        int tileIndex = Integer.parseInt(getTagValue("OwnedIndex", element));
        return (AbstractPropertyTile) data.getTiles().get(tileIndex);
    }

    public List<AbstractPlayer> getPlayerList(){
        return playerList;
    }
    public Map<AbstractPlayer, Tile> getPlayerPositionMap() { return playerPositionMap; }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    // to show off during demo (comment out later)
    public static void main(String[] args){
        XMLData data = new XMLData("DukeMonopoly.xml");
        SavedXMLData dat = new SavedXMLData("saved_xml.xml", data);
        for(AbstractPlayer p: dat.getPlayerList()){
            System.out.println("Player name: " + p.getMyPlayerName());
            System.out.println("Player icon: " + p.getMyIconPath());
            System.out.println("Player funds "+ p.getMoney());
            System.out.println("Player properties: ");
            for(AbstractPropertyTile prop: p.getProperties()){
                System.out.println(prop.getTitleDeed());
            }
        }
        for(AbstractPlayer p: dat.getPlayerPositionMap().keySet()){
            System.out.println("Player " + p.getMyPlayerName() + " is on Tile Number " + dat.getPlayerPositionMap().get(p).getTileIndex());
        }
    }

}
