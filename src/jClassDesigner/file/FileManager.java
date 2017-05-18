package jClassDesigner.file;

import jClassDesigner.Constants;
import jClassDesigner.data.ComponentUI_DataWrapper;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;
import jClassDesigner.data.DataManager;
import jClassDesigner.data.Method_DataWrapper;
import jClassDesigner.data.RawDataBank;
import jClassDesigner.data.SplittableConnector_DataWrapper;
import jClassDesigner.data.Variable_DataWrapper;
import jClassDesigner.gui.SplittableLine;
import java.math.BigDecimal;
import saf.AppTemplate;

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @author Jia Sheng Ma
 * @version 1.0
 */
public class FileManager implements AppFileComponent {
    
    AppTemplate app;
    
    // FOR JSON LOADING
        // DIAGRAM PROPERTIES
    static final String DIAGRAM_CLASS_NAME = "diagram_class_name";
    static final String LAYOUT_X = "diagram_layoutX";
    static final String LAYOUT_Y = "diagram_layoutY";
    static final String DIAGRAM_WIDTH = "diagram_width";
    static final String DIAGRAM_HEIGHT = "diagram_height";
    static final String VARIABLES = "variables";
    static final String METHODS = "methods";
    
    static final String DIAGRAMS = "diagrams";
    static final String COMPONENT_UIS = "component_uis";
    static final String TYPE = "type";
    static final String NAME = "name";
    static final String CLASS_NAME = "class_name";
    static final String INTERFACE_NAME = "interface_name";
    static final String PACKAGE_NAME = "package_name";
    static final String PARENT = "parent";
    static final String INTERFACES = "interfaces";
    static final String TRANSLATE_X= "translate_X";
    static final String TRANSLATE_Y= "translate_Y";
    
    static final String VARIABLE_ACCESS = "variable_access";
    static final String VARIABLE_STATIC = "variable_static";
    static final String VARIABLE_TYPE = "variable_type";
    static final String VARIABLE_NAME = "variable_name";
    
    static final String METHOD_ACCESS = "method_access";
    static final String METHOD_ABSTRACT = "method_abstract";
    static final String METHOD_STATIC = "method_static";
    static final String METHOD_RETURN_TYPE = "method_return_type";
    static final String METHOD_NAME = "method_name";
    static final String METHOD_ARGUMENTS = "method_arguments";
    static final String ARG_NAME = "arg";
    static final String ARG_TYPE = "arg_type";
    
    static final String CONNECTORS = "connectors";
    static final String CONNECTOR_TYPE = "connector_type";
    static final String ANCHORS = "anchors";
    static final String ANCHOR_X = "anchor_x";
    static final String ANCHOR_Y = "anchor_y";
    
    public static final String CODE_PATH = "./code/";
    public static final String PHOTO_PATH = "./photo";
            
        // COMPONENT UI PROPERTIES
//    static final String 
 
    /**
     * Default constructor.
     * @param app this application.
     */
    public FileManager(AppTemplate app) {

        this.app = app;
    }
    
    
    /**
     * This method is for saving user work, which in the case of this
     * application means the data that constitutes the page DOM.
     * 
     * @param data The data management component for this application.
     * 
     * @param filePath Path (including file name/extension) to where
     * to save the data to.
     * 
     * @throws IOException Thrown should there be an error writing 
     * out data to the file.
     */
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	DataManager dataManager = (DataManager)data;
	
        // GET UI COMPONENTS 
//        HashMap<VBox, VBox> componentUIs = dataManager.getComponentUIs();
        
