package simulation;

public class WallMap extends AbstractWorldMap {

    public WallMap(int initialAnimals, SimultationParamsState params) {
        this.mapSize = new Vector2d(params.mapWidth(), params.mapHeight());
        this.generateInitialAnimals(initialAnimals, params);

    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !(position.x > mapSize.x || position.x < 0 || position.y > mapSize.y || position.y < 0);
    }
}
