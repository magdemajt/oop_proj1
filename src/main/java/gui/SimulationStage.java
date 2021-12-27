package gui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import simulation.Animal;
import simulation.ISimulationEngine;
import simulation.IWorldMap;
import simulation.StatisticsGenerator;

public class SimulationStage extends Stage {

    private ISimulationEngine engine;
    private IWorldMap map;
    private StatisticsGenerator generator;

    MapPane mapPane;

    StatisticsPane statisticsPane;

    void setParams(ISimulationEngine engine, IWorldMap map, StatisticsGenerator statisticsGenerator) {
        this.engine = engine;
        engine.subscribeRerender(() -> {
            Platform.runLater(this::draw);
        });
        this.map = map;
        this.generator = statisticsGenerator;

        this.mapPane = new MapPane();
        this.statisticsPane = new StatisticsPane();
        this.statisticsPane.initialize();
    }

    void draw() {
        this.mapPane.getChildren().clear();
        this.mapPane.paintMap(this.map, (Animal a) -> {
            System.out.println(a.getEnergyValue());
        });
        this.statisticsPane.draw(this.generator);

        GridPane gridPane = new GridPane();

        gridPane.add(this.mapPane, 1, 1);
        gridPane.add(new ScrollPane(this.statisticsPane), 0, 1);


        Button pauseButton = new Button("Pause");

        pauseButton.setOnAction((e) -> {
            if (this.engine.isRunning()) {
                this.engine.pause();
                pauseButton.setText("Resume");
            } else {
                this.engine.resume();
                pauseButton.setText("Pause");
            }
        });

        gridPane.add(pauseButton, 1, 2);

        gridPane.setVgap(5);
        gridPane.setHgap(10);

        Scene scene = new Scene(gridPane);
        this.setScene(scene);
    }



}
