package frontend.views;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;

public class TimerView {
    private final long myDefaultLength = 120000;
    private final long myRefreshTime = 1000;
    private Label myLabel;
    private Timer myTimer;
    private TimerTask myRefresh;
    private long myCurrentTime;

    public TimerView(){
        myLabel = new Label();
        myLabel.setText("Timer =");
    }

    public void setAndStartDefault(){
        setAndStart(myDefaultLength,myRefreshTime);
    }

    public void setAndStart(long timeInSeconds, long refreshInterval){
        if(timeInSeconds>=0) {
            myCurrentTime = timeInSeconds;
            myTimer = new Timer();
            myRefresh = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> setAndStart(myCurrentTime - refreshInterval, refreshInterval));
                }
            };
            myTimer.schedule(myRefresh, refreshInterval);
        }
    }

    public Node getNode(){
        return myLabel;
    }

}
