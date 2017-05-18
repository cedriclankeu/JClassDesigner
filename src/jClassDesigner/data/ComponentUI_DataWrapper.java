
package jClassDesigner.data;

import java.util.ArrayList;

/**
 *
 * @author Jia Sheng Ma
 */
public class ComponentUI_DataWrapper {
    
    private String type;
    private String name;
    private String packageName;
    private String parent;
    private ArrayList<String> implementedInterfaces;
    private ArrayList<Variable_DataWrapper> variables;
    private ArrayList<Method_DataWrapper> methods;
    private double diagram_translateX;
    private double diagram_translateY;
    private ArrayList<SplittableConnector_DataWrapper> connectors;
    
    public ComponentUI_DataWrapper(String type) {
        this.type = type;
        implementedInterfaces = new ArrayList<>();
        variables = new ArrayList<>();
        methods = new ArrayList<>();
        connectors = new ArrayList<>();
    }
    public ComponentUI_DataWrapper() {
        implementedInterfaces = new ArrayList<>();
        variables = new ArrayList<>();
        methods = new ArrayList<>();
        connectors = new ArrayList<>();
    }
    
    //***************************** setters *****************************//
    /**
     * Sets the type of this component.
     * @param type Type of this component (Class, Interface, Abstract Class)
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * Sets the name of this component.
     * @param name Name of this component (Class, Interface, Abstract Class).
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Sets the package name of this component.
     * @param name Package name of this component (Class, Interface, Abstract Class).
     */
    public void setPackageName(String name) {
        packageName = name;
    }
    /**
     * Sets the parent of this component.
     * @param parent Parent of this component (Class, Interface, Abstract Class).
     */
    public void setParent(String parent) {
        this.parent = parent;
    }
    public void addImplementedInterface(String implementedInterface) {
        implementedInterfaces.add(implementedInterface);
    }
    public void addVariable(Variable_DataWrapper var) {
        variables.add(var);
    }
    public void addMethod(Method_DataWrapper method) {
        methods.add(method);
    }
    public void setTranslateX(double x) {
        diagram_translateX = x;
    }
    public void setTranslateY(double y) {
        diagram_translateY = y;
    }
    public void addConnector(SplittableConnector_DataWrapper connector) {
        connectors.add(connector);
    }
    
    //***************************** getters *****************************//
    /**
     * Receives the type of this component (Class, Interface, Abstract Class).
     * @return the type of this component.
     */
    public String getType() {
        return type;
    }
    /**
     * Receives the name of this component (Class, Interface, Abstract Class)..
     * @return  the name of this component.
     */
    public String getName() {
        return name;
    }
    public String getPackage() {
        return packageName;
    }
    public String getExtendedParent() {
        return parent;
    }
    public ArrayList<String> getImplementedInterfaces() {
        return implementedInterfaces;
    }
    public ArrayList<Variable_DataWrapper> getVariables() {
        return variables;
    }
    public ArrayList<Method_DataWrapper> getMethods() {
        return methods;
    }
    public double getTranslateX() {
        return diagram_translateX;
    }
    public double getTranslateY() {
        return diagram_translateY;
    }
    public ArrayList<SplittableConnector_DataWrapper> getConnectors() {
        return connectors;
    }
}
