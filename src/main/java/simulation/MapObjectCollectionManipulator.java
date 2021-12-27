package simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapObjectCollectionManipulator<T extends IMapObject> {
    List<T> mapObjects;

    MapObjectCollectionManipulator(List<T> mapObjects) {
        this.mapObjects = mapObjects;
    }

    public HashMap<Vector2d, List<T>> groupMapObjectsByPosition() {
        HashMap<Vector2d, List<T>> animalsMap = new HashMap<>();
        for (T mapObject : this.mapObjects) {
            List<T> animalList = animalsMap.get(mapObject.getPosition());
            if (animalList == null) {
                animalList = new ArrayList<>();
            }
            animalList.add(mapObject);
            animalsMap.put(mapObject.getPosition(), animalList);
        }
        return animalsMap;
    }

}
