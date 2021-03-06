package jClassDesigner.gui;

import jClassDesigner.Constants;
import jClassDesigner.controller.ComponentController;
import jClassDesigner.data.DataManager;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import saf.AppTemplate;

/**
 *
 * @author Jia Sheng Ma
 * @version 1.1
 */
public class AddClassUI extends VBox{
    
    AppTemplate app;
    Workspace workspace;
    
    HBox class_hbox;
    private final Label className_lb = new Label("Class Name:");
    TextField className_tf;
    
    HBox package_hbox;
    private final Label package_lb = new Label("Package:");
    TextField package_tf;
    
    HBox parent_hbox;
    private final Label parent_lb = new Label("Parent:");
    ParentComboBox parent_cb;
    
    HBox interface_hbox;
    private final Label interface_lb = new Label("Interface:");
    InterfaceComboBox interface_cb;
    ArrayList<String> implementedInterfaces;
    
    private HBox addInterface_hbox;

    private Button addClass_btn;
    private Button addInterface_btn;
    private TextField external_interface;
    
    // VARIABLES
    VBox variableContainer;
    HBox variableLabels_hbox;
    HBox variableHeader_hbox;
    private final Label variables_lb = new Label("Variables:");
    Button variablesAdd_btn;
    Button variablesRemove_btn;    
    ScrollPane variables_scrollPane;
    
    // METHODS
    VBox methodContainer;
    HBox methodLabels_hbox;
    HBox methodHeader_hbox;
    private final Label methods_lb = new Label("Methods:");
    Button methodsAdd_btn;
    Button methodsRemove_btn;
    ScrollPane methods_scrollPane;
    private final Label varName_lb = new Label("Name");
    private final Label methodName_lb = new Label("Name");
    private final Label type_lb = new Label("Type");
    private final Label varStatic_lb = new Label("Static");
    private final Label methodStatic_lb = new Label("Static");
    private final Label varAccess_lb = new Label("Access");
    private final Label methodAccess_lb = new Label("Access");
    private final Label return_lb = new Label("Return");
    private final Label abstract_lb = new Label("Abstract");
    private final Label argument_lb = new Label("Arguments");
    
    Button confirmation_btn;
    
    DiagramGenerator diagram;
    Variable variable;
    Method method;

    // Lists of variables and methods
    ArrayList<Variable> variables;
    ArrayList<Method> methods;
    
    // LIST OF API REFERENCES
    ArrayList<String> apiReferences;
    
    // LIST OF NODE CONNECTORS(SPLITTABLE LINES)
    ArrayList<SplittableLine> nodeConnectors;
    
    
    String className;
    String packageName;
    String parentName;
    
    private boolean isClassAbstract;
    private boolean hasParent;
    
    public AddClassUI(AppTemplate app) {
        parentName = "";
        this.app = app;
        isClassAbstract = false;
        hasParent = false;
        diagram = new DiagramGenerator();
        variables = new ArrayList<>();
        methods = new ArrayList<>();
        implementedInterfaces = new ArrayList<>();
        apiReferences = new ArrayList<>();
        nodeConnectors = new ArrayList<>();
        // SET UP DRAGGABLE
        ComponentController componentController = new ComponentController(app);
        componentController.setupDraggable(diagram);
        componentController.setupResizable(diagram);
        
        setUpLayout();
        setupHandler();
        initStyle();

    }

    public void addImplementedInterface(String implementedInterface) {
        implementedInterfaces.add(implementedInterface);
    }
    public ArrayList<String> getImplementedInterfaces() {
        return implementedInterfaces;
    }

    public boolean getHasParent() {
        if(parent_cb.getSelectionModel().getSelectedItem() != null && !(parent_cb.getValue().toString().equals(""))) {
            hasParent = true;
        } else {
            hasParent = false;
        }
        return hasParent;
    }
    
//    public AddClassUI() {
//        isClassAbstract = false;
//        diagram = new DiagramGenerator();
//        variables = new ArrayList<>();
//        methods = new ArrayList<>();
//        apiReferences = new ArrayList<>();
//        nodeConnectors = new ArrayList<>();
//        setUpLayout();
//        setupHandler();
//    }
    
