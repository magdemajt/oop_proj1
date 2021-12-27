package simulation;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MagicAnimalBreeder extends AnimalBreeder {
    private Runnable onMagicBreed;
    public MagicAnimalBreeder(IWorldMap map, Runnable onMagicBreed) {
        super(map);
        this.onMagicBreed = onMagicBreed;
    }


    public static final int MAGIC_BREED_AMOUNT = 3;
    private int magicBreedCount = 0;

    @Override
    List<Animal> breedAnimals() {
        List<Animal> oldAnimals = new LinkedList<>();
        List<Animal> newAnimals = super.breedAnimals();
        AtomicInteger numberOfAnimals = new AtomicInteger(0);
        this.map.getMapAnimals().forEach((vec, animals) -> {
            numberOfAnimals.addAndGet(animals.size());
            oldAnimals.addAll(animals);
        });
        if (magicBreedCount < MAGIC_BREED_AMOUNT && numberOfAnimals.get() == 5) {
           oldAnimals.forEach(a -> {
               Animal newAnimal = new Animal(this.map.getRandomFreePosition(), a.initialEnergy, a.genome);
               newAnimals.add(newAnimal);
           });
           this.magicBreedCount += 1;
           this.onMagicBreed.run();
        }
        return newAnimals;
    }
}
