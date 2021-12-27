package gui;

import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.GridPane;
import simulation.SimulationStateSnapshot;
import simulation.StatisticsGenerator;

import java.util.ArrayList;

public class StatisticsPane extends GridPane {

    StatisticChart<Integer> animalCountChart;
    StatisticChart<Integer> plantCountChart;
    StatisticChart<Double> averageChildChart;
    StatisticChart<Double> averageEnergyChart;
    StatisticChart<Double> averageLifeSpanChart;

    void initialize() {
        String xAxisLabel = "Epoch";

        this.animalCountChart = new StatisticChart<>(xAxisLabel, "Animal count",
                (snapshot) -> snapshot.animalCount());
        this.plantCountChart = new StatisticChart<>(xAxisLabel, "Plant count",
                (snapshot) -> snapshot.plantCount());
         this.averageChildChart = new StatisticChart<>(xAxisLabel, "Average child rate", snapshot -> snapshot.averageChildRate());
         this.averageEnergyChart = new StatisticChart<>(xAxisLabel, "Average energy", snapshot -> snapshot.averageEnergy());
         this.averageLifeSpanChart = new StatisticChart<>(xAxisLabel, "Average life span", snapshot -> snapshot.averageLifeSpan());

        this.add(animalCountChart.chart,0, 0);
        this.add(plantCountChart.chart, 0,1);
        this.add(averageChildChart.chart, 0,2);
        this.add(averageEnergyChart.chart, 0,3);
        this.add(averageLifeSpanChart.chart, 0,4);
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
        });
    }
}
