package simulation;

import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StatisticsGenerator {
    IWorldMap map;
    ArrayList<SimulationStateSnapshot> snapshots;

    public StatisticsGenerator(IWorldMap map) {
        this.snapshots = new ArrayList<>();
        this.map = map;
    }

    public void generateSnapshot(double averageLifeSpanForDead, double averageChildrenForAnimals) {
        AtomicInteger numberOfAnimals = new AtomicInteger(0);
        HashMap<Genome, Integer> genomeMap = new HashMap<>();
        AtomicLong totalEnergy = new AtomicLong(0);
        this.map.getMapAnimals().forEach((k, v) -> {
            v.forEach(animal -> {
                totalEnergy.addAndGet(animal.getEnergyValue());
                if (genomeMap.containsKey(animal.genome)) {
                    genomeMap.put(animal.genome, genomeMap.get(animal.genome) + 1);
                    return;
                }
                genomeMap.put(animal.genome, 0);
            });
            numberOfAnimals.addAndGet(v.size());
        });

        double averageEnergy = (double) totalEnergy.get() / numberOfAnimals.get();

        ArrayList<Genome> genomes = new ArrayList<>(genomeMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(3).map(Map.Entry::getKey).toList());

        this.snapshots.add(new SimulationStateSnapshot(numberOfAnimals.get(), this.map.getMapPlants().size(),
                genomes, averageEnergy, averageLifeSpanForDead, averageChildrenForAnimals));
    }

    public ArrayList<SimulationStateSnapshot> getRecentSnapshots() {
        ArrayList<SimulationStateSnapshot> recentSnapshots = new ArrayList<>(5);
        for (int i = this.snapshots.size() - 1 - 5; i < this.snapshots.size(); i++) {
            if (i > 0) {
                recentSnapshots.add(this.snapshots.get(i));
            }
        }
        return recentSnapshots;
    }

    public SimulationStateSnapshot getLastSnapshot() {
        if (this.snapshots.size() == 0) {
            return null;
        }
        return this.snapshots.get(this.snapshots.size() - 1);
    }

    public SimulationStateSnapshot getAverage() {
        int animals = 0;
        int plants = 0;
        double childRate = 0;
        double energy = 0;
        double lifeSpan = 0;

        for (SimulationStateSnapshot snap : this.snapshots) {
            animals += snap.animalCount();
            plants += snap.plantCount();
            childRate += snap.averageChildRate();
            energy += snap.averageEnergy();
            lifeSpan += snap.averageLifeSpan();
        }
        if (snapshots.isEmpty()) {
            return null;
        }
        return new SimulationStateSnapshot(animals / snapshots.size(),
                plants / snapshots.size(), new ArrayList<>(),
                energy / snapshots.size(), lifeSpan / snapshots.size(),
                childRate / snapshots.size()
        );
    }

    public ArrayList<SimulationStateSnapshot> getSnapshots() {
        return snapshots;
    }

    public int getEpochNumber() {
        return this.snapshots.size();
    }

    public Genome getDominantGenome() {
        if (this.getLastSnapshot().dominantGenome().isEmpty()) {
            return null;
        }
        return this.getLastSnapshot().dominantGenome().get(0);
    }
}
