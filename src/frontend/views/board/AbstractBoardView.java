package frontend.views.board;

import backend.assetholder.AbstractPlayer;
import backend.tile.Tile;
import frontend.views.IconView;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

abstract public class AbstractBoardView {

    public AbstractBoardView(double screenWidth, double screenHeight){
        setRoot();
        setScreenLimits(screenWidth,screenHeight);
    }

    abstract public void move(AbstractPlayer currPlayer, int numMoves);
    abstract public void move(AbstractPlayer currPlayer, Tile tile);

    abstract public void setRoot();
    abstract public void setScreenLimits(double screenWidth,double screenHeight);
    abstract public void makeBoard();
    abstract public Node getBoardViewNode();

    public abstract void removePlayer(AbstractPlayer forfeiter,Tile tile);
}
