package gui;

import com.sun.source.doctree.SummaryTree;
import javafx.application.Application;
import javafx.stage.Stage;
import simulation.NormalSimulationEngine;
import simulation.SimultationParamsState;
import simulation.StatisticsGenerator;
import simulation.WallMap;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

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
        StatisticsGenerator generator = new StatisticsGenerator(wallMap);
        NormalSimulationEngine engine = new NormalSimulationEngine(this.paramsState, wallMap, generator);

        SimulationStage s1 = new SimulationStage();
        s1.setParams(engine, wallMap, generator);

        s1.draw();
        s1.show();

        Thread thread = new Thread(engine);

        thread.start();

        Thread timeThread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(100);
                thread.interrupt();
                s1.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        timeThread.start();


    }

    @Override
    public void init() throws Exception {
        super.init();

    }

    public static void main(String[] args) {
        launch(App.class, args);
    }
}
