package gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import simulation.SimulationStateSnapshot;
import simulation.StatisticsGenerator;


public class StatisticsPane extends GridPane {

    StatisticChart<Integer> animalCountChart;
    StatisticChart<Integer> plantCountChart;
    StatisticChart<Double> averageChildChart;
    StatisticChart<Double> averageEnergyChart;
    StatisticChart<Double> averageLifeSpanChart;

    Text firstGenomeText;
    Text secondGenomeText;
    Text thirdGenomeText;

    void initialize(Runnable onSave) {
        this.setPadding(new Insets(10));
        String xAxisLabel = "Epoch";

        this.animalCountChart = new StatisticChart<>(xAxisLabel, "Animal count",
                SimulationStateSnapshot::animalCount);
        this.plantCountChart = new StatisticChart<>(xAxisLabel, "Plant count",
                SimulationStateSnapshot::plantCount);
         this.averageChildChart = new StatisticChart<>(xAxisLabel, "Average child rate", snapshot -> snapshot.averageChildRate());
         this.averageEnergyChart = new StatisticChart<>(xAxisLabel, "Average energy", snapshot -> snapshot.averageEnergy());
         this.averageLifeSpanChart = new StatisticChart<>(xAxisLabel, "Average life span", snapshot -> snapshot.averageLifeSpan());

        this.add(animalCountChart.chart,0, 0);
        this.add(plantCountChart.chart, 0,1);
        this.add(averageChildChart.chart, 0,2);
        this.add(averageEnergyChart.chart, 0,3);
        this.add(averageLifeSpanChart.chart, 0,4);
        this.firstGenomeText = new Text("");
        this.secondGenomeText = new Text("");
        this.thirdGenomeText = new Text("");
        VBox genomeBox = new VBox(new Text("Dominant genomes: "), this.firstGenomeText, this.secondGenomeText, this.thirdGenomeText);
        this.add(genomeBox, 0, 5);
        Button saveButton = new Button("Save statistics");
        saveButton.setOnAction(e -> {
            onSave.run();
        });
        this.add(saveButton, 0, 6);
    }

    void draw(StatisticsGenerator statisticsGenerator) {
        int currentEpoch = statisticsGenerator.getEpochNumber();
        SimulationStateSnapshot lastSnapshot = statisticsGenerator.getLastSnapshot();
        if (lastSnapshot == null) {
            return;
        }
        Platform.runLater(() -> {
            this.averageLifeSpanChart.addData(lastSnapshot, currentEpoch);
            this.averageEnergyChart.addData(lastSnapshot, currentEpoch);
            this.averageChildChart.addData(lastSnapshot, currentEpoch);
            this.animalCountChart.addData(lastSnapshot, currentEpoch);
            this.plantCountChart.addData(lastSnapshot, currentEpoch);
            if (!lastSnapshot.dominantGenome().isEmpty()) {
                this.firstGenomeText.setText("1. " + lastSnapshot.dominantGenome().get(0).toString());
            }
            if (lastSnapshot.dominantGenome().size() > 1) {
                this.secondGenomeText.setText("2. " + lastSnapshot.dominantGenome().get(1).toString());
            }
            if (lastSnapshot.dominantGenome().size() > 2) {
                this.secondGenomeText.setText("3. " + lastSnapshot.dominantGenome().get(1).toString());
            }
        });
    }
}
