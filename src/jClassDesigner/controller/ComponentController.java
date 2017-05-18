package jClassDesigner.controller;

import jClassDesigner.Constants;
import jClassDesigner.data.DataManager;
import jClassDesigner.data.AppState;
import jClassDesigner.data.RawDataBank;
import jClassDesigner.gui.AddClassUI;
import jClassDesigner.gui.AddInterfaceUI;
import jClassDesigner.gui.AggregationConnector;
import jClassDesigner.gui.ConnectorFactory;
import jClassDesigner.gui.DiagramGenerator;
import jClassDesigner.gui.InterfaceComboBox;
import jClassDesigner.gui.Method;
import jClassDesigner.gui.ParentComboBox;
import jClassDesigner.gui.ParentConnectorHead;
import jClassDesigner.gui.SplittableLine;
import jClassDesigner.gui.UsesConnector;
import jClassDesigner.gui.Variable;
import jClassDesigner.gui.Workspace;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import saf.AppTemplate;

/**
 * This class responds to interactions with other UI pose editing controls.
 * 
 * @author Jia Sheng Ma
 * @version 1.1
 */
public class ComponentController {

    private final String CLASS_EXIST_ERROR = "Class already exist, please choose another name.";
    private final Dialog class_exist_alert = new Alert(Alert.AlertType.WARNING, CLASS_EXIST_ERROR, ButtonType.OK);
    
    public final Label ABSTRACT_HEADER = new Label(Constants.ABSTRACT_CLASS_HEADER);
    
    AppTemplate app;
    AppState state;

    DataManager dataManager;
    ToolController toolController;
    double initX;
    double initY;
    double init_translateX;
    double init_translateY;
    
    public ComponentController(AppTemplate initApp) {
	app = initApp;
	dataManager = (DataManager)app.getDataComponent();
        toolController = new ToolController(app);
    }
    public ComponentController() {
        dataManager = (DataManager)app.getDataComponent();
        toolController = new ToolController(app);
    }

    /**
     * Adds new class to the design.
     */
    public void handleAddClass() {
        
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        AddClassUI classUI = new AddClassUI(app);
        //ParentComboBox parents = new ParentComboBox(classUI);
        DiagramGenerator diagram = classUI.getDiagram();
        
        // SET THE NEWLY CREATED DIAGRAM AS THE SELECTED NODE
        dataManager.setSelectedNode(diagram);
        
        // ADD A COPY TO DATAMANAGER
        dataManager.addDiagram(diagram);
        dataManager.addComponentUI(diagram, classUI);
        
        // ADD CLASS NAME TO DATA MANAGER
        dataManager.addClassName(classUI.getClassName_tf().getText());
        
        // ADD DIAGRAM TO WORK PANE FOR DISPLAY
        workspace.getWorkPane().getChildren().add(diagram);
        diagram.setIsAddedToCanvas(true);
        // CLEAR CURRENT UI
        workspace.getComponentToolbarPane().getChildren().clear();
        // ADD A NEW UI
        workspace.getComponentToolbarPane().getChildren().add(classUI);
        
        // MARK THE WORKSPACE AS EDITED
        workspace.getComponentToolbarPane().setDisable(false);
        
        // REFRESH AVAILABLE PARENTS
        refreshParentAndInterfaces(dataManager);
        
    } // END OF ADD CLASS
    
    /**
     * Adds new interface to the design.
     */
    public void handleAddInterface() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        AddInterfaceUI interfaceUI = new AddInterfaceUI(app);
        DiagramGenerator diagram = interfaceUI.getDiagram();
        
        // SET THE NEWLY CREATED DIAGRAM AS THE SELECTED NODE
        dataManager.setSelectedNode(diagram);
        
        // ADD A COPY TO DATAMANAGER
        dataManager.addDiagram(diagram);
        dataManager.addComponentUI(diagram, interfaceUI);
        
        // ADD CLASS NAME TO DATA MANAGER
        dataManager.addClassName(interfaceUI.getInterfaceName_tf().getText());
        
        // ADD DIAGRAM TO WORK PANE FOR DISPLAY
        workspace.getWorkPane().getChildren().add(diagram);
        diagram.setIsAddedToCanvas(true);
        // CLEAR CURRENT UI
        workspace.getComponentToolbarPane().getChildren().clear();
        // ADD A NEW UI
        workspace.getComponentToolbarPane().getChildren().add(interfaceUI);
        
        // MARK THE WORKSPACE AS EDITED
        workspace.getComponentToolbarPane().setDisable(false);
        
