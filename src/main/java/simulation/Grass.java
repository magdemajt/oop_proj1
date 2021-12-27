package simulation;

public class Grass implements IMapObject {
    private Vector2d position;
    private boolean isEaten;

    Grass(Vector2d position) {
        this.position = position;
        this.isEaten = false;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public void markGrassAsEaten() {

    }
}
