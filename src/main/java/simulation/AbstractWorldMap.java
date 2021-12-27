package simulation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractWorldMap implements IWorldMap {
    protected HashMap<Vector2d, List<IMapObject>> mapObjects;
    protected Vector2d mapSize;

    protected JungleCoords jungleCoords;

    abstract public boolean canMoveTo(Vector2d position);

    public void placeObject(IMapObject object, Vector2d position) {
        List<IMapObject> objectsAtPosition = new LinkedList<>();
        if (this.mapObjects.containsKey(position)) {
            objectsAtPosition = this.mapObjects.get(position);
        }

        objectsAtPosition.add(object);
        this.mapObjects.put(position, objectsAtPosition);
    }

    public final Vector2d getMapSize() {
        return this.mapSize;
    }

    public List<IMapObject> getObjectsAt(Vector2d position) {
        return this.mapObjects.get(position);
    }

    protected void generateInitialAnimals(int numberOfInitialAnimals, SimultationParamsState paramsState) {
        if (numberOfInitialAnimals > this.mapSize.x * this.mapSize.y) {
            throw new RuntimeException("There are too many animals for given map size");
        }
        HashMap<Vector2d, List<IMapObject>> objectsMap = new HashMap<>();

        Random randomX = new Random();
        Random randomY = new Random();

        while (objectsMap.size() < numberOfInitialAnimals) {
            Vector2d newPosition = new Vector2d(randomX.nextInt(this.mapSize.x), randomY.nextInt(this.mapSize.y));
            if (!objectsMap.containsKey(newPosition)) {
                List<IMapObject> list = new LinkedList<>();
                Animal a = new Animal(newPosition, paramsState.startEnergy());
                this.setAnimalListeners(a);
                list.add(a);
                objectsMap.put(newPosition, list);
            }
        }
        this.mapObjects = objectsMap;
    }

    @Override
    public HashMap<Vector2d, List<IMapObject>> getMapObjects() {
        return this.mapObjects;
    }

    public void setAnimalListeners(Animal animal) {
        animal.subscribeToPositionChange((pair) -> {
            Animal a = (Animal) pair.getValue();
            Vector2d oldPosition = pair.getKey();
            this.removeObjectFromList(a, oldPosition);
            this.placeObject(a, a.getPosition());
        });
    }

    @Override
    public HashMap<Vector2d, List<Animal>> getMapAnimals() {
        HashMap<Vector2d, List<Animal>> animals = new HashMap<>();

        this.mapObjects.forEach((key, value) -> {
            List<Animal> list = new LinkedList<>();
            value.forEach(obj -> {
                if (obj instanceof Animal) {
                    list.add((Animal) obj);
                }
            });

            if (list.size() > 0) {
                animals.put(key, list);
            }
        });

        return animals;
    }

    @Override
    public HashMap<Vector2d, List<Grass>> getMapPlants() {
        HashMap<Vector2d, List<Grass>> grassMap = new HashMap<>();

        this.mapObjects.forEach((key, value) -> {
            List<Grass> list = new LinkedList<>();
            value.forEach(obj -> {
                if (obj instanceof Grass) {
                    list.add((Grass) obj);
                }
            });

            if (list.size() > 0) {
                grassMap.put(key, list);
            }
        });

        return grassMap;
    }

    @Override
    public void removeObjectFromList(IMapObject obj) {
        AtomicReference<Vector2d> position = new AtomicReference<>(null);

        this.mapObjects.forEach((key, value) -> {
            if (value.contains(obj)) {
                position.set(key);
            }
        });
        if (position.get() != null) {
            this.mapObjects.get(position.get()).remove(obj);
        }
    }

    @Override
    public void removeObjectFromList(IMapObject obj, Vector2d position) {
        if (!this.mapObjects.containsKey(position)) {
            return;
        }
        this.mapObjects.get(position).remove(obj);
    }

    @Override
    public void clearEmptyLists() {
        HashMap<Vector2d, List<IMapObject>> cleanObjectsMap = new HashMap<>();
        this.mapObjects.forEach((key, value) -> {
            if (value.isEmpty()) {
                return;
            }
            cleanObjectsMap.put(key, value);
        });
        this.mapObjects = cleanObjectsMap;
    }
}
