package frontend.views.player_options;

import controller.Turn;

import javafx.scene.media.MediaPlayer;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import java.io.File;

import javafx.animation.RotateTransition;

/**
 * This class represents the View of a Dice or Die in the game
 *
 * @author Sam
 */
public class DiceView extends HBox {

    private final MediaPlayer         diceRollSound = new MediaPlayer(
                                                      new Media(new File("./data/diceRoll.mp3")
                                                                                    .toURI().toString()));
    private List<RotateTransition>    myRTList;
    private List<ImageView>           myDiceIcons;
    private String                    myPopupText;
    private Integer[]                 myRolls;
    private int                       myNumDieStates;
    private int                       myNumDie;

    /**
     * DiceView main constructor
     * @param nDie
     * @param nDieStates
     */
    public DiceView(int nDie, int nDieStates) {
        this.setSpacing(20);
        myNumDieStates = nDieStates;
        myNumDie = nDie;
        myDiceIcons = makeDiceIcons();
        myRTList = makeRTList();

        this.getChildren().addAll(myDiceIcons);
    }

    /**
     * Plays all rotate transition dice animations along with
     * the sound of rolling dice
     * @param diceIcons
     */
    public void playDiceAnimation(List<ImageView> diceIcons) {
        playDiceSound();
        for (RotateTransition rt : myRTList)
            rt.play();
    }

    /**
     * Displays roll results on the screen
     * @param turn
     */
    public void displayRollsPopup(final Turn turn) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("DICE ROLL");
        myPopupText = "Player " + turn.getMyCurrPlayer().getMyPlayerName() + " rolled a " + turn.getNumMoves();
        alert.setContentText(myPopupText);
        alert.showAndWait();
    }

    /**
     * Specifies what actions occur on update (i.e. after a roll)
     * @param turn
     */
    public void onUpdate(final Turn turn) {
        myRolls = turn.getRolls();
        playDiceAnimation(myDiceIcons);
        displayRollsPopup(turn);
    }

    private void playDiceSound() {
        diceRollSound.play();
    }

    private List<ImageView> makeDiceIcons() {
        List<ImageView> list = new ArrayList<>();

        for (int i = 0; i < myNumDie; i++) { // specifies how many dice icons are made
            ImageView icon = new ImageView(
                    new Image(this.getClass().getClassLoader().getResourceAsStream(
                            "dice" + (new Random().nextInt(myNumDieStates) + 1) + ".png"
                    ))
            );
            icon.setFitWidth(55);
            icon.setFitHeight(55);

            list.add(icon);
        }
        return list;
    }

    private List<RotateTransition> makeRTList() {
        List<RotateTransition> list = new ArrayList<>();

        for (int i = 0; i < myDiceIcons.size(); i++) {
            int currIndex = i;
            RotateTransition rt = new RotateTransition(Duration.seconds(1.5), myDiceIcons.get(i));
            rt.setFromAngle(0);
            rt.setToAngle(720);
            rt.setOnFinished(e -> setDice(myDiceIcons.get(currIndex), myRolls[currIndex]));

            list.add(rt);
        }
        return list;
    }

    /**
     * Getter for the text to be displayed post-roll
     * @return String       text for the roll
     */
    public String getMyPopupText() {
        return myPopupText;
    }

    private void setDice(ImageView diceIcon, final int roll) {
        diceIcon.setImage(new Image(
                this.getClass().getClassLoader().getResourceAsStream(
                        "dice" + roll + ".png"
                ))
        );
    }
}
