package jClassDesigner.data;

import jClassDesigner.gui.AddClassUI;
import jClassDesigner.gui.AddInterfaceUI;
import jClassDesigner.gui.DiagramGenerator;
import jClassDesigner.gui.Method;
import jClassDesigner.gui.ParentComboBox;
import jClassDesigner.gui.Variable;
import jClassDesigner.gui.Workspace;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.layout.VBox;
import saf.components.AppDataComponent;
import saf.AppTemplate;

/**
 * This class serves as the data management component for this application.
 *
 * @author Jia Sheng Ma
 * @version 1.1
 */
public class DataManager implements AppDataComponent {
    
    AppState state;

    AppTemplate app;
    
    private HashMap<VBox, VBox> componentUIList;    // K = DIAGRAM, V = UI
    private ArrayList<VBox> diagrams;
    private ArrayList<VBox> extraDiagrams;

    // FOR CHECKING DUPLICATES AND EXPORTING
    private ArrayList<String> classNames;
    private ArrayList<String> packageNames;
    
    private ArrayList<String> packageNameForExport;
    private ArrayList<String> codeForExport;
    
    private ArrayList<String> externalInterfaces;
    private ArrayList<String> externalClasses;
    
    private Node prev_selectedNode;
    private Node selectedNode;
    
    private Effect highlightedEffect;

    public DataManager(AppTemplate initApp) throws Exception {
	
	app = initApp;
        state = AppState.NEW_STATE;
        
        componentUIList = new HashMap<>();
        diagrams = new ArrayList<>();
        extraDiagrams = new ArrayList<>();
        
        classNames = new ArrayList<>();
        packageNames = new ArrayList<>();
        
        externalClasses = new ArrayList<>();
        externalInterfaces = new ArrayList<>();
        
        packageNameForExport = new ArrayList<>();
        codeForExport = new ArrayList<>();
        
        prev_selectedNode = null;
        selectedNode = null;
        
	// EFFECT FOR HIGHLIGHTING SELECTED NODE
	DropShadow dropShadowEffect = new DropShadow();
	dropShadowEffect.setOffsetX(0.0f);
	dropShadowEffect.setOffsetY(0.0f);
	dropShadowEffect.setSpread(1.0);
	dropShadowEffect.setColor(Color.YELLOW);
	dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
	dropShadowEffect.setRadius(3);
	highlightedEffect = dropShadowEffect;
    }

    /**
     * Constructor independent of app.
     */
    public DataManager() {
        state = AppState.NEW_STATE;
        componentUIList = new HashMap<>();
        diagrams = new ArrayList<>();
        extraDiagrams = new ArrayList<>();
        
        classNames = new ArrayList<>();
        packageNames = new ArrayList<>();
        
        externalClasses = new ArrayList<>();
        externalInterfaces = new ArrayList<>();
        
        packageNameForExport = new ArrayList<>();
        codeForExport = new ArrayList<>();
    }

    public ArrayList<VBox> getExtraDiagrams() {
        return extraDiagrams;
    }

    public void addExtraDiagram(VBox extraDiagram) {
        extraDiagrams.add(extraDiagram);
    }
    public void removeExtraDiagram(String s) {
        Workspace workspace = ((Workspace)app.getWorkspaceComponent());
        Pane workPane = workspace.getWorkPane();
        for(VBox v : extraDiagrams) {
            if(((DiagramGenerator)v).getDiagramClassNameLabel().getText().equals(s)) {
                // REMOVE DIAGRAM FROM DATA MANAGER
                extraDiagrams.remove(v);   
                
                for(Group g : ((DiagramGenerator)v).getConnectors()) {
                    workspace.getWorkPane().getChildren().remove(g);
                }
                // REMOVE DIAGRAM FROM WORKSPACE
                workPane.getChildren().remove(v);
                break;
            }
        }
    }
    
    public ArrayList<String> getExternalClasses() {
        return externalClasses;
    }

