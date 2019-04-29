package backend.assetholder;

public class HumanPlayer extends AbstractPlayer {
    public HumanPlayer(String name, String iconPath, Double money) {
        super( name, iconPath, money);
    }

    public boolean isAuto(){
        return false;
    }
}
