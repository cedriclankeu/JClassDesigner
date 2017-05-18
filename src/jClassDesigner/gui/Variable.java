package jClassDesigner.gui;

import jClassDesigner.Constants;
import jClassDesigner.controller.ComponentController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Jia Sheng Ma
 */
public class Variable extends HBox{
    
    private Label variableInfo;
    private TextField name;
    private TextField type;
    private CheckBox staticStatus;
    private ComboBox access;
    private final ObservableList<String> accessModifiers 
            = FXCollections.observableArrayList(Constants.PUBLIC, Constants.PROTECTED, Constants.PRIVATE);
    
    VBox componentUI;
    DiagramGenerator diagram;
    
    Button confirmation_btn;
//    AppTemplate app;
//    ComponentController componentController;
    
    public Variable(VBox componentUI) {
        super();
//        this.app = app;
        this.componentUI = componentUI;
        if(componentUI instanceof AddClassUI) {
            diagram = ((AddClassUI)componentUI).getDiagram(); 
            confirmation_btn = ((AddClassUI)componentUI).getConfirmation_btn();
        } else {
            diagram = ((AddInterfaceUI)componentUI).getDiagram(); 
            confirmation_btn = ((AddInterfaceUI)componentUI).getConfirmation_btn();
        }
        
        name = new TextField();
        type = new TextField();
        staticStatus = new CheckBox();
        access = new ComboBox(accessModifiers);
        
        variableInfo = new Label("");
        
        this.getChildren().addAll(name, type, staticStatus, access);
        
//        componentController = new ComponentController();
//        componentController.setupSelectable(this);
        setupHandler();
        initStyle();
    }
    
    public void setupHandler() {
//        name.addEventHandler(KeyEvent.ANY, e-> {
//           handleEditName();
//        });
        name.setOnKeyPressed(e -> {
//            handleEditName();
            confirmation_btn.setDisable(false);
        });
        type.setOnKeyPressed(e -> {
//            handleEditType();
            confirmation_btn.setDisable(false);
        });
        staticStatus.setOnAction(e -> {
//            handleEditStatic();
            confirmation_btn.setDisable(false);
        });
        access.setOnAction(e -> {
//            handleEditAccess();
            confirmation_btn.setDisable(false);
        });
    }
    
    public void initStyle() {
        
        this.setSpacing(Constants.SPACING);
        this.setPrefWidth(Constants.VAR_METHOD_WIDTH);
        this.getStyleClass().add(Constants.HBOXES);
        
        name.setPrefWidth(Constants.VARIABLE_BOX_WIDTH);
        type.setPrefWidth(Constants.VARIABLE_BOX_WIDTH);
        staticStatus.setPrefWidth(Constants.VARIABLE_BOX_WIDTH);
        access.setPrefWidth(Constants.VARIABLE_BOX_WIDTH);
    }

    /**
     * This method receives the value of name.
     * @return the value of name.
     */
    public TextField getName(){
        return name;
    }
      
    /**
     * This method receives the value of type.
     * @return the value of type.
     */
    public TextField getType(){
        return type;
    }
    
    /**
     * This method receives the value of staticStatus.
     * @return the value of staticStatus.
     */
    public CheckBox getStaticStatus(){
        return staticStatus;
    }
    
    /**
     * This method receives the value of access.
     * @return the value of access.
     */
    public ComboBox getAccess(){
        return access;
    }
    
    public void refreshVariableInfo() {
        String access = "";
        if(this.access.getSelectionModel().toString().equals("public")){
            access+="+";
        } else if(this.access.getSelectionModel().toString().equals("private")) {
            access+="-";
        } else if(this.access.getSelectionModel().toString().equals("protected")) {
            access+="#";
        } 
        String staticStatus = "";
        if(this.staticStatus.selectedProperty().getValue()) {
            staticStatus+="$";
        }
        String variableAsUMLInfo = access + staticStatus + " " + this.name.getText() + " : " + this.type.getText();
        
        // ADD A BUTTON FOR USER TO COMFIRM CHANGE AND THEN ADD THE INFO
        if(variableAsUMLInfo.trim().equals(".*:.*")) {
            variableAsUMLInfo = "";
        }
        variableInfo.setText(variableAsUMLInfo);
        //classUI.getDiagram().getVariableBox().getChildren().add(this.getVariableInfo());
    }
    
    public Label getVariableInfo() {
        refreshVariableInfo();
        return variableInfo;
    }
//    public String getName() {return name.getText();}
//    public String getType() {return type.getText();}
    //public String getStaticStatus() {return staticStatus.;}

    /**
     * Updates variable info in UML diagram.
     */
    private void handleEditName() {
        int index = diagram.variables_lb.indexOf(this);
        diagram.updateVariableLabel(variableInfo.getText(), index);
    }

    private void handleEditType() {
        
    }

    private void handleEditStatic() {
        
    }

    private void handleEditAccess() {
        
    }
    
}
