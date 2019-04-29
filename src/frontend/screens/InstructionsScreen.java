package frontend.screens;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Extends AbstractScreen. Represents the instructions screen rendered for any user help
 *
 * @author Sam
 */
public class InstructionsScreen extends AbstractScreen {
    private static final String SCREEN_TITLE = "Monopoly";
    private Scene myScene;
    private BorderPane myPane;
    private Label myInstructions = new Label("https://en.wikipedia.org/wiki/Monopoly_(game)");
    
    public InstructionsScreen(double sWidth, double sHeight, Stage stage, AbstractScreen parent) {
        super (sWidth, sHeight, stage,parent);
        myScene=makeScreen();
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
        makeInstructions();
        return new Scene(myPane,getScreenWidth(),getScreenHeight());
    }

    public BorderPane setBorderPane(double sWidth, double sHeight, GridPane gPane) {
        BorderPane bPane = new BorderPane();

        ImageView backgroundImg = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream("background.jpg")));
        backgroundImg.setFitWidth(sWidth);
        backgroundImg.setFitHeight(sHeight);
        bPane.getChildren().add(backgroundImg);
        bPane.setBottom(gPane);
        bPane.setCenter(myInstructions);

        bPane.setMargin(gPane, new Insets(0,0, 75, 0));

        return bPane;
    }

    private void makeInstructions() {
    }

    @Override
    public void changeDisplayNode(Node n) {

    }

    @Override
    public Scene getMyScene(){
        return myScene;
    }

}
