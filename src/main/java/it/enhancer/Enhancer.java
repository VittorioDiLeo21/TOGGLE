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
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
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
    private boolean firstOnDataTest;
    private boolean firstScrollToTest;
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

    private Statement firstPreScrollY = JavaParser.parseStatement("int preScrollYTOGGLE = 0;");
    private Statement preScrollY = JavaParser.parseStatement("preScrollYTOGGLE = 0;");
    private Statement firstPreScrollX = JavaParser.parseStatement("int preScrollXTOGGLE = 0;");
    private Statement preScrollX = JavaParser.parseStatement("preScrollXTOGGLE = 0;");
    private Statement firstScrollY = JavaParser.parseStatement("int[] scrollYTOGGLE = new int[2];");
    private Statement scrollY = JavaParser.parseStatement("scrollYTOGGLE = new int[2];");
    private Statement firstScrollX = JavaParser.parseStatement("int[] scrollXTOGGLE = new int[2];");
    private Statement scrollX = JavaParser.parseStatement("scrollXTOGGLE = new int[2];");
    private Statement firstScrollableClass = JavaParser.parseStatement("String scrollableClassTOGGLE = \"\";");
    private Statement scrollableClass = JavaParser.parseStatement("scrollableClassTOGGLE = \"\";");
    /*private Statement firstSingleItemFirstH = JavaParser.parseStatement("int heightFirstTOGGLE = 0;");
    private Statement singleItemFirstH = JavaParser.parseStatement("heightFirstTOGGLE = 0;");
    private Statement firstSingleItemFirstW = JavaParser.parseStatement("int widthFirstTOGGLE = 0;");
    private Statement singleItemFirstW = JavaParser.parseStatement("widthFirstTOGGLE = 0;");*/
    private Statement firstSingleItemH = JavaParser.parseStatement("int heightTOGGLE = 0;");
    private Statement singleItemH = JavaParser.parseStatement("heightTOGGLE = 0;");
    private Statement firstSingleItemW = JavaParser.parseStatement("int widthTOGGLE = 0;");
    private Statement singleItemW = JavaParser.parseStatement("widthTOGGLE = 0;");
    private Statement firstLoc = JavaParser.parseStatement("int[] locTOGGLE = new int[2];");
    private Statement loc = JavaParser.parseStatement("locTOGGLE = new int[2];");

    /*private Statement firstSingleItemLastH = JavaParser.parseStatement("int heightLastTOGGLE = 0;");
    private Statement singleItemLastH = JavaParser.parseStatement("heightLastTOGGLE = 0;");
    private Statement firstSingleItemLastW = JavaParser.parseStatement("int widthLastTOGGLE = 0;");
    private Statement singleItemLastW = JavaParser.parseStatement("widthLastTOGGLE = 0;");*/

    private Statement firstScrollDirectionStr = JavaParser.parseStatement("String scrollDirTOGGLE = \"\";");
    private Statement scrollDirectionStr = JavaParser.parseStatement("scrollDirTOGGLE = \"\";");
    private Statement firstScrollableDirectionStr = JavaParser.parseStatement("String scrollableDirTOGGLE = \"\";");
    private Statement scrollableDirectionStr = JavaParser.parseStatement("scrollableDirTOGGLE = \"\";");
    /*private Statement firstScrollableXDirectionStr = JavaParser.parseStatement("String scrollableXDirTOGGLE = \"\";");
    private Statement scrollableXDirectionStr = JavaParser.parseStatement("scrollableXDirTOGGLE = \"\";");*/
    private Statement firstPostScrollY = JavaParser.parseStatement("int postScrollYTOGGLE = 0;");
    private Statement postScrollY = JavaParser.parseStatement("postScrollYTOGGLE = 0;");
    private Statement firstPostScrollX = JavaParser.parseStatement("int postScrollXTOGGLE = 0;");
    private Statement postScrollX = JavaParser.parseStatement("postScrollXTOGGLE = 0;");


    private TryStmt screenCapture = (TryStmt) JavaParser.parseStatement("try { runOnUiThread(capture_task); } catch (Throwable t) { t.printStackTrace(); }");
    //private Statement dumpScreen = JavaParser.parseStatement("TOGGLETools.DumpScreen(now, device);");
    private Statement dumpScreenProgressive = JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, device);");

    private Statement scrollHandlerSetup = JavaParser.parseStatement("ScrollHandler.getItemsHeight(adapterViewTOGGLE);");
    /*private Statement getSingleItemHeightFirst = JavaParser.parseStatement("heightFirstTOGGLE = ScrollHandler.getSingleItemHeightIn(adapterViewTOGGLE,true);");
    private Statement getSingleItemHeightLast = JavaParser.parseStatement("heightLastTOGGLE = ScrollHandler.getSingleItemHeightIn(adapterViewTOGGLE,false);");
    private Statement getSingleItemWidthFirst = JavaParser.parseStatement("widthFirstTOGGLE = ScrollHandler.getSingleItemHeightIn(adapterViewTOGGLE,true);");
    private Statement getSingleItemWidthLast = JavaParser.parseStatement("widthLastTOGGLE = ScrollHandler.getSingleItemHeightIn(adapterViewTOGGLE,false);");*/
    private Statement getSingleItemHeightFirst = JavaParser.parseStatement("heightTOGGLE = ScrollHandler.getSingleItemHeightIn(adapterViewTOGGLE,true);");
    private Statement getSingleItemWidthFirst = JavaParser.parseStatement("widthTOGGLE = ScrollHandler.getSingleItemWidthIn(adapterViewTOGGLE,true);");

    private Statement findAdapterViewTopLeft = JavaParser.parseStatement("adapterViewTOGGLE.getLocationOnScreen(locTOGGLE);");
    private Statement getStartScrollXFromScrollable = JavaParser.parseStatement("scrollXTOGGLE[0] = ScrollHandler.getScrollXFromScrollable(scrollableTOGGLE);");
    private Statement getStartScrollYFromScrollable = JavaParser.parseStatement("scrollYTOGGLE[0] = ScrollHandler.getScrollYFromScrollable(scrollableTOGGLE);");
    private Statement getEndScrollXFromScrollable = JavaParser.parseStatement("scrollXTOGGLE[1] = ScrollHandler.getScrollXFromScrollable(scrollableTOGGLE);");
    private Statement getEndScrollYFromScrollable = JavaParser.parseStatement("scrollYTOGGLE[1] = ScrollHandler.getScrollYFromScrollable(scrollableTOGGLE);");
    private Statement getScrollableClass = JavaParser.parseStatement("scrollableClassTOGGLE = ScrollHandler.getScrollableClass(scrollableTOGGLE);");
    private IfStmt dirScrollStmt1 = (IfStmt) JavaParser.parseStatement(
            "if(preScrollYTOGGLE <= postScrollYTOGGLE){\r\n"
                    + "         scrollDirTOGGLE = \"scrolldown\";\r\n"
                    //+ "         heightTOGGLE = heightLastTOGGLE;"
                    + "     } else {\r\n"
                    + "         scrollDirTOGGLE =\"scrollup\";\r\n"
                    //+ "         heightTOGGLE = heightFirstTOGGLE;"
                    + "     }"
    );
    private IfStmt dirScrollStmt2 = (IfStmt) JavaParser.parseStatement(
            "if(preScrollXTOGGLE <= postScrollXTOGGLE){\r\n"
                    + "         scrollDirTOGGLE += \"right\";\r\n"
                    //+ "         widthTOGGLE = widthLastTOGGLE;"
                    + "     } else {\r\n"
                    + "         scrollDirTOGGLE += \"left\";\r\n"
                    //+ "         widthTOGGLE = widthFirstTOGGLE;"
                    + "     }"
    );
    private IfStmt dirScrollableStmt1 = (IfStmt) JavaParser.parseStatement(
            "if(scrollYTOGGLE[0] <= scrollXTOGGLE[1]){\r\n"
                    + "         scrollableDirTOGGLE = \"scrolldown\";\r\n"
                    + "     } else {\r\n"
                    + "         scrollableDirTOGGLE =\"scrollup\";\r\n"
                    + "     }"
    );
    private IfStmt dirScrollableStmt2 = (IfStmt) JavaParser.parseStatement(
            "if(scrollXTOGGLE[0] <= scrollXTOGGLE[1]){\r\n"
                    + "         scrollableDirTOGGLE += \"right\";\r\n"
                    + "     } else {\r\n"
                    + "         scrollableDirTOGGLE += \"left\";\r\n"
                    + "     }"
    );
    private TryStmt tryStmt = (TryStmt) JavaParser.parseStatement(
            "try {\n" +
            "            Thread.sleep(1000);\n" +
            "        } catch (Exception e) {\n" +
            "\n" +
            "        }");

    private TryStmt findStartingCoordY = (TryStmt) JavaParser.parseStatement(
            "try {\r\n"
                    + "            preScrollYTOGGLE = ScrollHandler.getActualOffsetFromTop(adapterViewTOGGLE);\r\n"
                    + "        } catch (Exception e) {\r\n"
                    + "            e.printStackTrace();\r\n"
                    + "        }"
    );

    private TryStmt findEndingCoordY = (TryStmt) JavaParser.parseStatement(
            "try {\r\n"
                    + "            postScrollYTOGGLE = ScrollHandler.getActualOffsetFromTop(adapterViewTOGGLE);\r\n"
                    + "        } catch (Exception e) {\r\n"
                    + "            e.printStackTrace();\r\n"
                    + "        }"
    );

    private TryStmt findStartingCoordX = (TryStmt) JavaParser.parseStatement(
            "try {\r\n"
                    + "            preScrollXTOGGLE = ScrollHandler.getActualOffsetFromStart(adapterViewTOGGLE);\r\n"
                    + "        } catch (Exception e) {\r\n"
                    + "            e.printStackTrace();\r\n"
                    + "        }"
    );

    private TryStmt findEndingCoordX = (TryStmt) JavaParser.parseStatement(
            "try {\r\n"
                    + "            postScrollXTOGGLE = ScrollHandler.getActualOffsetFromStart(adapterViewTOGGLE);\r\n"
                    + "        } catch (Exception e) {\r\n"
                    + "            e.printStackTrace();\r\n"
                    + "        }"
    );

    private String currentClass = "";

    public Enhancer(String packageName){
        this.packageName = packageName;
        this.statistic = new HashMap();
    }

    /**
     * Method to enhance a file whose path is filePath.
     * For a generic file at "<folderPath>/<fileName>.java" this method will generate :
     *  - <folderPath>/<fileName>Enhanced.java : The enhanced java class
     *  - <folderPath>/<fileName>_Statistic.txt : a file to collect statistics ?? todo
     *  - Eventually : a line reporting the exception in the log file //todo it could be not essential to specify
     * @param fileName
     * @param folderPath
     */
    public List<String> generateEnhancedClassFrom(String fileName,String folderPath){
        long time_begin = System.currentTimeMillis();
        List<String> testNames = new ArrayList<>();
        try {
            this.currentClass = fileName+"TOGGLE";
            populateEmptyStatistic();
            FileInputStream in = new FileInputStream( folderPath + fileName + ".java" );
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
            testNames = printMethods();
            //save statistics into file
            String statisticFileName = folderPath + fileName + "_Statistic.txt";
            Statistic.writeDataToFile(statistic, statisticFileName);

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            //TODO do something useful to handle different kinds of exceptions
            Utils.logException(e, "generateEnhancedClassFrom for " + folderPath + fileName);
        }

        long end_time = System.currentTimeMillis();
        long enhance_time = end_time - time_begin;
        System.out.println("Time to enhance = " + enhance_time);
        return testNames;
    }

    public List<String> printMethods() {
        List<String> testNames = new ArrayList<>();
        for (TypeDeclaration typeDec : compilationUnit.getTypes()) {
            List<BodyDeclaration> members = typeDec.getMembers();
            if (members != null) {
                for (BodyDeclaration member : members) {
                    if (member.isMethodDeclaration()) {
                        MethodDeclaration field = (MethodDeclaration) member;
                        NodeList<AnnotationExpr> annotations = member.getAnnotations();
                        for(AnnotationExpr ann:annotations){
                            if(ann.getName().asString().contains("Test")){
                                System.out.println("Method name: " + field.getNameAsString());
                                testNames.add(field.getNameAsString());
                                break;
                            }
                        }
                    }
                }
            }
        }
        return testNames;
    }

    /**
     * Method to enhance a file whose path is filePath.
     * For a generic file at "<folderPath>/<fileName>.java" this method will generate :
     *  - <folderPath>/<fileName>Enhanced.java : The enhanced java class
     *  - <folderPath>/<fileName>_Statistic.txt : a file to collect statistics ?? todo
     *  - Eventually : a line reporting the exception in the log file //todo it could be not essential to specify
     * @param filePath
     */
    public List<String> generateEnhancedClassFrom(String filePath){
        long time_begin = System.currentTimeMillis();
        List<String> testNames = new ArrayList<>();
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
            testNames = printMethods();
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
        return testNames;
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
        boolean shouldSet = true;
        for(ConstructorDeclaration constructor : constructors){
            constructor.setName(constructor.getName() + "Enhanced");
            /*if(shouldSet) {
                this.currentClass = constructor.getName().asString() + "TOGGLE";
                shouldSet = false;
            }*/
           /* constructor.getBody().asBlockStmt().addStatement(
                    JavaParser.parseStatement("TOGGLETools.setLogFileName(\""+constructor.getName().+"\");")
            );*/
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
        this.compilationUnit.addImport("android.view.View", false, false);
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
                firstOnDataTest = true;
                firstScrollToTest = true;
                parameters = new StringBuilder("");
                field = new StringBuilder("");

                //scan each statement
                int i = 0;
                while(i < nodes.size()) {
                    //gets the new index because the method has been enhanced
                    i = parseStatement(block, methodName, nodes.get(i), i);

                }
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
            if(!name.equals("intended") &&
                    !name.equals("intending") &&
                    !name.equals("perform") &&
                    !name.equals("check") ){
                String parameter = "";
                if(name.equals("atPosition")){
                    parseJsonArgument(j,null,0);
                    parameter = parameters.toString();
                    parameters = new StringBuilder();
                }
                operations.add(new Operation(name,parameter));

                //save occurrences for onView and onData
                Integer oldStatistic = statistic.get(name);
                //todo è corretto?
                if(oldStatistic == null)
                    oldStatistic = 0;
                statistic.put(name, oldStatistic + 1);
                if(name.equals("atPosition"))
                    return;
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
                parseJsonArgument(j, a = ((JSONObject) a.get(i)).getJSONArray("arguments"), 0);

                // check followed by perform or vice versa
                if (((JSONObject) a.get(0)).has("typeC")) {
                    JSONObject type = (JSONObject) ((JSONObject) a.get(0)).get("type");
                    parseLeftInArgument(type);
                    parseRightInArgument(type);
                    parseScopeInArgument(type);
                } else {
                    if (((JSONObject) a.get(0)).getString("type").equals("EnclosedExpr"))
                        a = new JSONArray().put(((JSONObject) a.get(0)).getJSONObject("inner"));

                    // parse left and right are used if there is a concatenation of strings
                    parseLeftInArgument((JSONObject) a.get(0));
                    parseRightInArgument((JSONObject) a.get(0));

                    // used when there are FieldAccessExpr
                    parseScopeInArgument((JSONObject) a.get(0));
                }
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
            String type;
            String name = "";
            if(a.getJSONObject(i).has("typeC")){
                type = a.getJSONObject(i).getString("typeC");
                name = a.getJSONObject(i).getJSONObject("type").getJSONObject("name").getString("identifier");
            } else {
                type = a.getJSONObject(i).getString("type");
                name = a.getJSONObject(i).getJSONObject("name").getString("identifier");
            }


            //name = a.getJSONObject(i).getJSONObject("name").getString("identifier");

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
            } else if (type.equals("ClassExpr") && isNotAnEspressoCommand(name)){
                parameters.append(name);
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
                try{
                    String type = a.getJSONObject(j).getString("typeC");
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
                    } else if(type.equals("ClassExpr")){
                        name = a.getJSONObject(j).getJSONObject("type").getJSONObject("name").getString("identifier");
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
                } catch (JSONException e2) {
                    Utils.logException(e, "methodParameters");
                }
            }
        }
    }

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
            try {
                //************************************
                File log = new File(methodName + ".txt");
                log.createNewFile();
                FileWriter myWriter = new FileWriter(log);
                myWriter.write(json);
                myWriter.close();
                //*****************************
            } catch (IOException e) {
                e.printStackTrace();
            }
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

            if(json.contains("ClassExpr")){
                String type = "type";
                for(int j = -1; (j=json.indexOf(type,j+1)) != -1; j++){
                    String old = json.substring(j,j+17);
                    if(old.equals("type\":\"ClassExpr\"")){
                        json = json.substring(0, j) + "typeC\": \"ClassExpr\"" + json.substring(j + 17);
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

            } /*catch (IOException e) {
                //************************************
                e.printStackTrace();
            }*/
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
            return enhanceMethodOnDataInAdapterView(block, methodName, stmt, i);
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
                        /*
                         * if (interactionType.isEmpty()) { new Exception(operations.get(j).getName() +
                         * " is not supported or is not an Espresso command").printStackTrace(); }
                         */
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

                    if (firstTest) {
                        firstTest = false;
                        block.addStatement(i, captureTask);
                        block.addStatement(++i, instrumentation);
                        block.addStatement(++i, device);
                        block.addStatement(++i, firstLogNum);
                        block.addStatement(++i, firstTestActivity);
                    } else if (j == 3 && interactionType.equals("check") || j == 2) {
                        block.addStatement(i, logNum);
                        block.addStatement(++i, activity);
                    } else {
                        block.addStatement(++i, logNum);
                        block.addStatement(++i, activity);
                    }

                    System.out.println("logcatting: methodname = " + methodName + "; searchType = " + searchType + "; searchKw = " + searchKw + "; interactionType = " + interactionType + "; interactionParams = " + interactionParams);
                    if(interactionType.equals("scrollto")){
                        //todo per lo scroll :
                        Statement firstScrollable = null;
                        Statement scrollable = null;
                        TryStmt firstScrollableTry = null;
                        TryStmt scrollableTry = null;

                        if(operations.get(1).getName().equals("withText")){
                            firstScrollable = JavaParser.parseStatement("View scrollableTOGGLE = null;");
                            firstScrollableTry = (TryStmt) JavaParser.parseStatement("try {\r\n" +
                                    "\tscrollableTOGGLE = ScrollHandler.findScrollableFromText(activityTOGGLETools,\"" + operations.get(1).getParameter().replace("\"","") + "\",false);\r\n" +
                                    "} catch(Exception e) {\r\n" +
                                    "}");
                            scrollableTry = (TryStmt) JavaParser.parseStatement("try {\r\n" +
                                    "\tscrollableTOGGLE = ScrollHandler.findScrollableFromText(activityTOGGLETools,\"" + operations.get(1).getParameter().replace("\"","") + "\",false);\r\n" +
                                    "} catch (Exception e) {\r\n" +
                                    "}");
                        } else if(operations.get(1).getName().equals("withId")){
                            firstScrollable = JavaParser.parseStatement("View scrollableTOGGLE = (View) ScrollHandler.getScrollableParent(activityTOGGLETools.findViewById(R.id." + operations.get(1).getParameter().replace("\"","") + "));");
                            scrollable = JavaParser.parseStatement("scrollableTOGGLE = (View) ScrollHandler.getScrollableParent(activityTOGGLETools.findViewById(R.id." + operations.get(1).getParameter().replace("\"","") + "));");
                        } else if(operations.get(1).getName().equals("withHint")){
                            firstScrollable = JavaParser.parseStatement("View scrollableTOGGLE = ScrollHandler.findScrollableFromText(activityTOGGLETools,\"" + operations.get(1).getParameter().replace("\"","") + "\",true);");
                            scrollable = JavaParser.parseStatement("scrollableTOGGLE = ScrollHandler.findScrollableFromText(activityTOGGLETools,\"" + operations.get(1).getParameter().replace("\"","") + "\",true);");
                        }
                        if((firstScrollable != null && scrollable != null) || ( firstScrollable!= null &&firstScrollableTry != null && scrollableTry != null)){
                            if(firstScrollToTest){
                                firstScrollToTest = false;
                                block.addStatement(++i,firstScrollX);
                                block.addStatement(++i,firstScrollY);
                                block.addStatement(++i, firstScrollable);
                                if(firstScrollableTry != null )
                                    block.addStatement(++i, firstScrollableTry);
                                block.addStatement(++i, firstScrollableClass);
                                block.addStatement(++i,firstScrollableDirectionStr);
                            } else {
                                block.addStatement(++i,scrollX);
                                block.addStatement(++i,scrollY);
                                block.addStatement(++i, scrollableClass);
                                if(scrollable != null)
                                    block.addStatement(++i, scrollable);
                                else if(scrollableTry != null )
                                    block.addStatement(++i, scrollableTry);
                                block.addStatement(++i,scrollableDirectionStr);
                            }

                            // 1 screenshot pre onView
                            block.addStatement(++i, JavaParser.parseStatement(
                                    "capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTaskProgressive(num, \"" + methodName + "\", activityTOGGLETools));"));

                            block.addStatement(++i, screenCapture);

                            // 2 take the initial offset from the start of the scrollView
                            block.addStatement(++i,getStartScrollXFromScrollable);
                            block.addStatement(++i,getStartScrollYFromScrollable);

                            // 3 take the pre scroll dump of the screen
                            block.addStatement(++i, JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, \"" + methodName + "\", device);"));

                            // 4 scroll
                            block.addStatement(++i,st);
                            block.addStatement(++i,tryStmt);

                            // 5 take the final offset inside the scrollView
                            block.addStatement(++i,getEndScrollXFromScrollable);
                            block.addStatement(++i,getEndScrollYFromScrollable);

                            // 6 log with the appropriate searchType
                            block.addStatement(++i,getScrollableClass);
                            block.addStatement(++i,dirScrollableStmt1);
                            block.addStatement(++i,dirScrollableStmt2);
                            LogCat log = new LogCat(methodName, "class-scrollTo", "scrollableClassTOGGLE+\"_;\"+ScrollHandler.getScrollableCoords(scrollableTOGGLE)", "scrollableDirTOGGLE",
                                    "scrollYTOGGLE[0]+\";\"+scrollYTOGGLE[1]+\";\"+scrollXTOGGLE[0]+\";\"+scrollXTOGGLE[1]+\";-1;-1;\"+scrollableTOGGLE.getHeight()+\";\"+scrollableTOGGLE.getWidth()+\";\"+scrollableTOGGLE.getLeft()+\";\"+scrollableTOGGLE.getTop()");
                            i = addLogInteractionToCu(log,i,block);

                            // 6.5 increment log number
                            block.addStatement(++i,logNum);

                            // 7 fare screenshot
                            block.addStatement(++i, JavaParser.parseStatement(
                                    "capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTaskProgressive(num, \"" + methodName + "\", activityTOGGLETools));"));

                            block.addStatement(++i, screenCapture);

                            // 8 fare dump
                            block.addStatement(++i, JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, \"" + methodName + "\", device);"));

                            // 9 loggare l'operazione che si sta facendo sulla view per la quale si è scrollato
                            log = new LogCat(methodName, searchType, searchKw, "check", "");
                            i = addLogInteractionToCu(log, i, block);
                        }
                    } else {
                        LogCat log = new LogCat(methodName, searchType, searchKw, interactionType, interactionParams);

                        //b.addStatement(++i, captureTaskValue);
                        block.addStatement(++i, JavaParser.parseStatement(
                                "capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTaskProgressive(num, \"" + methodName + "\", activityTOGGLETools));"));

                        block.addStatement(++i, screenCapture);

                        i = addLogInteractionToCu(log, i, block);

                        //b.addStatement(++i, dumpScreen);
                        block.addStatement(++i, JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, \"" + methodName + "\", device);"));
                        block.addStatement(++i, st);
                        block.addStatement(++i, tryStmt);
                    }
                }
            } else if(operations.get(1).getName().equals("allOf") || operations.get(1).getName().equals("anyOf")){
                boolean allOf = operations.get(1).getName().equals("allOf");
                String stmtString = stmt.toString();
                Statement st = JavaParser.parseStatement(stmtString);
                if (operations.size() > 1) {
                    block.remove(stmt);
                }
                String srct = "";
                String srck = "";
                for (int j = 2; j < operations.size(); j++) {
                    String interactionType = ViewMatchers.getSearchType(operations.get(j).getName());
                    String interactionParams = operations.get(j).getParameter();

                    if(interactionType.isEmpty()){
                        interactionType = ViewActions.getSearchType(operations.get(j).getName());

                        if(interactionType.isEmpty()){
                            interactionType = ViewAssertions.getSearchType(operations.get(j).getName());

                            if (srct.isEmpty() || interactionType.isEmpty()) {

                                if(interactionType.isEmpty()){
                                    block.addStatement(i, st);
                                    break;
                                }
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
                        if (firstTest) {
                            firstTest = false;
                            block.addStatement(i, captureTask);
                            block.addStatement(++i, instrumentation);
                            block.addStatement(++i, device);
                            block.addStatement(++i, firstLogNum);
                            block.addStatement(++i, firstTestActivity);
                        } else if (j == 3 && interactionType.equals("check") || j == 2) {
                            block.addStatement(i, logNum);
                            block.addStatement(++i, activity);
                            // this makes it work on test cases with multiple interactions avoiding the try
                            // statements to stay to the bottom
                        } else {
                            if(i >= block.getStatements().size())
                                i = block.getStatements().size()-1;
                            block.addStatement(++i, logNum);
                            block.addStatement(++i, activity);
                        }

                        System.out.println("logcatting: methodname = " + methodName + "; searchType = " + srct + "; searchKw = " + srck + "; interactionType = " + interactionType + "; interactionParams = " + interactionParams);
                        if(interactionType.equals("scrollto")){
                            Statement firstScrollable = null;
                            Statement scrollable = null;
                            TryStmt firstScrollableTry = null;
                            TryStmt scrollableTry = null;

                            if(srct.contains("id")){
                                int index = 2;
                                for(;index < j;index++){
                                    if(operations.get(index).getName().equals("withId"))
                                        break;
                                }
                                firstScrollable = JavaParser.parseStatement("View scrollableTOGGLE = (View) ScrollHandler.getScrollableParent(activityTOGGLETools.findViewById(R.id." + operations.get(index).getParameter().replace("\"","") + "));");
                                scrollable = JavaParser.parseStatement("scrollableTOGGLE = (View) ScrollHandler.getScrollableParent(activityTOGGLETools.findViewById(R.id." + operations.get(index).getParameter().replace("\"","") + "));");
                            } else if(srct.contains("text")) {
                                int index = 2;
                                boolean isWithText = false;
                                for(;index < j;index++){
                                    if(operations.get(index).getName().equals("withText")) {
                                        isWithText = true;
                                        break;
                                    } else if(operations.get(index).getName().equals("withHint")) {
                                        break;
                                    }
                                }
                                if(isWithText) {
                                    firstScrollable = JavaParser.parseStatement("View scrollableTOGGLE = null;");
                                    firstScrollableTry = (TryStmt) JavaParser.parseStatement("try {\r\n" +
                                            "\tscrollableTOGGLE = ScrollHandler.findScrollableFromText(activityTOGGLETools,\"" + operations.get(index).getParameter().replace("\"","") + "\",false);\r\n" +
                                            "} catch(Exception e) {\r\n" +
                                            "}");
                                    scrollableTry = (TryStmt) JavaParser.parseStatement("try {\r\n" +
                                            "\tscrollableTOGGLE = ScrollHandler.findScrollableFromText(activityTOGGLETools,\"" + operations.get(index).getParameter().replace("\"","") + "\",false);\r\n" +
                                            "} catch (Exception e) {\r\n" +
                                            "}");
                                } else if(srct.contains("withHint")) {
                                    firstScrollable = JavaParser.parseStatement("View scrollableTOGGLE = ScrollHandler.findScrollableFromText(activityTOGGLETools,\"" + operations.get(index).getParameter().replace("\"","") + "\",true);");
                                    scrollable = JavaParser.parseStatement("scrollableTOGGLE = ScrollHandler.findScrollableFromText(activityTOGGLETools,\"" + operations.get(index).getParameter().replace("\"","") + "\",true);");
                                }
                            }

                            if((firstScrollable != null && scrollable != null) || ( firstScrollable!= null &&firstScrollableTry != null && scrollableTry != null)){
                                if(firstScrollToTest){
                                    firstScrollToTest = false;
                                    block.addStatement(++i,firstScrollX);
                                    block.addStatement(++i,firstScrollY);
                                    block.addStatement(++i, firstScrollable);
                                    if(firstScrollableTry != null )
                                        block.addStatement(++i, firstScrollableTry);
                                    block.addStatement(++i, firstScrollableClass);
                                    block.addStatement(++i,firstScrollableDirectionStr);
                                } else {
                                    block.addStatement(++i,scrollX);
                                    block.addStatement(++i,scrollY);
                                    block.addStatement(++i, scrollableClass);
                                    if(scrollable != null)
                                        block.addStatement(++i, scrollable);
                                    else if(scrollableTry != null )
                                        block.addStatement(++i, scrollableTry);
                                    block.addStatement(++i,scrollableDirectionStr);
                                }

                                // 1 screenshot pre onView
                                block.addStatement(++i, JavaParser.parseStatement(
                                        "capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTaskProgressive(num, \"" + methodName + "\", activityTOGGLETools));"));

                                block.addStatement(++i, screenCapture);

                                // 2 take the initial offset from the start of the scrollView
                                block.addStatement(++i,getStartScrollXFromScrollable);
                                block.addStatement(++i,getStartScrollYFromScrollable);

                                // 3 take the pre scroll dump of the screen
                                block.addStatement(++i, JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, \"" + methodName + "\", device);"));

                                // 4 scroll
                                block.addStatement(++i,st);
                                block.addStatement(++i,tryStmt);

                                // 5 take the final offset inside the scrollView
                                block.addStatement(++i,getEndScrollXFromScrollable);
                                block.addStatement(++i,getEndScrollYFromScrollable);

                                // 6 log with the appropriate searchType
                                block.addStatement(++i,getScrollableClass);
                                block.addStatement(++i,dirScrollableStmt1);
                                block.addStatement(++i,dirScrollableStmt2);
                                LogCat log = new LogCat(methodName, "class-scrollTo", "scrollableClassTOGGLE+\"_;\"+ScrollHandler.getScrollableCoords(scrollableTOGGLE)", "scrollableDirTOGGLE",
                                        "scrollYTOGGLE[0]+\";\"+scrollYTOGGLE[1]+\";\"+scrollXTOGGLE[0]+\";\"+scrollXTOGGLE[1]+\";-1;-1;\"+scrollableTOGGLE.getHeight()+\";\"+scrollableTOGGLE.getWidth()+\";\"+scrollableTOGGLE.getLeft()+\";\"+scrollableTOGGLE.getTop()");
                                i = addLogInteractionToCu(log,i,block);

                                // 6.5 increment log number
                                block.addStatement(++i,logNum);

                                // 7 fare screenshot
                                block.addStatement(++i, JavaParser.parseStatement(
                                        "capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTaskProgressive(num, \"" + methodName + "\", activityTOGGLETools));"));

                                block.addStatement(++i, screenCapture);

                                // 8 fare dump
                                block.addStatement(++i, JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, \"" + methodName + "\", device);"));

                                // 9 loggare l'operazione che si sta facendo sulla view per la quale si è scrollato
                                log = new LogCat(methodName, srct, srck, "check", "");
                                i = addLogInteractionToCu(log, i, block);
                            }
                        } else {
                            LogCat log = new LogCat(methodName, srct, srck, interactionType, interactionParams);

                            //b.addStatement(++i, captureTaskValue);
                            block.addStatement(++i, JavaParser.parseStatement(
                                    "capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTaskProgressive(num, \"" + methodName + "\", activityTOGGLETools));"));

                            block.addStatement(++i, screenCapture);

                            i = addLogInteractionToCu(log, i, block);

                            //b.addStatement(++i, dumpScreen);
                            block.addStatement(++i, JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, \"" + methodName + "\", device);"));
                            block.addStatement(++i, st);
                            block.addStatement(++i, tryStmt);
                        }

                    } else {
                        if(srct.isEmpty()) {
                            srck = "";
                            srct = interactionType;
                            if(interactionParams.isEmpty())
                                srck = "-";
                            else
                                srck = interactionParams.replace("\"","");
                        } else {
                            if(allOf) {
                                srct = srct + "&" + interactionType;
                                srck = srck + "&" + interactionParams.replace("\"", "").replace(" ","_");
                            } else {
                                srct = srct + "|" + interactionType;
                                srck = srck + "|" + interactionParams.replace("\"", "").replace(" ","_");
                            }
                        }
                    }
                }
            }
            // }
        }

        return ++i;
    }

    private int enhanceMethodOnDataInAdapterView(BlockStmt block, String methodName, Statement stmt, int i) {
        try{
            // get onData(customMatcher(...)).inAdapterView(withId(R.id.'someId')
            int listIdIndex = 0;
            boolean found = false;
            while(!found && listIdIndex < (operations.size()-1)){
                if(operations.size() > (listIdIndex+1) &&
                        operations.get(listIdIndex).getName().equals("inAdapterView") &&
                        operations.get(listIdIndex+1).getName().equals("withId")){
                    found = true;
                }
                listIdIndex++;
            }
            if(!found){
                return ++i;
            }
            String listId = operations.get(listIdIndex).getParameter().replace("\"","");
            //Node inAdapterView = getOnDataInAdapterView(stmt);
            // get 'someId' in
            // onData(customMatcher(...)).inAdapterView(withId(R.id.'someId')
            //String listId = getIdInAdapterView(inAdapterView);
            Statement firstAdapterView = JavaParser.parseStatement("AdapterView adapterViewTOGGLE = ScrollHandler.findAdapterView(activityTOGGLETools.findViewById(R.id." + listId + "));\r\n");
            Statement adapterView = JavaParser.parseStatement("adapterViewTOGGLE = ScrollHandler.findAdapterView(activityTOGGLETools.findViewById(R.id." + listId + "));\r\n");

            addImportsOnData();

            // remove test
            Statement st = stmt;
            block.remove(stmt);

            if (firstTest) {
                firstTest = false;
                block.addStatement(i, captureTask);
                block.addStatement(++i, instrumentation);
                block.addStatement(++i, device);
                block.addStatement(++i, firstLogNum);
                block.addStatement(++i, firstTestActivity);
            } else {
                block.addStatement(i, logNum);
                block.addStatement(++i, activity);
            }
            if(firstOnDataTest){
                firstOnDataTest = false;
                block.addStatement(++i, firstAdapterView);
                block.addStatement(++i, firstPreScrollY);
                block.addStatement(++i, firstPreScrollX);
                block.addStatement(++i, firstPostScrollY);
                block.addStatement(++i, firstPostScrollX);
                block.addStatement(++i, firstScrollDirectionStr);
                //block.addStatement(++i, firstSingleItemFirstH);
                //block.addStatement(++i, firstSingleItemFirstW);
                //block.addStatement(++i, firstSingleItemLastH);
                //block.addStatement(++i, firstSingleItemLastW);
                block.addStatement(++i, firstSingleItemH);
                block.addStatement(++i, firstSingleItemW);
                block.addStatement(++i, firstLoc);
            } else {
                block.addStatement(++i, adapterView);
                block.addStatement(++i, preScrollY);
                block.addStatement(++i, preScrollX);
                block.addStatement(++i, postScrollY);
                block.addStatement(++i, postScrollX);
                block.addStatement(++i, scrollDirectionStr);
                /*block.addStatement(++i, singleItemFirstH);
                block.addStatement(++i, singleItemFirstW);
                block.addStatement(++i, singleItemLastH);
                block.addStatement(++i, singleItemLastW);*/
                block.addStatement(++i, singleItemH);
                block.addStatement(++i, singleItemW);
                block.addStatement(++i, loc);
            }
            block.addStatement(++i, scrollHandlerSetup);

            LogCat log;

            block.addStatement(++i, this.findStartingCoordY);
            block.addStatement(++i, this.findStartingCoordX);
            block.addStatement(++i, this.findAdapterViewTopLeft);
            //todo bisognerebbe prima capire se questa operazione fa un click o no
            // e se fa un click bisogna prima fare un check(isDisplayed); poi si prende l'offset finale e poi si esegue il test originale
            // quindi quanto sotto va cambiato
            // if it's empty could be a check
            int numberOfOperations = operations.size();
            String interaction = operations.get(numberOfOperations - 1).getName();
            String interactionType = ViewActions.getSearchType(interaction);
            String interactionParams = operations.get(numberOfOperations - 1).getParameter();
            block.addStatement(++i,getSingleItemHeightFirst);
            //block.addStatement(++i,getSingleItemHeightLast);
            block.addStatement(++i,getSingleItemWidthFirst);
            //block.addStatement(++i,getSingleItemWidthLast);
            Statement stmt2 = null;
            if (interactionType.isEmpty()) {
                // if it's not empty it is a check
                if (!ViewAssertions.getSearchType(interaction).isEmpty()) {
                    interactionType = "check";
                    interactionParams = "";
                }
            } else {
                //it is not a check so we should split the espresso interaction
                int index = stmt.toString().lastIndexOf(".perform(");
                String substmt = stmt.toString().substring(0,index);
                substmt = substmt+".check(matches(isDisplayed()));";
                stmt2 = stmt;
                stmt = JavaParser.parseStatement(substmt);
            }
            //take a screenshot of the starting screen and dump the view hierarchy
            block.addStatement(++i, JavaParser.parseStatement(
                    "capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTaskProgressive(num, \"" + methodName + "\", activityTOGGLETools));"));
            block.addStatement(++i, screenCapture);
            block.addStatement(++i, JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, \"" +methodName + "\", device);"));

            //execute the statement
            block.addStatement(++i,stmt);
            block.addStatement(++i,tryStmt);
            block.addStatement(++i, findEndingCoordY);
            block.addStatement(++i, findEndingCoordX);
            block.addStatement(++i, dirScrollStmt1);
            block.addStatement(++i, dirScrollStmt2);
            log = new LogCat(methodName, "id-adapterView", "\"" + listId + "\"", "scrollDirTOGGLE",
                    "preScrollYTOGGLE+\";\"+postScrollYTOGGLE+\";\"+preScrollXTOGGLE+\";\"+postScrollXTOGGLE+\";\"+heightTOGGLE+\";\"+widthTOGGLE+\";\"+adapterViewTOGGLE.getHeight()+\";\"+adapterViewTOGGLE.getWidth()+\";\"+locTOGGLE[0]+\";\"+locTOGGLE[1]");
            i = addLogInteractionToCu(log,i,block);
            //block.addStatement(++i, JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, \"" +methodName + "\", device);"));
            String searchType = "";
            String searchKw = "";
            for(int j = 1; j < operations.size(); j++){
                if(operations.get(j).getName().equals("is")){
                    searchType = "text_adapterView";
                    searchKw = "\""+listId+"_;"+operations.get(j).getParameter().replace("\"","")+"\"";
                    break;
                }else if(operations.get(j).getName().equals("atPosition")){
                    searchType = "atposition";
                    searchKw = "\""+listId +"_;"+operations.get(j).getParameter()+"_;\"+adapterViewTOGGLE.getFirstVisiblePosition()+\"_;\"+adapterViewTOGGLE.getLastVisiblePosition()";
                    break;
                }/*else if(){

                }*/
            }
            block.addStatement(++i, logNum);
            block.addStatement(++i, JavaParser.parseStatement(
                    "capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTaskProgressive(num, \"" + methodName + "\", activityTOGGLETools));"));


            block.addStatement(++i, screenCapture);
            block.addStatement(++i, JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, \"" +methodName + "\", device);"));
            log = new LogCat(methodName, searchType, searchKw, "check","");
            i = addLogInteractionToCu(log,i,block);


            if(stmt2 != null){
                block.addStatement(++i, logNum);
                block.addStatement(++i, tryStmt);
                block.addStatement(++i, JavaParser.parseStatement(
                        "capture_task = new FutureTask<Boolean> (new TOGGLETools.TakeScreenCaptureTaskProgressive(num, \"" + methodName + "\", activityTOGGLETools));"));

                block.addStatement(++i, screenCapture);
                log = new LogCat(methodName, "position-adapterView", "\""+listId+"_;\"+ScrollHandler.getAdapterViewPosFrom(postScrollYTOGGLE,postScrollXTOGGLE,adapterViewTOGGLE)", interactionType,
                        interactionParams);
                i = addLogInteractionToCu(log,i,block);
                block.addStatement(++i, JavaParser.parseStatement("TOGGLETools.DumpScreenProgressive(num, \"" +methodName + "\", device);"));

                block.addStatement(++i, stmt2);
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
        compilationUnit.addImport("android.widget.AdapterView",false,false);
        //todo
    }

    private String getIdInAdapterView(Node inAdapterView) throws Exception {
        Node withIdNode = inAdapterView.getChildNodes().get(2);
        Node resourceIdNode = withIdNode.getChildNodes().get(1);
        Node idNode = resourceIdNode.getChildNodes().get(1);
        return idNode.toString();
    }

    private Node getOnDatAtPosition(Statement stmt) {
        List<Node> children = stmt.getChildNodes();
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

        boolean anyAll = false;
        if(log.getSearchType().contains("&") ||
                log.getSearchType().contains("|") ||
                (log.getSearchType().contains("class") && !isScrollingInteraction(log.getInteractionType()))){
            String tmp ="\"" + log.getSearchKw() +"\"";
            log.setSearchKw(tmp);
        }

        /*for (Operation o : operations) {
            if (o.getName().equals("anyOf") || o.getName().equals("allOf") || o.getName().equals("instanceOf")) {
                anyAll = true;
                break;
            }
        }

        if(anyAll){
            String tmp ="\"" + log.getSearchKw() +"\"";
            log.setSearchKw(tmp);
        }*/
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



                        l = JavaParser.parseStatement(
                                "TOGGLETools.LogInteractionProgressive(\""+this.currentClass+"\", num, " + "\"" + log.getMethodName()
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


                        l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(\""+this.currentClass+"\", num, " + "\"" + log.getMethodName()
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


                    l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(\""+this.currentClass+"\", num, " + "\"" + log.getMethodName() + "\","
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

                        l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(\""+this.currentClass+"\", num, " + "\"" + log.getMethodName() + "\","
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

                        l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(\""+this.currentClass+"\", num, " + "\"" + log.getMethodName() + "\","
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


                    l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(\""+this.currentClass+"\", num, " + "\"" + log.getMethodName() + "\","
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


                stmt = "TOGGLETools.LogInteractionProgressive(\""+this.currentClass+"\", num, " + "\"" + log.getMethodName() + "\"," + "\"" + log.getSearchType()
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
                l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(\""+this.currentClass+"\", num, " + "\"" + log.getMethodName() + "\","
                        + "\"-\", \"-\"," + "\"" + log.getInteractionType() + "\"" + ");");


                //l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\"" + log.getMethodName() + "\","
                //		+ "\"-\", \"-\"," + "\"" + log.getInteractionType() + "\"" + ");");
                break;


            case "typeintofocused":

                System.out.println(log.getInteractionParams());
                l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(\""+this.currentClass+"\", num, " + "\"" + log.getMethodName() + "\","
                        + "\"-\", \"-\"," + "\"" + log.getInteractionType() + "\", \"" + log.getInteractionParams() + "\"" + ");");


                //l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now, " + "\"" + log.getMethodName() + "\","
                //		+ "\"-\", \"-\"," + "\"" + log.getInteractionType() + "\", \"" + log.getInteractionParams() + "\"" + ");");
                break;
            case "scrollDirTOGGLE":
            case "scrollableDirTOGGLE":
                System.out.println(log.getInteractionParams());
                System.out.println("TOGGLETools.LogInteractionProgressive(\""+this.currentClass+"\", num, " + "\"" + log.getMethodName() + "\","
                        + "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw() + ","
                        + log.getInteractionType() + "," + log.getInteractionParams() + ");");
                l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(\""+this.currentClass+"\", num, " + "\"" + log.getMethodName() + "\","
                        + "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw() + ","
                        + log.getInteractionType() + "," + log.getInteractionParams() + ");");

                break;
            default:
                if (log.getInteractionParams().isEmpty()) {
                    l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(\"" + this.currentClass + "\", num, " + "\"" + log.getMethodName() + "\","
                            + "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw() + "," + "\""
                            + log.getInteractionType() + "\"" + ");");
                }

                    //l = JavaParser.parseStatement("TOGGLETools.LogInteraction(now," + "\"" + log.getMethodName() + "\","
                    //		+ "\"" + log.getSearchType() + "\"" + "," + log.getSearchKw() + "," + "\""
                    //		+ log.getInteractionType() + "\"" + ");");
                else

                    l = JavaParser.parseStatement("TOGGLETools.LogInteractionProgressive(\""+this.currentClass+"\", num, " + "\"" + log.getMethodName() + "\","
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

    private boolean isScrollingInteraction(String interactionType) {
        return interactionType.contains("scrollDirTOGGLE") ||
                interactionType.contains("scrollableDirTOGGLE");
    }

    private void addFullCheck(BlockStmt b, String methodName, int i) {
        Statement currDisp = JavaParser
                .parseStatement("Rect currdisp = TOGGLETools.GetCurrentDisplaySize(activityTOGGLETools);");


        String stmt = "TOGGLETools.LogInteractionProgressive(\""+this.currentClass+"\", num, " + "\"" + methodName
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
