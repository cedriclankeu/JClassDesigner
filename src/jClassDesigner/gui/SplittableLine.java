package jClassDesigner.gui;

import java.util.ArrayList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author Jia Sheng Ma
 */
public class SplittableLine extends Line implements Splittable, Mergeable{
    
    public final double LINE_WIDTH = 3.0;

    Anchor anchorStart;
    Anchor anchorEnd;
    private ArrayList<DoubleProperty> anchorXes;
    private ArrayList<DoubleProperty> anchorYs;
    private ArrayList<Anchor> anchors;
    
    /**
     * 
     * @param startX connector head x
     * @param startY connector head y
     * @param endX   child x
     * @param endY   child y
     */
    public SplittableLine(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
        anchors = new ArrayList<>();
        anchorXes = new ArrayList<>();
        anchorYs = new ArrayList<>();
        setStrokeWidth(LINE_WIDTH);
        setStroke(Color.DARKSLATEGRAY);
        
        anchorStart = new Anchor(startX, startY);
        anchorEnd = new Anchor(endX, endY);
        
        // BIND

        this.startXProperty().bind(anchorStart.centerXProperty());
        this.startYProperty().bind(anchorStart.centerYProperty());
        this.endXProperty().bind(anchorEnd.centerXProperty());
        this.endYProperty().bind(anchorEnd.centerYProperty());
    }
    
    @Override
    public void split(double startX, double startY, double endX, double endY) {
        double centerX = (startX + endX) / 2;
        double centerY = (startY + endY) / 2;
        DoubleProperty centerX_DoubleProperty = new SimpleDoubleProperty(centerX);
        DoubleProperty centerY_DoubleProperty = new SimpleDoubleProperty(centerY);
        System.out.println("not supported yet");
    }
    
    @Override
    public void merge(double startX, double startY, double endX, double endY) {
        System.out.println("not supported yet");
    }

    public void addAnchor(DoubleProperty x, DoubleProperty y) {
        
        // TODO
        // SET UP DRAGGABLE
    }

    public Anchor getAnchorStart() {
        return anchorStart;
    }
    public Anchor getAnchorEnd() {
        return anchorEnd;
    }
    public ArrayList<DoubleProperty> getAnchorXes() {
        return anchorXes;
    }
    public ArrayList<DoubleProperty> getAnchorYs() {
        return anchorYs;
    }
}
