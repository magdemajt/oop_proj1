package simulation;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class Animal implements IMapObject, IPositionChangeObservable {
    public final Genome genome;
    private Vector2d position;

    private MapDirection facing;

    public final int initialEnergy;

    private int energyValue;

    private final List<Consumer<Pair<Vector2d, IMapObject>>> subscriptions;

    private final List<Consumer<Pair<AnimalEvent, Animal>>> animalEventSubscribers;

    private Animal lastChild;

    private int aliveFor;

    private int childrenCount;

    private boolean isObserved = false;

    private boolean isObservedDecendant = false;

    Animal parent;

    public Vector2d getPosition() {
        return this.position;
    }

    public int getEnergyValue() {
        return this.energyValue;
    }

    Animal(Vector2d initialPosition, int startingEnergyValue) {
        this.genome = new Genome();
        this.position = initialPosition;
        this.energyValue = startingEnergyValue;
        this.initialEnergy = startingEnergyValue;
        this.facing = MapDirection.NORTH;
        this.subscriptions = new LinkedList<>();
        this.animalEventSubscribers = new LinkedList<>();
        this.aliveFor = 0;
        this.childrenCount = 0;
        this.parent = null;
    }

    Animal(Vector2d initialPosition, int startingEnergyValue, Genome genome) {
        this.genome = genome;
        this.position = initialPosition;
        this.energyValue = startingEnergyValue;
        this.initialEnergy = startingEnergyValue;
        this.facing = MapDirection.NORTH;
        this.subscriptions = new LinkedList<>();
        this.aliveFor = 0;
        this.childrenCount = 0;
        this.parent = null;
        this.animalEventSubscribers = new LinkedList<>();
    }

    public void turn(MapDirection turnDirection) {
        this.facing = this.facing.addDirections(turnDirection);
    }

    public Vector2d getForwardVector() {
        return this.facing.getMoveVector();
    }

    public void moveForward() {
        Vector2d oldPosition = this.position;
        this.position = this.position.add(this.getForwardVector());
        this.nextPosition(oldPosition);
    }

    public void moveBackward() {
        Vector2d oldPosition = this.position;
        this.position = this.position.add(this.getForwardVector().getBackwardFacing());
        this.nextPosition(oldPosition);
    }

    public void eat(int energyIncrease) {
        this.energyValue = Math.min(this.energyValue + energyIncrease, this.initialEnergy);
        this.sendEvent(AnimalEvent.EAT);
    }
    public void removeEnergy(int energyValue) {
        if (energyValue < 0) {
            throw new IllegalArgumentException("Energy subtract value should be greater than 0");
        }
        this.energyValue -= energyValue;
    }

    public int getRandomGene() {
        return this.genome.getRandomGene();
    }

    @Override
    public void subscribeToPositionChange(Consumer<Pair<Vector2d, IMapObject>> subscription) {
        this.subscriptions.add(subscription);
    }

    public void subscribe(Consumer<Pair<AnimalEvent, Animal>> subscription) {
        this.animalEventSubscribers.add(subscription);
        this.isObserved = true;
    }

    public void removeAllSubscriptions() {
        this.animalEventSubscribers.clear();
    }

    @Override
    public void nextPosition(Vector2d position) {
        this.subscriptions.forEach(pairConsumer -> {
            pairConsumer.accept(new Pair<>(position, this));
        });
    }

    public void anotherYearHasPassed() {
        this.aliveFor += 1;
    }
    public int getAliveFor() {
        return this.aliveFor;
    }

    public int getChildrenCount() {
        return this.childrenCount;
    }

    private void sendEvent(AnimalEvent event) {
        this.animalEventSubscribers.forEach((s) -> {
            System.out.println(event.toString());
            s.accept(new Pair<>(event, this));
        });
    }

    public void addChild(Animal child) {
        child.isObservedDecendant = this.isObserved || this.isObservedDecendant;
        this.lastChild = child;
        this.childrenCount += 1;
        this.sendEvent(AnimalEvent.GIVE_BIRTH);
    }

    public Animal getLastChild() {
        return this.lastChild;
    }

    public void markAsDead() {
        this.sendEvent(AnimalEvent.DEATH);
    }
}
