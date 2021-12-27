package simulation;

import java.util.HashMap;
import java.util.List;

public interface IWorldMap {
    Vector2d getMapSize();
    List<IMapObject> getObjectsAt(Vector2d position);
    boolean canMoveTo(Vector2d position);
    void placeObject(IMapObject object, Vector2d position);
    HashMap<Vector2d, List<IMapObject>> getMapObjects();
    HashMap<Vector2d, List<Animal>> getMapAnimals();
    HashMap<Vector2d, List<Grass>> getMapPlants();
    void removeObjectFromList(IMapObject obj);
    void removeObjectFromList(IMapObject obj, Vector2d position);
    void clearEmptyLists();
    void setAnimalListeners(Animal a);
    Vector2d getRandomFreePosition();
}
