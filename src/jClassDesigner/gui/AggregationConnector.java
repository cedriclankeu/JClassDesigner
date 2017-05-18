package jClassDesigner.gui;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;

/**
 *
 * @author Jia Sheng Ma 
 */
public final class AggregationConnector extends ConnectorHead {
    
    /**
     * 
     * @param parentDiagram where connector head connects to
     * @param childDiagram  where line of connector connects to
     */
    public AggregationConnector(DiagramGenerator childDiagram, DiagramGenerator parentDiagram) {
        // INITTIALIZE PROPERTIES
        /* KEEP THE ORIGINAL DATA */
        backupParentX = parentDiagram.translateXProperty().get();
        backupParentY = parentDiagram.translateYProperty().get();
        backupChildX = childDiagram.translateXProperty().get();
        backupChildY = childDiagram.translateYProperty().get();
        
        /* TRANSLATE THE DIAGRAMS BACK TO ZERO */
        childDiagram.translateXProperty().set(0);
        childDiagram.translateYProperty().set(0);
        parentDiagram.translateXProperty().set(0);
        parentDiagram.translateYProperty().set(0);
        
        childX = childDiagram.translateXProperty();
        childY = childDiagram.translateYProperty();
        parentX = parentDiagram.translateXProperty();
        parentY = parentDiagram.translateYProperty();
        
        initShape();
        
        /* PUT THE DIAGRAMS BACK TO WHERE THEY WERE*/
        childDiagram.translateXProperty().set(backupChildX);
        childDiagram.translateYProperty().set(backupChildY);
        parentDiagram.translateXProperty().set(backupParentX);
        parentDiagram.translateYProperty().set(backupParentY);
    }
    
    @Override
    public void initShape() {
        // DRAW DIAMOND HEAD
        this.getPoints().addAll(new Double[]{
            parentX.get(),      parentY.get(),
            parentX.get(),      parentY.get()-10.0,
            parentX.get()-10.0, parentY.get(),
            parentX.get(),      parentY.get()+10.0,
            parentX.get()+10,   parentY.get(),
            parentX.get(),      parentY.get()-10.0
        });
        // BIND TO PARENT
        this.translateXProperty().bind(parentX);
        this.translateYProperty().bind(parentY);
        
        this.setStroke(Color.BLACK);
        this.setFill(Color.WHITESMOKE);
    }

    @Override
    public Color getFillColor() {
        return Color.WHITESMOKE;
    }
}
