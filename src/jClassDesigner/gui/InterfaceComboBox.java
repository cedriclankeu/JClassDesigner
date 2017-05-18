/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jClassDesigner.gui;

import jClassDesigner.data.DataManager;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;

/**
 *
 * @author Jia Sheng Ma
 */
public class InterfaceComboBox extends CheckComboBox<Object>{
    
    //private ObservableList<Object> interfaces = FXCollections.observableArrayList();
    private final VBox componentUI;
    
    //private ArrayList<String> implementedInterfaces;

//    public ArrayList<String> getImplementedInterfaces() {
//        return implementedInterfaces;
//    }
    /**
     * 
     */
    public InterfaceComboBox(VBox componentUI) {
        this.componentUI = componentUI;
//        implementedInterfaces = new ArrayList<>();
        this.getCheckModel().getCheckedItems().addListener(new ListChangeListener<Object>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Object> c) {
                ObservableList<Object> list = getCheckModel().getCheckedItems();
//                updateImplementedInterfaces(list);
                if(componentUI instanceof AddClassUI) { // enable confirmation button
                    ((AddClassUI)componentUI).getConfirmation_btn().setDisable(false);
                } else {                                // enable confirmation button
                    ((AddInterfaceUI)componentUI).getConfirmation_btn().setDisable(false);
                }
            }

        });
    }
    
    public void refreshAvailableInterface(List<String> interfaceNames) {
        
        String nameOfThisClass;
        if(componentUI instanceof AddClassUI) {
            nameOfThisClass = ((AddClassUI)componentUI).getClassName_tf().getText();
        } else {
            nameOfThisClass = ((AddInterfaceUI)componentUI).getInterfaceName_tf().getText();
        }
        
        // TODO: 
        // GET AND SAVE ALL THE CHECKED ITEMS TO THE COMPONENT UI
        
        if(componentUI instanceof AddClassUI) {
            // CLEAR OLD CHECKED ITEMS IN THE COMPONENT UI FIRST
            ((AddClassUI)componentUI).getImplementedInterfaces().clear();
            for(Object o : this.getCheckModel().getCheckedItems()) {
                ((AddClassUI)componentUI).addImplementedInterface((String)o);
            }
            
        } else {
            ((AddInterfaceUI)componentUI).getImplementedInterfaces().clear();
            for(Object o : this.getCheckModel().getCheckedItems()) {
                ((AddInterfaceUI)componentUI).addImplementedInterface((String)o);
            }
        }
        
        // CLEAR OLD DATA
        this.getItems().clear();

        // REFRESH: ADD ALL THE AVAILABLE INTERFACE NAME TO THE COMBOBOX EXCEPT FOR THIS ONE
        // ADD EVERY OTHER INTERFACES AS AVAILABLE PARENTS EXCEPT FOR THIS
        // DONT REMOVE, JUST DONT ADD
        for(String interfaceName : interfaceNames) {
            if(!interfaceName.equals(nameOfThisClass)/* && (componentUI instanceof AddInterfaceUI)*/) {
                this.getItems().add(interfaceName);
            }
        }
        // SET/CHECK VALUES
        ArrayList<String> implementedInterfaces;
        if(componentUI instanceof AddClassUI) {
            implementedInterfaces = ((AddClassUI)componentUI).getImplementedInterfaces();
        } else {
            implementedInterfaces = ((AddInterfaceUI)componentUI).getImplementedInterfaces();
        }
        // FIXME: SOME ITEMS ARENT CHECKED
        for(String s : implementedInterfaces) {
            this.getCheckModel().check(s);
//            System.out.println("checked: " + this.getCheckModel().isChecked(s));
        }
    }
    
    /**
     * Extracts the list of interfaces being selected
     * @return a list of selected interfaces.
     */
//    public ArrayList<String> getImplementedInterfaces() {
//        ArrayList<String> implementedInterfaces = new ArrayList<>();
//        for(Object o : this.getCheckModel().getCheckedItems()) {
//            if(o instanceof String) {
//                implementedInterfaces.add((String)o);
//            }
//        }
//        return implementedInterfaces;
//    }
    
//    private void updateImplementedInterfaces(ObservableList<Object> list) {
//        implementedInterfaces.clear();
//        for(Object o : list) {
//            implementedInterfaces.add((String)o);
//        }
//    }
}
