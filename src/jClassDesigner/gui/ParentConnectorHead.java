package jClassDesigner.gui;

import javafx.scene.paint.Color;

/**
 *
 * @author Jia Sheng Ma
 */
public final class ParentConnectorHead extends ConnectorHead {
    /**
     * 
     * @param parentDiagram where connector head connects to
     * @param childDiagram  where line of connector connects to
     */
    public ParentConnectorHead(DiagramGenerator parentDiagram, DiagramGenerator childDiagram) {
         // INITTIALIZE PROPERTIES
        /* KEEP THE ORIGINAL DATA */
        backupParentX = parentDiagram.translateXProperty().get();
        backupParentY = parentDiagram.translateYProperty().get();
        backupChildX = childDiagram.translateXProperty().get();
        backupChildY = childDiagram.translateYProperty().get();
        
        /* TRANSLATE THE DIAGRAMS BACK TO ZERO */
        parentDiagram.translateXProperty().set(0);
        parentDiagram.translateYProperty().set(0);
        childDiagram.translateXProperty().set(0);
        childDiagram.translateYProperty().set(0);
        
        parentX = parentDiagram.translateXProperty();
        parentY = parentDiagram.translateYProperty();
        childX = childDiagram.translateXProperty();
        childY = childDiagram.translateYProperty();
        
        initShape();
        
        /* PUT THE DIAGRAMS BACK TO WHERE THEY WERE*/
        parentDiagram.translateXProperty().set(backupParentX);
        parentDiagram.translateYProperty().set(backupParentY);
        childDiagram.translateXProperty().set(backupChildX);
        childDiagram.translateYProperty().set(backupChildY);
    }
    
    @Override
    public void initShape() {
        // DRAW HEAD
        this.getPoints().addAll(new Double[]{
            parentX.get()+8.5, parentY.get(),
            parentX.get()-10, parentY.get()+7.5,
            parentX.get()-10, parentY.get()-7.5
        });
        // BIND TO PARENT
        this.translateXProperty().bind(parentX);
        this.translateYProperty().bind(parentY);
        
        this.setFill(Color.BLACK);

    }

    @Override
    public Color getFillColor() {
        return Color.BLACK;
    }
}
