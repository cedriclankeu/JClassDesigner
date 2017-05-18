package jClassDesigner.data;

import jClassDesigner.Constants;
import jClassDesigner.controller.ComponentController;
import jClassDesigner.gui.AddClassUI;
import jClassDesigner.gui.AddInterfaceUI;
import jClassDesigner.gui.DiagramGenerator;
import jClassDesigner.gui.Method;
import jClassDesigner.gui.Variable;
import jClassDesigner.gui.Workspace;
import java.util.ArrayList;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.controlsfx.control.IndexedCheckModel;
import saf.AppTemplate;

/**
 *
 * @author Jia Sheng Ma
 */
public class RawDataBank {

    public static ArrayList<ComponentUI_DataWrapper> data = new ArrayList<>();  
    
    public RawDataBank() {}
    
    /**
     * Called when save data, 
     * @param dataManager 
     */
    public static void extractDataFromDataManager(DataManager dataManager) {
        // CLEAR OLD DATA TO SAVE NEW DATA
        data.clear();
        
        // EXTRACT NEW DATA
        for(VBox v : dataManager.getComponentUIs().values()) {
            if(v instanceof AddClassUI) {
                AddClassUI c = (AddClassUI)v;
                ComponentUI_DataWrapper ui = new ComponentUI_DataWrapper(Constants.CLASS);
                ui.setName(c.getClassName());
                ui.setPackageName(c.getPackageName());
                for(String s : c.getImplementedInterfaces()/*getInterfaceComboBox().getImplementedInterfaces()*/) {
                    ui.addImplementedInterface(s);
                }
                ui.setParent(c.getparent());
                ui.setTranslateX(c.getDiagram().getTranslateX());
                ui.setTranslateY(c.getDiagram().getTranslateY());
                // Variable data
                for(Variable var: c.getVariables()) {
                    Variable_DataWrapper variable = new Variable_DataWrapper();
                    variable.setName(var.getName().getText());
                    variable.setType(var.getType().getText());
                    variable.setStatic(var.getStaticStatus().selectedProperty().getValue());
                    if(var.getAccess().getValue()!=null) {
                        variable.setAccess(var.getAccess().getValue().toString());
                    }
                    
                    ui.addVariable(variable);
                }
                
                // Method data
                for(Method m : c.getMethods()) {
                    Method_DataWrapper method = new Method_DataWrapper();
                
                    method.setName(m.getName().getText());
                    method.setReturnType(m.getReturnType().getText());
                    method.setStatic(m.getStaticStatus().selectedProperty().getValue());
                    // IF THERE'S AN ABSTRACT METHOD, THE CLASS IS ABSTRACT
                    if(m.getAbstractStatus().selectedProperty().getValue()) {
                        ui.setType(Constants.ABSTRACT_CLASS);
                    }
                    method.setAbstract(m.getAbstractStatus().selectedProperty().getValue());
                    if(m.getAccess().getValue()!=null) {
                        method.setAccess(m.getAccess().getValue().toString());
                    }
                    for(TextField tf : m.getArgTypes()) {
                        method.addArgument(tf.getText());
                    }
                    ui.addMethod(method);
                }
                // ADD DATA TO LIST
                data.add(ui);
                
            } // END OF INSTANCE OF CLASS
            else {
                AddInterfaceUI it = (AddInterfaceUI)v;
                ComponentUI_DataWrapper ui = new ComponentUI_DataWrapper(Constants.INTERFACE);
                ui.setName(it.getInterfaceName());
                ui.setPackageName(it.getPackageName());
                ui.setParent(it.getparent());
                for(String s : it.getImplementedInterfaces()/*getInterfaceComboBox().getImplementedInterfaces()*/) {
                    ui.addImplementedInterface(s);
                }
                ui.setTranslateX(it.getDiagram().getTranslateX());
                ui.setTranslateY(it.getDiagram().getTranslateY());
                // Variable data
                for(Variable var: it.getVariables()) {
                    Variable_DataWrapper variable = new Variable_DataWrapper();
                    variable.setName(var.getName().getText());
                    variable.setType(var.getType().getText());
                    variable.setStatic(var.getStaticStatus().selectedProperty().getValue());
                    if(var.getAccess().getValue()!=null) {
                        variable.setAccess(var.getAccess().getValue().toString());
                    }
                    
                    ui.addVariable(variable);
                }
                
                // Method data
                for(Method m : it.getMethods()) {
                    Method_DataWrapper method = new Method_DataWrapper();
                
                    method.setName(m.getName().getText());
                    method.setReturnType(m.getReturnType().getText());
                    method.setStatic(m.getStaticStatus().selectedProperty().getValue());
                    method.setAbstract(m.getAbstractStatus().selectedProperty().getValue());
                    if(m.getAccess().getValue()!=null) {
                        method.setAccess(m.getAccess().getValue().toString());
                    }
                    for(TextField tf : m.getArgTypes()) {
                        method.addArgument(tf.getText());
                    }
                    ui.addMethod(method);
                }
                
                // ADD DATA TO LIST
                data.add(ui);
            } // END OF INSTANCE OF INTERAFCE
        }
    }
    
