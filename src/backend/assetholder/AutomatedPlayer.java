package backend.assetholder;

public class AutomatedPlayer extends AbstractPlayer {
    public AutomatedPlayer(String name, String iconPath, Double money) {
        super(name, iconPath, money);
    }

    public boolean isAuto() { return true; }
}
