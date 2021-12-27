package simulation;

import java.util.ArrayList;
import java.util.List;

public class AnimalMover {
    private int moveEnergy;

    private IWorldMap map;

    public AnimalMover(int moveEnergy, IWorldMap map) {
        this.moveEnergy = moveEnergy;
        this.map = map;
    }

    private List<Animal> getAnimals() {
        List<Animal> animals = new ArrayList<>();
        this.map.getMapAnimals().values().forEach(animals::addAll);
        return animals;
    }

    void moveAnimals() {

        for (Animal a : this.getAnimals()) {
             int movement = a.getRandomGene();

             switch (movement) {
                 case 0 -> {
                     if (this.map.canMoveTo(a.getPosition().add(a.getForwardVector()))) {
                         a.moveForward();
                     }
                 }
                 case 4 -> {
                     if (this.map.canMoveTo(a.getPosition().add(a.getForwardVector().getBackwardFacing()))) {
                         a.moveBackward();
                     }
                 }
                 default -> a.turn(MapDirection.fromInt(movement));
             }
            a.removeEnergy(this.moveEnergy);
        }
        this.map.clearEmptyLists();
    }
}
