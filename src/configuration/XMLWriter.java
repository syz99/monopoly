package configuration;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import backend.board.AbstractBoard;
import backend.tile.AbstractPropertyTile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLWriter {

    public static void writeXML(String xmlFilePath, AbstractBoard board){
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("SavedGame");
            document.appendChild(root);

            for(int i = 0; i<board.getMyPlayerList().size(); i++){
                Element player = document.createElement("Player");
                root.appendChild(player);

                Element name = document.createElement("Name");
                name.appendChild(document.createTextNode(board.getMyPlayerList().get(i).getMyPlayerName()));
                player.appendChild(name);

                Element playerIcon = document.createElement("Icon");
                playerIcon.appendChild(document.createTextNode(board.getMyPlayerList().get(i).getMyIconPath()));
                player.appendChild(playerIcon);

//                Element bankruptcy = document.createElement("Bankruptcy");
//                bankruptcy.appendChild(document.createTextNode(board.getMyPlayerList().get(i).isBankrupt()));
//                player.appendChild(bankruptcy);


                Element tileIndex = document.createElement("TileIndex");
                tileIndex.appendChild(document.createTextNode("" + board.getPlayerTile(board.getMyPlayerList().get(i)).getTileIndex()));
                player.appendChild(tileIndex);


                Element funds = document.createElement("Money");
                funds.appendChild(document.createTextNode("" + board.getMyPlayerList().get(i).getMoney()));
                player.appendChild(funds);

                for(AbstractPropertyTile property: board.getMyPlayerList().get(i).getProperties()){
                    Element prop = document.createElement("Property");
                    player.appendChild(prop);

                    Element ownedIndex = document.createElement("OwnedIndex");
                    ownedIndex.appendChild(document.createTextNode("" + property.getTileIndex()));
                    prop.appendChild(ownedIndex);

                    Element upgradeStatus = document.createElement("UpgradeStatus");
                    upgradeStatus.appendChild(document.createTextNode(property.getCurrentInUpgradeOrder()));
                    prop.appendChild(upgradeStatus);

                    Element mortgageStatus = document.createElement("MortgageStatus");
                    mortgageStatus.appendChild(document.createTextNode("" + property.isMortgaged()));
                    prop.appendChild(upgradeStatus);
                }
            }
            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging
            transformer.transform(domSource, streamResult);

    } catch(ParserConfigurationException pce) {
        pce.printStackTrace();
    } catch (TransformerException tfe) {
        tfe.printStackTrace();
    }
}

}