    public void addExternalClass(String externalClass) {
        externalClasses.add(externalClass);
    }

    public ArrayList<String> getExternalInterfaces() {
        return externalInterfaces;
    }

    public void addExternalInterface(String externalInterface) {
        externalInterfaces.add(externalInterface);
    }
    
    /**
     * Sets the node that's being selected, and highlights it.
     * @param node selected node
     */
    public void setSelectedNode(Node node) {
        prev_selectedNode = selectedNode;
        
        if(prev_selectedNode != null) {
            // CLEAR HIGHLIGHT OF THE PREVIOUS SELECTED NODE
            prev_selectedNode.setEffect(null);
        }
        
        selectedNode = node;
        
        // HIGHLIGHT IF THE NODE SELECTED IS NOT NULL
        if(selectedNode!=null) {
            selectedNode.setEffect(highlightedEffect);
        }
        
    }
    
    public Node getSelectedNode() {
        return selectedNode;
    }
    
    public void addComponentUI(VBox diagram, VBox ui) {
        componentUIList.put(diagram, ui);
    }
    
    public HashMap<VBox, VBox> getComponentUIs() {
        return componentUIList;
    }
    public VBox getComponentUI(VBox diagram) {
        return componentUIList.get(diagram);
    }
    
    public void addClassName(String name) {
        classNames.add(name);
    } 
    public ArrayList<String> getClassNames() {
        return classNames;
    }
    
    public void addPackage(String packageName) {
        packageNames.add(packageName);
    }
    public ArrayList<String> getPackageNames() {
        return packageNames;
    }
    
    public void addDiagram(VBox diagram) {
        diagrams.add(diagram);
    }
    /**
     * 
     * @return a list of class/interface diagrams
     */
    public ArrayList<VBox> getDiagrams() {
        return diagrams;
    }
    
    public DiagramGenerator getDiagram(String diagramName) {
        DiagramGenerator diagram = null;
        for(VBox d : diagrams) {
            if(((DiagramGenerator)d).getDiagramClassNameLabel().getText().equals(diagramName)) {
                diagram = (DiagramGenerator)d;
                break;
            }
        }
        if(diagram == null) {
            for(VBox d : extraDiagrams) {
                if(((DiagramGenerator)d).getDiagramClassNameLabel().getText().equals(diagramName)) {
                    diagram = (DiagramGenerator)d;
                    break;
                }    
            }
        }
        
        return diagram;
    }

    /**
     * Groups package and code together for exporting.
     */
    public void groupPackageAndCode() {
        // clear and add back to refresh data
        codeForExport.clear();
        packageNameForExport.clear();
        for(VBox componentUI : componentUIList.values()) {
            if(componentUI instanceof AddClassUI) {
                AddClassUI classUI = (AddClassUI)componentUI;
                
                String packageName = getPackageName(classUI);
                String code = getCode(classUI);

                packageNameForExport.add(packageName);
                codeForExport.add(code);
                
            } else {
                AddInterfaceUI interfaceUI = (AddInterfaceUI)componentUI;
                
                String packageName = getPackageName(interfaceUI);
                String code = getCode(interfaceUI);

                packageNameForExport.add(packageName);
                codeForExport.add(code);
            }
        } // end of for each VBox componentUI
    }
    
    /**
     * Parses the user input of package name into an array ofcomponentUIname for exporting code.
     * @param componentUI UML design
     * @return an array of package names of this design.
     */
    public String getPackageName(VBox componentUI) {
        
        if(componentUI instanceof AddClassUI) {
            AddClassUI classUI = (AddClassUI)componentUI;
            // SPLIT PACKAGE BY . FOR NESTED DIRECTORIES.
            return classUI.getPackageName_tf().getText().replace('.','/') + "/";
        } else if(componentUI instanceof AddInterfaceUI) {
            AddInterfaceUI interfaceUI = (AddInterfaceUI)componentUI;
            return interfaceUI.getPackageName_tf().getText().replace('.','/') + "/";
        } else {
            return "";
        }
    }
    
