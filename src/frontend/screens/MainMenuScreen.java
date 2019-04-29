package frontend.screens;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javafx.geometry.Pos;


/**
 * Extends AbstractScreen. Represents the main menu which is first loaded on game start
 *
 * @author Sam
 */
public class MainMenuScreen extends AbstractScreen {
    private static final String SCREEN_TITLE = "Monopoly";
    private Scene myScene;
    private BorderPane myPane;

    public MainMenuScreen(double sWidth, double sHeight, Stage stage, AbstractScreen parent) {
        super(sWidth, sHeight, stage, parent);
        myScene = makeScreen();
    }

    @Override
    public Scene makeScreen() {
//        Text titleText = new Text("MONOPOLY");
//        titleText.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 25));
//        titleText.setFill(Color.DARKGREEN);
        Image logo = new Image("monopolylogo.png");
        ImageView iv1 = new ImageView();
        // resizes the image to have width of 100 while preserving the ratio and using
        // higher quality filtering method; this ImageView is also cached to
        // improve performance
        ImageView iv2 = new ImageView();
        iv2.setImage(logo);
        iv2.setFitWidth(700);
        iv2.setPreserveRatio(true);
        iv2.setSmooth(true);
        iv2.setCache(true);
//        Text titleText = new Text("MONOPOLY");
//        titleText.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 25));
//        titleText.setFill(Color.DARKGREEN);

        Button playButton = new Button("PLAY: Normal mode");
        playButton.setOnAction(f -> handleNormalModeButton(getMyStage()));

        Button randomModeButton = new Button("TESTING");
//        // TODO:
//        randomModeButton.setOnAction(r -> handleCustomModeButton(getMyStage()));

        Button instructButton = new Button("INSTRUCTIONS");
        instructButton.setOnAction(i -> handleInstructButton(getMyStage()));

        Button boardButton = new Button("PLAY: NORMAL MODE");
        boardButton.setOnAction(i -> handleBoardButton(getMyStage()));

//        ComboBox

//        Button rulesButton = new Button("rules");
//        rulesButton.setOnAction(i -> handleRulesButton(getMyStage()));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(15);

//        gridPane.add(playButton, 1, 0);
//        gridPane.add(randomModeButton, 2, 0);
//        gridPane.add(instructButton, 3, 0);
        gridPane.add(boardButton,1,0);
        gridPane.add(instructButton, 2, 0);


        myPane = setBorderPane(
                getScreenWidth(),
                getScreenHeight(),
                gridPane
        );
        myPane.setCenter(iv2);

        gridPane.setAlignment(Pos.BOTTOM_CENTER);

        return new Scene(myPane, getScreenWidth(), getScreenHeight());
    }

    @Override
    public void changeDisplayNode(Node n) {
        myPane.setCenter(n);
    }

    private void handleBoardButton(Stage myStage) {
        AbstractScreen BoardModeScreen = new BoardModeScreen(
                getScreenWidth(),
                getScreenHeight(),
                myStage,this
        );
        completeStage(myStage, BoardModeScreen);
    }

    private void handleNormalModeButton(Stage stage) {
        AbstractScreen normalModeScreen = new NormalModeScreen(
            getScreenWidth(),
            getScreenHeight(),
            stage,this
        );
        completeStage(stage, normalModeScreen);
    }

//    private void handleRulesButton(Stage stage) {
//        AbstractScreen rulesScreen = null;
//        try {
//            rulesScreen = new RulesScreen(
//                    getScreenWidth(),
//                    getScreenHeight(),
//                    stage,this
//            );
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        }
//        completeStage(stage, rulesScreen);
//    }

//    private void handleCustomModeButton(Stage stage) {
////        AbstractScreen customModeScreen = new CustomModeScreen(
////            getScreenWidth(),
////            getScreenHeight(),
////            stage
////        );
//        TestingScreen test = new TestingScreen(
//            getScreenWidth(),
//            getScreenHeight(),
//            stage,this
//        );
//
//        completeStage(stage, test);
//    }

    private void handleInstructButton(Stage stage) {
        AbstractScreen instructionsScreen = new InstructionsScreen(
            getScreenWidth(),
            getScreenHeight(),
            stage,this
        );
        completeStage(stage, instructionsScreen);
    }

    private void completeStage(Stage stage, AbstractScreen menu) {
        stage.close();
        stage.setScene((menu.getMyScene()));
        stage.show();
    }

    /**
     * Getter of the Scene
     * @return
     */
    public Scene getMyIntroScene() { return myScene; }
}
