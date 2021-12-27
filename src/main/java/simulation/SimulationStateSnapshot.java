package simulation;

import java.util.ArrayList;

public record SimulationStateSnapshot(int animalCount, int plantCount, ArrayList<Genome> dominantGenome,
                                      double averageEnergy, double averageLifeSpan, double averageChildRate) {
}