        // REFRESH AVAILABLE PARENTS
        refreshParentAndInterfaces(dataManager);
        
    } // END OF ADD INTERFACE
    
    /**
     * Refreshes the available parent classes (whenever a new class is added).
     * @param dataManager is where the list of available parent classes is stored/extracted from.
     */
    public void refreshParentAndInterfaces(DataManager dataManager) {
        System.out.println("Refreshing parent and interface combo boxes..");
        ArrayList<VBox> componentUIs = new ArrayList<>();
        ArrayList<String> availableParentClasses = new ArrayList<>();
        ArrayList<String> availableInterfaces = new ArrayList<>();
        
        // GET COMPONENT UIS, AND COMPOSE A LIST OF PARENTS
        for(VBox v : dataManager.getComponentUIs().values()) {
            if(v instanceof AddClassUI) {
                AddClassUI classUI = (AddClassUI)v;
                componentUIs.add(classUI);
                availableParentClasses.add(classUI.getClassName());
            } else {
                AddInterfaceUI interfaceUI = (AddInterfaceUI)v;
                componentUIs.add(interfaceUI);
                availableInterfaces.add(interfaceUI.getInterfaceName());
            }
        } // END OF GET COMPONENT UIS, AND COMPOSE A LIST OF PARENTS
        
        // ADD EXTERNAL INTERFACES TO THE LIST
        for(String s : dataManager.getExternalInterfaces()) {
            availableInterfaces.add(s);
            // DEBUG: 
            System.out.println(s);
        }
        // REFRESH EACH PARENT COMBO BOX
        for(VBox v : componentUIs) {
            if(v instanceof AddClassUI) {
                AddClassUI classUI = (AddClassUI)v;
                classUI.getParentComboBox().refreshAvailableParents(availableParentClasses);
                // ADD EXTERNAL CLASS BACK IN 
                for(String s : dataManager.getExternalClasses()) {
                    classUI.getParentComboBox().getItems().add(s);
                }
                
                classUI.getInterfaceComboBox().refreshAvailableInterface(availableInterfaces);
//                // ADD THE EXTERNAL INTERFACES BACK IN
//                for(String s : dataManager.getExternalInterfaces()) {
//                    classUI.getInterfaceComboBox().getItems().add(s);
//                }
            } else {
                AddInterfaceUI interfaceUI = (AddInterfaceUI)v;
                interfaceUI.getInterfaceComboBox().refreshAvailableInterface(availableInterfaces);
                // ADD THE EXTERNAL INTERFACES BACK IN
//                for(String s : dataManager.getExternalInterfaces()) {
//                    interfaceUI.getInterfaceComboBox().getItems().add(s);
//                }
            }
        } // END OF REFRESH EACH PARENT COMBO BOX
        
        
        System.out.println("Finished refreshing parent and interface combo boxes..");
        
    } // END OF REFRESH PARENT COMBO BOX
    
    /**
     * Sets up the node as draggable.
     * @param node node to be set as draggable.
     */
    public void setupDraggable(Node node) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        node.addEventFilter(MouseEvent.MOUSE_PRESSED, (final MouseEvent mouseEvent) -> {
            onSelect(node, mouseEvent, workspace);
        });

        node.addEventFilter(MouseEvent.MOUSE_DRAGGED, (final MouseEvent mouseEvent) -> {
            if(dataManager.getAppState()==AppState.SELECT_STATE) {
                if(workspace.getSnap_cbx().selectedProperty().get()) {
                    int newX = ((int)(init_translateX + mouseEvent.getSceneX() - initX)/20)*20;
                    int newY = ((int)(init_translateY + mouseEvent.getSceneY() - initY)/20)*20;
                    node.setTranslateX(newX);
                    node.setTranslateY(newY);
                } else {
                    node.setTranslateX(init_translateX + mouseEvent.getSceneX() - initX);
                    node.setTranslateY(init_translateY + mouseEvent.getSceneY() - initY);
                }
                // UPDATE TOOLBAR CONTROL
                app.getGUI().updateToolbarControls(false);
            }
            
        });
    }
    public void onSelect(Node node, MouseEvent mouseEvent, Workspace workspace) {
        if(dataManager.getAppState()==AppState.SELECT_STATE || dataManager.getAppState()==AppState.RESIZE_STATE) {
                initX = mouseEvent.getSceneX();
                initY = mouseEvent.getSceneY();
                init_translateX = node.getTranslateX();
                init_translateY = node.getTranslateY();
                dataManager.setSelectedNode(node);
        
                // ADD SELECTED DIAGRAM'S COMPOENENT TOOLBAR PANE
                VBox ui = dataManager.getComponentUI((VBox)node);
                if(ui !=null) {
                    // CLEAR COMPONENT TOOLBAR PANE
                    workspace.getComponentToolbarPane().getChildren().clear();
                    workspace.getComponentToolbarPane().getChildren().add(ui);
                    workspace.getComponentToolbarPane().setDisable(false);
                }
        }
    }
    
    /**
     * Select the node that is being clicked (used for variable and method).
     * @param node 
     */
    public void setupSelectable(Node node) {
        node.addEventFilter(MouseEvent.MOUSE_PRESSED, (final MouseEvent mouseEvent) -> {
            if(dataManager.getAppState() == AppState.SELECT_STATE || 
            dataManager.getAppState() == AppState.RESIZE_STATE || 
            dataManager.getAppState() == AppState.NEW_STATE ||
            dataManager.getAppState() == AppState.DRAG_STATE) {
               // SET THE CLICKED NODE AS SELECTED
               dataManager.setSelectedNode(node);
            }
        });
    }
    
    double initResizeX;
    double initResizeY;
    boolean isDraggingBottom;
    boolean isDraggingRight;
    boolean isDraggingSE;
    /**
     * Sets up resizable.
     * @param node node to be resized
     */
    public void setupResizable(VBox node) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        
        node.addEventFilter(MouseEvent.MOUSE_PRESSED, (final MouseEvent me) -> {
            initResizeX = me.getX();
            initResizeY = me.getY();
            
            if(isAtSE(me, node)) {
                isDraggingSE = true;
            } else if(isAtBottom(me, node)) {
                isDraggingBottom = true;
            } else if(isAtRight(me, node)) {
                isDraggingRight = true;
            }
            onSelect(node, me, workspace);
        }); // MOUSE PRESSED
        
        // MOUSE DRAGGED: RESIZE
        node.addEventFilter(MouseEvent.MOUSE_DRAGGED, (final MouseEvent me) -> {
            // SET THE PREFSIZE OF THE NODE ACCORDINGLY
            if(dataManager.getAppState()==AppState.RESIZE_STATE) {
                double deltaX;
                double deltaY;
                double newWidth;
                double newHeight;
                double finalX;
                double finalY;
                if(workspace.getSnap_cbx().selectedProperty().get()) {
                    finalX = ((int)me.getX()/20)*20;
                finalY = ((int)me.getY()/20)*20;
                } else {
                    finalX = me.getX();
                    finalY = me.getY();
                }
                
                if(isAtSE(me, node) || isDraggingSE) {
                    resizeNodeWidth(finalX, node);
                    resizeNodeHeight(finalY, node);
                }
                else if(isAtBottom(me, node) || isDraggingBottom) { 
                    resizeNodeHeight(finalY, node);
                } else if(isAtRight(me, node) || isDraggingRight) {
                    resizeNodeWidth(finalX, node);
                } else if(isAtSE(me, node)) {
                    
                }
                initResizeX = finalX;
                initResizeY = finalY;
                
            }
        }); // MOUSE DRAGGED: RESIZE
        
        // MOUSE RELEASED
        node.addEventFilter(MouseEvent.MOUSE_RELEASED, (final MouseEvent me) -> {
            node.setCursor(Cursor.DEFAULT);
            isDraggingSE = false;
            isDraggingBottom = false;
            isDraggingRight = false;
        });
        
        // MOUSE OVER: CHANGE THE CURSOR
        node.addEventFilter(MouseEvent.MOUSE_MOVED, (final MouseEvent me) -> {
            if(dataManager.getAppState()==AppState.RESIZE_STATE) {
                if(isAtSE(me, node))
                    node.setCursor(Cursor.SE_RESIZE);
                else if(isAtSW(me, node))
                    node.setCursor(Cursor.SW_RESIZE);
                else if(isAtNE(me, node))
                    node.setCursor(Cursor.NE_RESIZE);
                else if(isAtNW(me, node))
                    node.setCursor(Cursor.NW_RESIZE);
                else if(isAtTop(me, node) || isAtBottom(me, node))
                    node.setCursor(Cursor.V_RESIZE);
                else if(isAtLeft(me, node) || isAtRight(me, node)) 
                    node.setCursor(Cursor.H_RESIZE);
                else
                    node.setCursor(Cursor.DEFAULT);
            }
        });
    } // END OF SETUP RESIZABLE
    
    private void resizeNodeHeight(double finalY, VBox node) {
        double newHeight = finalY - initResizeY + node.getMinHeight();
        node.setMinHeight(newHeight);
    }
    private void resizeNodeWidth(double finalX, VBox node) {
        double newWidth = finalX - initResizeX + node.getMinWidth();
        node.setMinWidth(newWidth);
    }
    
    private boolean isAtTop(MouseEvent me, VBox vBox) {
        return (me.getY() < (Constants.RESIZE_MARGIN) && me.getY() > (0-Constants.RESIZE_MARGIN));
    }
    private boolean isAtBottom(MouseEvent me, VBox vBox) {
        return (me.getY() > (vBox.getHeight() - Constants.RESIZE_MARGIN) && me.getY() < (vBox.getHeight() + Constants.RESIZE_MARGIN));
    }
    private boolean isAtLeft(MouseEvent me, VBox vBox) {
        return (me.getX() < (Constants.RESIZE_MARGIN));
    }
    private boolean isAtRight(MouseEvent me, VBox vBox) {
        return (me.getX() > (vBox.getWidth() - Constants.RESIZE_MARGIN) && me.getX() < (vBox.getWidth() + Constants.RESIZE_MARGIN));
    }
    private boolean isAtSE(MouseEvent me, VBox vBox) {
        return (isAtBottom(me, vBox) && isAtRight(me, vBox));
    }
    private boolean isAtSW(MouseEvent me, VBox vBox) {
        return (isAtBottom(me, vBox) && isAtLeft(me, vBox));
    }
    private boolean isAtNW(MouseEvent me, VBox vBox) {
        return (isAtTop(me, vBox) && isAtLeft(me, vBox));
    }
    private boolean isAtNE(MouseEvent me, VBox vBox) {
        return (isAtTop(me, vBox) && isAtRight(me, vBox));
    }

    /**
     * 
     * @param ui AddClassUI/InterfaceUI corresponding to the diagram selected.
     * @param tf package name input text field
     * @param packageName package name
     */
    public void handleEditPackageName(VBox ui, TextField tf, String packageName) {
        String name = packageName.trim();

            // REMOVE OLD PACKAGE NAME FROM DATA MANAGER
            if(ui instanceof AddClassUI) {
                dataManager.getPackageNames().remove(((AddClassUI)ui).getPackageName());
            }
            if(name != null) {
                // ADD PACKAGE NAME TO DATA MANAGER                
                dataManager.addPackage(name);
                ((AddClassUI)ui).setPackageName(name);
            }
    }
    
    public void handleEditClassName(VBox componentUI, TextField tf, String className) {
        //TODO: Should not allow entry of package name if its package/class combo already exists in design
       String name = className.trim();
       
       //FIXME: 
       if(dataManager.getClassNames().contains(name)) {
            class_exist_alert.show();
            // CLEAR TEXTFIELD
            tf.clear();
            
        } else {
            // REMOVE OLD CLASS NAME FROM DATA MANAGER
            if(componentUI instanceof AddClassUI) {
                dataManager.getClassNames().remove(((AddClassUI)componentUI).getClassName());

                if(!name.equals("")) {
                    // ADD CLASS NAME TO DATA MANAGER
                    dataManager.addClassName(name);
                    ((AddClassUI)componentUI).setClassName(name);
                }
            } else {
                dataManager.getClassNames().remove(((AddInterfaceUI)componentUI).getInterfaceName());

                if(!name.equals("")) {
                    // ADD CLASS NAME TO DATA MANAGER
                    dataManager.addClassName(name);
                    ((AddInterfaceUI)componentUI).setInterfaceName(name);
                }
            }
        }
    }
    
    /**
     * Un-highlights.
     * @param x mouse x
     * @param y mouse y
     */
    public void handleWorkPanePressed(double x, double y, boolean isControlDown) {
        // IF THE SELECTED NODE IS NULL, DESELECT THE CURRENT SELECTED SHAPE
        if(!isNodeClicked(x, y) && isControlDown) {
            unHighlight();
            dataManager.setSelectedNode(null);
            
            Workspace workspace = (Workspace)app.getWorkspaceComponent();
            workspace.getComponentToolbarPane().setDisable(true);
        }
        
    }
    private void unHighlight() {
        if(dataManager.getSelectedNode()!=null)
            dataManager.getSelectedNode().setEffect(null);
    }
    
    public boolean isNodeClicked(double x, double y) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        ObservableList<Node> nodes = workspace.getWorkPane().getChildren();
        boolean isClicked = false;
        for(int i = nodes.size()-1; i > 0; i--) {
            if(nodes.get(i).contains(x,y)) {
                isClicked = true;
                break;
            }
        }
	return isClicked;
    }

    /**
     * Adds a variable to the componentUI and diagram.
     * @param componentUI to whom the variable is added.
     */
    public void handleAddVariable(VBox componentUI) {
        Variable newVar = new Variable(componentUI);
        setupSelectable(newVar); // SETUP SELECTABLE
        if(componentUI instanceof AddClassUI) {
            ((AddClassUI)componentUI).getVariableContainer().getChildren().add(newVar);
            ((AddClassUI)componentUI).addVariableToList(newVar);
        } else {
           ((AddInterfaceUI)componentUI).getVariableContainer().getChildren().add(newVar);
           ((AddInterfaceUI)componentUI).addVariableToList(newVar); 
        }
    }

    /**
     * Removes the variable from the component UI and the diagram.
     * @param componentUI 
     */
    public void handleRemoveVariable(VBox componentUI) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        if(dataManager.getSelectedNode() instanceof Variable) {
            Variable variable = (Variable)dataManager.getSelectedNode();
            if(componentUI instanceof AddClassUI) {
                // REMOVE VARIABLE FROM COMPONENT UI
                ((AddClassUI)componentUI).getVariableContainer().getChildren().remove(variable);
                // REMOVE VARIABLE FROM DIAGRAM
                ((AddClassUI)componentUI).getVariables().remove(variable);
                
                String type = variable.getType().getText();
                // REMOVE ITS DIAGRAM IF ITS TYPE IS NOT PRIMITIVE
                if(!type.equals("") && !dataManager.isPrimitiveType(type)) {
                    System.out.println("remove ? " + ((AddClassUI)componentUI).getListofConnectedDiagrams().remove(type));
                    // IF DIAGRAM IS NOT IN USE, REMOVE IT FROM WORKSPACE AS WELL AS EXTRA DIAGRAM
                    dataManager.removeExtraDiagram(type);
                }
                
            } else {
                // REMOVE VARIABLE FROM COMPONENT UI
                ((AddInterfaceUI)componentUI).getVariableContainer().getChildren().remove(variable);
                // REMOVE VARIABLE FROM DIAGRAM
                ((AddInterfaceUI)componentUI).getVariables().remove(variable);
                
                String type = variable.getType().getText();
                // REMOVE ITS DIAGRAM IF ITS TYPE IS NOT PRIMITIVE
                if(!type.equals("") && !dataManager.isPrimitiveType(type)) {
                    System.out.println("remove ? " + ((AddInterfaceUI)componentUI).getListofConnectedDiagrams().remove(type));
                    dataManager.removeExtraDiagram(type);
                }
            }
            
            
        }
    }

    public void handleAddMethod(VBox componentUI) {
        Method method = new Method(componentUI);
        setupSelectable(method);
        if(componentUI instanceof AddClassUI) {
            ((AddClassUI)componentUI).getMethodContainer().getChildren().add(method);
            ((AddClassUI)componentUI).addMethodToList(method);
        } else {
            ((AddInterfaceUI)componentUI).getMethodContainer().getChildren().add(method);
            ((AddInterfaceUI)componentUI).addMethodToList(method);
        }
    }

    public void handleRemoveMethod(VBox componentUI) {
        // TODO: REMOVE LABEL IN DIAGRAM
        if(dataManager.getSelectedNode() instanceof Method) {
            Method method = (Method)dataManager.getSelectedNode();
            if(componentUI instanceof AddClassUI) {
                ((AddClassUI)componentUI).getMethodContainer().getChildren().remove(method);
                ((AddClassUI)componentUI).getMethods().remove(method);
                String type = method.getReturnType().getText();
                // REMOVE ITS DIAGRAM IF ITS TYPE IS NOT PRIMITIVE
                if(!type.equals("") && !dataManager.isPrimitiveType(type)) {
                    dataManager.removeExtraDiagram(type);
                    System.out.println("remove ? " + ((AddClassUI)componentUI).getListofConnectedDiagrams().remove(type));
                }

            } else {
                ((AddInterfaceUI)componentUI).getMethodContainer().getChildren().remove(method);
                ((AddInterfaceUI)componentUI).getMethods().remove(method);
                String type = method.getReturnType().getText();
                // REMOVE ITS DIAGRAM IF ITS TYPE IS NOT PRIMITIVE
                if(!type.equals("") && !dataManager.isPrimitiveType(type)) {
                    dataManager.removeExtraDiagram(type);
                    System.out.println("remove ? " + ((AddInterfaceUI)componentUI).getListofConnectedDiagrams().remove(type));
                }

            }
        }
    }

    public void handleSetParent(ParentComboBox parent_cb, VBox componentUI) {
        String parentName = (String)(parent_cb.getSelectionModel().getSelectedItem());
        if((componentUI instanceof AddClassUI) && parentName !=null) {
            ((AddClassUI)componentUI).setParent(parentName);
        }
    }
    
    //TODO: 
    public void handleKeyPressed(KeyEvent e) {
        if(dataManager.getSelectedNode() instanceof SplittableLine) {
            SplittableLine connector = ((SplittableLine)dataManager.getSelectedNode());
            double startX = connector.startXProperty().get();
            double endX = connector.endXProperty().get();
            double startY = connector.startYProperty().get();
            double endY = connector.endYProperty().get();
            
            switch(e.getCode()) {
                case S:     // split connector
                    connector.split(startX, startY, endX, endY);
                    break;
                    
                case M:     // merge 
                    connector.merge(startX, startY, endX, endY);
                    break;
                    
                default:    // do nothing
                    
                    break;
            }
        }
    }

    /**
     * Updates the display of the UML diagram.
     * @param componentUI the componentUI whose diagram is to be updated.
     */
    public void handleConfirmDesign(VBox componentUI) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        System.out.println("Refreshing design..");
        ArrayList<Variable> variables;
        ArrayList<Method> methods;
        DiagramGenerator diagram;
        ArrayList<String> implementedInterfaces;      // for storing implemented interfaces to display on canvas
        String extendedParent  ="";
        String componentUIName ="";
        
        // REFRESH AVAILABLE PARENT AND INTERFACES COMBO BOXES
        refreshParentAndInterfaces(dataManager);
        
        // OBTAIN COMPONENT UI INFO
        if(componentUI instanceof AddClassUI) {
            AddClassUI classUI = (AddClassUI)componentUI;
            variables = classUI.getVariables();
            methods = classUI.getMethods();
            diagram = classUI.getDiagram();
            extendedParent = classUI.getparent();
            implementedInterfaces = classUI.getImplementedInterfaces();//getInterfaceComboBox().getImplementedInterfaces();
            componentUIName = classUI.getClassName();
        } else {
            AddInterfaceUI interfaceUI = (AddInterfaceUI)componentUI;
            variables = interfaceUI.getVariables();
            methods = interfaceUI.getMethods();
            diagram = interfaceUI.getDiagram();
            implementedInterfaces = interfaceUI.getImplementedInterfaces();//getInterfaceComboBox().getImplementedInterfaces();
            componentUIName = interfaceUI.getInterfaceName();
        }
        
        // REFRESH VARIABLES IN THE DIAGRAM
        refreshVariableLabels(diagram, variables);
        // REFRESH METHODS IN THE DIAGRAM
        refreshMethodLabels(diagram, methods, componentUI);
        
        // UPDATE RELATIONSHIPS
        if(componentUI instanceof AddClassUI) {
            if(!extendedParent.equals("")) {
                // CONNECT THE DIAGRAM IF THE DIAGRAM EXISTS
                DiagramGenerator parentDiagram;
                if(isDiagramExist(extendedParent)) {
                    parentDiagram = dataManager.getDiagram(extendedParent);

                } else {
                    // CREATE NEW DIAGRAM IF THE EXPECTED DIAGRAM DOES NOT EXIST
                    parentDiagram = new DiagramGenerator(extendedParent);
                    addDiagramToCanvas(parentDiagram);
                }
                // CONNECT PARENT AND CHILD
                ParentConnectorHead parentConnectorHead = new ParentConnectorHead(parentDiagram, diagram);
                Group parentConnector = ConnectorFactory.getConnector(parentConnectorHead, this);
                // ADD CONNECTOR TO DIAGRAM IF IT DOES NOT EXIST
                if(!parentDiagram.getConnectors().contains(parentConnector) || !diagram.getConnectors().contains(parentConnector)) {
                    // ADD THIS CONNECTOR TO THE DIAGRAM FOR THE RECORD
                    parentDiagram.addConnector(parentConnector);
                    diagram.addConnector(parentConnector);

                    workspace.getWorkPane().getChildren().add(parentConnector);
                    System.out.println(extendedParent + " added to canvas.");
                }
            }
        }
        for(String s : implementedInterfaces) {
            if(!(s.replaceAll(" ", "").equals(""))) {
                // FIXME
                DiagramGenerator interfaceDiagram;
                if(isDiagramExist(s)) {
                    interfaceDiagram = dataManager.getDiagram(s);
                } else {
                    interfaceDiagram = new DiagramGenerator(s);
                    addDiagramToCanvas(interfaceDiagram);
                }
                // CONNECT INTERFACE AND CHILD
                ParentConnectorHead parentConnectorHead = new ParentConnectorHead(interfaceDiagram, diagram);
                Group parentConnector = ConnectorFactory.getInterfaceConnector(parentConnectorHead, this);
                // ADD CONNECTOR TO DIAGRAM IF IT DOES NOT EXIST
                if(!interfaceDiagram.getConnectors().contains(parentConnector) && !diagram.getConnectors().contains(parentConnector)) {
                    // ADD THIS CONNECTOR TO THE DIAGRAM FOR THE RECORD
                    interfaceDiagram.addConnector(parentConnector);
                    diagram.addConnector(parentConnector);
                    workspace.getWorkPane().getChildren().add(parentConnector);

                    System.out.println(s + " added to canvas.");
                }
            }
        }
        // TODO:
        
        for(VBox v : dataManager.getDiagrams()) {
            ArrayList<String> connectedDiagrams;
                // GET CONNECTED DIAGRAMS
                if(componentUI instanceof AddClassUI) {
                    connectedDiagrams = ((AddClassUI)componentUI).getListofConnectedDiagrams();
                } else {
                    connectedDiagrams = ((AddInterfaceUI)componentUI).getListofConnectedDiagrams();
                }
                toolController.removeUnusedDiagrams(connectedDiagrams);
        }