        if(app!=null) {
            RawDataBank.extractDataFromDataManager(dataManager);
            System.out.println(">>> Data extracted from DataManager to RawDataBank successfully ");
        }
        ArrayList<ComponentUI_DataWrapper> componentUIs = RawDataBank.data;
        // CREATE JSON ARRAY BUILDER 
        JsonArrayBuilder componentUIsArrayBuilder = Json.createArrayBuilder();
        // FILL JSON ARRAY BUILDER WITH UI COMPONENTS - TO MAKE A UI COMPONENT JSON ARRAY
        fillJABuilderWithComponentUI(componentUIs, componentUIsArrayBuilder);
        JsonArray componentUIsArray = componentUIsArrayBuilder.build();
	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(COMPONENT_UIS, componentUIsArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    /**
     * 
     * @param ui
     * @return 
     */
    public JsonObject makeComponentUIJsonObject(ComponentUI_DataWrapper ui) {
        // TODO: DO ADDCLASS UI FIRST, INTERFACE UI LATER
        
        
//        if(ui.getType().equals(Constants.CLASS)) {
            double diagram_translateX = ui.getTranslateX();
            double diagram_translateY = ui.getTranslateY();
            // MAKE INTERFACES JSON ARRAY
            JsonArrayBuilder interfaceArrayBuilder = Json.createArrayBuilder();
            fillJABuilderWithInterfaces(ui.getImplementedInterfaces(), interfaceArrayBuilder);
            // MAKE VARIABLE JSON ARRAY
            JsonArrayBuilder variablesArrayBuilder = Json.createArrayBuilder();
            fillJABuilderWithVariables(ui.getVariables(), variablesArrayBuilder);
            // MAKE METHOD JSON ARRAY
            JsonArrayBuilder methodsArrayBuilder = Json.createArrayBuilder();
            fillJABuilderWithMethods(ui.getMethods(), methodsArrayBuilder);
            // MAKE CONNECTOR JSON ARRAY
            JsonArrayBuilder connectorsArrayBuilder = Json.createArrayBuilder();
            fillJABuilderWithConnectors(ui.getConnectors(), connectorsArrayBuilder);
            
            JsonObject componentUIJson = Json.createObjectBuilder()
                    .add(TYPE, ui.getType())
                    .add(NAME, ui.getName())
                    .add(PACKAGE_NAME, ui.getPackage())
                    .add(PARENT, ui.getExtendedParent())
                    .add(INTERFACES, interfaceArrayBuilder)
                    .add(VARIABLES, variablesArrayBuilder)
                    .add(METHODS, methodsArrayBuilder)
                    .add(TRANSLATE_X, diagram_translateX+"")
                    .add(TRANSLATE_Y, diagram_translateY+"")
                    .add(CONNECTORS, connectorsArrayBuilder)
                    .build();
            return componentUIJson;
//        } else {
//            // ELSE IT IS AN INTERFACE
//            double diagram_translateX = ui.getTranslateX();
//            double diagram_translateY = ui.getTranslateY();
//            // MAKE VARIABLE JSON ARRAY
//            JsonArrayBuilder variablesArrayBuilder = Json.createArrayBuilder();
//            fillJABuilderWithVariables(ui.getVariables(), variablesArrayBuilder);
//            // MAKE METHOD JSON ARRAY
//            JsonArrayBuilder methodsArrayBuilder = Json.createArrayBuilder();
//            fillJABuilderWithMethods(ui.getMethods(), methodsArrayBuilder);
//            // MAKE CONNECTOR JSON ARRAY
//            JsonArrayBuilder connectorsArrayBuilder = Json.createArrayBuilder();
//            fillJABuilderWithConnectors(ui.getConnectors(), connectorsArrayBuilder);
//            
//            JsonObject componentUIJson = Json.createObjectBuilder()
//                    .add(TYPE, ui.getType())
//                    .add(NAME, ui.getName())
//                    .add(PACKAGE_NAME, ui.getPackage())
//                    .add(VARIABLES, variablesArrayBuilder)
//                    .add(METHODS, methodsArrayBuilder)
//                    .add(TRANSLATE_X, diagram_translateX+"")
//                    .add(TRANSLATE_Y, diagram_translateY+"")
//                    .add(CONNECTORS, connectorsArrayBuilder)
//                    .build();
//            return componentUIJson;
//        }
    }
    /**
     * 
     * @param componentUIs 
     * @param arrayBuilder 
     */
    public void fillJABuilderWithComponentUI(ArrayList<ComponentUI_DataWrapper> componentUIs, JsonArrayBuilder arrayBuilder) {
        for(ComponentUI_DataWrapper ui : componentUIs) {
            JsonObject componentUIObject = makeComponentUIJsonObject(ui);
            arrayBuilder.add(componentUIObject);
        }
    }
    
    /**
     * Parses the Variable object to a json object.
     * @param variable target to be parsed.
     * @return a json object representing this Variable object.
     */
    public JsonObject makeVariableJsonObject(Variable_DataWrapper variable) {
        
        String access = "";
        if(variable.getVariableAccess()!=null) {
            access+=variable.getVariableAccess();
        }
        boolean staticStatus =  variable.getVariableStatic();
        JsonObject variableJson = Json.createObjectBuilder()
                    .add(VARIABLE_ACCESS, access)
                    .add(VARIABLE_STATIC, staticStatus)
                    .add(VARIABLE_TYPE, variable.getType())
                    .add(VARIABLE_NAME, variable.getName())
                    .build();
        return variableJson;
    }
    /**
     * Fills the JsonArrayBuilder with variable json objects.
     * @param variables a list of Variable objects
     * @param arrayBuilder JsonArrayBuilder to be filled.
     */
    public void fillJABuilderWithVariables(ArrayList<Variable_DataWrapper> variables, JsonArrayBuilder arrayBuilder) {
        for(Variable_DataWrapper var : variables) {
            JsonObject variableJsonObject = makeVariableJsonObject(var);
            arrayBuilder.add(variableJsonObject);
        }
    } // end of make variable json array
    
    /**
     * Parses the Variable object to a json object.
     * @param method
     * @return a json object representing this Variable object.
     */
    public JsonObject makeMethodJsonObject(Method_DataWrapper method) {
        boolean abstractStatus = method.getAbstract();
        String access="";
        if(method.getAccess()!=null){
            access = checkAndGetAccess(method.getAccess());
        }
        
        boolean staticStatus =  method.getMethodStatic();
        
        // MAKE ARGUMENT JSON ARRAY
        JsonArrayBuilder argsArrayBuilder = Json.createArrayBuilder();
        fillJABuilderWithArgs(method.getArguments(), argsArrayBuilder);
        
        JsonObject methodJson = Json.createObjectBuilder()
                    .add(METHOD_ABSTRACT, abstractStatus)
                    .add(METHOD_ACCESS, access)
                    .add(METHOD_STATIC, staticStatus)
                    .add(METHOD_RETURN_TYPE, method.getReturnType())    
                    .add(METHOD_NAME, method.getName())
                    .add(METHOD_ARGUMENTS, argsArrayBuilder)
                    .build();
        return methodJson;
    }
    /**
     * Fills the JsonArrayBuilder with Method json objects.
     * @param methods
     * @param arrayBuilder JsonArrayBuilder to be filled.
     */
    public void fillJABuilderWithMethods(ArrayList<Method_DataWrapper> methods, JsonArrayBuilder arrayBuilder) {
        for(Method_DataWrapper m : methods) {
            JsonObject methodJsonObject = makeMethodJsonObject(m);
            arrayBuilder.add(methodJsonObject);
        }
    } // end of make method json array
    
    public JsonObject makeImplementedInterfaceJsonObject(String implementedInterface) {
        JsonObject interfaceJsonObject = Json.createObjectBuilder().add(INTERFACE_NAME, implementedInterface).build();
        return interfaceJsonObject;
    }
    public void fillJABuilderWithInterfaces(ArrayList<String> implementedInterfaces, JsonArrayBuilder arrayBuilder) {
        System.out.println("interface\t-\t");
        for(int i = 0; i < implementedInterfaces.size(); i++) {
            String s = implementedInterfaces.get(i);
            System.out.println(s + " ");
            JsonObject interfaceJsonObject = makeImplementedInterfaceJsonObject(s);
            arrayBuilder.add(interfaceJsonObject);
        }
    }
    
    public JsonObject makeArgumentJsonObject(String arg) {
        JsonObject argJson = Json.createObjectBuilder().add(ARG_TYPE, arg).build();
        return argJson;
    }
    public void fillJABuilderWithArgs(ArrayList<String> argTypes, JsonArrayBuilder arrayBuilder) {
        
        for(int i = 0; i < argTypes.size(); i++) {
            JsonObject argJsonObject = makeArgumentJsonObject(argTypes.get(i));
            arrayBuilder.add(argJsonObject);
        }
    }
    
    /******************************************* Connector JsonObject *******************************************/
    
    public void fillJABuilderWithConnectors(ArrayList<SplittableConnector_DataWrapper> connectors, JsonArrayBuilder connectorsArrayBuilder) {
        for(SplittableConnector_DataWrapper c : connectors) {
            JsonObject connectorJson = makeConnectorJsonObject(c);
            connectorsArrayBuilder.add(connectorJson);
        }
    }
    
    public JsonObject makeConnectorJsonObject(SplittableConnector_DataWrapper connector) {
        ArrayList<Double> anchorXes = connector.getAnchorXes();
        ArrayList<Double> anchorYs = connector.getAnchorYs();
        JsonArrayBuilder anchorsArrayBuilder = Json.createArrayBuilder();
        fillJABuilderWithAnchors(anchorXes, anchorYs, anchorsArrayBuilder);
        
        JsonObject connectorJson = Json.createObjectBuilder()
                    .add(CONNECTOR_TYPE,connector.getType())
                    .add(ANCHORS, anchorsArrayBuilder)
                    .build();
        return connectorJson;
    }
    
    private void fillJABuilderWithAnchors(ArrayList<Double> anchorXes, ArrayList<Double> anchorYs, JsonArrayBuilder anchorsArrayBuilder) {
        for(int i = 0; i < anchorXes.size(); i++) {
            JsonObject anchor = makeAnchorJsonObject(anchorXes.get(i), anchorYs.get(i));
            anchorsArrayBuilder.add(anchor);
        }
    }
    private JsonObject makeAnchorJsonObject(double x, double y) {
        JsonObject anchorJson = Json.createObjectBuilder()
                    .add(ANCHOR_X, x+"")
                    .add(ANCHOR_Y, y+"")
                    .build();
        return anchorJson;
    }
    // end of creation of Connector JsonObject
    
    /**
     * This method loads data from a JSON formatted file into the data 
     * management component and then forces the updating of the workspace
     * such that the user may edit the data.
     * 
     * @param data Data management component where we'll load the file into.
     * 
     * @param filePath Path (including file name/extension) to where
     * to load the data from.
     * 
     * @throws IOException Thrown should there be an error reading
     * in data from the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
	// CLEAR THE OLD DATA OUT
	DataManager dataManager = (DataManager)data;
	dataManager.reset();
	
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
	JsonArray componentUIsArray = json.getJsonArray(COMPONENT_UIS);
	
	// LOAD COMPONENT UIS(WITH DIAGRAMS)
        loadComponentUIs(componentUIsArray, dataManager);
    }
    
    public double getDataAsDouble(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().doubleValue();	
    }
    
    /**
     * Loads component UI
     * @param jso
     * @return 
     */
    private ComponentUI_DataWrapper loadComponentUI(JsonObject jso) {
//        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        ComponentUI_DataWrapper componentUI = new ComponentUI_DataWrapper();
        
        System.out.println("---------------------------------------------------------------------------------");
        // LOAD DATA
        String type = jso.getString(TYPE);
            System.out.println("type\t\t-\t" + type);
        String name = jso.getString(NAME);
            System.out.println("name\t\t-\t" + name);
        String package_name = jso.getString(PACKAGE_NAME);
            System.out.println("package_name\t-\t" + package_name);
        String parent_name = jso.getString(PARENT);
            System.out.println("parent\t\t-\t" + parent_name);

            // SET VALUES
        componentUI.setType(type);
        componentUI.setName(name);
        componentUI.setPackageName(package_name);
        componentUI.setParent(parent_name);
        
        JsonArray interfaceJsonArray = jso.getJsonArray(INTERFACES);
        loadImplementedInterfaces(interfaceJsonArray, componentUI);
        
        // LOAD VARIABLES
        JsonArray variablesJsonArray = jso.getJsonArray(VARIABLES);
        loadVariables(variablesJsonArray, componentUI);
        
        // LOAD METHODS
        JsonArray methodsJsonArray = jso.getJsonArray(METHODS);
        loadMethods(methodsJsonArray, componentUI);
        
        // LOAD DIAGRAM POSITION
        double diagram_translateX = Double.parseDouble(jso.getString(TRANSLATE_X));
        double diagram_translateY = Double.parseDouble(jso.getString(TRANSLATE_Y));
        componentUI.setTranslateX(diagram_translateX);
        componentUI.setTranslateY(diagram_translateY);
        System.out.println("Diagram translateX: " + diagram_translateX);
        System.out.println("Diagram translateY: " + diagram_translateY);
        
        // LOAD CONNECTORS
        JsonArray connectorJsonArray = jso.getJsonArray(CONNECTORS);
        loadConnectors(connectorJsonArray, componentUI);
        // TODO: print connectors
        System.out.println("Number of connectors: " + connectorJsonArray.size());
        
        System.out.println("---------------------------------------------------------------------------------");
        return componentUI;
    }
    
    private void loadComponentUIs(JsonArray componentUIsArray, DataManager dataManager) {

        for(int i = 0; i < componentUIsArray.size(); i++) {
            JsonObject jso = componentUIsArray.getJsonObject(i);
            ComponentUI_DataWrapper componentUI = loadComponentUI(jso);
            
            // ADD LOADED DATA TO DATA MANAGER
            // dataManager.addComponentUI(diagram, componentUI);
            // dataManager.addDiagram(diagram);
            RawDataBank.data.add(componentUI);
            
        }
    }
    private String loadImplementedInterface(JsonObject jso) {
        return jso.getString(INTERFACE_NAME);
    }
    private void loadImplementedInterfaces(JsonArray interfaceJsonArray, ComponentUI_DataWrapper componentUI) {
        for(int i = 0; i < interfaceJsonArray.size(); i++) {
            componentUI.addImplementedInterface(loadImplementedInterface(interfaceJsonArray.getJsonObject(i)));
        }
    }

    private Variable_DataWrapper loadVariable(JsonObject jso) {
        // LOAD DATA FROM JSON
        String variableAccess = jso.getString(VARIABLE_ACCESS);
        String variableType = jso.getString(VARIABLE_TYPE);
        String variableName = jso.getString(VARIABLE_NAME);
        boolean variableStatic = jso.getBoolean(VARIABLE_STATIC);
        
        // CREATE VARIABLE OBJECT
        Variable_DataWrapper variable = new Variable_DataWrapper();
        variable.setAccess(variableAccess);
        variable.setName(variableName);
        variable.setType(variableType);
        variable.setStatic(variableStatic);
        
        System.out.println("Variable: "
                + "\n\taccess\t-\t" + variableAccess
                + "\n\tstatic\t-\t" + variableStatic
                + "\n\ttype\t-\t" + variableType 
                + "\n\tname\t-\t" + variableName);
        
        return variable;
    }
    private void loadVariables(JsonArray variablesJsonArray, ComponentUI_DataWrapper componentUI) {
        for(int i = 0; i < variablesJsonArray.size(); i++) {
            Variable_DataWrapper variable = loadVariable(variablesJsonArray.getJsonObject(i));
            componentUI.addVariable(variable);
        }
    }
    
    /**
     * Loads all the data associated with Method_DataWrapper.
     * @param jso
     * @return 
     */
    private Method_DataWrapper loadMethod(JsonObject jso) {
        // LOAD DATA FROM JSON
        String methodAccess = jso.getString(METHOD_ACCESS);
        String methodType = jso.getString(METHOD_RETURN_TYPE);
        String methodName = jso.getString(METHOD_NAME);
        boolean methodStatic = jso.getBoolean(METHOD_STATIC);
        boolean methodAbstact = jso.getBoolean(METHOD_ABSTRACT);
        JsonArray argTypesJsonArray = jso.getJsonArray(METHOD_ARGUMENTS);
        
        // CREATE METHOD OBJECT
        Method_DataWrapper method = new Method_DataWrapper();
        method.setAccess(methodAccess);
        method.setName(methodName);
        method.setReturnType(methodType);
        method.setStatic(methodStatic);
        method.setAbstract(methodAbstact);
        
        System.out.println("Method: "
                + "\n\taccess  \t-\t" + methodAccess
                + "\n\tstatic  \t-\t" + methodStatic
                + "\n\tabstract\t-\t" + methodAbstact
                + "\n\ttype    \t-\t" + methodType 
                + "\n\tname    \t-\t" + methodName);
        
        loadArgTypes(argTypesJsonArray, method);
        
        
        return method;
    }
    /**
     * Helper method to load methods.
     * @see loadMethod(JsonObject jso)
     * @param methodsJsonArray
     * @param componentUI 
     */
    private void loadMethods(JsonArray methodsJsonArray, ComponentUI_DataWrapper componentUI) {
        for(int i = 0; i < methodsJsonArray.size(); i++) {
            Method_DataWrapper method = loadMethod(methodsJsonArray.getJsonObject(i));
            componentUI.addMethod(method);
        }
    }
    
    private String loadArgType(JsonObject jso) {
        String argType = jso.getString(ARG_TYPE);
        System.out.println("\targType \t-\t" + argType);
        return argType;
    }
    private void loadArgTypes(JsonArray argTypesArray, Method_DataWrapper method) {
        for(int i = 0; i < argTypesArray.size(); i++) {
            String argType = loadArgType(argTypesArray.getJsonObject(i));
            method.addArgument(argType);
            // add arg for display
        }
    }
    
    private SplittableConnector_DataWrapper loadConnector(JsonObject jso) {
        SplittableConnector_DataWrapper connector = new SplittableConnector_DataWrapper();
        // LOAD AND SET CONNECTOR TYPE
        String type = jso.getString(CONNECTOR_TYPE);
        connector.setType(type);
        // LOAD AND SET ANCHORS 
//        ArrayList<Double> anchorXes = new ArrayList<>();
//        ArrayList<Double> anchorYs = new ArrayList<>();
        JsonArray anchorsArray = jso.getJsonArray(ANCHORS);
        loadAnchors(connector, anchorsArray);
        
        return connector;
    }
    private void loadConnectors(JsonArray connectorsArray, ComponentUI_DataWrapper componentUI) {
        for(int i = 0; i < connectorsArray.size(); i++) {
            SplittableConnector_DataWrapper connector = loadConnector(connectorsArray.getJsonObject(i));
            componentUI.addConnector(connector);
        }
    }
    private void loadAnchors(SplittableConnector_DataWrapper connector, JsonArray anchorsArray) {
        for(int i = 0; i < anchorsArray.size(); i++) {
            JsonObject anchorJso = anchorsArray.getJsonObject(i);
            double x = Double.parseDouble(anchorJso.getString(ANCHOR_X));
            double y = Double.parseDouble(anchorJso.getString(ANCHOR_Y));
            connector.addAnchor(x,y);
        }
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    private String checkAndGetAccess(String access) {
        if(access.equals(Constants.PUBLIC)) {
            access = Constants.PUBLIC;
        } else if(access.equals(Constants.PRIVATE)) {
            access = Constants.PRIVATE;
        } else if(access.equals(Constants.PROTECTED)) {
            access = Constants.PROTECTED;
        } else {
            access = "";
        }
        return access;
    }
    
    /**
     * This method exports the contents of the data manager to a 
     * Web page including the html page, needed directories, and
     * the CSS file.
     * 
     * @param data The data management component.
     * 
     * @param filePath Path (including file name/extension) to where
     * to export the page to.
     * 
     * @throws IOException Thrown should there be an error writing
     * out data to the file.
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        
    }
    
    /**
     * This method is provided to satisfy the compiler, but it
     * is not used by this application.
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {

    }

    

}
