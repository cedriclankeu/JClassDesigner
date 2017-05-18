/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jClassDesigner.gui;

import jClassDesigner.Constants;
import jClassDesigner.controller.ComponentController;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author majiasheng
 */
public class Method extends HBox{
    private TextField name;
    private TextField returnType;
    private CheckBox staticStatus;
    private CheckBox abstractStatus;
    private ComboBox access;
    
    private ScrollPane argumentScrollPane;
    private VBox argumentBtnContainer;
    private HBox argumentContainer;
    private ArrayList<TextField> argTypes;
    private ArrayList<String> argNames;
    private final String argNamePrefix = "arg";
    //private TextField arg1;
    private Button argAdd_btn;
    private Button argRemove_btn;
    private int argNameCounter = 0;
    
    private Label methodInfo;
    
    private final ObservableList<String> accessModifiers = FXCollections.observableArrayList(Constants.PUBLIC, Constants.PROTECTED, Constants.PRIVATE);
    
    Button confirmation_btn;
    
//    ComponentController componentController;
    
    VBox componentUI;
    
    public Method(VBox componentUI) {
        this.componentUI=componentUI;
        name = new TextField();
        returnType = new TextField();
        staticStatus = new CheckBox();
        abstractStatus = new CheckBox();
        access = new ComboBox(accessModifiers);
        argTypes = new ArrayList<>();
        argNames = new ArrayList<>();
        argumentContainer = new HBox();
        
        argAdd_btn = new Button("+arg");
        argRemove_btn = new Button("-arg");
        argumentBtnContainer = new VBox(argAdd_btn, argRemove_btn);
        
        
        argumentScrollPane = new ScrollPane();
        argumentScrollPane.setContent(argumentContainer);
        
        argumentScrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        this.getChildren().addAll(name, returnType, staticStatus, abstractStatus, access,argumentBtnContainer, argumentScrollPane);
        
        if(componentUI instanceof AddClassUI) {
            confirmation_btn = ((AddClassUI)componentUI).getConfirmation_btn();
        } else {
            confirmation_btn = ((AddInterfaceUI)componentUI).getConfirmation_btn();
        }
        
//        componentController = new ComponentController();
//        componentController.setupSelectable(this);
//        
        setupHandler();
        initStyle();
        
    }
    public void setupHandler() {

        name.setOnKeyPressed(e -> {
//            handleEditMethodName();
            confirmation_btn.setDisable(false);
        });
        returnType.setOnKeyPressed(e -> {
//            handleEditReturnType();
            confirmation_btn.setDisable(false);
        });
        staticStatus.setOnAction(e -> {
//            handleEditMethodStatic();
            confirmation_btn.setDisable(false);
        });
        abstractStatus.setOnAction(e ->{
//            handleEditAbstractStatus();
            confirmation_btn.setDisable(false);
        });
        access.setOnAction(e -> {
//            handleEditMethodAccess();
            confirmation_btn.setDisable(false);
        });
        
        argAdd_btn.setOnAction(e -> {
            handleAddArg();
            confirmation_btn.setDisable(false);
        });
        argRemove_btn.setOnAction(e -> {
            handleRemoveArg();
            confirmation_btn.setDisable(false);
        });
    } // END OF SET UP HANDLERS

    /**
     * This method receives the value of name.
     * @return the value of name.
     */
    public TextField getName(){return name;}

    /**
     * This method receives the value of returnType.
     * @return the value of returnType.
     */
    public TextField getReturnType(){return returnType;}
    
    /**
     * This method receives the value of staticStatus.
     * @return the value of staticStatus.
     */
    public CheckBox getStaticStatus(){return staticStatus;}

    /**
     * This method receives the value of abstractStatus.
     * @return the value of abstractStatus.
     */
    public CheckBox getAbstractStatus(){return abstractStatus;}
    
    /**
     * This method receives the value of access.
     * @return the value of access.
     */
    public ComboBox getAccess(){return access;}
    
    /**
     * This method receives the value of argTypes.
     * @return the value of argTypes.
     */
    public ArrayList<TextField> getArgTypes(){return argTypes;}
    
    public HBox getArgumentContainer() {
        return argumentContainer;
    }
    public ScrollPane getArgumentScrollPane() {
        return argumentScrollPane;
    }
    
    
    //    public ArrayList<String> getArgNames() {return argNames;}

    public void addArgType(TextField argType) {
        argTypes.add(argType);
//        TextField arg = new TextField();
//        this.getChildren().add(arg);
    }
    
    public void removeArgument() {
        
    }
    
    public void refreshMethodInfo() {
        
    }
//    
//    public Label getMethodInfo() {
//        refreshMethodInfo();
//        return methodInfo;
//    }
    
    public Label getMethodInfo() {
        refreshMethodInfo();
        return methodInfo;
    }
    
    public void initStyle() {
        argumentBtnContainer.setSpacing(-5);
        argumentScrollPane.setPrefWidth(1.5*(Constants.METHOD_BOX_WIDTH));
        this.setSpacing(Constants.SPACING);
        //this.setPrefWidth(Constants.VAR_METHOD_WIDTH);
        this.getStyleClass().add(Constants.HBOXES);
        
        argAdd_btn.setScaleX(0.7);
        argAdd_btn.setScaleY(0.7);
        argRemove_btn.setScaleX(0.7);
        argRemove_btn.setScaleY(0.7);
        
        name.setPrefWidth(Constants.METHOD_BOX_WIDTH);
        returnType.setPrefWidth(Constants.METHOD_BOX_WIDTH);
        staticStatus.setPrefWidth(Constants.METHOD_BOX_WIDTH);
        abstractStatus.setPrefWidth(Constants.METHOD_BOX_WIDTH);
        access.setPrefWidth(Constants.METHOD_BOX_WIDTH);
    }

    private void handleEditMethodName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void handleEditReturnType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void handleEditMethodStatic() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void handleEditAbstractStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void handleEditMethodAccess() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void handleAddArg() {
        
        TextField newArg = new TextField();
        
        newArg.setPromptText("argument");
        argTypes.add(newArg);
        VBox methodContainer;
        if(componentUI instanceof AddClassUI) {
            methodContainer = ((AddClassUI)componentUI).getMethodContainer();
        } else {
            methodContainer = ((AddInterfaceUI)componentUI).getMethodContainer();
        }
        
        newArg.setPrefWidth(Constants.METHOD_BOX_WIDTH);
//        methodContainer.setPrefWidth(methodContainer.getPrefWidth()+200);
        
        argNameCounter++;
//        argNames.add(argNamePrefix+argNameCounter);
        int size = this.getChildren().size();
        argumentContainer.getChildren().add(newArg);
        System.out.println("arg added");
    }

    private void handleRemoveArg() {
        // SAVE ONE ARGUMENT AS DEFAULT NUMBER
        if(argTypes.size() > 0) {
            // REMOVE THE RIGHTMOST ARG
            argTypes.remove(argTypes.size()-1);
//            argNames.remove(argNames.size()-1);
            argNameCounter--;
            int size = argumentContainer.getChildren().size();
            argumentContainer.getChildren().remove(size-1);
//            this.setPrefWidth(this.getPrefWidth() - Constants.METHOD_BOX_WIDTH);
        
            System.out.println("arg removed");
        }
        
    }
    
}
