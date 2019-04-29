package frontend.screens;

import controller.GameSetUpController;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.FontPosture;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import javafx.geometry.HPos;
import javafx.geometry.Pos;

/**
 * For BOARD testing purposes
 *
 * @author Luis
 */
public class BoardModeScreen extends AbstractScreen{

    private GameSetUpController myController;
    private Scene myScene;
    private BorderPane myPane;

    private static String CONFIG_FILE;

    /**
     * Default constructor
     * @param sWidth
     * @param sHeight
     * @param stage
     */
    public BoardModeScreen(double sWidth, double sHeight, Stage stage, AbstractScreen parent) {
        super(sWidth, sHeight, stage, parent);
        myController = new GameSetUpController(sWidth, sHeight, this);
        myScene = makeScreen();
        myScene.setOnKeyPressed(keyEvent -> handleKeyInput(keyEvent.getCode()));
    }

    @Override
    public Scene makeScreen() {
        Text titleText = new Text("**MENU TITLE FROM PROPERTIES**");
        titleText.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 25));
        titleText.setFill(Color.DARKGREEN);

        Button backToMainButton = new Button("Back to Main Menu");
        backToMainButton.setOnAction(f -> handleBackToMainButton(getMyStage()));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(15);

        gridPane.addRow(0, backToMainButton);
        gridPane.setHalignment(backToMainButton, HPos.CENTER);

        myPane = setBorderPane(
                getScreenWidth(),
                getScreenHeight(),
                gridPane
        );

        //bPane.setCenter(titleText);

        gridPane.setAlignment(Pos.BOTTOM_CENTER);
        return new Scene(myPane, getScreenWidth(), getScreenHeight());
    }

    @Override
    public BorderPane setBorderPane(double sWidth, double sHeight, GridPane gPane) {
        BorderPane bPane = new BorderPane();

        ImageView backgroundImg = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(myController.getBackround())));
        backgroundImg.setFitWidth(sWidth);
        backgroundImg.setFitHeight(sHeight);
        bPane.getChildren().add(backgroundImg);
        bPane.setCenter(myController.getNode());
        bPane.setBottom(gPane);

        bPane.setMargin(gPane, new Insets(0,0, 75, 0));

        return bPane;
    }

    @Override
    public Scene  getMyScene(){ return myScene; }

    @Override
    public void changeDisplayNode(Node n) {
        myPane.setCenter(n);
    }

    private void handleKeyInput(KeyCode code) {
        if (code == KeyCode.Q) {
            backToParent();
            //handleBackToMainButton(getMyStage());
        }
    }
}
