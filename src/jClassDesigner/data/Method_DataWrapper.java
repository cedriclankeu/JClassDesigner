
package jClassDesigner.data;

import java.util.ArrayList;

/**
 *
 * @author Jia Sheng Ma
 */
public class Method_DataWrapper {
    private String method_type;
    private String method_name;
    private boolean method_static;
    private boolean method_abstract;
    private String method_access;
    private ArrayList<String> method_argument;
    
    public Method_DataWrapper() {
        method_argument = new ArrayList<>();
    }
    
    // setters
    public void setReturnType(String type) {
        method_type = type;
    } 
    public void setName(String name) {
        method_name = name;
    }
    public void setStatic(boolean s) {
        method_static = s;
    }
    public void setAbstract(boolean a) {
        method_abstract = a;
    }
    public void setAccess(String access) {
        method_access = access;
    }
    public void addArgument(String arg) {
        method_argument.add(arg);
    }
    
    // getters
    public String getReturnType() {
        return method_type;
    }
    public String getName() {
        return method_name;
    }
    public boolean getMethodStatic() {
        return method_static;
    }
    public boolean getAbstract() {
        return method_abstract;
    }
    public String getAccess() {
        return method_access;
    }
    public ArrayList<String> getArguments() {
        return method_argument;
    }
}
