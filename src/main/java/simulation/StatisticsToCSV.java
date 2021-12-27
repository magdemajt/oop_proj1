package simulation;

import java.io.FileWriter;
import java.io.IOException;

public class StatisticsToCSV {
    private StatisticsGenerator generator;

    public StatisticsToCSV(StatisticsGenerator generator) {
        this.generator = generator;
    }

    String statisticsToCSV() {
        String csvContent =  generator.getSnapshots().stream().map(this::snapshotToCSV).reduce("", (str, acc) -> str + acc + "\n");
        csvContent += this.snapshotToCSV(this.generator.getAverage());
        return csvContent;
    }

    String snapshotToCSV(SimulationStateSnapshot snapshot) {
        if (snapshot == null) {
            return "";
        }

        return snapshot.animalCount() + ";"
                + snapshot.plantCount() + ";"
                + snapshot.averageLifeSpan() + ";"
                + snapshot.averageEnergy() + ";"
                + snapshot.averageChildRate() + ";";
    }

    public void saveStatistics(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.append(this.statisticsToCSV());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
