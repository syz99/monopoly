package frontend.views;

import controller.GameSetUpController;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public class GameConfigView {
    private HBox myButtonBox;
    private GameSetUpController myController;

    public GameConfigView(GameSetUpController controller){
        myController = controller;
        makeButtons();
    }

    private void makeButtons() {
        myButtonBox = new HBox();
        Button saveButton = new Button("Save");
        Button loadButton = new Button("Load");
        Button newGameButton = new Button("New Game");
        saveButton.setOnAction(actionEvent -> myController.handleSave());
        loadButton.setOnAction(actionEvent -> myController.handleLoad());
        newGameButton.setOnAction(actionEvent -> myController.handleNewGame());
        myButtonBox.getChildren().addAll(saveButton,loadButton,newGameButton);
    }

    public Node getNode(){
        return myButtonBox;
    }

    public File generateLoadDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Config File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        return fileChooser.showOpenDialog(null);
    }

    public File getSavePath(String message, String defaultRelativePath) {
        DirectoryChooser saveChooser = new DirectoryChooser();
        saveChooser.setTitle(message);
        saveChooser.setInitialDirectory(new File(System.getProperty("user.dir")+defaultRelativePath));
        return saveChooser.showDialog(null);
    }
}
