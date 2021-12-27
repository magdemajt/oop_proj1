package simulation;

import java.util.LinkedList;
import java.util.List;

public class ObservedAnimalHandler {
    Animal observedAnimal;
    List<Animal> observedAnimalDescendants;

    ObservedAnimalHandler() {
        this.observedAnimal = null;
        this.observedAnimalDescendants = new LinkedList<>();
    }

    public void subscribeToAnimal(Animal a) {
        if (this.observedAnimal != null) {
            this.observedAnimal.removeAllSubscriptions();
            this.observedAnimalDescendants = new LinkedList<>();
        }
        this.observedAnimal = a;
        a.subscribe((pair) -> {
            if (pair.getKey() == AnimalEvent.GIVE_BIRTH) {
                this.onBirthGiven(pair.getValue());
            }
        });
    }

    private void onBirthGiven(Animal animal) {
        this.observedAnimalDescendants.add(animal.getLastChild());
    }
}