    public void setUpLayout() {
        // CLASS NAME
        class_hbox = new HBox();
        className_tf = new TextField();
        className_tf.setPromptText("Class Name");
        class_hbox.getChildren().addAll(className_lb, className_tf);
        this.getChildren().add(class_hbox);        
        className = className_tf.getText();
        
        // PACKAGE
        package_hbox = new HBox();
        package_tf = new TextField();
        package_tf.setPromptText("Package Name");
        package_hbox.getChildren().addAll(package_lb, package_tf);
        this.getChildren().add(package_hbox);
        packageName = package_tf.getText();
        
        // PARENT
        parent_hbox = new HBox();
        parent_cb = new ParentComboBox(this);
        parent_hbox.getChildren().addAll(parent_lb, parent_cb);
        this.getChildren().add(parent_hbox);
        
        // INTERFACE
        interface_hbox = new HBox();
        interface_cb = new InterfaceComboBox(this);
        interface_hbox.getChildren().addAll(interface_lb, interface_cb);
        this.getChildren().add(interface_hbox);
        
        addInterface_hbox = new HBox();
        addClass_btn = new Button("+Class");
        addInterface_btn = new Button("+Interface");
        external_interface = new TextField();
        external_interface.setPromptText("Add External Class/Interface");
        addInterface_hbox.getChildren().addAll(addClass_btn, addInterface_btn, external_interface);
        this.getChildren().add(addInterface_hbox);
        
        //VARIABLE
        variableContainer = new VBox();

            // VARIABLE HEADER
        variableHeader_hbox = new HBox();
        variablesAdd_btn = new Button("+");
        variablesRemove_btn = new Button("-");
        variableHeader_hbox.getChildren().addAll(variables_lb, variablesAdd_btn, variablesRemove_btn);
        variableContainer.getChildren().add(variableHeader_hbox);

            // VARIABLE LABELS
        variableLabels_hbox = new HBox();
        variableLabels_hbox.getChildren().addAll(varName_lb, type_lb, varStatic_lb, varAccess_lb);
        variableContainer.getChildren().add(variableLabels_hbox);
        
            // VARIABLE INPUTS
//        variable = new Variable(this);
//        variableContainer.getChildren().add(variable);
//        addVariableToList(variable);
        
        variables_scrollPane = new ScrollPane();
        variables_scrollPane.setContent(variableContainer);
        this.getChildren().add(variables_scrollPane);
        
        // METHOD
        methodContainer = new VBox();
        methodContainer.setPrefWidth(1.36*(Constants.COMPONENT_TOOLBAR_WIDTH));
            // METHOD HEADER
        methodHeader_hbox = new HBox();
        methodsAdd_btn = new Button("+");
        methodsRemove_btn = new Button("-"); 
        methodHeader_hbox.getChildren().addAll(methods_lb, methodsAdd_btn, methodsRemove_btn);
        methodContainer.getChildren().add(methodHeader_hbox);
        
            // METHOD LABELS
        methodLabels_hbox = new HBox();
        methodLabels_hbox.getChildren().addAll(methodName_lb, return_lb, methodStatic_lb, abstract_lb, 
                                                methodAccess_lb, argument_lb);        
        
        
        methodContainer.getChildren().add(methodLabels_hbox);
        
            // METHOD INPUTS
//        method = new Method(this);
//        methodContainer.getChildren().add(method);
//        addMethodToList(method);
        
        methods_scrollPane = new ScrollPane();
        methods_scrollPane.setContent(methodContainer);
        this.getChildren().add(methods_scrollPane);
        
        // CONFIRMATION BUTTON
        confirmation_btn = new Button("Confirm Design/Refresh");
        confirmation_btn.setDisable(true);
        this.getChildren().add(confirmation_btn);
        
    }
    
