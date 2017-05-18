
package jClassDesigner.gui;

import jClassDesigner.controller.ComponentController;
import jClassDesigner.data.DataManager;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Group;
import javafx.scene.shape.Polygon;
import saf.AppTemplate;

/**
 *
 * @author Jia Sheng Ma
 */
public class ConnectorFactory {
    
    public static Group getConnector(ConnectorHead connectorHead, ComponentController componentController) {
        
        DoubleProperty parentX, parentY, childX, childY;
        SplittableLine splittableLine;
        
        parentX = connectorHead.getParentX();
        parentY = connectorHead.getParentY();
        childX  = connectorHead.getChildX();
        childY  = connectorHead.getChildY();
        splittableLine = new SplittableLine(parentX, parentY, childX, childY);
        
        splittableLine.setStroke(connectorHead.getFillColor());
        
        connectorHead.translateXProperty().bind(splittableLine.getAnchorStart()./*translateXProperty()*/centerXProperty());
        connectorHead.translateYProperty().bind(splittableLine.getAnchorStart()./*translateYProperty()*/centerYProperty());
        
        componentController.setupSelectable(splittableLine);
        // Use a Group to group all the component of a connector together.
        Group connector = new Group();
        connector.getChildren().addAll(connectorHead, splittableLine);
        
        return connector;
    }
    public static Group getInterfaceConnector(ConnectorHead connectorHead, ComponentController componentController) {
        
        DoubleProperty parentX, parentY, childX, childY;
        SplittableLine splittableLine;
        
        parentX = connectorHead.getParentX();
        parentY = connectorHead.getParentY();
        childX  = connectorHead.getChildX();
        childY  = connectorHead.getChildY();
        splittableLine = new SplittableLine(parentX, parentY, childX, childY);
        
        connectorHead.translateXProperty().bind(splittableLine.getAnchorStart().centerXProperty());
        connectorHead.translateYProperty().bind(splittableLine.getAnchorStart().centerYProperty());
        // DASHED
        splittableLine.getStrokeDashArray().addAll(5.0, 10.0);
        componentController.setupSelectable(splittableLine);
        // Use a Group to group all the component of a connector together.
        Group connector = new Group();
        connector.getChildren().addAll(connectorHead, splittableLine);
        
        return connector;
    }
    
}
