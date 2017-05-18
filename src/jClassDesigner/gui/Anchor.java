package jClassDesigner.gui;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * 
 * @author Jia Sheng Ma
 */
public class Anchor extends Circle {
    
    /**
     * 
     * @param x x to bind
     * @param y y to bind
     */
    public Anchor(DoubleProperty x, DoubleProperty y) {
        super(x.get(), y.get(), 2.5 , Color.BLACK);
        this.centerXProperty().bind(x);
        this.centerYProperty().bind(y);
    }   
}
