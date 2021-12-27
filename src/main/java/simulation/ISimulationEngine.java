package simulation;

import java.util.function.Consumer;

public interface ISimulationEngine extends Runnable {
    IWorldMap getMap();
    void subscribeRerender(Runnable onTickEnd);
    void pause();
    void resume();
    boolean isRunning();
    void setRefreshSpeed(int refreshSpeedMiliseconds);

    double getDeadAnimalsAverageLifeSpan();
    double getAverageChildRate();
}