    /**
     * Create objects with given data in data bank.
     * (Only reloadWorkspace() method should call this method.)
     * @param app
     * @param workspace 
     * @param dataManager 
     * @param componentController 
     */
    public static void createObjects(AppTemplate app, Workspace workspace, DataManager dataManager, ComponentController componentController) {
        // TODO: CLEAR OLD DATA IN DATA MANAGER 
        //       RELOAD DATA TO DATA MANAGER 
        
        for(int i = 0; i < data.size(); i++) {
            // ** CREATE OBJECT **//
            if(data.get(i).getType().equals(Constants.CLASS)) {
                AddClassUI classUI = new AddClassUI(app);
                // TODO: create everything else, add to containers
                classUI.getClassName_tf().setText(data.get(i).getName());
                classUI.getPackageName_tf().setText(data.get(i).getPackage());
                classUI.setPackageName(data.get(i).getPackage());
                classUI.getParentComboBox().setValue(data.get(i).getExtendedParent());
                classUI.setParent(data.get(i).getExtendedParent());
//                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@" + data.get(i).getExtendedParent());
                // IMPLEMENTED INTERFACES
                for(int l = 0; l < data.get(i).getImplementedInterfaces().size(); l++) {
                    String itf = data.get(i).getImplementedInterfaces().get(l);
                    // ADD THE INTERFACES IN
                    classUI.getInterfaceComboBox().getItems().add(itf);
                }
                for(Object o : classUI.getInterfaceComboBox().getItems()) {
                    // SET SELECTED VALUES 
                    classUI.getInterfaceComboBox().getCheckModel().check(o);
                }
                // VARIABLE
                for(int j = 0; j < data.get(i).getVariables().size(); j++) {
                    Variable_DataWrapper variable_data = data.get(i).getVariables().get(j);
                    Variable variable = new Variable(classUI);
                    componentController.setupSelectable(variable);
                    variable.getName().setText(variable_data.getName());
                    variable.getType().setText(variable_data.getType());
                    variable.getStaticStatus().selectedProperty().set(variable_data.getVariableStatic());
                    variable.getAccess().setValue(variable_data.getVariableAccess());
                    // ADD VARIABLE TO LIST
                    classUI.addVariableToList(variable);
                    // ADD VARIABLE TO UI
                    classUI.addVariableToClassUI(variable);
                }
                // METHOD
                for(int k = 0; k < data.get(i).getMethods().size(); k++) {
                    Method_DataWrapper method_data = data.get(i).getMethods().get(k);
                    Method method = new Method(classUI);
                    componentController.setupSelectable(method);
                    method.getName().setText(method_data.getName());
                    method.getReturnType().setText(method_data.getReturnType());
                    method.getStaticStatus().selectedProperty().set(method_data.getMethodStatic());
                    method.getAbstractStatus().selectedProperty().set(method_data.getAbstract());
                    method.getAccess().setValue(method_data.getAccess());
                    
                    for(int l = 0; l < method_data.getArguments().size(); l++) {
                        method.getArgTypes().add(new TextField((method_data.getArguments()).get(l)));
                        method.getArgumentContainer().getChildren().add(new TextField((method_data.getArguments()).get(l)));
                        //((method.getArgTypes()).get(l)).setText((method_data.getArguments()).get(l));
                    }
                    // ADD METHOD TO LIST
                    classUI.addMethodToList(method);
                    // ADD METHOD TO UI
                    classUI.addMethodToClassUI(method);
                }
                DiagramGenerator diagram = classUI.getDiagram();
                diagram.editClassName(data.get(i).getName());
                diagram.setTranslateX(data.get(i).getTranslateX());
                diagram.setTranslateY(data.get(i).getTranslateY());
                // ** ADD TO DATAMANAGER ** //
                dataManager.addComponentUI(diagram, classUI);
                dataManager.addDiagram(diagram);
                // ** ADD TO WORKSPACE **//
                workspace.getWorkPane().getChildren().add(diagram);
                
                
            } // end of if Constants.CLASS 

            // CREATE OBJECT
            else if(data.get(i).getType().equals(Constants.INTERFACE)) {
                AddInterfaceUI interfaceUI = new AddInterfaceUI(app);
                // TODO: create everything else, add to containers
                interfaceUI.getInterfaceName_tf().setText(data.get(i).getName());
                interfaceUI.getPackageName_tf().setText(data.get(i).getPackage());
                // todo: 
                for(int l = 0; l < data.get(i).getImplementedInterfaces().size(); l++) {
                    String itf = data.get(i).getImplementedInterfaces().get(l);
                    // ADD THE INTERFACES IN
                    interfaceUI.getInterfaceComboBox().getItems().add(itf);
                }
                for(Object o : interfaceUI.getInterfaceComboBox().getItems()) {
                    // SET SELECTED VALUES 
                    // FIXME
                    interfaceUI.getInterfaceComboBox().getCheckModel().check(o);
                }
                // VARIABLE
                for(int j = 0; j < data.get(i).getVariables().size(); j++) {
                    Variable_DataWrapper variable_data = data.get(i).getVariables().get(j);
                    Variable variable = new Variable(interfaceUI);
                    componentController.setupSelectable(variable);
                    variable.getName().setText(variable_data.getName());
                    variable.getType().setText(variable_data.getType());
                    variable.getStaticStatus().selectedProperty().set(variable_data.getVariableStatic());
                    variable.getAccess().setValue(variable_data.getVariableAccess());
                    // ADD VARIABLE TO LIST
                    interfaceUI.addVariableToList(variable);
                    // ADD VARIABLE TO UI
                    interfaceUI.addVariableToInterfaceUI(variable);
                }
                // METHOD
                for(int k = 0; k < data.get(i).getMethods().size(); k++) {
                    Method_DataWrapper method_data = data.get(i).getMethods().get(k);
                    Method method = new Method(interfaceUI);
                    componentController.setupSelectable(method);
                    method.getName().setText(method_data.getName());
                    method.getReturnType().setText(method_data.getReturnType());
                    method.getStaticStatus().selectedProperty().set(method_data.getMethodStatic());
                    method.getAbstractStatus().selectedProperty().set(method_data.getAbstract());
                    method.getAccess().setValue(method_data.getAccess());
                    
                    for(int l = 0; l < method_data.getArguments().size(); l++) {
                        method.getArgTypes().add(new TextField((method_data.getArguments()).get(l)));
                        method.getArgumentContainer().getChildren().add(new TextField((method_data.getArguments()).get(l)));
                    }
                    // ADD METHOD TO LIST
                    interfaceUI.addMethodToList(method);
                    // ADD METHOD TO UI
                    interfaceUI.addMethodToInterfaceUI(method);
                }
                DiagramGenerator diagram = interfaceUI.getDiagram();
                diagram.editClassName(data.get(i).getName());
                diagram.setTranslateX(data.get(i).getTranslateX());
                diagram.setTranslateY(data.get(i).getTranslateY());
                // ** ADD TO DATAMANAGER ** //
                dataManager.addComponentUI(diagram, interfaceUI);
                dataManager.addDiagram(diagram);
                // ** ADD TO WORKSPACE **//
                workspace.getWorkPane().getChildren().add(diagram);
                
            } // end of if Constants.INTERFACE
            
        }
        
    }
    
}
