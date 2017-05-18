package jClassDesigner.gui;

import jClassDesigner.Constants;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

/**
 * This class contains all the available classes the designer created 
 * in a choice box so that the designer can choose appropriate classes
 * for the use of inheritance.
 * 
 * @author Jia Sheng Ma
 * @version 1.0
 */
public class ParentComboBox extends ComboBox {
    /*AppTemplate app;*/
    //DataManager dataManager;
    public static ObservableList<String> parentClasses;
    List<String> parentClassNames;

    VBox componentUI;
    
    public ParentComboBox(VBox componentUI) {
        /*this.app = app;
        dataManager = (DataManager)app.getDataComponent();*/
        
        // ENABLE EDIT TO ENABLE FLEXIBILITY FOR USERS
//        this.setEditable(true);
        
        parentClassNames = new ArrayList<>();
        this.componentUI = componentUI;
        
//        dataManager = new DataManager();
//        parentClassNames = dataManager.getClassNames();
        parentClasses = FXCollections.observableArrayList(parentClassNames);
        // ALL AVAILABLE PARENT CLASSES/INTERFACE HERE, EXCLUDE THIS CLASS
        this.setItems(parentClasses);
    }
    
    /**
     * Add available classes to the parent_cbb for each class to choose parent class.
     * @return parent choice box that contains available classes for this class to extend.
     */
    public ObservableList<String> getParentClasses() {
        return parentClasses;
    }
    public void addParent(String parentName) {
        parentClasses.add(parentName);
    }
    
    /**
     * Refreshes every component UI's parent combo box.
     * @param parents the list of available parent from the current design.
     */
    public void refreshAvailableParents(List<String> parents) {
        
        this.getItems().clear();
        this.getItems().add("");
        // ADD EVERY OTHER CLASSES AS AVAILABLE PARENTS EXCEPT FOR THIS
        // DONT REMOVE, JUST DONT ADD IT
        
        String thisClass="";
        // GET THE NAME OF THIS CLASS/INTERFACE
        if(componentUI instanceof AddClassUI) {
            thisClass += ((AddClassUI)componentUI).getClassName_tf().getText();
        } 
        // ADD ALL THE AVAILABLE CLASSES TO THE COMBO BOX
        for(String parent : parents) {
            if(!parent.equals(thisClass)) {
                this.getItems().add(parent);
            }
        }
        
        // SET VALUE
//        if(((AddClassUI)componentUI).getHasParent()) {
            this.setValue(((AddClassUI)componentUI).getparent());
//        }
    }
    
}