//        toolController.cleanUnusedDiagram(workspace, dataManager);
        System.out.println("Finished refreshing design..");
    } // END OF HANDLE CONFIRM DESIGN

    /**
     * 
     * @param method whose arguments are to be parsed as a UML string format.
     * @return UML string representation of arguments in the method.
     */
    public String getArgs(Method method) {
        String argsAsUMLLabel = "";
        ArrayList<TextField> argTypes = method.getArgTypes();        
        for(int i = 0; i < argTypes.size(); i++) {
            if(i == 0) {
                if(!argTypes.get(i).getText().trim().equals("")) {
                    argsAsUMLLabel = argsAsUMLLabel + ("arg" + (i+1) + " : " + argTypes.get(i).getText()).trim();
                }
            } else {
                if(!argTypes.get(i).getText().trim().equals("")) {
                    argsAsUMLLabel = argsAsUMLLabel + (", " + "arg" + (i+1) + " : " + argTypes.get(i).getText()).trim();
                }
            }
        }
        return argsAsUMLLabel;
    }
    
    /**
     * Updates/refreshes the variable labels in the diagram
     * @param diagram
     * @param variables 
     */
    public void refreshVariableLabels(DiagramGenerator diagram, ArrayList<Variable> variables) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        //////////////////////////////////////////////////
        // UPDATE UML'S VARIABLE LABELS:                //
        //      - CLEAR THE DIAGRAM'S VARIABLE CONTAINER//
        //      - ADD UP-TO-DATE INFO                   //
        //////////////////////////////////////////////////
        System.out.println("Refreshing variables..");
        diagram.getVariableBox().getChildren().clear();
        for(Variable variable : variables) {
            
            String name         = (variable.getName().getText()!=null)?variable.getName().getText():"";
            String type         = (variable.getType().getText()!=null)?variable.getType().getText():"";
            String access       = "";
            if(variable.getAccess().getValue()!=null) {
                if(variable.getAccess().getValue().toString().equals(Constants.PUBLIC)) {
                    access += "+";
                } else if(variable.getAccess().getValue().toString().equals(Constants.PRIVATE)) {
                    access += "-";
                }  else if(variable.getAccess().getValue().toString().equals(Constants.PROTECTED)) {
                    access += "#";
                } 
            }
            
            String staticStatus = "";
            if(variable.getStaticStatus().selectedProperty().get()) {
                staticStatus += "$";
            }
            
            // In the form of (access? static? argName: argType )
            String variable_str = access + "" + staticStatus + "" + name + " : " + type;
            
            // CLEAN UP FORMAT
            variable_str = variable_str.trim();
            variable_str = variable_str.replaceAll("\\s+", " ");
            
            if(variable_str.replaceAll("\\s+", "").equals(":")) {
                // IF NOTHING IS ADDED, DONT ADD THIS LABEL TO THE DIAGRAM
            } else {
                // ADD TO DIAGRAM
                Label variable_lb = new Label(variable_str);
                diagram.getVariableBox().getChildren().add(variable_lb);
            }
            
            // ADD AGGREGATION CONNECTOR
            for(Variable var : variables) {
                if(!type.equals("") && !dataManager.isPrimitiveType(type)) {
                    DiagramGenerator aggregateDiagram;
                    if(isDiagramExist(type)) {
                        aggregateDiagram = dataManager.getDiagram(type);
                    } else {
                        aggregateDiagram = new DiagramGenerator(type);
                        addDiagramToCanvas(aggregateDiagram);
                    }
                     // CONNECT INTERFACE AND CHILD
                    AggregationConnector aggregationConnectorHead = new AggregationConnector(aggregateDiagram, diagram);
                    Group aggregationConnector = ConnectorFactory.getConnector(aggregationConnectorHead, this);

                    if(!aggregateDiagram.getConnectors().contains(aggregationConnector) && !diagram.getConnectors().contains(aggregationConnector)) {
                        // ADD THIS CONNECTOR TO THE DIAGRAM FOR THE RECORD
                        aggregateDiagram.addConnector(aggregationConnector);
                        diagram.addConnector(aggregationConnector);
                        // DISPLAY THE CONNECTOR ON SCREEN
                        workspace.getWorkPane().getChildren().add(aggregationConnector);
                    }
                }

            }
            
        } 
        System.out.println("Finished refreshing variables..");
    } // END OF UPDATING VARIABLE LABELS

    /**
     * Updates/refreshes the method labels in the diagrams.
     * @param diagram 
     * @param methods
     * @param componentUI 
     */
    public void refreshMethodLabels(DiagramGenerator diagram, ArrayList<Method> methods, VBox componentUI) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        //////////////////////////////////////////////////
        // UPDATE UML'S METHOD LABELS:                  //
        //      - CLEAR THE DIAGRAM'S METHOD CONTAINER  //
        //      - ADD UP-TO-DATE INFO                   //
        //////////////////////////////////////////////////
        System.out.println("Refreshing methods..");
        int numberOfAbstractMethods = 0;    // TO KEEP TRACK OF ABSTRACT METHODS IN ORDER TO DETERMINE ABSTRACT CLASS
        diagram.getMethoBox().getChildren().clear();
        for(Method method : methods) {
            
            String name         = method.getName().getText();
            String returnType   = method.getReturnType().getText();
            String access       = "";
            if(method.getAccess().getValue()!=null) {
                if(method.getAccess().getValue().toString().equals(Constants.PUBLIC)) {
                    access += "+";
                } else if(method.getAccess().getValue().toString().equals(Constants.PRIVATE)) {
                    access += "-";
                }  else if(method.getAccess().getValue().toString().equals(Constants.PROTECTED)) {
                    access += "#";
                } 
            }
            
            if(method.getAbstractStatus().selectedProperty().get() && (componentUI instanceof AddClassUI)) {
                numberOfAbstractMethods++;
            }
            
            String staticStatus = "";
            if(method.getStaticStatus().selectedProperty().get()) {
                staticStatus += "$";
            }
            
            // ]n the form of (access? static? (argName: argType) : returnType )
            // TODO: ADD ARGUMENTS
            String method_str = access + staticStatus + "" + name + "("+ getArgs(method) + ") : " + returnType;
            
            // CLEAN UP FORMAT
            method_str = method_str.trim();
            method_str = method_str.replaceAll("\\s+", " ");
            
            if(method_str.replaceAll("\\s+", "").equals("():")) {
                // IF NOTHING IS ADDED, DONT ADD THIS LABEL TO THE DIAGRAM
            } else {
                // ADD TO DIAGRAM
                Label method_lb = new Label(method_str);
                diagram.getMethoBox().getChildren().add(method_lb);
            }

        } // END OF UPDATING METHOD LABELS
        
        // ADDRESS ABSTRACT CLASS
        if(numberOfAbstractMethods > 0 && (componentUI instanceof AddClassUI)) {
            // UPDATE THE CLASS BOX TO ABSTRACT
            diagram.getClassBox().getChildren().clear();
            diagram.getClassBox().getChildren().add(ABSTRACT_HEADER);
            diagram.getClassBox().getChildren().add(diagram.getDiagramClassNameLabel());

        } else {
            // REFRESH OTHERWISE, FOR CLASS ONLY, INTERFACE DO NOT NEED TO CHANGE
            if(componentUI instanceof AddClassUI) {
                diagram.getClassBox().getChildren().clear();
                diagram.getClassBox().getChildren().add(diagram.getDiagramClassNameLabel());
            }
        }
        
        // TODO: ADD USES CONNECTOR
