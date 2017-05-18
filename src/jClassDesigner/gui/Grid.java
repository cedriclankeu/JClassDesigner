package jClassDesigner.gui;

import jClassDesigner.Constants;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Screen;

/**
 *
 * @author Jia Sheng Ma
 * @version 1.1
 */
public class Grid extends Pane{
    private static Grid grid;
    private Grid() {}
    public static Grid getGrid() {
        grid = new Grid();
        grid.setStyle("-fx-background-color:cadetblue;");
        grid.setPrefSize(Constants.WORKPANE_WIDTH, Constants.WORKPANE_HEIGHT);
        drawGrid(grid);
        
        return grid;
    }
    private static void drawGrid(Grid g) {
        for (int i = 0; i < Constants.WORKPANE_HEIGHT; i+=20) {
            Line top_bottom = new Line(0, i, Constants.WORKPANE_WIDTH, i);
            top_bottom.setStroke(Color.BLACK);

            g.getChildren().add(0, top_bottom);
        }
        for (int i = 0; i < Constants.WORKPANE_WIDTH; i+=20) {
            Line left_right = new Line(i, 0, i, Constants.WORKPANE_HEIGHT);
            left_right.setStroke(Color.BLACK);

            g.getChildren().add(1, left_right);
        }
    }
}