    public void setupHandler() {
        ComponentController componentController = new ComponentController(app);

        className_tf.addEventHandler(KeyEvent.ANY, e->{
            diagram.editClassName(className_tf.textProperty().getValue());
            confirmation_btn.setDisable(false);
        });
        
        className_tf.setOnAction(e -> {
            String className = className_tf.textProperty().getValue().trim();
            diagram.editClassName(className_tf.getText());
            componentController.handleEditClassName(this, className_tf, className);
            confirmation_btn.setDisable(false);
        });
        
        package_tf.setOnAction(e -> {
            String packageName = package_tf.getText().trim();
            componentController.handleEditPackageName(this, package_tf, packageName);
            confirmation_btn.setDisable(false);
        });
        
        parent_cb.setOnAction(e -> {
            componentController.handleSetParent(parent_cb, this);
            confirmation_btn.setDisable(false);
        });
        addClass_btn.setOnAction(e ->{
            componentController.handleAddExternalClass(this, external_interface);
            confirmation_btn.setDisable(false);
        });
        addInterface_btn.setOnAction(e -> {
            componentController.handleAddExternalInterface(this, external_interface);
            confirmation_btn.setDisable(false);
        });
        
        variablesAdd_btn.setOnAction(e -> {
            componentController.handleAddVariable(this);
            confirmation_btn.setDisable(false);
        });
        variablesRemove_btn.setOnAction(e -> {
            componentController.handleRemoveVariable(this);
            confirmation_btn.setDisable(false);
        });
        
        methodsAdd_btn.setOnAction(e -> {
            componentController.handleAddMethod(this);
            confirmation_btn.setDisable(false);
        });
        methodsRemove_btn.setOnAction(e -> {
            componentController.handleRemoveMethod(this);
            confirmation_btn.setDisable(false);
        });
        confirmation_btn.setOnAction(e -> {
            componentController.handleConfirmDesign(this);
            confirmation_btn.setDisable(true);
        });
        
    }
    
    public TextField getClassName_tf() {return className_tf;}
    public TextField getPackageName_tf() {return package_tf;}
    
    public void setClassName(String className) {
        this.className = className;
    }        
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public void addVariableToList(Variable v) {
        variables.add(v);
    }
    public void addMethodToList(Method m) {
        methods.add(m);
    }
    public void setParent(String p) {
        // remove current parent-child node connector and add a new one
        parentName = p;
    }
    public String getparent() {
        return parentName;
    }
    
    public String getClassName() {
        return className_tf.getText();
    }
    public String getPackageName() {
        return package_tf.getText();
    }
    public String getParentName() {
        return (parent_cb.getSelectionModel().getSelectedItem()!=null)?(parent_cb.getSelectionModel().getSelectedItem().toString()):"";
    }
    public ParentComboBox getParentComboBox() {
        return parent_cb;
    }
    public InterfaceComboBox getInterfaceComboBox() {
        return interface_cb;
    }
    public ArrayList<Variable> getVariables() {
        return variables;
    }
    public ArrayList<Method> getMethods() {
        return methods;
    }
    public DiagramGenerator getDiagram() {
        return diagram;
    }
    
    public Button getConfirmation_btn() {
        return confirmation_btn;
    }
    
    public void addVariableToClassUI(Variable variable) {
        variableContainer.getChildren().add(variable);
    }
    public VBox getVariableContainer() {
        return variableContainer;
    }
    
    public void addMethodToClassUI(Method method) {
        methodContainer.getChildren().add(method);
    }
    public VBox getMethodContainer() {
        return methodContainer;
    }
    
    /**
     * Refreshes the available parent classes.
     */
    public void refreshParentComboBox() {
        
    }
    
    public void setIsClassAbstract(boolean isAbstract) {
        isClassAbstract = isAbstract;
    }
    
    public boolean getIsClassAbstract() {
        return isClassAbstract;
    }
    
    /**
     * Adds an api reference to this class when a api is being referenced
     * (in variables, primitive type excluded).
     * @param apiReference api reference to add.
     */
    public void addApiReferences(String apiReference) {
        apiReferences.add(apiReference);
    }
    public void removeApiReference(String apiReference) {
        apiReferences.remove(apiReference);
    }
    public ArrayList<String> getApiReferences() {
        return apiReferences;
    }
    
