package frontend.screens;

import engine.MonopolyDriver;

import javafx.scene.Node;
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
 * Represents an abstraction of any screen menu within the game app
 * i.e. the medium on which View components will lie
 *
 * @author Sam
 */
abstract public class AbstractScreen{

    private double    screenWidth;
    private double    screenHeight;
    private Stage     myStage;
    private Scene     myScene;
    private String    myScreenTitle;
    private AbstractScreen myParent;

    /**
     * AbstractScreen main constructor
     * @param sWidth
     * @param sHeight
     * @param stage
     */
    public AbstractScreen(double sWidth, double sHeight, Stage stage, AbstractScreen parent) {
        screenWidth = sWidth;
        screenHeight = sHeight;
        myStage = stage;
        myParent = parent;
        // myScene = makeScreen();
    }

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

        BorderPane bPane = setBorderPane(
            getScreenWidth(),
            getScreenHeight(),
            gridPane
        );

        bPane.setCenter(titleText);

        gridPane.setAlignment(Pos.BOTTOM_CENTER);

        return new Scene(bPane, getScreenWidth(), getScreenHeight());
    }

    public BorderPane setBorderPane(double sWidth, double sHeight, GridPane gPane) {
        BorderPane bPane = new BorderPane();

        ImageView backgroundImg = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream("background.jpg")));
        backgroundImg.setFitWidth(sWidth);
        backgroundImg.setFitHeight(sHeight);

        bPane.getChildren().add(backgroundImg);
//        bPane.setCenter(getScreenText());
        bPane.setBottom(gPane);

        bPane.setMargin(gPane, new Insets(0,0, 75, 0));

        return bPane;
    }
    public double getScreenWidth()  { return screenWidth; }
    public double getScreenHeight() { return screenHeight; }
    public Stage  getMyStage()      { return myStage; }
    public Scene  getMyScene()      { return myScene; }

    protected void handleBackToMainButton(Stage stage) {
        stage.close();
        MonopolyDriver md = new MonopolyDriver();
        md.start(stage);
    }

    public void backToParent(){
        if(myParent!=null){
            myStage.close();
            myStage.setScene(myParent.makeScreen());
            myStage.show();
        }
    }

    abstract public void changeDisplayNode(Node n);

}