    /**
     * Integrate all the parts of a skeleton code.
     * @param componentUI    
     * @return skeleton code of a single UML diagram.
     */
    public String getCode(VBox componentUI) {
        // check variable and method for classes that needed import
        ArrayList<String> imports = new ArrayList();
        ImportSelectionDialog importSelectionDialog = new ImportSelectionDialog();
        
        String variableAsCode = getVariableAsCode(componentUI, importSelectionDialog, imports);
        String methodAsCode = getMethodAsCode(componentUI, importSelectionDialog, imports);
        
        // classAsCode contains code to return
        String classAsCode = "";
        for(int i = 0; i < imports.size(); i++) {
            classAsCode+=(imports.get(i)+'\n');
        }
        
        classAsCode += "public ";
        
        if(componentUI instanceof AddClassUI) {
            AddClassUI classUI = ((AddClassUI)componentUI);
            if(methodAsCode.contains("abstract")) {
                classAsCode += ("abstract class " + (classUI.getClassName_tf().getText()));
            } else {
                classAsCode += ("class " + (classUI.getClassName_tf().getText()));
            }
            // IF THIS CLASS EXTENDS A CLASS, THEN ADD "EXTENDS" + PARENT
            if((classUI.getParentName() != null) && !(classUI.getParentName().equals(""))) {
                classAsCode += (" extends " + classUI.getParentName());
            }
            // IF THIS CLASS IMPLEMENTS AN INTERFACE, THEN ADD "IMPLEMENTS" + INTERFACE
            int size = classUI.getInterfaceComboBox().getCheckModel().getCheckedItems().size();
            if(classUI.getHasParent() && size>0) {
                classAsCode += (" implements ");
                int i = 0;
                for(Object o : classUI.getInterfaceComboBox().getCheckModel().getCheckedItems()) {
                    if(i == 0) {
                        classAsCode += (o.toString());
                    } else {
                        classAsCode += (", " + o.toString());
                    }
                    i++;
                }
            }
            
        } else {
            AddInterfaceUI interfaceUI = (AddInterfaceUI)componentUI;
            classAsCode += ("interface " + (interfaceUI.getInterfaceName_tf().getText()));
            int size = interfaceUI.getInterfaceComboBox().getCheckModel().getCheckedItems().size();
            if(size>0) {
                classAsCode += (" implements ");
                int i = 0;
                for(Object o : interfaceUI.getInterfaceComboBox().getCheckModel().getCheckedItems()) {
                    if(i == 0) {
                        classAsCode += (o.toString());
                    } else {
                        classAsCode += (", " + o.toString());
                    }
                    i++;
                }
            }
            
        }
        
        classAsCode += (" {\n" + variableAsCode + "\n" + methodAsCode + "\n}\n");
        
        return classAsCode;
    }
    
    /**
     * Helper method to compose code for exporting.
     * @param componentUI
     * @return 
     */
    public String getVariableAsCode(VBox componentUI, ImportSelectionDialog importSelectionDialog, ArrayList<String> imports) {
        String variablesAsCode = "";
        ArrayList<Variable> variables = new ArrayList<>();
        if(componentUI instanceof AddClassUI) {
            AddClassUI classUI = (AddClassUI)componentUI;
            variables = classUI.getVariables();
        } else if(componentUI instanceof AddInterfaceUI) {
            AddInterfaceUI interfaceUI = (AddInterfaceUI)componentUI;
            variables = interfaceUI.getVariables();
        }
        // COMPOSE VARIABLES AS CODE
        for(Variable v : variables) {
                String variableAsCode = "    ";
                String access = "";
                if(v.getAccess().getValue()!=null) {
                    access += v.getAccess().getValue().toString();
                }

                String staticStatus = "";
                if(v.getStaticStatus().selectedProperty().getValue()) {
                    staticStatus += "static";
                }
                String type = v.getType().getText().trim();
                // CHECK FOR NON PRIMITIVE TYPE FOR IMPORT
                importSelectionDialog.clearOptions(); // clear last list of options
                if(!isPrimitiveType(type)) {
                    for(String s : APIs.apis) {
                        if(s.contains(type)) {
                            importSelectionDialog.addImportSelection(s);
                        }
                    }
                    importSelectionDialog.setImportTarget(type);
                    importSelectionDialog.show(imports);
                }
                String varName = v.getName().getText().trim();
                // FIXME: ONLY COMPOSE CODE IF THERE'S VARIABLE 
                // COMPOSE SKELETON CODE 
                variableAsCode = variableAsCode + (access + " " + staticStatus + " " + type + " " + varName).replaceAll("\\s+", " ") + ";\n";
                variablesAsCode+=variableAsCode;
            }

        return variablesAsCode;
    }
    
