package gui;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import simulation.SimulationStateSnapshot;

import java.util.ArrayList;
import java.util.function.Function;

public class StatisticChart<R> {
    private XYChart.Series series;
    private Function<SimulationStateSnapshot, R> dataAccessor;
    LineChart chart;
    StatisticChart(String xLabel, String yLabel, Function<SimulationStateSnapshot, R> dataAccessor) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel(xLabel);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yLabel);

        this.chart = new LineChart(xAxis, yAxis);
        this.series = new XYChart.Series();
        this.dataAccessor = dataAccessor;

        series.setName(yLabel);
        this.chart.getData().add(series);
    }
    public void addData(SimulationStateSnapshot snapshot, int currentEpoch) {
        if (this.series.getData().size() > 30) {
            this.series.getData().remove(0, 20);
        }
        this.series.getData().add(new XYChart.Data<>(currentEpoch, this.dataAccessor.apply(snapshot)));
    }



}
