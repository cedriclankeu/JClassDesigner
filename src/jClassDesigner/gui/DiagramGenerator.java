package jClassDesigner.gui;

import jClassDesigner.Constants;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Jia Sheng Ma
 * @version 1.1
 */
public class DiagramGenerator extends VBox{
    
    private double initX;
    private double initY;
    private double init_translateX;
    private double init_translateY;
    Label className;
    
    ArrayList<Group> connectors;
    
    ArrayList<Variable> variables_var;  // TO STORE A LIST OF VARIABLE
    
    ArrayList<Label> variables_lb;      // TO STORE A LIST OF VARIABLE INFO
    ArrayList<Label> methods_lb;        // TO STORE A LIST OF METHOD INFO
    VBox classBox;
    VBox variableBox;
    VBox methodBox;
    
    boolean isAddedToCanvas;
    boolean isClassOrInterface;
    
    public DiagramGenerator() {
        isClassOrInterface = true;
        
        connectors = new ArrayList<>();
        
        variables_lb = new ArrayList<>();
        methods_lb = new ArrayList<>();
        className = new Label(Constants.DEFAULT_CLASS_NAME);
        
        // INITTIALIZE THE CONTAINERS
        classBox = new VBox(className);
        variableBox = new VBox();
        methodBox = new VBox();
        
        isAddedToCanvas = false;
        
        // ADD THE CONTAINERS TO THE DIAGRAM
        this.getChildren().add(classBox);
        this.getChildren().add(variableBox);
        this.getChildren().add(methodBox);

        initStyle();
    } // END OF CONSTRUCTOR
    
    /**
     * For creating parent class/interface diagram on the canvas.
     * @param name name of parent class/interface
     */
    public DiagramGenerator(String name) {
        isClassOrInterface = false;
        
        connectors = new ArrayList<>();
        
        variables_lb = new ArrayList<>();
        methods_lb = new ArrayList<>();
        className = new Label(name);
        
        // INITTIALIZE THE CONTAINERS
        classBox = new VBox(className);
        variableBox = new VBox();
        methodBox = new VBox();
        
        isAddedToCanvas = false;
        
        // ADD THE CONTAINERS TO THE DIAGRAM
        this.getChildren().add(classBox);
        
        initStyle2();
    }

    public boolean getIsClassOrInterface() {
        return isClassOrInterface;
    }

    public void setIsAddedToCanvas(boolean isAddedToCanvas) {
        this.isAddedToCanvas = isAddedToCanvas;
    }

    public boolean getIsAddedToCanvas() {
        return isAddedToCanvas;
    }
    
    /**
     * For adding extra information, such as {abstract} class and <<interface>>.
     * @return the container of the class/interface name box  
     * (the top box in the UML diagram).
     */
    public VBox getClassBox() {
        return classBox;
    }
    
    /**
     * For adding variables_lb.
     * @return the box for adding variables_lb.
     */
    public VBox getVariableBox() {
        return variableBox;
    }
    /**
     * For adding methods_lb.
     * @return the box for adding methods_lb.
     */
    public VBox getMethoBox() {
        return methodBox;
    }
    
    public void addVariable(Variable variable) {
        Label varInfo = variable.getVariableInfo();
        variables_lb.add(varInfo);
        variableBox.getChildren().add(varInfo);
    }
    
    public void addMethod(Method method) {
        
    }
    
    /**
     * For creating diagrams in loading pre-existing info.
     * @param name class name
     * @param layoutX layout x of diagram
     * @param layoutY layout y of diagram
     */
    public DiagramGenerator(String name, double layoutX, double layoutY) {
        initStyle();
    }
    
    /**
     * Changes the class name in the diagram.
     * @param name class name of the diagram.
     */
    public void editClassName(String name) {
            className.setText(name);
    }
    
    /**
     * Edits the string representation of the variable in the UML diagram.
     * @param varInfo string representation of the variable in the UML diagram
     * @param varIndexInDiagram index of the variable being edited.
     */
    public void updateVariableLabel(String varInfo, int varIndexInDiagram) {
        variables_lb.get(varIndexInDiagram).setText(varInfo);
    }
    
    /**
     * Edits the string representation of the method in the UML diagram.
     * @param s string representation of the method in the UML diagram
     * @param lb the method being edited.
     */
    public void editMethod(Label lb, String s) {
        lb.setText(s);
    }
    
    /**
     * 
     * @return the list of variable info.
     */
    public ArrayList<Label> getVariableLabels() {
        return variables_lb;
    }
    
    /**
     * 
     * @return the list of variable info.
     */
    public ArrayList<Label> getMethodLabels() {
        return methods_lb;
    }
    
    /**
     * @return the Class name of this UML class diagram.
     */
    public Label getDiagramClassNameLabel() {
        return className;
    }
    
    public void addConnector(Group connector) {
        connectors.add(connector);
    }
    public ArrayList<Group> getConnectors() {
        return connectors;
    }
    
    
    /**
     * Used for loading in the diagrams only. 
     * Sets up the location of the diagram to where is was last saved.
     * @param translateX amount of x translation of the diagram.
     * @param translateY amount of y translation location of the diagram.
     */
    public void setupLocation(double translateX, double translateY) {
        this.setTranslateX(translateX);
        this.setTranslateY(translateY);
    }
    
    /**
     * Refreshes this diagram on the workspace 
     * (usually called when components are readied).
     */
    public void refresh() {
        
    }
    
    /**
     * Sets up the style of the diagrams.
     */
    public void initStyle() {
        this.setMinSize(Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT);
        this.getStyleClass().add(Constants.DIAGRAM);
        classBox.getStyleClass().add(Constants.DIAGRAM_COMPONENTS);
        classBox.setStyle("-fx-alignment: CENTER");
        variableBox.getStyleClass().add(Constants.DIAGRAM_COMPONENTS);
        methodBox.getStyleClass().add(Constants.DIAGRAM_COMPONENTS);
        variableBox.setMinSize(Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT/2);
        methodBox.setMinSize(Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT/2);
        //className.setAlignment(Pos.CENTER);
    }
    public void initStyle2() {
        this.setMinSize(Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT);
        this.getStyleClass().add(Constants.DIAGRAM);
        this.setStyle("-fx-border-width: 1px; -fx-border-color : BLACK; -fx-alignment: CENTER;");
    }
    
}
