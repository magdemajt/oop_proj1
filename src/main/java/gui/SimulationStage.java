package gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import simulation.*;

import java.util.Date;

public class SimulationStage extends Stage {

    private ISimulationEngine engine;
    private IWorldMap map;
    private StatisticsGenerator generator;
    private ObservedAnimalHandler observedAnimalHandler;

    private boolean genomeMode;

    private boolean showDominantGenome;

    MapPane mapPane;

    StatisticsPane statisticsPane;

    SelectedAnimalPane selectedAnimalPane;

    void setParams(ISimulationEngine engine, IWorldMap map, StatisticsGenerator statisticsGenerator) {
        this.engine = engine;
        engine.subscribeRerender(() -> Platform.runLater(this::draw));
        this.map = map;
        this.generator = statisticsGenerator;

        this.observedAnimalHandler = new ObservedAnimalHandler();

        this.mapPane = new MapPane();
        this.mapPane.setObservedAnimalHandler(this.observedAnimalHandler);
        this.statisticsPane = new StatisticsPane();
        this.statisticsPane.initialize(() -> {
            StatisticsToCSV statisticsToCSV = new StatisticsToCSV(this.generator);
            statisticsToCSV.saveStatistics("statistics-" + new Date().getTime() + "-" + this.generator.getEpochNumber() + "-" + this.map.getClass().getName() + ".csv");
        });

        this.selectedAnimalPane = new SelectedAnimalPane();
        this.selectedAnimalPane.initialize(this.observedAnimalHandler);
        this.showDominantGenome = false;
        this.genomeMode = true;
    }

    void draw() {
        this.mapPane.getChildren().clear();
        this.mapPane.paintMap(this.map, (Animal a) -> {
            if (!this.engine.isRunning()) {
                if (this.genomeMode) {
                    Platform.runLater(() -> {
                        Alert genomeAlert = new Alert(Alert.AlertType.INFORMATION, "Animal genome type: " + a.genome.toString());
                        genomeAlert.show();
                    });
                } else {
                    this.observedAnimalHandler.subscribeToAnimal(a);
                }
            }
        }, this.showDominantGenome ? this.generator.getDominantGenome() : null);
        this.statisticsPane.draw(this.generator);
        this.selectedAnimalPane.draw();

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

        Button genomeModeButton = new Button(this.genomeMode ? "Change to select animal mode" : "Change to genome mode");

        genomeModeButton.setOnAction((e) -> {
            this.genomeMode = !this.genomeMode;
            if (this.genomeMode) {
                genomeModeButton.setText("Change to select animal mode");
            } else {
                genomeModeButton.setText("Change to genome mode");
            }
        });

        Button dominantGenomeButton = new Button(this.showDominantGenome ? "Hide dominant genome animals" : "Show dominant genome animals");

        dominantGenomeButton.setOnAction((e) -> {
            this.showDominantGenome = !this.showDominantGenome;
            dominantGenomeButton.setText(this.showDominantGenome ? "Hide dominant genome animals" : "Show dominant genome animals");
        });

        GridPane actionsGridPane = new GridPane();
        actionsGridPane.setPadding(new Insets(10));
        actionsGridPane.add(pauseButton, 0, 0);
        actionsGridPane.add(genomeModeButton, 0, 1);
        actionsGridPane.add(dominantGenomeButton, 0, 2);
        actionsGridPane.add(this.selectedAnimalPane, 1, 0);

        gridPane.add(actionsGridPane, 1, 2);


        gridPane.setVgap(5);
        gridPane.setHgap(10);

        Scene scene = new Scene(gridPane);
        this.setScene(scene);
    }


}
