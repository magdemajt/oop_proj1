package simulation;

public class RoundedMap extends AbstractWorldMap {
    public RoundedMap(int initialAnimals, SimultationParamsState params) {
        super(initialAnimals, params);
    }
    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }

    @Override
    public void placeObject(IMapObject object, Vector2d position) {
        Vector2d positionWithinMap = new Vector2d((position.x + this.mapSize.x) % this.mapSize.x, (position.y + this.mapSize.y) % this.mapSize.y);
        super.placeObject(object, positionWithinMap);
    }
}
