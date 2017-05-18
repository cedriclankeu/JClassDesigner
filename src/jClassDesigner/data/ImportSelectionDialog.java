/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jClassDesigner.data;

import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.control.ChoiceDialog;

/**
 *
 * @author majiasheng
 */
public class ImportSelectionDialog {
    private ChoiceDialog<String> importContainer;
    public ImportSelectionDialog() {
        importContainer = new ChoiceDialog<>();
        importContainer.setTitle("Choose Import");
        importContainer.setHeaderText("Please choose an Import");
        importContainer.setContentText("Imports");
    }
    public void setImportTarget(String s) {
        importContainer.setHeaderText("Please choose an Import for: " + s);
    }
    public void clearOptions() {
        importContainer.getItems().clear();
    }
    public void addImportSelection(String s) {
        importContainer.getItems().add(s);
    }
    public void show(ArrayList<String> imports) {
        Optional<String> result = importContainer.showAndWait();
        result.ifPresent(s -> imports.add(s));
    }
    
}
