package jClassDesigner;

import javafx.stage.Screen;

/**
 *
 * @author Jia Sheng Ma
 * @version 1.0
 */
public class Constants {
    
    public static final String VBOXES = "vboxes";
    public static final String HBOXES = "hboxes";
    public static final String CLASSUI = "classUI";
    public final static String EDIT_TOOLBAR_PANE = "editToolbarPane";
    public final static String VIEW_TOOLBAR_PANE = "viewToolbarPane";
    public final static String GRID_SNAP_VBOX = "gridSnap_vbox";
    public final static String TEXT_VBOX = "text_vbox";
    public final static String CHECKBOX = "cbx";
    public static final String WORKSPACE = "workspace";
    public static final String WORKSPACE_PANES = "workspace_panes";
    public static final String DIAGRAM = "diagram";
    public static final String DIAGRAM_COMPONENTS = "diagram_components";
    
    public static final double DEFAULT_WIDTH = 80.0;
    public static final double DEFAULT_HEIGHT = 50.0;
    public static final String DEFAULT_CLASS_NAME = "Class Name";
    public static final String DEFAULT_INTERFACE_NAME = "Interface Name";
    public static final String INTERFACE_HEADER = "<<interface>>";
    
    public static final String ABSTRACT_CLASS_HEADER = "{abstract}";
    
    public static final double SPACING = 10;
    public static final double CLASSUI_SPACING = 25;
    public static final double WORKPANE_WIDTH = Screen.getPrimary().getBounds().getWidth()*2.0/3.0*2.0;
    public static final double WORKPANE_HEIGHT = Screen.getPrimary().getBounds().getHeight()*2.0;
    public static final double SCROLL_WORKPANE_WIDTH = Screen.getPrimary().getBounds().getWidth()*2.0/3.0;
    public static final double SCROLL_WORKPANE_HEIGHT = Screen.getPrimary().getBounds().getHeight();
    public static final double COMPONENT_TOOLBAR_WIDTH = Screen.getPrimary().getBounds().getWidth() - SCROLL_WORKPANE_WIDTH;
    public static final double COMPONENT_TOOLBAR_HEIGHT = Screen.getPrimary().getBounds().getHeight();
    
    public static final double VAR_METHOD_WIDTH = COMPONENT_TOOLBAR_WIDTH * 10.0/11.0;
    public static final double VAR_METHOD_SCROLLPANE_WIDTH = VAR_METHOD_WIDTH*1.1;
    public static final double HBOXES_WIDTH = VAR_METHOD_WIDTH/2.0;
            
    public static final double VARIABLE_BOX_WIDTH = VAR_METHOD_WIDTH/4.0;
    public static final double VARIABLE_BOX_HEIGHT = VARIABLE_BOX_WIDTH * 3.0/5.0;
    public static final double METHOD_BOX_WIDTH = COMPONENT_TOOLBAR_WIDTH/6.0;
    public static final double METHOD_BOX_HEIGHT = VARIABLE_BOX_WIDTH * 3.0/5.0;
    
    public static final String PUBLIC = "public";
    public static final String PRIVATE = "private";
    public static final String PROTECTED = "protected";
    public static final String STATIC = "static";
    public static final int PUBLIC_INDEX = 0;
    public static final int PROTECTED_INDEX = 1;
    public static final int PRIVATE_INDEX = 2;
    
    public static final String CLASS = "class";
    public static final String INTERFACE = "interface";
    public static final String ABSTRACT_CLASS = "abstract class";
    
    public static final String PARENT_CONNECTOR = "parent connector";
    public static final String AGGREGATION_CONNECTOR = "aggregation connector";
    public static final String USES_CONNECTOR = "uses connector";
    public static final String INTERFACE_CONNECTOR = "interface connector";
    
    public static final String EXTERNAL_CLASS = "Add External Class";
    
    public static final double RESIZE_MARGIN = 5.0;
}