    /**
     * Helper method to compose code for exporting.
     * @param componentUI
     * @return 
     */
    public String getMethodAsCode(VBox componentUI, ImportSelectionDialog importSelectionDialog, ArrayList<String> imports) {
        boolean isAbstractMethod = false;
        String methodsAsCode = "";
        ArrayList<Method> methods = new ArrayList<>();
        if(componentUI instanceof AddClassUI) {
            AddClassUI classUI = (AddClassUI)componentUI;
            methods = classUI.getMethods();
        } else if(componentUI instanceof AddInterfaceUI) {
            AddInterfaceUI interfaceUI = (AddInterfaceUI)componentUI;
            methods = interfaceUI.getMethods();
        }
        
        // COMPOSE METHODS AS CODE 
        for(Method m : methods) {
                String methodAsCode = "    ";
                String access = "";
                if(m.getAccess().getValue()!=null) {
                    access += m.getAccess().getValue().toString();
                }
                String abstractStatus = "";
                if(m.getAbstractStatus().selectedProperty().getValue()) {
                    abstractStatus += "abstract";
                    isAbstractMethod = true;
                }
                String staticStatus = "";
                if(m.getStaticStatus().selectedProperty().getValue()) {
                    staticStatus += "static";
                }
                String returnType = m.getReturnType().getText().trim();
                // CHECK FOR NON PRIMITIVE TYPE FOR IMPORT
                importSelectionDialog.clearOptions(); // clear last list of options
                if(!isPrimitiveType(returnType)) {
                    for(String s : APIs.apis) {
                        if(s.contains(returnType)) {
                            importSelectionDialog.addImportSelection(s);
                        }
                    }
                    importSelectionDialog.setImportTarget(returnType);
                    importSelectionDialog.show(imports);
                }
                
                String name = m.getName().getText();
                String argAsCode = getArgsAsCode(m);
                
                // FIXME: ONLY COMPOSE CODE IF THERE'S METHOD
                // COMPOSE SKELETON CODE 
                if(isAbstractMethod || (componentUI instanceof AddInterfaceUI)) {
                    methodAsCode = methodAsCode + (access + " " + abstractStatus + " " + staticStatus + " " 
                                    + returnType + " " + name + "(" + argAsCode + ");").replaceAll("\\s+", " ");
                } else {
                    methodAsCode = methodAsCode + (access + " " + abstractStatus + " " + staticStatus + " " 
                                    + returnType + " " + name + "(" + argAsCode + ") {").replaceAll("\\s+", " ");
                    if(returnType.equals("void") || returnType.equals("")) {
                        methodAsCode += ("\n\n    }\n");
                    } else {
                        methodAsCode += ("\n        throw new UnsupportedOperationException(\"Not supported yet.\");\n    }\n");
                    }
                }
                // add method code to list of method codes
                methodsAsCode+=methodAsCode;
            }
        
        return methodsAsCode;
    }
    
