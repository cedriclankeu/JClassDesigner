package jClassDesigner.controller;

import jClassDesigner.data.AppState;
import jClassDesigner.data.DataManager;
import jClassDesigner.file.FileManager;
import jClassDesigner.gui.AddClassUI;
import jClassDesigner.gui.AddInterfaceUI;
import jClassDesigner.gui.DiagramGenerator;
import jClassDesigner.gui.Grid;
import jClassDesigner.gui.Workspace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import saf.AppTemplate;
import saf.ui.AppGUI;

/**
 * This class responds to interactions with the rendering surface.
 * 
 * @author Jia Sheng Ma
 * @version 1.0
 */
public class ToolController {
    AppTemplate app;
    DataManager dataManager;
    AppState state;
    AppGUI gui;
    
    public ToolController(AppTemplate initApp) {
	app = initApp;
        gui = app.getGUI();
        dataManager = (DataManager)app.getDataComponent();
    }

    public void handleSelect() {
        dataManager.setAppState(AppState.SELECT_STATE);
    }

    public void handleResize() {
        dataManager.setAppState(AppState.RESIZE_STATE);
    }

    /**
     * Removes selected class diagram and its corresponding component ui.
     */
    public void handleRemove() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        
        DiagramGenerator selectedNode;
        if((dataManager.getSelectedNode() != null) && dataManager.getSelectedNode() instanceof DiagramGenerator) {
            
            // if it is implemented interfaces or extended classes or used apis, remove just the box
            // as well as the selection in the parent/interface, and corresponding var/ argument
            
            selectedNode = (DiagramGenerator)(dataManager.getSelectedNode());
            
            if(selectedNode.getIsClassOrInterface()) {
                // GET CORRECSPONDING COMPONENT UI, SAVE IT FOR LATER USE
                VBox componentUI = dataManager.getComponentUI(selectedNode);
                
                // REMOVE DIAGRAM FROM DATA MANAGER 
                dataManager.getDiagrams().remove(selectedNode);
                // REMOVE ITS CORRESPONDING COMPONENT UI FROM DATA MANAGER 
                dataManager.getComponentUIs().remove(selectedNode);
                // AND FROM WORK PANE
                workspace.getWorkPane().getChildren().remove((DiagramGenerator)selectedNode);
                // REMOVE ASSOCIATED CONNECTORS
                for(Group g : ((DiagramGenerator)selectedNode).getConnectors()) {
                    workspace.getWorkPane().getChildren().remove(g);
                }
                // TODO: REMOVE ASSOCIATED DIAGRAMS
                
                ArrayList<String> connectedDiagrams;
//                String diagramToBeRemoved;
                // GET CONNECTED DIAGRAMS
                if(componentUI instanceof AddClassUI) {
//                    diagramToBeRemoved = ((AddClassUI)componentUI).getClassName();
                    connectedDiagrams = ((AddClassUI)componentUI).getListofConnectedDiagrams();
                } else {
//                    diagramToBeRemoved = ((AddInterfaceUI)componentUI).getInterfaceName();
                    connectedDiagrams = ((AddInterfaceUI)componentUI).getListofConnectedDiagrams();
                }
                // remove un used diagrams
                removeUnusedDiagrams(connectedDiagrams);
                
                // AND FROM COMPONENT TOOL BAR
                workspace.getComponentToolbarPane().getChildren().clear();
            } else {
                
                workspace.getWorkPane().getChildren().remove((DiagramGenerator)selectedNode);
                // TODO: REMOVE THIS FROM COMBO BOX'S CHECKED ITEM
                // REMOVE ASSOCIATED CONNECTORS
                for(Group g : ((DiagramGenerator)selectedNode).getConnectors()) {
                    workspace.getWorkPane().getChildren().remove(g);
                }
                
            }
            
        }
        // MARK AS UNSAVED
        app.getGUI().updateToolbarControls(false);
    }

    public void removeUnusedDiagrams(ArrayList<String> connectedDiagrams) {
        for(String diagramToRemove : connectedDiagrams) {
            boolean isUsed = false;
            for(VBox v : dataManager.getComponentUIs().values()) {
                if(v instanceof AddClassUI) {
                    for(String diagramToKeep : ((AddClassUI)v).getListofConnectedDiagrams()) {
                        if(diagramToRemove.equals(diagramToKeep)) {
                            isUsed = true;
                        }
                    }
                } else {
                    for(String diagramToKeep : ((AddInterfaceUI)v).getListofConnectedDiagrams()) {
                        if(diagramToRemove.equals(diagramToKeep)) {
                            isUsed = true;
                        }
                    }
                }
            }
            // IF THE DIAGRAM TO BE REMOVED IS NOT USED BY OTHER DIAGRAM, REMOVE IT
            // FROM THE CANVAS AND DATA MANAGER
            if(!isUsed) {
                System.out.println("@@@@@@@@@@ diagram not used, remove");
                dataManager.removeExtraDiagram(diagramToRemove);
            } else {
                System.out.println("########## diagram in use, dont remove");
            }
        }
    }
    
    public void cleanUnusedDiagram(Workspace workspace, DataManager dataManager) {
        ArrayList<VBox> allUsedDiagrams = dataManager.getAllDiagrams();
        ArrayList<VBox> diagramInWorkspace = new ArrayList<>();
        Pane workPane = workspace.getWorkPane();
        for(Node node : workPane.getChildren()) {
            if(node instanceof VBox) {
                diagramInWorkspace.add((VBox)node);
            }
        }
        System.out.println("diagram in workspace: " + diagramInWorkspace.toString());
        for(VBox v : diagramInWorkspace) {
            if(!allUsedDiagrams.contains(v)) {
                workPane.getChildren().remove(v);
                System.out.println("shout!shout!shout!shout!shout!shout!shout!shout!shout!shout!");
            }
        }
        System.out.println("all used diagrams: " + allUsedDiagrams.toString());
    }

    public void handleUndo() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        /*DataManager */dataManager = (DataManager)app.getDataComponent();
        
        app.getGUI().updateToolbarControls(false);
    }

    public void handleRedo() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        /*DataManager */dataManager = (DataManager)app.getDataComponent();
        
        app.getGUI().updateToolbarControls(false);
    }

    /**
     * Zooms in.
     */
    public void handleZoomIn() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        Pane workPane = workspace.getWorkPane();
        // PREVENT ZOOMING WHEN NOTHING IS IN WORK PANE
