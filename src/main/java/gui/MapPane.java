package gui;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import simulation.*;

import java.util.List;
import java.util.function.Consumer;

public class MapPane extends GridPane {

    static final int RECTANGlE_SIZE = 25;

    private ObservedAnimalHandler observedAnimalHandler;

    public void setObservedAnimalHandler(ObservedAnimalHandler handler) {
        this.observedAnimalHandler = handler;
    }
    public void paintMap(IWorldMap map, Consumer<Animal> animalClickHandler, Genome dominantGenome) {
        this.setHgap(5);
        this.setVgap(5);
        this.setPadding(new Insets(5));
        Vector2d size = map.getMapSize();

        for (int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                List<IMapObject> objects = map.getObjectsAt(new Vector2d(x, y));
                if (objects == null) {
                    this.add(new Rectangle(RECTANGlE_SIZE, RECTANGlE_SIZE, new Color(1, 1, 1, 0)), x, size.y - y);
                    continue;
                }
                if (objects.size() == 1) {
                    IMapObject mapObject = objects.get(0);
                    Rectangle animalRectangle = new Rectangle(RECTANGlE_SIZE, RECTANGlE_SIZE);
                    Color rectangleColor = null;
                    if (mapObject instanceof Animal animal && animal.getEnergyValue() > 0) {
                        if (this.observedAnimalHandler.isAnimalObserved(animal)) {
                            animalRectangle.setStrokeType(StrokeType.INSIDE);
                            animalRectangle.setStroke(new Color(0, 1, 0, 1));
                            animalRectangle.setStrokeWidth(3);
                        }
                        if (animal.genome == dominantGenome) {
                            animalRectangle.setStrokeType(StrokeType.CENTERED);
                            animalRectangle.setStroke(new Color(0, 1, 1, 1));
                            animalRectangle.setStrokeWidth(3);
                        }
                        animalRectangle.setOnMouseClicked((e) -> {
                            animalClickHandler.accept(animal);
                        });
                        double color = ((double)animal.getEnergyValue() / (double)animal.initialEnergy);
                        rectangleColor = new Color(color, 0, (1.0-color), 1);
                    }
                    if (mapObject instanceof Grass) {
                        rectangleColor = new Color(0 , 200.0/255, 0, 1);
                    }
                    animalRectangle.setFill(rectangleColor);
                    this.add(animalRectangle, x, size.y - y);
                } else if (objects.size() > 1) {
                    int count = (int) objects.stream().takeWhile(o -> o instanceof Animal).count();
                    Rectangle animalsRectange = new Rectangle(RECTANGlE_SIZE, RECTANGlE_SIZE, new Color(1, 0, 1, 1));
                    Group g = new Group(animalsRectange, new Label("" + count));
                    this.add(g, x, size.y - y);
                }
            }
        }
    }
}
