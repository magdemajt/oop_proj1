package simulation;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class NormalSimulationEngine implements ISimulationEngine {
    AnimalBreeder breeder;
    AnimalMover mover;
    AnimalFeeder feeder;
    PlantGrower grower;

    Runnable onTickEnd;

    IWorldMap map;

    SimultationParamsState paramsState;

    private boolean paused;
    private int simulationRefreshTime;

    private AtomicInteger deadAnimals;

    private AtomicLong deadAnimalsCombinedLifeSpan;


    private StatisticsGenerator statisticsGenerator;

    public NormalSimulationEngine(SimultationParamsState paramsState, IWorldMap map, StatisticsGenerator statisticsGenerator) {
        this.paramsState = paramsState;
        this.breeder = new AnimalBreeder(map);
        this.mover = new AnimalMover(paramsState.moveEnergy(), map);
        this.feeder = new AnimalFeeder(paramsState.plantEnergy(), map);
        JungleGenerationStrategy jungleGenerationStrategy = new JungleGenerationStrategy(paramsState.jungleRatio(), map.getMapSize());
        this.grower = new PlantGrower(jungleGenerationStrategy.getJungleBoundaries(), map);
        this.map = map;
        this.simulationRefreshTime = 1000;

        this.grower.initializeJungle();
        this.deadAnimals = new AtomicInteger(0);
        this.deadAnimalsCombinedLifeSpan = new AtomicLong(0);
        this.statisticsGenerator = statisticsGenerator;
    }

    @Override
    public IWorldMap getMap() {
        return this.map;
    }


    private void removeDeadAnimals() {
        this.map.getMapObjects().forEach((key, value) -> {
            List<IMapObject> toRemove = value.stream().filter(obj -> {
                if (obj instanceof Animal a) {
                    return a.getEnergyValue() <= 0;
                }
                return false;
            }).toList();
            this.deadAnimals.addAndGet(toRemove.size());
            toRemove.forEach(v -> {
                this.deadAnimalsCombinedLifeSpan.addAndGet(((Animal) v).getAliveFor());
                this.map.removeObjectFromList(v, key);
            });
        });
        this.map.clearEmptyLists();
    }

    private void generateStatistics() {
        this.statisticsGenerator.generateSnapshot(this.getDeadAnimalsAverageLifeSpan(), this.getAverageChildRate());
    }

    private void moveAnimals() {
        this.mover.moveAnimals();
    }

    private void eat() {
        this.feeder.feedAnimals();
    }

    private void breedAnimals() {
        List<Animal> newAnimals = this.breeder.breedAnimals();
        newAnimals.forEach(a -> {
            this.map.setAnimalListeners(a);
            this.map.placeObject(a, a.getPosition());
        });
    }

    private void growPlants() {
        this.grower.growJungle();
    }

    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(this.simulationRefreshTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this.paused) {
                continue;
            }
            this.removeDeadAnimals();
            this.moveAnimals();
            this.eat();
            this.breedAnimals();
            this.growPlants();

            this.map.getMapAnimals().forEach((k, list) -> {
                list.forEach(Animal::anotherYearHasPassed);
            });
            this.generateStatistics();
            try {
                if (this.onTickEnd != null) {
                    this.onTickEnd.run();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void subscribeRerender(Runnable onTickEnd) {
        this.onTickEnd = onTickEnd;
    }

    @Override
    public void pause() {
        this.paused = true;
    }

    @Override
    public void resume() {
        this.paused = false;
    }

    @Override
    public void setRefreshSpeed(int refreshSpeedMiliseconds) {
        this.simulationRefreshTime = refreshSpeedMiliseconds;
    }

    @Override
    public double getDeadAnimalsAverageLifeSpan() {
        System.out.println(this.deadAnimalsCombinedLifeSpan.get() + " " + this.deadAnimals.get() + " " + (double) this.deadAnimalsCombinedLifeSpan.get() / this.deadAnimals.get());
        if (this.deadAnimals.get() == 0) {
            return 0.0;
        }
        return (double) this.deadAnimalsCombinedLifeSpan.get() / this.deadAnimals.get();
    }

    @Override
    public double getAverageChildRate() {
        AtomicInteger numberOfChiildren = new AtomicInteger(0);
        AtomicInteger animalsCount = new AtomicInteger(0);
        this.map.getMapAnimals().forEach((k, v) -> {
            v.forEach(a -> {
                numberOfChiildren.addAndGet(a.getChildrenCount());
                animalsCount.incrementAndGet();
            });
        });
        return (double) numberOfChiildren.get() / animalsCount.get();
    }

    @Override
    public boolean isRunning() {
        return !this.paused;
    }
}
