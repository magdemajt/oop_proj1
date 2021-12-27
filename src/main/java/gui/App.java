package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import simulation.*;

public class App extends Application {

    SimultationParamsState paramsState;

    @Override
    public void start(Stage primaryStage) {
        ParamEntryStage paramEntryStage = new ParamEntryStage();
        paramEntryStage.initialize(s -> this.paramsState = s);
        paramEntryStage.showAndWait();

        if (this.paramsState == null) {
            primaryStage.close();
        }
        WallMap wallMap = new WallMap(15, this.paramsState);
        RoundedMap roundedMap = new RoundedMap(15, this.paramsState);
        StatisticsGenerator generator = new StatisticsGenerator(wallMap);
        StatisticsGenerator generatorTwo = new StatisticsGenerator(roundedMap);

        AnimalBreeder breederOne = paramsState.isMagicBreedModeMapOne() ? new MagicAnimalBreeder(wallMap, () -> {
            Platform.runLater(() -> {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Magic breeding in progress");
                a.show();
            });
        }) : new AnimalBreeder(wallMap);
        AnimalBreeder breederTwo = paramsState.isMagicBreedModeMapTwo() ? new MagicAnimalBreeder(roundedMap, () -> {}) : new AnimalBreeder(roundedMap);

        NormalSimulationEngine engine = new NormalSimulationEngine(this.paramsState, wallMap, generator, breederOne);
        NormalSimulationEngine engineTwo = new NormalSimulationEngine(this.paramsState, roundedMap, generatorTwo, breederTwo);

        SimulationStage s1 = new SimulationStage();
        SimulationStage s2 = new SimulationStage();
        s1.setParams(engine, wallMap, generator);
        s2.setParams(engineTwo, roundedMap, generatorTwo);

        s1.draw();
        s2.draw();


        s1.show();
        s2.show();

        Thread thread = new Thread(engine);
        Thread threadTwo = new Thread(engineTwo);

        thread.start();

        threadTwo.start();


    }

    @Override
    public void init() throws Exception {
        super.init();

    }

    public static void main(String[] args) {
        launch(App.class, args);
    }
}
