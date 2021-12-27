package simulation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AnimalFeeder {

    final int energyIncreaseOnEat;
    private IWorldMap map;



    AnimalFeeder(int energyIncreaseOnEat, IWorldMap map) {
        this.energyIncreaseOnEat = energyIncreaseOnEat;
        this.map = map;
    }



    // TODO Fix this to work with HashMap and update objectsMap in WorldMap

    public void feedAnimals() {
        HashMap<Vector2d, List<Grass>> grassMap = this.map.getMapPlants();

        HashMap<Vector2d, List<Animal>> animalsMap = this.map.getMapAnimals();

        for (Vector2d position : grassMap.keySet()) {
            if (!animalsMap.containsKey(position)) {
                continue;
            }
            List<Animal> currentPositionAnimals = animalsMap.get(position);

            if (currentPositionAnimals.size() == 1) {
                Animal a = currentPositionAnimals.get(0);
                a.eat(this.energyIncreaseOnEat);
            } else {
                List<Animal> eatingAnimals = new LinkedList<>();
                for (Animal a : currentPositionAnimals) {
                    if (eatingAnimals.size() == 0 || eatingAnimals.get(0).getEnergyValue() == a.getEnergyValue()) {
                        eatingAnimals.add(a);
                    }
                    if (eatingAnimals.get(0).getEnergyValue() < a.getEnergyValue()) {
                        eatingAnimals = new LinkedList<>();
                        eatingAnimals.add(a);
                    }
                }
                for (Animal a : eatingAnimals) {
                    a.eat(this.energyIncreaseOnEat / eatingAnimals.size());
                }
            }

            Grass grassOnPosition = grassMap.get(position).get(0);
            this.map.removeObjectFromList(grassOnPosition, position);
        }
    }
}
