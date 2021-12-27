package simulation;

import javafx.util.Pair;

import java.util.function.Consumer;

public interface IPositionChangeObservable {
    void subscribeToPositionChange(Consumer<Pair<Vector2d, IMapObject>> subscription);
    void nextPosition(Vector2d oldPosition);
}