    public void compileListofConnectedDiagrams(ArrayList<String> list) {
        DataManager dataManager = (DataManager)app.getDataComponent();
        list.add(getparent());
        for(String s : implementedInterfaces/*interface_cb.getImplementedInterfaces()*/) {
            list.add(s);
        }
        for(Variable var : variables) {
            String type = var.getType().getText();
            if(!(dataManager.isPrimitiveType(type))) {
                list.add(type);
            }
        }
        for(Method method : methods) {
            String type = method.getReturnType().getText();
            if(!(dataManager.isPrimitiveType(type))) {
                list.add(type);
            }
        }
    }
    /**
     * For other diagrams to check this diagram's connected diagrams
     * to decide whether or not to keep the connected diagram upon
     * this one's destruction.
     * @return list of connected diagrams.
     */
    public ArrayList<String> getListofConnectedDiagrams() {
        ArrayList<String> connectedDiagrams = new ArrayList<>();
        compileListofConnectedDiagrams(connectedDiagrams);
        return connectedDiagrams;
    }
    
    public void initStyle() {
        
        this.setSpacing(Constants.CLASSUI_SPACING);
        this.setPrefSize(Constants.COMPONENT_TOOLBAR_WIDTH, Constants.COMPONENT_TOOLBAR_HEIGHT);
        this.getStyleClass().add(Constants.CLASSUI);
        
        variableHeader_hbox.getStyleClass().add(Constants.HBOXES);
        variableLabels_hbox.getStyleClass().add(Constants.HBOXES);
        methodHeader_hbox.getStyleClass().add(Constants.HBOXES);
        methodLabels_hbox.getStyleClass().add(Constants.HBOXES);
        
        className_lb.setPrefWidth(Constants.HBOXES_WIDTH);
        className_tf.setPrefWidth(Constants.HBOXES_WIDTH);
        package_lb.setPrefWidth(Constants.HBOXES_WIDTH);
        package_tf.setPrefWidth(Constants.HBOXES_WIDTH);
        parent_lb.setPrefWidth(Constants.HBOXES_WIDTH);
        parent_cb.setPrefWidth(Constants.HBOXES_WIDTH);
        interface_lb.setPrefWidth(Constants.HBOXES_WIDTH);
        interface_cb.setPrefWidth(Constants.HBOXES_WIDTH);
        
        variableLabels_hbox.setSpacing(Constants.SPACING);
        variableHeader_hbox.setSpacing(Constants.SPACING);
        variableContainer.getStyleClass().add(Constants.VBOXES);
        variables_scrollPane.setPrefWidth(Constants.VAR_METHOD_SCROLLPANE_WIDTH);
        variables_scrollPane.setPrefHeight(Constants.VARIABLE_BOX_HEIGHT*3);
        
        methodLabels_hbox.setSpacing(Constants.SPACING);
        methodHeader_hbox.setSpacing(Constants.SPACING);
        methodContainer.getStyleClass().add(Constants.VBOXES);
        methods_scrollPane.setPrefWidth(Constants.VAR_METHOD_SCROLLPANE_WIDTH);
        methods_scrollPane.setPrefHeight(Constants.METHOD_BOX_HEIGHT*3);
        
        varAccess_lb.setPrefWidth(Constants.VARIABLE_BOX_WIDTH);
        varName_lb.setPrefWidth(Constants.VARIABLE_BOX_WIDTH);
        varStatic_lb.setPrefWidth(Constants.VARIABLE_BOX_WIDTH);
        methodName_lb.setPrefWidth(Constants.METHOD_BOX_WIDTH);
        type_lb.setPrefWidth(Constants.VARIABLE_BOX_WIDTH);
        methodStatic_lb.setPrefWidth(Constants.METHOD_BOX_WIDTH);
        methodAccess_lb.setPrefWidth(Constants.METHOD_BOX_WIDTH);
        return_lb.setPrefWidth(Constants.METHOD_BOX_WIDTH);
        abstract_lb.setPrefWidth(Constants.METHOD_BOX_WIDTH);
        argument_lb.setPrefWidth(Constants.METHOD_BOX_WIDTH);
    }
    
}