    /**
     * Helper method to compose code for exporting.
     * @param m
     * @return 
     */
    public String getArgsAsCode(Method m) {
        String argsAsCode = "";
        ArrayList<TextField> argTypes = m.getArgTypes();        
        for(int i = 0; i < argTypes.size(); i++) {
            if(i == 0) {
                if(!argTypes.get(i).getText().trim().equals("")) {
                    argsAsCode = argsAsCode + (argTypes.get(i).getText() + " arg" + (i+1)).trim();
                }
            } else {
                if(!argTypes.get(i).getText().trim().equals("")) {
                    argsAsCode = argsAsCode + (", " + argTypes.get(i).getText() + " arg" + (i+1)).trim();
                }
            }
        }
        return argsAsCode;
    }
    
    public ArrayList<String> getPackageNameForExport() {
        return packageNameForExport;
    }
    
    public ArrayList<String> getCodeForExport() {
        return codeForExport;
    }
    
    public void setAppState(AppState state) {
        this.state = state;
    }
    public AppState getAppState() {
        return state;
    }
    
    /**
     * Upon saving request, 
     */
    public void exportToRawData() {
        
    }
    public ArrayList<ComponentUI_DataWrapper> getRawData() {
        return RawDataBank.data;
    }
    
    public ArrayList<VBox> getAllDiagrams() {
        ArrayList<VBox> allDiagrams = new ArrayList<>();
        // GETHER ALL THE DIAGRAMS TOGETHER
        for(VBox v : getDiagrams()) { allDiagrams.add(v);}
        for(VBox v : getExtraDiagrams()) { allDiagrams.add(v);}
        return allDiagrams;
    }
    
    /**
     * Checks if the diagram is used by other members in the design.
     * @param componentUI diagram's associated componentUI
     * @param nameOfDiagram name of the diagram to be checked
     * @return true if the diagram is in use, false otherwise.
     */
    public boolean isDiagramInUse(VBox ui, String nameOfDiagram) {
        boolean inUse = false;
        for(VBox componentUI : getComponentUIs().values()) {
            ArrayList<String> connectedDiagrams;
            if(componentUI instanceof AddClassUI) {
                connectedDiagrams = ((AddClassUI)componentUI).getListofConnectedDiagrams();
            } else {
                connectedDiagrams = ((AddInterfaceUI)componentUI).getListofConnectedDiagrams();
            }
            for(String s : connectedDiagrams) {
                if(s.equals(nameOfDiagram)) {
                    inUse = true;
                    break;
                }
            }
        }
        
        return inUse;
    }
    
    public void removeExtraDiagramByName(String name) {
        for(VBox v : extraDiagrams) {
            if(((DiagramGenerator)v).getDiagramClassNameLabel().getText().equals(name)) {
                extraDiagrams.remove(v);
                break;
            }
        }
    }
    /**
     * Checks if the type of a variable is primitive.
     * (Used in exporting code, the user should import
     * the package of the java api used)
     * @param type type of a variable to be checked.
     * @return true if the type of the variable is primitive, 
     * false otherwise.
     */
    public boolean isPrimitiveType(String type) {
        if(type.equals("int") || type.equals("boolean")
        || type.equals("String") || type.equals("char") 
        || type.equals("long") || type.equals("short")
        || type.equals("double") || type.equals("float")
        || type.equals("void")) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Resets the application to new.
     */
    @Override
    public void reset() {
        state = AppState.NEW_STATE;
        this.classNames.clear();
        this.codeForExport.clear();
        this.componentUIList.clear();
        this.diagrams.clear();
        this.packageNameForExport.clear();
        this.packageNames.clear();
        // clear data
        
        RawDataBank.data.clear();

        // CLEAR THE WORKSPACE
        if(app/*.getWorkspaceComponent()*/!=null) {
            Workspace workspace = ((Workspace)app.getWorkspaceComponent());
            workspace.getWorkPane().getChildren().clear();
            workspace.getComponentToolbarPane().getChildren().clear();
            workspace.getGrid_cbx().selectedProperty().set(false);
            workspace.getSnap_cbx().selectedProperty().set(false);
            workspace.getWorkPane().setScaleX(1);
            workspace.getWorkPane().setScaleY(1);
        }
    }
}
