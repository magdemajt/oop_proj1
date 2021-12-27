package simulation;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class PlantGrower {

    private JungleCoords coords;
    private IWorldMap map;

    public PlantGrower(JungleCoords coords, IWorldMap map) {
        this.coords = coords;
        this.map = map;
    }


    public void initializeJungle() {

        int minWidth = this.coords.leftCorner().x;
        int maxWidth = this.coords.rightCorner().x;
        int minHeight = this.coords.leftCorner().y;
        int maxHeight = this.coords.rightCorner().y;

        for (int x = minWidth ; x < maxWidth; x++) {
            for (int y = minHeight; y < maxHeight; y++) {
                Vector2d position = new Vector2d(x, y);
                this.map.placeObject(new Grass(position), position);
            }
        }
    }

    public void growJungle() {
        HashMap<Vector2d, List<Grass>> grassMap = this.map.getMapPlants();
        HashMap<Vector2d, List<Animal>> animalsMap = this.map.getMapAnimals();

        int minWidth = this.coords.leftCorner().x;
        int maxWidth = this.coords.rightCorner().x;
        int minHeight = this.coords.leftCorner().y;
        int maxHeight = this.coords.rightCorner().y;

        Set<Vector2d> occupiedPositions = animalsMap.keySet().stream()
                .filter(pos -> this.coords.isVectorWithin(pos))
                .collect(Collectors.toSet());
        occupiedPositions.addAll(grassMap.keySet());

        if (occupiedPositions.size() >= this.coords.getSize()) {
            return;
        }

        Random random = new Random();

        for (int x = minWidth ; x < maxWidth; x++) {
            for (int y = minHeight; y < maxHeight; y++) {
                Vector2d position = new Vector2d(x, y);
                if (occupiedPositions.contains(position)) {
                    continue;
                }

                if (random.nextInt(100) > 50) {
                    this.map.placeObject(new Grass(position), position);
                    return;
                }
            }
        }
    }
}
