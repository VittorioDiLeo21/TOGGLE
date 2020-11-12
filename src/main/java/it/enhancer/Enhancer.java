package it.enhancer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.printer.JsonPrinter;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.javaparser.ast.CompilationUnit;

public class Enhancer {

    private CompilationUnit compilationUnit;
    private List<Operation> operations;
    private boolean firstTest;
    private StringBuilder parameters;
    private StringBuilder field;

    private Map<String, Integer> statistic;
    private String packageName;

    private String version;

    private Statement captureTask = JavaParser.parseStatement("FutureTask<Boolean> capture_task = null;");
    private Statement instrumentation = JavaParser.parseStatement("Instrumentation instr = InstrumentationRegistry.getInstrumentation();");
    private Statement device = JavaParser.parseStatement("UiDevice device = UiDevice.getInstance(instr);");
    private Statement firstTestDate = JavaParser.parseStatement("Date now = new Date();");
    private Statement firstLogNum = JavaParser.parseStatement("int num = 0;");
    private Statement date = JavaParser.parseStatement("now = new Date();");
    private Statement logNum = JavaParser.parseStatement("num++;");
    private Statement firstTestActivity = JavaParser.parseStatement("Activity activityTOGGLETools = getActivityInstance();");
    private Statement activity = JavaParser.parseStatement("activityTOGGLETools = getActivityInstance();");
    private Statement captureTaskValue = JavaParser.parseStatement("capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTask(now, activityTOGGLETools));");
    private Statement captureTaskValueProgressive = JavaParser.parseStatement("capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTaskProgressive(num, activityTOGGLETools));");
    private TryStmt screenCapture = (TryStmt) JavaParser.parseStatement("try { runOnUiThread(capture_task); } catch (Throwable t) { t.printStackTrace(); }");
    //private Statement dumpScreen = JavaParser.parseStatement("TOGGLETools.DumpScreen(now, device);");
    private Statement dumpScreenProgressive = JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, device);");

    private TryStmt tryStmt = (TryStmt) JavaParser.parseStatement(
            "try {\n" +
            "            Thread.sleep(1000);\n" +
            "        } catch (Exception e) {\n" +
            "\n" +
            "        }");


    public Enhancer(String packageName){
        this.packageName = packageName;
        this.statistic = new HashMap();
    }

    /*private void addToggleToolsImportsToCompilationUnit(){

    }

    public void injectToggleTools(String folderPath){
        long time_begin = System.currentTimeMillis();

        try {
            String filePath = folderPath+"TOGGLETools.java";
            FileInputStream in = new FileInputStream( filePath );
            compilationUnit = JavaParser.parse(in);

            addToggleToolsImportsToCompilationUnit();
            addPrivateField();
            changeConstructorsName();
            addActivityInstanceMethod();

            //visit the body of all methods in the class
            compilationUnit.accept(new MethodVisitor(), null);
            // System.out.println(compilationUnit.toString());

            System.out.println("");
            String fileNameEnhanced = folderPath + fileName + "Enhanced.java";
            System.out.println("Saving everything to " + fileNameEnhanced);

            PrintWriter w = new PrintWriter(fileNameEnhanced,"UTF-8");
            w.print(compilationUnit.toString());
            w.close();

            //save statistics into file
            String statisticFileName = folderPath + fileName + "_Statistic.txt";
            Statistic.writeDataToFile(statistic, statisticFileName);

            //} catch (FileNotFoundException f) {
            //	System.out.println("File: " + filePath + " not found!");
            //} catch (UnsupportedEncodingException u) {
            //	System.out.println("Unsupported encoding on enhanced file");

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            //TODO do something useful to handle different kinds of exceptions
            Utils.logException(e, "generateEnhancedClassFrom for " + filePath);
        }

        long end_time = System.currentTimeMillis();
        long enhance_time = end_time - time_begin;
        System.out.println("Time to enhance = " + enhance_time);
    }*/

    /**
     * Method to enhance a file whose path is filePath.
     * For a generic file at "<folderPath>/<fileName>.java" this method will generate :
     *  - <folderPath>/<fileName>Enhanced.java : The enhanced java class
     *  - <folderPath>/<fileName>_Statistic.txt : a file to collect statistics ?? todo
     *  - Eventually : a line reporting the exception in the log file //todo it could not be essential to be specified
     * @param filePath
     */
    public void generateEnhancedClassFrom(String filePath){
        long time_begin = System.currentTimeMillis();

        try {
            populateEmptyStatistic();
            int slashIndex = filePath.lastIndexOf('/');
            int dotIndex = filePath.lastIndexOf('.');

            String folderPath = filePath.substring(0,slashIndex + 1);
            String fileName = filePath.substring(slashIndex+1,dotIndex);
            FileInputStream in = new FileInputStream( filePath );
            compilationUnit = JavaParser.parse(in);

            addImportsToCompilationUnit();
            addPrivateField();
            changeConstructorsName();
            addActivityInstanceMethod();

            //visit the body of all methods in the class
            compilationUnit.accept(new MethodVisitor(), null);
            // System.out.println(compilationUnit.toString());

            System.out.println("");
            String fileNameEnhanced = folderPath + fileName + "Enhanced.java";
            System.out.println("Saving everything to " + fileNameEnhanced);

            PrintWriter w = new PrintWriter(fileNameEnhanced,"UTF-8");
            w.print(compilationUnit.toString());
            w.close();

            //save statistics into file
            String statisticFileName = folderPath + fileName + "_Statistic.txt";
            Statistic.writeDataToFile(statistic, statisticFileName);

            //} catch (FileNotFoundException f) {
            //	System.out.println("File: " + filePath + " not found!");
            //} catch (UnsupportedEncodingException u) {
            //	System.out.println("Unsupported encoding on enhanced file");

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            //TODO do something useful to handle different kinds of exceptions
            Utils.logException(e, "generateEnhancedClassFrom for " + filePath);
        }

        long end_time = System.currentTimeMillis();
        long enhance_time = end_time - time_begin;
        System.out.println("Time to enhance = " + enhance_time);
    }

    /**
     *
     */
    private void populateStatisticFromFile(String statisticFilePath){
        statistic = Statistic.readDataFromFile(statisticFilePath);
    }

    /**
     * Populate the HashMap containing the statistics ?? //todo
     */
    private void populateEmptyStatistic() { statistic = Statistic.populateInitialMap(); }

    /**
     *
     */
    private void addPrivateField() {
        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = Navigator.findNodeOfGivenClass(compilationUnit,ClassOrInterfaceDeclaration.class);
        classOrInterfaceDeclaration.setName(classOrInterfaceDeclaration.getName()+"Enhanced");

        if(isNotInMembersList(classOrInterfaceDeclaration,"currentActivity")){
            BodyDeclaration<?> field = JavaParser.parseBodyDeclaration("private Activity currentActivity;");
            classOrInterfaceDeclaration.getMembers().add(0,field);
        }
    }

    /**
     *
     */
    private void changeConstructorsName(){
        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = Navigator.findNodeOfGivenClass(compilationUnit,ClassOrInterfaceDeclaration.class);
        List<ConstructorDeclaration> constructors = classOrInterfaceDeclaration.getConstructors();
        for(ConstructorDeclaration constructor : constructors){
            constructor.setName(constructor.getName() + "Enhanced");
        }
    }

    /**
     *
     */
    private void addImportsToCompilationUnit(){
        NodeList<ImportDeclaration> imports = compilationUnit.getImports();
        this.version = "android.support.";

        for(ImportDeclaration i : imports){
            String name = i.getNameAsString();
            if(name.startsWith("android.support."))
                break;
            else if(name.startsWith("androidx.")){
                version = "androidx.";
                break;
            }
        }
        // imports only if it does not exist
        //compilationUnit.addImport(packageName + ".TOGGLETools", false, false);
        this.compilationUnit.addImport("java.util.Date", false, false);
        this.compilationUnit.addImport("android.app.Activity", false, false);
        this.compilationUnit.addImport("android.app.Instrumentation", false, false);
        this.compilationUnit.addImport("java.util.Collection", false, false);
        this.compilationUnit.addImport(version + "test.InstrumentationRegistry", false, false);
        this.compilationUnit.addImport(version + "test.InstrumentationRegistry.getInstrumentation", true, false);
        this.compilationUnit.addImport("android.widget.TextView", false, false);
        this.compilationUnit.addImport(version + "test.runner.lifecycle.ActivityLifecycleMonitorRegistry", false, false);
        this.compilationUnit.addImport(version + "test.uiautomator.UiDevice", false, false);
        this.compilationUnit.addImport("android.graphics.Rect", false, false);
        this.compilationUnit.addImport("java.util.concurrent.FutureTask", false, false);
        this.compilationUnit.addImport(version + "test.runner.lifecycle.Stage.RESUMED", true, false);
        this.compilationUnit.addImport(version + "test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread", true, false);
    }

    private void addActivityInstanceMethod() {
        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = Navigator.findNodeOfGivenClass(compilationUnit,ClassOrInterfaceDeclaration.class);

        if(isNotInMembersList(classOrInterfaceDeclaration,"getActivityInstance")){
            MethodDeclaration method = new MethodDeclaration();

            String body =
                    "{" +
                            "   getInstrumentation().runOnMainSync(new Runnable() {\n" +
                            "       public void run() {\n" +
                            "           Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);\n" +
                            "           if (resumedActivities.iterator().hasNext()){\n" +
                            "               currentActivity = (Activity) resumedActivities.iterator().next();\n" +
                            "           }\n" +
                            "       }\n" +
                            "   });\n" +
                            "\n" +
                            "   return currentActivity;" +
                    "}";
            method.setName("getActivityInstance");
            method.setPublic(true);
            method.setType("Activity");
            BlockStmt b = JavaParser.parseBlock(body);
            method.setBody(b);

            // adds the method at the bottom of the class. The private field
            // "currentActivity" is included in the members
            classOrInterfaceDeclaration.getMembers().add(classOrInterfaceDeclaration.getMembers().size(),method);
        }
    }

    private boolean isNotInMembersList(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, String member){
        NodeList<BodyDeclaration<?>> members = classOrInterfaceDeclaration.getMembers();
        for(BodyDeclaration<?> mbr : members){
            if((mbr.isFieldDeclaration() && mbr.getChildNodes().get(0).toString().equals(member))
            || (mbr instanceof MethodDeclaration && ((MethodDeclaration)mbr).getName().getIdentifier().equals(member)))
                return false;
        }
        return true;
    }

    private class MethodVisitor extends ModifierVisitor<Void> {
        @Override
        public MethodDeclaration visit(MethodDeclaration method, Void arg){
            /*
             * Here you can access the attributes of the method. This method will be called
             * for all methods in this compilation unit, including inner class methods
             */
            super.visit(method,arg);
            //todo : add a check on the optional and act consequently
            BlockStmt block = method.getBody().get();

            String body = block.toString();
            String methodName = method.getNameAsString();
            if(body.contains("onView") ||
            body.contains("onData") ||
            body.contains("intended") ||
            body.contains("intending")){
                NodeList<Statement> nodes = block.getStatements();
                firstTest = true;
                parameters = new StringBuilder("");
                field = new StringBuilder("");

                //scan each statement
                int i = 0;
                while(i < nodes.size())
                    //gets the new index because the method has been enhanced
                    i = parseStatement(block,methodName,nodes.get(i),i);

                //add fullcheck at the bottom of the method
                if(!firstTest)
                    addFullCheck(block,methodName,i);
            }

            return method;
        }
    }

    private void parseJsonScope(JSONObject j){
        try{
            parseJsonScope(j = j.getJSONObject("scope"));

            //gets onView or onData and all nested performs and checks but the last one
            String name = j.getJSONObject("name").getString("identifier");
            if(!name.equals("intended") && !name.equals("intending") && !name.equals("perform") && !name.equals("check")){
                operations.add(new Operation(name,""));

                //save occurrences for onView and onData
                Integer oldStatistic = statistic.get(name);
                statistic.put(name, oldStatistic + 1);
            }

            parseJsonArgument(j,null,0);
        } catch (JSONException exc){
            // TODO: handle exception
            Utils.logException(exc, "parseJsonScope");
        }
    }

    private void parseJsonArgument(JSONObject j, JSONArray a, int i){
        try{
            if(a == null) {
                parseJsonArgument(j, a = j.getJSONArray("arguments"), 0);
            } else {
                parseJsonArgument(j, a = ((JSONObject) a.get(i)).getJSONArray("arguments"),0);

                // check followed by perform or vice versa
                if (((JSONObject) a.get(0)).getString("type").equals("EnclosedExpr"))
                    a = new JSONArray().put(((JSONObject) a.get(0)).getJSONObject("inner"));

                // parse left and right are used if there is a concatenation of strings
                parseLeftInArgument((JSONObject) a.get(0));
                parseRightInArgument((JSONObject) a.get(0));

                // used when there are FieldAccessExpr
                parseScopeInArgument((JSONObject) a.get(0));
            }

            // field is empty if the parameter is not a FieldAccessExpr otherwise contains
            // only the first part ES: obj. or R.id.
            methodOverloading(a, i);
        } catch (JSONException exc) {
            // TODO: handle exception
            Utils.logException(exc, "parseJsonArgument");
        }
    }

    private void parseLeftInArgument(JSONObject j) {
        try {
            parseLeftInArgument(j = j.getJSONObject("left"));
            parseJsonArgument(j, null, 0);
            parseScopeInArgument(j);

            // call methodOverloading(...);
            methodOverloading(new JSONArray("[" + j + "]"), 0);

            parseRightInArgument(j);
        } catch (Exception e) {
            Utils.logException(e, "parseLeftInArgument");
        }
    }

    private void parseRightInArgument(JSONObject j) {
        try {
            parseRightInArgument(j = j.getJSONObject("right"));
            parseJsonArgument(j, null, 0);
            parseScopeInArgument(j);

            // call methodOverloading(...);
            // methodOverloading(new JSONArray("[" + j + "]"), 0);
        } catch (Exception e) {
            // TODO: handle exception
            Utils.logException(e, "parseRightInArgument");

        }
    }

    private void parseScopeInArgument(JSONObject j) {
        try {
            if (field.toString().isEmpty()) {
                parseScopeInArgument(j = j.getJSONObject("scope"));
                String type = j.getString("type");
                String name = "";
                String index = "";

                if (type.equals("ArrayAccessExpr")) {
                    name = j.getJSONObject("name").getJSONObject("name").getString("identifier");
                    if (j.getJSONObject("index").getString("type").equals("NameExpr"))
                        index = j.getJSONObject("index").getJSONObject("name").getString("identifier");
                    else
                        index = j.getJSONObject("index").getString("value");
                } else
                    name = j.getJSONObject("name").getString("identifier");

                if (!type.equals("MethodCallExpr") && type.equals("ArrayAccessExpr"))
                    field.append(name + "[" + index + "].");
                else if (!type.equals("MethodCallExpr"))
                    field.append(name + ".");
                else
                    field.append(name + "(");

                parseJsonArgument(j, null, 0);

                if (type.equals("MethodCallExpr")) {
                    if (field.charAt(field.length() - 1) == ',')
                        field.deleteCharAt(field.length() - 1);
                    field.append(").");
                }
            }
        } catch (JSONException e) {
            try {
                parseScopeInArgument(j.getJSONObject("name"));
            } catch (JSONException e2) {
                // caught exception
                Utils.logException(e, "parseScopeInArgument");

            }
        }
    }

    private void methodOverloading(JSONArray a, int i) {
        try {
            String type = a.getJSONObject(i).getString("type");
            String name = "";

            name = a.getJSONObject(i).getJSONObject("name").getString("identifier");

            if (!field.toString().isEmpty() && !field.toString().startsWith("R.id.")
                    // && !field.toString().startsWith("ViewMatchers.") &&
                    // !field.toString().startsWith("ViewActions.")
                    /* && !field.toString().startsWith("Matchers.") */ && isNotAnEspressoCommand(name)) {
                String fd = field.toString();
                name = fd.concat(name);
            }

            String parametersValue = parameters.toString();

            // appends the methodCall to the Espresso command parameters
            if ((type.equals("MethodCallExpr")) && isNotAnEspressoCommand(name)) {

                if (parametersValue.isEmpty())
                    parameters.append(name + "()");
                else
                    parameters = new StringBuilder(name + "(" + parametersValue + ")");
                field = new StringBuilder("");

                // adds the Espresso command to the list of operations
            } else if (type.equals("MethodCallExpr")) {
                // if the command is an assertion then "order" the list
                if (!ViewAssertions.getSearchType(name).equals("") || name.equals("allOf") || name.equals("anyOf")) {
                    int numberOfArguments = a.getJSONObject(i).getJSONArray("arguments").length();
                    if (operations.size() >= numberOfArguments)
                        operations.add(operations.size() - numberOfArguments, new Operation(name, parametersValue));
                    else
                        operations.add(new Operation(name, parametersValue));
                } else
                    operations.add(new Operation(name, parametersValue));

                // save occurrences for statistic
                Integer oldStatistic = statistic.get(name);
                statistic.put(name, oldStatistic.intValue() + 1);

                parameters = new StringBuilder("");
                field = new StringBuilder("");
            }

            parseJsonArgument(null, a, ++i);
            methodOverloading(a, i);

        } catch (JSONException e) {
            // add parameters to the operation list
            methodParameters(a, 0);
        }
    }

    private boolean isNotAnEspressoCommand(String name){
        String[] espressoCommands = EspressoCommands.getCommands();

        int low = 0;
        int high = espressoCommands.length-1;
        int mid;

        while(low <= high){
            mid = (low+high) / 2;
            if(espressoCommands[mid].compareTo(name) < 0){
                low = mid + 1;
            } else if (espressoCommands[mid].compareTo(name) > 0){
                high = mid - 1;
            } else {
                return false;
            }
        }
        return true;
    }

    private void methodParameters(JSONArray a, int j){
        try {
            // handles any parameter that is not a variable
            String type = a.getJSONObject(j).getString("type");
            String value = "";

            if (type.equals("BinaryExpr"))
                value = a.getJSONObject(j).getJSONObject("right").getString("value");
            else
                value = a.getJSONObject(j).getString("value");

            String parametersValue = parameters.toString();

            if (value.contains("\n")) {
                StringBuffer v = new StringBuffer(value);
                int index = v.indexOf("\n");
                while (index >= 0) {
                    v.replace(index, index + 1, "\\n");
                    index = v.indexOf("\n", index + 2);
                }
                value = v.toString();
            }

            if (field.toString().isEmpty() || type.equals("BinaryExpr")) {
                if (type.equals("BinaryExpr")) {
                    if (parametersValue.isEmpty())
                        parameters.append("\"" + value + "\"");
                    else
                        parameters.append("+" + "\"" + value + "\"");
                } else if (type.equals("StringLiteralExpr")) {
                    if (parametersValue.isEmpty())
                        parameters.append("\"" + value + "\"");
                    else
                        parameters.append("," + "\"" + value + "\"");
                } else {
                    if (parametersValue.isEmpty())
                        parameters.append(value);
                    else
                        parameters.append("," + value);
                }
            } else {
                if (type.equals("StringLiteralExpr"))
                    field.append("\"" + value + "\"" + ",");
                else
                    field.append(value + ",");
            }

            methodParameters(a, ++j);
        } catch (JSONException e) {
            try {
                String type = a.getJSONObject(j).getString("type");
                String name = "";
                String index = "";

                if (type.equals("ArrayAccessExpr")) {
                    name = a.getJSONObject(j).getJSONObject("name").getJSONObject("name").getString("identifier");
                    if (a.getJSONObject(j).getJSONObject("index").getString("type").equals("NameExpr"))
                        index = a.getJSONObject(j).getJSONObject("index").getJSONObject("name").getString("identifier");
                    else
                        index = a.getJSONObject(j).getJSONObject("index").getString("value");
                } else if (type.equals("BinaryExpr")) {
                    name = a.getJSONObject(j).getJSONObject("right").getJSONObject("name").getString("identifier");
                } else
                    name = a.getJSONObject(j).getJSONObject("name").getString("identifier");

                if (!field.toString().isEmpty() && !field.toString().startsWith("R.id.")
                        // && !field.toString().startsWith("ViewMatchers.") &&
                        // !field.toString().startsWith("ViewActions.")
                        /* && !field.toString().startsWith("Matchers.") */ && isNotAnEspressoCommand(name)) {
                    field.append(name + ",");
                    name = field.toString();
                }

                String parametersValue = parameters.toString();

                if (type.equals("BinaryExpr") || !type.equals("MethodCallExpr") && !parameters.toString().contains(name)
                        || (type.equals("MethodCallExpr") && isNotAnEspressoCommand(name)
                        && !parameters.toString().contains(name))) {

                    if (type.equals("BinaryExpr")) {
                        type = a.getJSONObject(j).getString("type");

                        if (name.endsWith(","))
                            name = name.substring(0, name.length() - 1);
                        if (type.equals("StringLiteralExpr"))
                            name = "\"" + name + "\"";
                        if (parametersValue.isEmpty())
                            parameters.append(name);
                        else {
                            type = a.getJSONObject(j).getJSONObject("right").getString("type");
                            int commaIndex = -1;
                            if (type.equals("MethodCallExpr") && (commaIndex = parameters.lastIndexOf(",")) != -1) {
                                int numberOfArguments = a.getJSONObject(j).getJSONObject("right")
                                        .getJSONArray("arguments").length();
                                for (int w = 0; w < numberOfArguments - 1; w++)
                                    commaIndex = parametersValue.substring(0, commaIndex).lastIndexOf(",");
                                parameters.replace(commaIndex, commaIndex + 1, "+" + name + "(");
                                parameters.append(")");
                            } else
                                parameters.append("+" + name);
                        }
                        // field access
                    } else if (type.equals("FieldAccessExpr") && field.toString().startsWith("R.id.")) {
                        if (parametersValue.isEmpty())
                            parameters.append("\"" + name + "\"");
                        else
                            parameters.append("," + "\"" + name + "\"");

                        field = new StringBuilder("");

                        // field access
                    } else if (type.equals("FieldAccessExpr")) {
                        if (parametersValue.isEmpty())
                            parameters.append(name.substring(0, name.length() - 1));
                        else
                            parameters.append("," + name.substring(0, name.length() - 1));

                        // array access
                    } else if (type.equals("ArrayAccessExpr")) {
                        if (name.charAt(name.length() - 1) != ',') {

                            if (parametersValue.isEmpty())
                                parameters.append(name + "[" + index + "]");
                            else
                                parameters.append("," + name + "[" + index + "]");
                        } else {
                            if (parametersValue.isEmpty())
                                parameters.append(name.substring(0, name.length() - 1) + "[" + index + "]");
                            else
                                parameters.append("," + name.substring(0, name.length() - 1) + "[" + index + "]");
                        }
                        // method call
                    } else if ((type.equals("MethodCallExpr"))) {
                        // substring is used to remove the comma at the end of the string
                        if (parametersValue.isEmpty())
                            parameters.append(name.substring(0, name.length() - 1) + "()");
                        else
                            parameters = new StringBuilder(
                                    name.substring(0, name.length() - 1) + "(" + parametersValue + ")");
                        field = new StringBuilder("");

                        // name expr
                    } else {
                        if (field.toString().isEmpty()) {
                            if (parametersValue.isEmpty())
                                parameters.append(name);
                            else
                                parameters.append("," + name);
                        }
                    }
                }
                methodParameters(a, ++j);
            } catch (JSONException e1) {
                // TODO: handle exception
                Utils.logException(e, "methodParameters");
            }
        }
    }
    //todo i == index
    private int parseStatement(BlockStmt block, String methodName, Statement stmt, int i){

        //System.out.println("parsing statement + " + block.toString() + " from row " + i);
        System.out.println(stmt);
        int index = i;

        String stmtString = stmt.toString();

        operations = new ArrayList<>();
        if(stmtString.contains("onView") ||
            stmtString.contains("onData") ||
            stmtString.contains("intended") ||
            stmtString.contains("intending")){

            JsonPrinter printer = new JsonPrinter(true);
            String json = printer.output(stmt);

            // TEST CASES like : ViewInteraction vi = onView(withId(...)).perform(...);
            if(json.contains("VariableDeclarator")) {
                String type = "type";

                for(int j = -1; (j = json.indexOf(type,j+1)) != -1; j++) {
                    String old = json.substring(j,j+26);
                    if(old.equals("type\":\"VariableDeclarator\"")){
                        json = json.substring(0, j) + "typeV\": \"VariableDeclarator\"" + json.substring(j + 26);
                        break;
                    }
                }
            }

            try {
                JSONObject j = new JSONObject(json);
                // System.out.println(j.toString());
                j = j.getJSONObject("expression");

                String type = j.getString("type");

                // vi = onView(withId(...)).perform(...);

                if (type.equals("AssignExpr")) {
                    j = j.getJSONObject("value");

                    // ViewInteraction vi = onView(withId(...)).perform(...);
                } else if (type.equals("VariableDeclarationExpr")) {
                    j = j.getJSONArray("variables").getJSONObject(0).getJSONObject("initializer");
                }

                parseJsonScope(j);

                // gets the last check or perform
                // operations.add(new Operation(j.getJSONObject("name").getString("identifier"),
                // ""));

                parseJsonArgument(j, null, 0);

                System.out.println(operations.toString());

                // returns the next index after enhancing the method
                return enhanceMethod(block, methodName, stmt, i);
            } catch (JSONException e) {
                // CAN'T PARSE STATEMENT
                //e.printStackTrace();
                Utils.logException(e, "parseStatement");

            }
            //handling of independent Expresso actions
        } else {
            String op = "";
            String parameter = "";
            if (stmtString.contains("closeSoftKeyboard();")) {
                op = "closeSoftKeyboard";
            } else if (stmtString.contains("pressBack();")) {
                op = "pressBack";
            } else if (stmtString.contains("pressBackUnconditionally();")) {
                op = "pressBackUnconditionally";
            } else if (stmtString.contains("openActionBarOverflowOrOptionsMenu(")) {
                op = "openActionBarOverflowOrOptionsMenu";
            } else if (stmtString.contains("openContextualActionModeOverflowMenu();")) {
                op = "openContextualActionModeOverflowMenu";
            } else if (stmtString.contains("typeTextIntoFocusedView(")) {
                try {
                    op = "typeTextIntoFocusedView";
                    parameter = stmtString.split("typeTextIntoFocusedView\\(")[1].split("\"")[1];

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //bisogna aggiungere parametro per typetextintofocsedview prendendolo tra le due parentesi

            }
            if (!op.isEmpty()) {
                Integer oldStatistic = statistic.get(op);
                statistic.put(op, oldStatistic + 1);

                // enhance interaction
                operations.add(new Operation("blank", "-"));
                operations.add(new Operation(op, parameter));
                return enhanceMethod(block, methodName, stmt, i);
            }
        }

        // return the next index if the statement is not a test
        return ++index;
    }

    private int enhanceMethod(BlockStmt block, String methodName, Statement stmt, int i) {
        // this works on test cases with one matcher
        String searchType = "";
        String searchKw = "";


        System.out.println("OPERATION SIZE = " + operations.size());
        for (Operation o: operations) {
            System.out.println(o.toString());
        }

        // if the test is with onData the handling is different
        if (operations.get(0).getName().equals("onData")) {
            return enhanceMethodOnData(block, methodName, stmt, i);
        } else {

            if (operations.size() > 0) {

                searchType = ViewMatchers.getSearchType(operations.get(1).getName());
                searchKw = operations.get(1).getParameter();
                System.out.println("searchtype = " + searchType + "; - searchKw = " + searchKw);

            }


            if (operations.size() == 2) {
                //management of cases like pressback
                System.out.println("operation type = " + operations.get(1).getName());
                String interactionType = ViewActions.getSearchType(operations.get(1).getName());
                String interactionParams = operations.get(1).getParameter();

                System.out.println("interaction type = " + interactionType);
                System.out.println("interaction params = " + interactionParams);

                if (interactionType.equals("pressback") || interactionType.equals("typeintofocused") || interactionType.equals("closekeyboard") ||
                        interactionType.equals("pressbackunconditionally") || interactionType.equals("openactionbaroverfloworoptionsmenu") ||
                        interactionType.equals("opencontextualactionmodeoverflowmenu") || interactionType.equals("pressmenukey")) {

                    searchType = "-";
                    searchKw = "-";
                    System.out.println("methodName = " + methodName + "; searchType = " + searchType + "; searchKw = " + searchKw + "; interactionType = " + interactionType + "; interactionParams = " + interactionParams);
                    LogCat log = new LogCat(methodName, searchType, searchKw, interactionType, interactionParams);


                    String stmtString = stmt.toString();
                    Statement st = JavaParser.parseStatement(stmtString);

                    //b.addStatement(i, date);
                    block.addStatement(i, logNum);
                    block.addStatement(++i, activity);
                    //	b.addStatement(++i, captureTaskValue);
                    block.addStatement(++i, JavaParser.parseStatement(
                            "capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTaskProgressive(num, \"" + methodName + "\", activityTOGGLETools));"));


                    block.addStatement(++i, screenCapture);
                    i = addLogInteractionToCu(log, i, block);
                    //b.addStatement(++i, dumpScreen);
                    block.addStatement(++i, JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, \"" +methodName + "\", device);"));
                    block.addStatement(++i, st);
                    block.addStatement(++i, tryStmt);


                }

            }



            if (!searchType.isEmpty() || searchType.equals("-")) {

                String stmtString = stmt.toString();
                Statement st = JavaParser.parseStatement(stmtString);

                if (operations.size() > 1) {
                    block.remove(stmt);
                }


                for (int j = 2; j < operations.size(); j++) {

                    String interactionType = ViewActions.getSearchType(operations.get(j).getName());
                    String interactionParams = operations.get(j).getParameter();
                    /*
                     * if (interactionType.isEmpty()) { new Exception(operations.get(j).getName() +
                     * " is not supported or is not an Espresso command").printStackTrace(); }
                     */

                    // if (!interactionType.equals("perform") && !interactionType.equals("check")) {

                    if (interactionType.isEmpty()) {
                        interactionType = ViewAssertions.getSearchType(operations.get(j).getName());

                        if (searchType.isEmpty() || interactionType.isEmpty()) {
                            block.addStatement(i, st);
                            break;
                        }

                        // log only if the assertion is 'matches'. Leave out isLeft, isRight ecc... for
                        // now.
                        if (interactionType.equals("matches") && canItBeAnAssertionParameter(operations.get(++j)))
                            interactionType = "check";
                        else {
                            block.addStatement(i, st);
                            break;
                        }

                    }


                    System.out.println("logcatting: methodname = " + methodName + "; searchType = " + searchType + "; searchKw = " + searchKw + "; interactionType = " + interactionType + "; interactionParams = " + interactionParams);
                    LogCat log = new LogCat(methodName, searchType, searchKw, interactionType, interactionParams);



                    if (firstTest) {
                        firstTest = false;
                        block.addStatement(i, captureTask);
                        block.addStatement(++i, instrumentation);
                        block.addStatement(++i, device);
                        block.addStatement(++i, firstLogNum);
                        //b.addStatement(++i, firstTestDate);
                        block.addStatement(++i, firstTestActivity);
                    } else if (j == 3 && interactionType.equals("check") || j == 2) {
                        block.addStatement(i, logNum);
                        //b.addStatement(i, date);
                        block.addStatement(++i, activity);

                        // this makes it work on test cases with multiple interactions avoiding the try
                        // statements to stay to the bottom
                    } else {
                        //b.addStatement(++i, date);
                        block.addStatement(++i, logNum);
                        block.addStatement(++i, activity);
                    }

                    //b.addStatement(++i, captureTaskValue);
                    block.addStatement(++i, JavaParser.parseStatement(
                            "capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTaskProgressive(num, \"" + methodName + "\", activityTOGGLETools));"));

                    block.addStatement(++i, screenCapture);

                    i = addLogInteractionToCu(log, i, block);

                    //b.addStatement(++i, dumpScreen);
                    block.addStatement(++i, JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, \"" +methodName + "\", device);"));
                    block.addStatement(++i, st);
                    block.addStatement(++i, tryStmt);
                }

            }
            // }
        }

        return ++i;
    }

    private int enhanceMethodOnData(BlockStmt block, String methodName, Statement stmt, int i) {
        Statement firstVisiblePosition = JavaParser.parseStatement("int firstVisiblePosition = 0;");
        Statement height = JavaParser.parseStatement("int height = 0;");
        Statement offset = JavaParser.parseStatement("int offset = 0;");

        try {
            // get onData(customMatcher(...)).inAdapterView(withId(R.id.'someId')
            Node inAdapterView = getOnDataInAdapterView(stmt);

            // get 'someId' in
            // onData(customMatcher(...)).inAdapterView(withId(R.id.'someId')
            String listId = getIdInAdapterView(inAdapterView);

            TryStmt populateDataFromList = (TryStmt) JavaParser.parseStatement("try {\r\n"
                    + "            ListView l = activityTOGGLETools.findViewById(R.id." + listId + ");\r\n"
                    + "            int position = l.getFirstVisiblePosition() - firstVisiblePosition;\r\n"
                    + "            firstVisiblePosition = l.getFirstVisiblePosition();\r\n"
                    + "            View c = l.getChildAt(0);\r\n" + "            View v = l.getSelectedView();\r\n"
                    + "            offset = v.getTop();\r\n" + "            height = c.getHeight();\r\n"
                    + "            scrolly" + listId + "= -c.getTop() + position * height;\r\n"
                    + "        } catch (Exception e) {\r\n" + "            try {\r\n"
                    + "                GridView l = activityTOGGLETools.findViewById(R.id." + listId + ");\r\n"
                    + "                int position = l.getFirstVisiblePosition() - firstVisiblePosition;\r\n"
                    + "                firstVisiblePosition = l.getFirstVisiblePosition();\r\n"
                    + "                View c = l.getChildAt(0);\r\n"
                    + "                View v = l.getSelectedView();\r\n" + "                offset = v.getTop();\r\n"
                    + "                height = c.getHeight();\r\n" + "                scrolly" + listId
                    + " = -c.getTop() + position * height;\r\n" + "            } catch (Exception e1) {\r\n"
                    + "                // try {\r\n" + "                // Spinner s = ...;\r\n"
                    + "                // ...\r\n" + "                // } catch (Exception ei){\r\n"
                    + "                // try {\r\n" + "                // ...\r\n" + "                // }\r\n"
                    + "                // }\r\n" + "            }\r\n" + "        }");

            addImportsOnData();

            String scrollToString = inAdapterView.toString() + ".perform(scrollTo());";
            Statement scrollToStatement = JavaParser.parseStatement(scrollToString);

            // remove test
            Statement st = stmt;
            block.remove(stmt);

            if (firstTest) {
                firstTest = false;
                block.addStatement(i, captureTask);
                block.addStatement(++i, instrumentation);
                block.addStatement(++i, device);
                //b.addStatement(++i, firstTestDate);
                block.addStatement(++i, firstLogNum);
                block.addStatement(++i, firstTestActivity);
            } else {
                //b.addStatement(i, date);
                block.addStatement(i, logNum);
                block.addStatement(++i, activity);
            }

            // first screen capture before scrolling
            //	b.addStatement(++i, captureTaskValue);
            block.addStatement(++i, JavaParser.parseStatement(
                    "capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTaskProgressive(num, \"" + methodName + "\", activityTOGGLETools));"));

            block.addStatement(++i, screenCapture);
            //b.addStatement(++i, dumpScreen);
            block.addStatement(++i, JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, \"" +methodName + "\", device);"));

            LogCat log = new LogCat(methodName, "id", "\"" + listId + "\"", "scrollto", "");
            i = addLogInteractionToCu(log, i, block);

            // add scrollTo test
            block.addStatement(++i, scrollToStatement);

            // add variables
            if (!block.toString().contains("int firstVisiblePosition = 0;")) {
                block.addStatement(++i, firstVisiblePosition);
                block.addStatement(++i, height);
                block.addStatement(++i, offset);
            }

            Statement scrolly = JavaParser.parseStatement("int scrolly" + listId + " = 0;");
            if (!block.toString().contains("int scrolly" + listId + " = 0;")) {
                block.addStatement(++i, scrolly);
            }

            // populate data in variables
            block.addStatement(++i, populateDataFromList);

            //b.addStatement(++i, date);
            block.addStatement(++i, logNum);
            // log scrollTo interaction with parameters
            log = new LogCat(methodName, "id", "\"" + listId + "\"", "scrollto",
                    "scrolly" + listId + "+\";\"+height+\";\"+offset");
            i = addLogInteractionToCu(log, i, block);

            // second screen capture after scrolling
            //	b.addStatement(++i, captureTaskValue);
            block.addStatement(++i, JavaParser.parseStatement(
                    "capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTaskProgressive(num, \"" + methodName + "\", activityTOGGLETools));"));

            block.addStatement(++i, screenCapture);

            // take the interaction to perform on the list
            int numberOfOperations = operations.size();
            String interaction = operations.get(numberOfOperations - 1).getName();
            String interactionType = ViewActions.getSearchType(interaction);
            String interactionParams = operations.get(numberOfOperations - 1).getParameter();

            // if it's empty could be a check
            if (interactionType.isEmpty())
                // if it's not empty is a check
                if (!ViewAssertions.getSearchType(interaction).isEmpty()) {
                    interactionType = "check";
                    interactionParams = "";
                }

            // if the interaction is scrollTo, it is ignored because the generated code
            // already does it
            if (!interactionType.equals("scrollto")) {
                log = new LogCat(methodName, "id", "\"" + listId + "\"", interactionType, interactionParams);
                i = addLogInteractionToCu(log, i, block);

                //b.addStatement(++i, dumpScreen);
                block.addStatement(++i, JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, \"" +methodName + "\", device);"));
                block.addStatement(++i, st);
                block.addStatement(++i, tryStmt);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("EXCEPTION IN ENHANCER!");
            Utils.logException(e, "enhanceMethodOnData");


        }

        return ++i;
    }

    private void addImportsOnData() {
        compilationUnit.addImport("android.widget.ListView", false, false);
        compilationUnit.addImport("android.widget.GridView", false, false);
        compilationUnit.addImport("android.view.View", false, false);
        compilationUnit.addImport(version + "test.espresso.action.ViewActions.scrollTo", true, false);
    }

    private String getIdInAdapterView(Node inAdapterView) throws Exception {
        Node withIdNode = inAdapterView.getChildNodes().get(2);
        Node resourceIdNode = withIdNode.getChildNodes().get(1);
        Node idNode = resourceIdNode.getChildNodes().get(1);
        return idNode.toString();
    }

    private Node getOnDataInAdapterView(Statement s) throws Exception {
        List<Node> children = s.getChildNodes();
        Node precPrec = null;
        Node prec = null;
        Node c = null;

        // take the node with the correct 'structure' from
        // onData(customMatcher(...)).inAdapterView(withId(R.id.'someId').perform(...)
        while (true) {
            children = children.get(0).getChildNodes();

            // onData(customMatcher(...)).inAdapterView(withId(R.id.'someId')
            precPrec = prec;

            // onData(customMatcher(...))
            prec = c;

            // onData
            c = children.get(0);

            if (!children.get(0).getClass().toString().endsWith("MethodCallExpr"))
                break;
        }

        return precPrec;
    }

    private boolean canItBeAnAssertionParameter(Operation operation) {
        String[] assertionParameters = { "hasEllipsizedText", "hasFocus", "isChecked", "isClickable",
                "isCompletelyDisplayed", "isDisplayed", "isEnabled", "isNotChecked", "isSelected",
                "withEffectiveVisibility", "withSpinnerText", "withText" };
        String name = operation.getName();

        int low = 0;
        int high = assertionParameters.length - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;

            if (assertionParameters[mid].compareTo(name) < 0) {
                low = mid + 1;
            } else if (assertionParameters[mid].compareTo(name) > 0) {
                high = mid - 1;
            } else {
                return true;
            }
        }

        /*
         * for (String par : assertionParameters) { if (par.equals(operation.getName()))
         * return true; }
         */

        return false;
    }

    private int addLogInteractionToCu(LogCat log, int i, BlockStmt b) {
        Statement l = null;
        String stmt = "";

        if (log.getInteractionType().isEmpty())
            log.setInteractionType("");

        // default handles the normal behavior of the parameters. ES: click(),
        // typeText("TextToBeReplaced")
        switch (log.getInteractionType()) {
            case "replacetext":
                // the 'i' in the variable name is used to make it unique in case we have
                // multiple interactions of the same type
                // substring removes the " from the string

                // TODO: handle different behaviors based on the type of parameter
                if (log.getSearchKw().charAt(0) == '"') {
                    log.setSearchKw(log.getSearchKw().substring(1, log.getSearchKw().length() - 1));
                    if (log.getSearchType().equals("id")) {
                        stmt = "int textToBeReplacedLength" + i + " = ((TextView) activityTOGGLETools.findViewById(R.id."
                                + log.getSearchKw() + ")).getText().length();";

                        b.addStatement(++i, JavaParser.parseStatement(stmt));



                        l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(num, " + "\"" + log.getMethodName()
                                + "\", " + "\"" + log.getSearchType() + "\"" + "," + "\"" + log.getSearchKw() + "\"" + ","
                                + "\"" + log.getInteractionType() + "\", String.valueOf(textToBeReplacedLength" + (i - 1)
                                + ")+\";\"+" + log.getInteractionParams() + ");");



                        //l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\"" + log.getMethodName()
                        //		+ "\", " + "\"" + log.getSearchType() + "\"" + "," + "\"" + log.getSearchKw() + "\"" + ","
                        //		+ "\"" + log.getInteractionType() + "\", String.valueOf(textToBeReplacedLength" + (i - 1)
                        //		+ ")+\";\"+" + log.getInteractionParams() + ");");
                    } else {
                        stmt = "String searchKw = \"" + log.getSearchKw() + "\";";
                        String stmt2 = "int textToBeReplacedLength" + i + " = searchKw.length();";

                        b.addStatement(++i, JavaParser.parseStatement(stmt));
                        b.addStatement(++i, JavaParser.parseStatement(stmt2));


                        l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(num, " + "\"" + log.getMethodName()
                                + "\", " + "\"" + log.getSearchType() + "\"" + "," + "\"" + log.getSearchKw() + "\"" + ","
                                + "\"" + log.getInteractionType() + "\", String.valueOf(textToBeReplacedLength" + (i - 2)
                                + ")+\";\"+" + log.getInteractionParams() + ");");


                        //l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\"" + log.getMethodName()
                        //		+ "\", " + "\"" + log.getSearchType() + "\"" + "," + "\"" + log.getSearchKw() + "\"" + ","
                        //		+ "\"" + log.getInteractionType() + "\", String.valueOf(textToBeReplacedLength" + (i - 2)
                        //		+ ")+\";\"+" + log.getInteractionParams() + ");");
                    }
                } else {
                    if (log.getSearchType().equals("id"))
                        stmt = "int textToBeReplacedLength" + i + " = ((TextView) activityTOGGLETools.findViewById("
                                + log.getSearchKw() + ")).getText().length();";
                    else
                        stmt = "int textToBeReplacedLength" + i + " = " + log.getSearchKw() + ".length();";

                    b.addStatement(++i, JavaParser.parseStatement(stmt));


                    l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(num, " + "\"" + log.getMethodName() + "\","
                            + "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw() + "," + "\""
                            + log.getInteractionType() + "\", String.valueOf(textToBeReplacedLength" + (i - 1) + ")+\";\"+"
                            + log.getInteractionParams() + ");");

                    //l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\"" + log.getMethodName() + "\","
                    //		+ "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw() + "," + "\""
                    //		+ log.getInteractionType() + "\", String.valueOf(textToBeReplacedLength" + (i - 1) + ")+\";\"+"
                    //		+ log.getInteractionParams() + ");");

                }
                break;
            case "cleartext":

                // TODO: handle different behaviors based on the type of parameter
                if (log.getSearchKw().charAt(0) == '"') {
                    log.setSearchKw(log.getSearchKw().substring(1, log.getSearchKw().length() - 1));
                    if (log.getSearchType().equals("id")) {
                        stmt = "int textToBeClearedLength" + i + " = ((TextView) activityTOGGLETools.findViewById(R.id."
                                + log.getSearchKw() + ")).getText().length();";
                        b.addStatement(++i, JavaParser.parseStatement(stmt));

                        l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(num," + "\"" + log.getMethodName() + "\","
                                + "\"" + log.getSearchType() + "\"" + "," + "\"" + log.getSearchKw() + "\"" + "," + "\""
                                + log.getInteractionType() + "\", String.valueOf(textToBeClearedLength" + (i - 1) + "));");

                        //l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now," + "\"" + log.getMethodName() + "\","
                        //		+ "\"" + log.getSearchType() + "\"" + "," + "\"" + log.getSearchKw() + "\"" + "," + "\""
                        //		+ log.getInteractionType() + "\", String.valueOf(textToBeClearedLength" + (i - 1) + "));");
                    } else {
                        stmt = "String searchKw = \"" + log.getSearchKw() + "\";";
                        String stmt2 = "int textToBeClearedLength" + i + " = searchKw.length();";

                        b.addStatement(++i, JavaParser.parseStatement(stmt));
                        b.addStatement(++i, JavaParser.parseStatement(stmt2));

                        l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(num," + "\"" + log.getMethodName() + "\","
                                + "\"" + log.getSearchType() + "\"" + "," + "\"" + log.getSearchKw() + "\"" + "," + "\""
                                + log.getInteractionType() + "\", String.valueOf(textToBeClearedLength" + (i - 2) + "));");

                        //l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now," + "\"" + log.getMethodName() + "\","
                        //		+ "\"" + log.getSearchType() + "\"" + "," + "\"" + log.getSearchKw() + "\"" + "," + "\""
                        //		+ log.getInteractionType() + "\", String.valueOf(textToBeClearedLength" + (i - 2) + "));");
                    }
                } else {
                    if (log.getSearchType().equals("id"))
                        stmt = "int textToBeClearedLength" + i + " = ((TextView) activityTOGGLETools.findViewById("
                                + log.getSearchKw() + ")).getText().length();";
                    else
                        stmt = "int textToBeReplacedLength" + i + " = " + log.getSearchKw() + ".length();";

                    b.addStatement(++i, JavaParser.parseStatement(stmt));


                    l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(num," + "\"" + log.getMethodName() + "\","
                            + "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw() + "," + "\""
                            + log.getInteractionType() + "\", String.valueOf(textToBeClearedLength" + (i - 1) + "));");

                    //l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now," + "\"" + log.getMethodName() + "\","
                    //		+ "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw() + "," + "\""
                    //		+ log.getInteractionType() + "\", String.valueOf(textToBeClearedLength" + (i - 1) + "));");
                }
                break;
            case "presskey":
                Statement val = JavaParser.parseStatement(
                        "String espressoKeyVal" + i + " = String.valueOf(" + log.getInteractionParams() + ");");
                Statement keyArray = JavaParser
                        .parseStatement("String[] espressoKeyArray" + i + " = espressoKeyVal" + i + ".split(\",\");");
                IfStmt ifStmt = (IfStmt) JavaParser.parseStatement("if(espressoKeyArray" + i + ".length > 1) {\n"
                        + "            int espressoKeyArrayIndex" + i + " = espressoKeyArray" + i + "[0].indexOf(\":\");\n"
                        + "            espressoKeyVal" + i + " = espressoKeyArray" + i
                        + "[0].substring(espressoKeyArrayIndex" + i + "+1).trim();\n" + "        }");

                b.addStatement(++i, val);
                b.addStatement(++i, keyArray);
                b.addStatement(++i, ifStmt);


                stmt = "TOGGLETools.LogInteractionProgressive(num," + "\"" + log.getMethodName() + "\"," + "\"" + log.getSearchType()
                        + "\"" + "," + log.getSearchKw() + "," + "\"" + log.getInteractionType() + "\"" + ", espressoKeyVal"
                        + (i - 3) + ");";

                //stmt = "TOGGLETools.LogInteraction(now," + "\"" + log.getMethodName() + "\"," + "\"" + log.getSearchType()
                //		+ "\"" + "," + log.getSearchKw() + "," + "\"" + log.getInteractionType() + "\"" + ", espressoKeyVal"
                //		+ (i - 3) + ");";

                l = JavaParser.parseStatement(stmt);

                break;
            case "pressback":
            case "pressbackunconditionally":
            case "closekeyboard":
            case "openactionbaroverfloworoptionsmenu":
            case "opencontextualactionmodeoverflowmenu":
                l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(num, " + "\"" + log.getMethodName() + "\","
                        + "\"-\", \"-\"," + "\"" + log.getInteractionType() + "\"" + ");");


                //l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\"" + log.getMethodName() + "\","
                //		+ "\"-\", \"-\"," + "\"" + log.getInteractionType() + "\"" + ");");
                break;


            case "typeintofocused":

                System.out.println(log.getInteractionParams());
                l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(num, " + "\"" + log.getMethodName() + "\","
                        + "\"-\", \"-\"," + "\"" + log.getInteractionType() + "\", \"" + log.getInteractionParams() + "\"" + ");");


                //l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\"" + log.getMethodName() + "\","
                //		+ "\"-\", \"-\"," + "\"" + log.getInteractionType() + "\", \"" + log.getInteractionParams() + "\"" + ");");
                break;

            default:
                if (log.getInteractionParams().isEmpty())
                    l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(num," + "\"" + log.getMethodName() + "\","
                            + "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw() + "," + "\""
                            + log.getInteractionType() + "\"" + ");");


                    //l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now," + "\"" + log.getMethodName() + "\","
                    //		+ "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw() + "," + "\""
                    //		+ log.getInteractionType() + "\"" + ");");
                else

                    l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(num," + "\"" + log.getMethodName() + "\","
                            + "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw() + "," + "\""
                            + log.getInteractionType() + "\"" + "," + log.getInteractionParams() + ");");

                //l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now," + "\"" + log.getMethodName() + "\","
                //		+ "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw() + "," + "\""
                //		+ log.getInteractionType() + "\"" + "," + log.getInteractionParams() + ");");
                break;
        }

        if (l != null)
            b.addStatement(++i, l);

        return i;
    }

    private void addFullCheck(BlockStmt b, String methodName, int i) {
        Statement currDisp = JavaParser
                .parseStatement("Rect currdisp = TOGGLETools.GetCurrentDisplaySize(activityTOGGLETools);");


        String stmt = "TOGGLETools.LogInteractionProgressive(num," + "\"" + methodName
                + "\",\"-\", \"-\", \"fullcheck\", currdisp.bottom+\";\"+currdisp.top+\";\"+currdisp.right+\";\"+currdisp.left);";



        //String stmt = "TOGGLETools.LogInteraction(now," + "\"" + methodName
        //		+ "\",\"-\", \"-\", \"fullcheck\", currdisp.bottom+\";\"+currdisp.top+\";\"+currdisp.right+\";\"+currdisp.left);";
        Statement log = JavaParser.parseStatement(stmt);

        // if (i > b.getStatements().size())
        // --i;

        //b.addStatement(i, date);
        b.addStatement(i, logNum);
        b.addStatement(++i, activity);
        //	b.addStatement(++i, captureTaskValue);
        b.addStatement(++i, JavaParser.parseStatement(
                "capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTaskProgressive(num, \"" + methodName + "\", activityTOGGLETools));"));

        b.addStatement(++i, currDisp);
        b.addStatement(++i, screenCapture);
        b.addStatement(++i, log);
        //b.addStatement(++i, dumpScreen);
        b.addStatement(++i, JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, \"" +methodName + "\", device);"));
    }
}