//        for(Method method : methods) {
//            String type = method.getReturnType().getText();
//            if(!(dataManager.isPrimitiveType(type))) {
//                DiagramGenerator usesDiagram;
//                if(isDiagramExist(type)) {
//                    usesDiagram = dataManager.getDiagram(type);
//                } else {
//                    usesDiagram = new DiagramGenerator(type);
//                    addDiagramToCanvas(diagram);
//                }
//                
//                UsesConnector usesConnectorHead = new UsesConnector(usesDiagram, diagram);
//                Group usesConnector = ConnectorFactory.getConnector(usesConnectorHead, this);
//                if(!usesDiagram.getConnectors().contains(usesConnector) && !diagram.getConnectors().contains(usesConnector)) {
//                    // ADD THIS CONNECTOR TO THE DIAGRAM FOR THE RECORD
//                    usesDiagram.addConnector(usesConnector);
//                    diagram.addConnector(usesConnector);
//                    workspace.getWorkPane().getChildren().add(usesConnector);
//                }
//            }
//        }
        for(Method method : methods) {
                String type = (method.getReturnType().getText()!=null)?method.getReturnType().getText():"";
                if(!type.equals("") && !dataManager.isPrimitiveType(type)) {  // SHOULD NOT ADD A NO NAMER API
                    DiagramGenerator usesDiagram;

                    if(isDiagramExist(type)) {
                        usesDiagram = dataManager.getDiagram(type);
                    } else {
                        usesDiagram = new DiagramGenerator(type);
                        addDiagramToCanvas(usesDiagram);
                    }
                    // CONNECT INTERFACE AND CHILD
                    UsesConnector usesConnectorHead = new UsesConnector(usesDiagram, diagram);
                    Group parentConnector = ConnectorFactory.getConnector(usesConnectorHead, this);

                    if(!usesDiagram.getConnectors().contains(parentConnector) && !diagram.getConnectors().contains(parentConnector)) {
                        // ADD THIS CONNECTOR TO THE DIAGRAM FOR THE RECORD
                        usesDiagram.addConnector(parentConnector);
                        diagram.addConnector(parentConnector);
                        // DISPLAY THE CONNECTOR ON SCREEN
                        workspace.getWorkPane().getChildren().add(parentConnector);
                    }
                }

            }
        
        System.out.println("Finished refreshing methods..");
    } // END OF UPDATE METHOD LABELS
    
    public void addDiagramToCanvas(DiagramGenerator diagram) {
        
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        setupDraggable(diagram);
        workspace.getWorkPane().getChildren().add(diagram);
        dataManager.addExtraDiagram(diagram);
        diagram.setIsAddedToCanvas(true);
    }
    
    public void handleAddExternalClass(AddClassUI classUI, TextField tf) {
        String externalClass = tf.getText();
        
        if(tf.getText().equals("")) {
            Dialog d = new Alert(Alert.AlertType.WARNING, "Class name cannot be blank", ButtonType.OK);
            d.show();
        } else {
            // DO NOT ADD WHAT'S ALREADY EXIST
            boolean exist = false;
            for(Object o : classUI.getParentComboBox().getItems()) {
                if(o.equals(externalClass)) {
                    exist = true;
                    break;
                }
            }
            if(!exist) {
                classUI.getParentComboBox().getItems().add(externalClass);
                dataManager.addExternalClass(externalClass);
                
                Dialog d = new Alert(Alert.AlertType.INFORMATION, "Class Added", ButtonType.OK);
                d.show();
                tf.clear();  // CLEAR THE TEXTFIELD AFTER THE INTERFACE IS ADDED
            } else {
                Dialog d = new Alert(Alert.AlertType.WARNING, "Class Already Exist.", ButtonType.OK);
                d.show();
                tf.selectAll();
            }
        }
    }
    /**
     * Adds external interface to interface combo box.
     * @param componentUI
     * @param externalInterface 
     */
    public void handleAddExternalInterface(VBox componentUI, TextField externalInterface) {
        InterfaceComboBox interfaceComboBox;
        if(componentUI instanceof AddClassUI) {
            interfaceComboBox = ((AddClassUI)componentUI).getInterfaceComboBox();
        } else {
            interfaceComboBox = ((AddInterfaceUI)componentUI).getInterfaceComboBox();
        }
        String ex_interface = externalInterface.getText();
        if(ex_interface.equals("")) {
            Dialog d = new Alert(Alert.AlertType.WARNING, "Interface name cannot be blank", ButtonType.OK);
            d.show();
        } else {
            // DO NOT ADD WHAT'S ALREADY EXIST
            boolean exist = false;
            for(Object o : interfaceComboBox.getItems()) {
                if(o.equals(ex_interface)) {
                    exist = true;
                    break;
                }
            }
            if(!exist) {
                interfaceComboBox.getItems().add(ex_interface);
                dataManager.addExternalInterface(ex_interface);
                Dialog d = new Alert(Alert.AlertType.INFORMATION, "Interface Added", ButtonType.OK);
                d.show();
                externalInterface.clear();  // CLEAR THE TEXTFIELD AFTER THE INTERFACE IS ADDED
            } else {
                Dialog d = new Alert(Alert.AlertType.WARNING, "Interface Already Exist.", ButtonType.OK);
                d.show();
                externalInterface.selectAll();
            }
        }
        
    }

    public boolean isDiagramExist(String name) {
        boolean exist = false;

        for(VBox d : dataManager.getDiagrams()) {
            String name1 = ((DiagramGenerator)d).getDiagramClassNameLabel().getText();
            if(name.equals(name1)) {
                exist = true;
                break;
            }
        }
        if(!exist) {
            for(VBox d : dataManager.getExtraDiagrams()) {
                String name2 = ((DiagramGenerator)d).getDiagramClassNameLabel().getText();
                if(name.equals(name2)) {
                    exist = true;
                    break;
                }
            }
        }
        return exist;
    }
 
}