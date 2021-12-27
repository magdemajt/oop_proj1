package simulation;

import java.util.*;

public class AnimalBreeder {

    IWorldMap map;

    public AnimalBreeder(IWorldMap map) {
        this.map = map;
    }


    List<Animal> breedAnimals() {
        HashMap<Vector2d, List<Animal>> animals = this.map.getMapAnimals();

        animals.forEach((key, value) -> {
            List<Animal> animalsAvailableToBreed = value.stream().filter(a -> 2 * a.getEnergyValue() >= a.initialEnergy).toList();
            animals.put(key, animalsAvailableToBreed);
        });

        List<Animal> newAnimals = new LinkedList<>();

        animals.values().forEach(animalList -> {
            List<Animal> sortedAnimalList = new LinkedList<>(animalList);
            sortedAnimalList.sort(Comparator.comparingInt(Animal::getEnergyValue).reversed());
            if (sortedAnimalList.size() >= 2) {
                Animal father = sortedAnimalList.get(0);
                Animal mother = sortedAnimalList.get(1);
                int motherGenomeParticipation = (mother.getEnergyValue() * 100) / (father.getEnergyValue() + mother.getEnergyValue());
                Genome newGenome = father.genome.mixGenomes(mother.genome, motherGenomeParticipation);
                Animal child = new Animal(father.getPosition(), (mother.getEnergyValue() + father.getEnergyValue()) / 4, newGenome);

                mother.removeEnergy(mother.getEnergyValue() / 4);
                father.removeEnergy(father.getEnergyValue() / 4);
                mother.addChild(child);
                father.addChild(child);
                newAnimals.add(child);
            }
        });

        return newAnimals;
    }

}
