package gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import simulation.SimultationParamsState;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

public class ParamEntryStage extends Stage {
    void initialize(Consumer<SimultationParamsState> simultationParamsStateCallable) {
        GridPane gridPane = new GridPane();


        this.setTitle("Enter simulation params");

        Label widthFieldLabel = new Label("Map width");
        TextField widthField = new TextField("20");

        Label heightFieldLabel = new Label("Map height");
        TextField heightField = new TextField("20");

        Label startEnergyFieldLabel = new Label("Starting energy");
        TextField startEnergyField = new TextField("200");

        Label moveEnergyFieldLabel = new Label("Move energy lose");
        TextField moveEnergyField = new TextField("2");

        Label plantEnergyFieldLabel = new Label("Plant energy increase");
        TextField plantEnergyField = new TextField("25");

        Label jungleRatioFieldLabel = new Label("Jungle ratio (percent)");
        TextField jungleRatioField = new TextField("40");

        Button confirmButton = new Button("Proceed");
        confirmButton.setOnAction(e -> {
            int width = Integer.parseInt(widthField.getText(), 10);
            int height = Integer.parseInt(heightField.getText(), 10);
            int startEnergy = Integer.parseInt(startEnergyField.getText(), 10);
            int moveEnergy = Integer.parseInt(moveEnergyField.getText(), 10);
            int plantEnergy = Integer.parseInt(plantEnergyField.getText(), 10);
            int jungleRatio = Integer.parseInt(jungleRatioField.getText(), 10);

            SimultationParamsState state = new SimultationParamsState(width, height, startEnergy, moveEnergy, plantEnergy, jungleRatio);

            simultationParamsStateCallable.accept(state);
            this.close();
        });

        gridPane.add(widthFieldLabel, 0, 1);
        gridPane.add(widthField, 1, 1);

        gridPane.add(heightFieldLabel, 0, 2);
        gridPane.add(heightField, 1, 2);

        gridPane.add(startEnergyFieldLabel, 0, 3);
        gridPane.add(startEnergyField, 1, 3);

        gridPane.add(moveEnergyFieldLabel, 0, 4);
        gridPane.add(moveEnergyField, 1, 4);

        gridPane.add(plantEnergyFieldLabel, 0, 5);
        gridPane.add(plantEnergyField, 1, 5);

        gridPane.add(jungleRatioFieldLabel, 0, 6);
        gridPane.add(jungleRatioField, 1, 6);

        gridPane.add(confirmButton, 0, 8);


        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        Scene scene = new Scene(gridPane);

        this.setScene(scene);
    }
}