//        workPane.setPrefSize(workPane.getPrefWidth()*2, workPane.getPrefHeight()*2);
        workPane.setScaleX(workPane.getScaleX() * 2.0);
        workPane.setScaleY(workPane.getScaleY() * 2.0);
        
        
//        for(Node node : workPane.getChildren()) {
//            node.setScaleX(node.getScaleX() * 2.0);
//            node.setScaleY(node.getScaleY() * 2.0);
//        }
    }

    /**
     * Zooms out.
     */
    public void handleZoomOut() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        Pane workPane = workspace.getWorkPane();
        // PREVENT ZOOMING WHEN NOTHING IS IN WORK PANE
        
        workPane.setScaleX(workPane.getScaleX() / 2.0);
        workPane.setScaleY(workPane.getScaleY() / 2.0);
//        workPane.setPrefSize(workPane.getPrefWidth()/2, workPane.getPrefHeight()/2);
//        for(Node node : workPane.getChildren()) {
//            node.setScaleX(node.getScaleX() / 2.0);
//            node.setScaleY(node.getScaleY() / 2.0);
//        }
    }

    public void handleGridCheck(CheckBox grid_cbx) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        
        if(grid_cbx.selectedProperty().getValue()) {
            workspace.getWorkPane().getChildren().add(0, Grid.getGrid());
        } else {
            if(workspace.getWorkPane().getChildren().get(0) instanceof Grid) {
                // REMOVE ONLY THE GRID
                workspace.getWorkPane().getChildren().remove(0);
            }
        }
        
        app.getGUI().updateToolbarControls(false);
    }

    public void handleSnapCheck(CheckBox snap_cbx) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        ArrayList<VBox> allDiagrams = dataManager.getAllDiagrams();
        
        // SNAP TO POSITION
        if(snap_cbx.selectedProperty().get()) {
            snapToPos(allDiagrams);
        }
        
        app.getGUI().updateToolbarControls(false);
        
    }
    public void snapToPos(ArrayList<VBox> allDiagrams) {
        for(VBox v : allDiagrams) {
            DiagramGenerator d = (DiagramGenerator)v;
            int newX = (int)(d.getTranslateX()/20.0)*20;
            int newY = (int)(d.getTranslateY()/20.0)*20;
            d.setTranslateX(newX);
            d.setTranslateY(newY);
        }
    }

    public void handleExportPhotoRequest() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
	Pane workPane = workspace.getWorkPane();
	WritableImage snapshot = workPane.snapshot(new SnapshotParameters(), null);        

            // PROMPT THE USER FOR A FILE NAME
            FileChooser fc = new FileChooser();
            File snapshot_dir = new File(FileManager.PHOTO_PATH);
            if(!snapshot_dir.exists()){
                snapshot_dir.mkdir();
            }
            fc.setInitialDirectory(snapshot_dir);
            fc.setTitle("Save UML Design");
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("UML Design", "*.png"));

            File file = fc.showSaveDialog(app.getGUI().getWindow());
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, 
                    new BufferedImage((int)snapshot.getWidth(), (int)snapshot.getHeight(), 1)), "png", file);
            Dialog alert = new Alert(Alert.AlertType.INFORMATION, "Design Photo Exported Succussfully", ButtonType.OK);
            alert.show();
        } catch (IOException ioe) {
	    Dialog alert = new Alert(Alert.AlertType.ERROR, "ERROR IN EXPORTING PHOTO", ButtonType.OK);
            alert.show();
        }
    }
    
    /**
     * Exports UML design to code.
     */
    public void handleExportCodeRequest() {
        System.out.println("Start exporting code\n>>>");
        // GORUP PACKAGE AND CODE FOR EXPORTING
        dataManager.groupPackageAndCode();
        
        // GET THE LIST OF CLASSNAMES TO NAME THE FILE
        Collection<VBox> uis = dataManager.getComponentUIs().values();
        ArrayList<String> classNames = new ArrayList<>();
        for(VBox ui : uis) {
            String fileName = "";
            if(ui instanceof AddClassUI) {
                fileName+=((AddClassUI)ui).getClassName_tf().getText();
            } else if(ui instanceof AddInterfaceUI) {
                fileName += ((AddInterfaceUI)ui).getInterfaceName_tf().getText();
            }
            fileName+=".java";
            classNames.add(fileName);
            // DEBUG: 
            //System.out.println("file name: " + fileName);
        }
        
        // GET DIRECTORIES PATH ARRAY
        ArrayList<String> packageSets = dataManager.getPackageNameForExport();
        
        // GET CODE ARRAY
        ArrayList<String> codes = dataManager.getCodeForExport();
        boolean exportSuccess = false;
        // OUTPUT VALUE(Code) TO KEY(Directories)
        for(int i = 0; i < codes.size(); i++) {
            String path = FileManager.CODE_PATH;
            path += (packageSets.get(i));
            
            File dir = new File(path);
                // FIRST CREATE DIRECTORIES
            System.out.println("Directories created: " + dir.mkdirs());

            try {
                // path should be path + class name.java
                String filePath = path + classNames.get(i);
                PrintWriter pw = new PrintWriter(filePath);
                pw.print(codes.get(i));
                pw.close();
                
                // NOTIFY THE USERS ONCE DONE EXPORTING
                exportSuccess = true;
            } catch(FileNotFoundException fnfe) {
                Dialog alert = new Alert(Alert.AlertType.ERROR, "ERROR IN WRITING TO FILE", ButtonType.OK);
                alert.show();
            }
        
        }
        if(exportSuccess) {
            Dialog alert = new Alert(Alert.AlertType.INFORMATION, "Exported code successfully.", ButtonType.OK);
            alert.show();
        }
        
        System.out.println("<<<\nExporting code terminated.");
    }
    
}
