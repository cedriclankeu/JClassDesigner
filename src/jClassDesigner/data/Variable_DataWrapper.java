package jClassDesigner.data;

/**
 *
 * @author Jia Sheng Ma
 */
public class Variable_DataWrapper {
    private String variable_type;
    private String variable_name;
    private boolean variable_static;
    private String variable_access;
    
    public Variable_DataWrapper() {
        
    }
    
    // setters
    public void setType(String type) {
        variable_type = type;
    } 
    public void setName(String name) {
        variable_name = name;
    }
    public void setStatic(boolean s) {
        variable_static = s;
    }
    public void setAccess(String access) {
        variable_access = access;
    }
    
    // getters
    public String getType() {
        return variable_type;
    }
    public String getName() {
        return variable_name;
    }
    public boolean getVariableStatic() {
        return variable_static;
    }
    public String getVariableAccess() {
        return variable_access;
    }
}
