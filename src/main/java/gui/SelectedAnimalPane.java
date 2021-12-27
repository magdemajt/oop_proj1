package gui;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import simulation.ObservedAnimalHandler;

public class SelectedAnimalPane extends GridPane {
    private ObservedAnimalHandler observedAnimalHandler;
    public void initialize(ObservedAnimalHandler handler) {
        this.observedAnimalHandler = handler;
        this.setHgap(5);
        this.setVgap(5);
        this.setPadding(new Insets(5));
    }

    public void draw() {

        Text title = new Text("Selected animal");

        this.add(title,1, 0);

        Text numberOfChildrenText = new Text("Children: " + this.observedAnimalHandler.getChildrenCount());
        this.add(numberOfChildrenText, 0, 1);
        Text numberOfDescendantsText = new Text("Descendants: " + this.observedAnimalHandler.getNumberOfDescendants());
        this.add(numberOfDescendantsText, 1, 1);


        if (this.observedAnimalHandler.isDead()) {
            Text deathText = new Text("Died at: " + this.observedAnimalHandler.getAliveFor());
            this.add(deathText, 2, 1);
        }


    }
}
