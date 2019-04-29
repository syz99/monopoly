package frontend.views.player_stats;

import backend.assetholder.AbstractPlayer;

import java.util.List;

public interface StatsView {

    void update(List<AbstractPlayer> playerList, AbstractPlayer forfeiter);
}
