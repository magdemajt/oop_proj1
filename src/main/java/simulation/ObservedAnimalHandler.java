package simulation;

import java.util.LinkedList;
import java.util.List;

public class ObservedAnimalHandler {
    Animal observedAnimal;
    List<Animal> observedAnimalDescendants;
    private boolean isDead;

    public ObservedAnimalHandler() {
        this.observedAnimal = null;
        this.observedAnimalDescendants = new LinkedList<>();
        this.isDead = false;
    }

    public void subscribeToAnimal(Animal a) {
        if (this.observedAnimal != null) {
            this.isDead = false;
            this.observedAnimal.removeAllSubscriptions();
            this.observedAnimalDescendants = new LinkedList<>();
        }
        this.observedAnimal = a;
        a.subscribe((pair) -> {
            if (pair.getKey() == AnimalEvent.GIVE_BIRTH) {
                this.onBirthGiven(pair.getValue());
            } else if (pair.getKey() == AnimalEvent.DEATH) {
                this.isDead = true;
            }
        });
    }

    private void onBirthGiven(Animal animal) {
        this.observedAnimalDescendants.add(animal.getLastChild());
    }

    public boolean isAnimalObserved(Animal a) {
        return a == this.observedAnimal || this.observedAnimalDescendants.contains(a);
    }

    public boolean isDead() {
        return isDead;
    }

    public Animal getObservedAnimal() {
        return observedAnimal;
    }

    public int getChildrenCount() {
        if (this.observedAnimal == null) {
            return 0;
        }
        return this.observedAnimal.getChildrenCount();
    }
    public int getAliveFor() {
        if (this.observedAnimal == null) {
            return 0;
        }
        return this.observedAnimal.getAliveFor();
    }

    public int getNumberOfDescendants() {
        return this.observedAnimalDescendants.size();
    }
}
