package jClassDesigner.gui;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 *
 * @author Jia Sheng Ma
 */
public abstract class ConnectorHead extends Polygon{
    double backupParentX, backupParentY, backupChildX, backupChildY;
    DoubleProperty parentX, parentY, childX, childY;
    
    public DoubleProperty getParentX()  { return parentX;}
    public DoubleProperty getParentY()  { return parentY;}
    public DoubleProperty getChildX()   { return childX;}
    public DoubleProperty getChildY()   { return childY;}
    
    public abstract void initShape();
    public abstract Color getFillColor();
}
