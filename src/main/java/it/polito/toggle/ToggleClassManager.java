package it.polito.toggle;

import it.polito.toggle.exceptions.ToggleException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ToggleClassManager {
    //private static final String logcat_filename = "logcat.txt";
    private static final String logcat_filename = "mylog.txt";
    private static List<ToggleInteraction> interactions = new ArrayList<>();
    private static final String logcat_tool_tag = "touchtest";

    private String class_name;
    private String package_name;
    private String starting_folder;
    private ArrayList<String> testNames;

    private int deviceWidth;
    private int deviceHeight;
    private int actualWidth;
    private int actualHeight;

    public ToggleClassManager(String class_name,String package_name,String starting_folder,ArrayList<String> testNames) {
        super();

        this.class_name = class_name;
        this.package_name = package_name;
        this.starting_folder = starting_folder;
        this.testNames = testNames;

        this.deviceWidth = -1;
        this.deviceHeight = -1;
        this.actualWidth = -1;
        this.actualHeight = -1;
    }

    public ToggleClassManager(String class_name,String package_name,String starting_folder,ArrayList<String> testNames,int deviceWidth, int actualWidth) {
        super();

        this.class_name = class_name;
        this.package_name = package_name;
        this.starting_folder = starting_folder;
        this.testNames = testNames;

        this.deviceWidth = deviceWidth;
        this.deviceHeight = -1;
        this.actualWidth = actualWidth;
        this.actualHeight = -1;
    }

    public ToggleClassManager(
            String class_name,
            String package_name,
            String starting_folder,
            ArrayList<String> testNames,
            String deviceMeasures,
            int actualWidth,
            int actualHeight) {
        super();

        this.class_name = class_name;
        this.package_name = package_name;
        this.starting_folder = starting_folder;
        this.testNames = testNames;

        this.deviceWidth = Integer.parseInt(deviceMeasures.split("x")[0]);
        this.deviceHeight = Integer.parseInt(deviceMeasures.split("x")[1]);
        this.actualWidth = actualWidth;
        this.actualHeight = actualHeight;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getStarting_folder() {
        return starting_folder;
    }

    public void setStarting_folder(String starting_folder) {
        this.starting_folder = starting_folder;
    }

    public ArrayList<String> getTestNames() {
        return testNames;
    }

    public void setTestNames(ArrayList<String> testNames) {
        this.testNames = testNames;
    }

    //***************
    //returns in a string the right header for the creation of a class
    //***************

    private static ArrayList<String> createHeaders(){
        ArrayList<String> headers = new ArrayList<>();

        headers.add("import java.awt.AWTException;");
        headers.add("import java.awt.MouseInfo;");
        headers.add("import java.awt.Robot;");
        headers.add("import java.awt.event.InputEvent;");
        headers.add("import java.awt.image.BufferedImage;");
        headers.add("import java.io.File;");
        headers.add("import java.io.IOException;");
        headers.add("import javax.imageio.ImageIO;");
        headers.add("import eyeautomate.ScriptRunner;");
        headers.add("import eyeautomate.VizionEngine;");
        headers.add("import org.sikuli.script.*;");
        headers.add("import eye.Eye;");
        headers.add("import eye.Eye.RecognitionMode;");
        headers.add("import eye.Match;");
        headers.add("import eyeautomate.*;");
        headers.add("import Utils.AppStarter;");

        return headers;
    }

    private ArrayList<String> createEyeAutomateOrSikuliRun(){
        ArrayList<String> res = new ArrayList<>();
        res.add("public static void run(String[] args) throws InterruptedException, IOException {");
        res.add("\n");
        res.add("\tint tests_ok = 0;");
        res.add("\tint tests_failed = 0;");

        res.add("\tlong startTime = 0;");
        res.add("\tlong endTime = 0;");
        res.add("\tlong executionTime = 0;");
        res.add("\tString curr_test_return = \"\";");
        res.add("\tString curr_test_res = \"\";");
        res.add("\tint curr_test_interactions = 0;");

        for (String test: testNames) {
            res.add("\tAppStarter.start();");
            res.add("\tSystem.out.println(\"Starting test + " + test + "\");");

            res.add("\tstartTime = System.currentTimeMillis();");
            res.add("\ttry {");
            res.add("\t\tcurr_test_return = " + test + "();");
            //res.add("\t\tif (curr_test_return == true) {");
            //res.add("\t\tcurr_test_return = " + test + "();");
            res.add("\t\tcurr_test_res = curr_test_return.split(\";\")[0];");
            res.add("\t\tcurr_test_interactions = Integer.valueOf(curr_test_return.split(\";\")[1]);");
            res.add("\t\tif (curr_test_res.equals(\"pass\")) {");


            res.add("\t\t\tSystem.out.println(\"" + test + " ok\");");
            res.add("\t\t\ttests_ok++;");
            res.add("\t\t}");
            res.add("\t\telse {");
            res.add("\t\t\tSystem.out.println(\"" + test + " failed\");");
            res.add("\t\t\ttests_failed++;");
            res.add("\t\t}");
            res.add("\t}");
            res.add("\tcatch (Exception e) {");
            res.add("\t\tSystem.out.println(\"" + test + " failed: \" + e.getMessage());");
            res.add("\t\ttests_failed++;");
            res.add("\t}");


            res.add("\tendTime = System.currentTimeMillis();");
            res.add("\texecutionTime = endTime - startTime;");
            res.add("\tSystem.out.println(\"Execution time: \" + executionTime);");
            res.add("\tSystem.out.println(\"" + package_name + ";" + class_name + ";" + test + ";\" + curr_test_res +\";\" + executionTime + \";\" +  curr_test_interactions);");
            res.add("\tAppStarter.stop();");
            res.add("\tThread.sleep(2000);");

            res.add("\n\n");

        }

        res.add("\tSystem.out.println(\"Passed tests: \" + tests_ok);");
        res.add("\tSystem.out.println(\"Failed tests: \" + tests_failed);");


        res.add("\treturn;");
        res.add("}");

        return res;
    }

    private ArrayList<String> createCombinedRunEyeAutomateFirst(){
        ArrayList<String> res = new ArrayList<>();

        res.add("public static void run(String[] args) throws InterruptedException, IOException {");
        res.add("\n");
        res.add("\tint tests_ok = 0;");
        res.add("\tint tests_failed = 0;");

        res.add("\tint eyeautomate_failures = 0; //number of fallbacks");
        res.add("\tint curr_test_interactions = 0;");


        res.add("\tlong startTime = 0;");
        res.add("\tlong endTime = 0;");
        res.add("\tlong executionTime = 0;");
        res.add("\tString curr_test_return = \"\";");
        res.add("\tString curr_test_res = \"\";");

        res.add("\tint curr_test_eyeautomate_failures = 0;");


        for (String test: testNames) {
            res.add("\tAppStarter.start();");
            res.add("\tSystem.out.println(\"Starting test + " + test + "\");");

            res.add("\tstartTime = System.currentTimeMillis();");
            res.add("\ttry {");
            res.add("\t\tcurr_test_return = " + test + "();");
            res.add("\t\tcurr_test_res = curr_test_return.split(\";\")[0];");
            res.add("\t\tcurr_test_eyeautomate_failures = Integer.valueOf(curr_test_return.split(\";\")[1]);");
            res.add("\t\tcurr_test_interactions = Integer.valueOf(curr_test_return.split(\";\")[2]);");
            res.add("\t\tif (curr_test_res.equals(\"pass\")) {");
            res.add("\t\t\tSystem.out.println(\"" + test + " ok\");");
            res.add("\t\t\ttests_ok++;");
            res.add("\t\t}");
            res.add("\t\telse {");
            res.add("\t\t\tSystem.out.println(\"" + test + " failed\");");
            res.add("\t\t\ttests_failed++;");
            res.add("\t\t}");
            res.add("\t}");
            res.add("\tcatch (Exception e) {");
            res.add("\t\tSystem.out.println(\"" + test + " failed: \" + e.getMessage());");
            res.add("\t\ttests_failed++;");
            res.add("\t}");

            res.add("\tSystem.out.println(\"Number of EyeAutomate failures: \" + curr_test_eyeautomate_failures);");
            res.add("\teyeautomate_failures += curr_test_eyeautomate_failures;");


            res.add("\tendTime = System.currentTimeMillis();");
            res.add("\texecutionTime = endTime - startTime;");
            res.add("\tSystem.out.println(\"Execution time: \" + executionTime);");


            res.add("\tSystem.out.println(\"" + package_name + ";" + class_name + ";" + test + ";\" + curr_test_res +\";\" + executionTime + \";\" +  curr_test_interactions + \";\" + curr_test_eyeautomate_failures);");
            res.add("\tAppStarter.stop();");
            res.add("\tThread.sleep(2000);");
            res.add("\n\n");

        }

        res.add("\tSystem.out.println(\"Passed tests: \" + tests_ok);");
        res.add("\tSystem.out.println(\"Failed tests: \" + tests_failed);");
        res.add("\tSystem.out.println(\"Total Eyeautomate failures: \" + eyeautomate_failures);");



        res.add("\treturn;");
        res.add("}");

        return res;
    }

    private ArrayList<String> createCombinedRunSikuliFirst() {
        ArrayList<String> res = new ArrayList<>();

        res.add("public static void run(String[] args) throws InterruptedException, IOException {");
        res.add("\n");
        res.add("\tint tests_ok = 0;");
        res.add("\tint tests_failed = 0;");

        res.add("\tint sikuli_failures = 0; //number of fallbacks");
        res.add("\tint curr_test_interactions = 0;");

        res.add("\tlong startTime = 0;");
        res.add("\tlong endTime = 0;");
        res.add("\tlong executionTime = 0;");
        res.add("\tString curr_test_return = \"\";");
        res.add("\tString curr_test_res = \"\";");

        res.add("\tint curr_test_sikuli_failures = 0;");


        for (String test: testNames) {
            res.add("\tAppStarter.start();");
            res.add("\tstartTime = System.currentTimeMillis();");
            res.add("\ttry {");
            res.add("\t\tcurr_test_return = " + test + "();");
            res.add("\t\tcurr_test_res = curr_test_return.split(\";\")[0];");
            res.add("\t\tcurr_test_sikuli_failures = Integer.valueOf(curr_test_return.split(\";\")[1]);");
            res.add("\t\tcurr_test_interactions = Integer.valueOf(curr_test_return.split(\";\")[2]);");

            res.add("\t\tif (curr_test_res.equals(\"pass\")) {");
            res.add("\t\t\tSystem.out.println(\"" + test + " ok\");");
            res.add("\t\t\ttests_ok++;");
            res.add("\t\t}");
            res.add("\t\telse {");
            res.add("\t\t\tSystem.out.println(\"" + test + " failed\");");
            res.add("\t\t\ttests_failed++;");
            res.add("\t\t}");
            res.add("\t}");
            res.add("\tcatch (Exception e) {");
            res.add("\t\tSystem.out.println(\"" + test + " failed: \" + e.getMessage());");
            res.add("\t\ttests_failed++;");
            res.add("\t}");


            res.add("\tSystem.out.println(\"Number of Sikuli failures: \" + curr_test_sikuli_failures);");
            res.add("\tsikuli_failures += curr_test_sikuli_failures;");


            res.add("\tendTime = System.currentTimeMillis();");
            res.add("\texecutionTime = endTime - startTime;");
            res.add("\tSystem.out.println(\"Execution time: \" + executionTime);");


            res.add("\n\n");
            res.add("\tAppStarter.stop();");
            res.add("\tThread.sleep(2000);");
            res.add("\tSystem.out.println(\"" + package_name + ";" + class_name + ";" + test + ";\" + curr_test_res +\";\" + executionTime + \";\" +  curr_test_interactions + \";\" + curr_test_sikuli_failures);");
            res.add("\n\n");

        }

        res.add("\tSystem.out.println(\"Passed tests: \" + tests_ok);");
        res.add("\tSystem.out.println(\"Failed tests: \" + tests_failed);");
        res.add("\tSystem.out.println(\"Total Sikuli failures: \" + sikuli_failures);");



        res.add("\treturn;");
        res.add("}");

        return res;
    }

    private ArrayList<String> createEyeAutomateOrSikuliJavaMain() {
        ArrayList<String> res = new ArrayList<>();
        res.add("public static void main(String[] args) throws InterruptedException {");
        res.add("\n");
        res.add("\tint tests_ok = 0;");
        res.add("\tint tests_failed = 0;");

        res.add("\tlong startTime = 0;");
        res.add("\tlong endTime = 0;");
        res.add("\tlong executionTime = 0;");
        res.add("\tString curr_test_return = \"\";");
        res.add("\tString curr_test_res = \"\";");
        res.add("\tint curr_test_interactions = 0;");

        for (String test: testNames) {

            res.add("\tSystem.out.println(\"Starting test + " + test + "\");");

            res.add("\tstartTime = System.currentTimeMillis();");
            res.add("\ttry {");
            res.add("\t\tcurr_test_return = " + test + "();");
            //res.add("\t\tif (curr_test_return == true) {");
            //res.add("\t\tcurr_test_return = " + test + "();");
            res.add("\t\tcurr_test_res = curr_test_return.split(\";\")[0];");
            res.add("\t\tcurr_test_interactions = Integer.valueOf(curr_test_return.split(\";\")[1]);");
            res.add("\t\tif (curr_test_res.equals(\"pass\")) {");


            res.add("\t\t\tSystem.out.println(\"" + test + " ok\");");
            res.add("\t\t\ttests_ok++;");
            res.add("\t\t}");
            res.add("\t\telse {");
            res.add("\t\t\tSystem.out.println(\"" + test + " failed\");");
            res.add("\t\t\ttests_failed++;");
            res.add("\t\t}");
            res.add("\t}");
            res.add("\tcatch (Exception e) {");
            res.add("\t\tSystem.out.println(\"" + test + " failed: \" + e.getMessage());");
            res.add("\t\ttests_failed++;");
            res.add("\t}");


            res.add("\tendTime = System.currentTimeMillis();");
            res.add("\texecutionTime = endTime - startTime;");
            res.add("\tSystem.out.println(\"Execution time: \" + executionTime);");
            res.add("\tSystem.out.println(\"" + package_name + ";" + class_name + ";" + test + ";\" + curr_test_res +\";\" + executionTime + \";\" +  curr_test_interactions);");
            res.add("\tThread.sleep(2000);");

            res.add("\n\n");

        }

        res.add("\tSystem.out.println(\"Passed tests: \" + tests_ok);");
        res.add("\tSystem.out.println(\"Failed tests: \" + tests_failed);");


        res.add("\treturn;");
        res.add("}");

        return res;
    }

    private ArrayList<String> createCombinedMainEyeAutomateFirst(){
        ArrayList<String> res = new ArrayList<>();

        res.add("public static void main(String[] args) throws InterruptedException {");
        res.add("\n");
        res.add("\tint tests_ok = 0;");
        res.add("\tint tests_failed = 0;");

        res.add("\tint eyeautomate_failures = 0; //number of fallbacks");
        res.add("\tint curr_test_interactions = 0;");


        res.add("\tlong startTime = 0;");
        res.add("\tlong endTime = 0;");
        res.add("\tlong executionTime = 0;");
        res.add("\tString curr_test_return = \"\";");
        res.add("\tString curr_test_res = \"\";");

        res.add("\tint curr_test_eyeautomate_failures = 0;");


        for (String test: testNames) {

            res.add("\tSystem.out.println(\"Starting test + " + test + "\");");

            res.add("\tstartTime = System.currentTimeMillis();");
            res.add("\ttry {");
            res.add("\t\tcurr_test_return = " + test + "();");
            res.add("\t\tcurr_test_res = curr_test_return.split(\";\")[0];");
            res.add("\t\tcurr_test_eyeautomate_failures = Integer.valueOf(curr_test_return.split(\";\")[1]);");
            res.add("\t\tcurr_test_interactions = Integer.valueOf(curr_test_return.split(\";\")[2]);");
            res.add("\t\tif (curr_test_res.equals(\"pass\")) {");
            res.add("\t\t\tSystem.out.println(\"" + test + " ok\");");
            res.add("\t\t\ttests_ok++;");
            res.add("\t\t}");
            res.add("\t\telse {");
            res.add("\t\t\tSystem.out.println(\"" + test + " failed\");");
            res.add("\t\t\ttests_failed++;");
            res.add("\t\t}");
            res.add("\t}");
            res.add("\tcatch (Exception e) {");
            res.add("\t\tSystem.out.println(\"" + test + " failed: \" + e.getMessage());");
            res.add("\t\ttests_failed++;");
            res.add("\t}");

            res.add("\tSystem.out.println(\"Number of EyeAutomate failures: \" + curr_test_eyeautomate_failures);");
            res.add("\teyeautomate_failures += curr_test_eyeautomate_failures;");


            res.add("\tendTime = System.currentTimeMillis();");
            res.add("\texecutionTime = endTime - startTime;");
            res.add("\tSystem.out.println(\"Execution time: \" + executionTime);");


            res.add("\tSystem.out.println(\"" + package_name + ";" + class_name + ";" + test + ";\" + curr_test_res +\";\" + executionTime + \";\" +  curr_test_interactions + \";\" + curr_test_eyeautomate_failures);");
            res.add("\tThread.sleep(2000);");
            res.add("\n\n");

        }

        res.add("\tSystem.out.println(\"Passed tests: \" + tests_ok);");
        res.add("\tSystem.out.println(\"Failed tests: \" + tests_failed);");
        res.add("\tSystem.out.println(\"Total Eyeautomate failures: \" + eyeautomate_failures);");



        res.add("\treturn;");
        res.add("}");

        return res;
    }

    private ArrayList<String> createCombinedMainSikuliFirst() {
        ArrayList<String> res = new ArrayList<>();

        res.add("public static void main(String[] args) throws InterruptedException {");
        res.add("\n");
        res.add("\tint tests_ok = 0;");
        res.add("\tint tests_failed = 0;");

        res.add("\tint sikuli_failures = 0; //number of fallbacks");
        res.add("\tint curr_test_interactions = 0;");

        res.add("\tlong startTime = 0;");
        res.add("\tlong endTime = 0;");
        res.add("\tlong executionTime = 0;");
        res.add("\tString curr_test_return = \"\";");
        res.add("\tString curr_test_res = \"\";");

        res.add("\tint curr_test_sikuli_failures = 0;");


        for (String test: testNames) {

            res.add("\tstartTime = System.currentTimeMillis();");
            res.add("\ttry {");
            res.add("\t\tcurr_test_return = " + test + "();");
            res.add("\t\tcurr_test_res = curr_test_return.split(\";\")[0];");
            res.add("\t\tcurr_test_sikuli_failures = Integer.valueOf(curr_test_return.split(\";\")[1]);");
            res.add("\t\tcurr_test_interactions = Integer.valueOf(curr_test_return.split(\";\")[2]);");

            res.add("\t\tif (curr_test_res.equals(\"pass\")) {");
            res.add("\t\t\tSystem.out.println(\"" + test + " ok\");");
            res.add("\t\t\ttests_ok++;");
            res.add("\t\t}");
            res.add("\t\telse {");
            res.add("\t\t\tSystem.out.println(\"" + test + " failed\");");
            res.add("\t\t\ttests_failed++;");
            res.add("\t\t}");
            res.add("\t}");
            res.add("\tcatch (Exception e) {");
            res.add("\t\tSystem.out.println(\"" + test + " failed: \" + e.getMessage());");
            res.add("\t\ttests_failed++;");
            res.add("\t}");


            res.add("\tSystem.out.println(\"Number of Sikuli failures: \" + curr_test_sikuli_failures);");
            res.add("\tsikuli_failures += curr_test_sikuli_failures;");


            res.add("\tendTime = System.currentTimeMillis();");
            res.add("\texecutionTime = endTime - startTime;");
            res.add("\tSystem.out.println(\"Execution time: \" + executionTime);");


            res.add("\n\n");
            res.add("\tThread.sleep(2000);");
            res.add("\tSystem.out.println(\"" + package_name + ";" + class_name + ";" + test + ";\" + curr_test_res +\";\" + executionTime + \";\" +  curr_test_interactions + \";\" + curr_test_sikuli_failures);");
            res.add("\n\n");

        }

        res.add("\tSystem.out.println(\"Passed tests: \" + tests_ok);");
        res.add("\tSystem.out.println(\"Failed tests: \" + tests_failed);");
        res.add("\tSystem.out.println(\"Total Sikuli failures: \" + sikuli_failures);");



        res.add("\treturn;");
        res.add("}");

        return res;
    }

    public ArrayList<String> createClass() throws IOException, XPathExpressionException, SAXException, ParserConfigurationException, ToggleException {
        //TODO
        //FUNZIONAMENTO: si aggiunge al logger come primo parametro dopo TOGGLETOOL il nome del test;
        //createClass lancia per ogni nome di test ricevuto un toggle translator.
        //gli script sikuli ed eyeautomate vengono salvati direttamente all'interno della cartella con le giuste immagini
        //gli script java vengono salvati all'interno di una cartella dove:
        //1) si salvano le immagini (tutte)
        //2) si crea una classe Main.java, la classe main contiene un metodo per ogni test + nel main lancia tutti i test e fa girare statistiche su quanti hanno ritornato true e quanti null

        int method_interactions = 0;

        float ratioH = this.actualHeight / (float) this.deviceHeight;
        float ratioW = this.actualWidth / (float) this.deviceWidth;

        ArrayList<String> test_class_code = new ArrayList<>();

        ArrayList<String> eyeautomate_only = new ArrayList<>();
        ArrayList<String> sikuli_only = new ArrayList<>();
        ArrayList<String> eyeautomate_sikuli = new ArrayList<>();
        ArrayList<String> sikuli_eyeautomate = new ArrayList<>();

        //ADD HEADERS
        for(String header : createHeaders()){
            eyeautomate_only.add(header);
            sikuli_only.add(header);
            eyeautomate_sikuli.add(header);
            sikuli_eyeautomate.add(header);
        }

        //add class spec
        eyeautomate_only.add("\n\n");
        eyeautomate_only.add("public class " + class_name + "EyeAutomate { ");
        eyeautomate_only.add("\n\n");

        sikuli_only.add("\n\n");
        sikuli_only.add("public class " + class_name + "Sikuli { ");
        sikuli_only.add("\n\n");

        eyeautomate_sikuli.add("\n\n");
        eyeautomate_sikuli.add("public class " + class_name + "EyeAutomateSikuli { ");
        eyeautomate_sikuli.add("\n\n");

        sikuli_eyeautomate.add("\n\n");
        sikuli_eyeautomate.add("public class " + class_name+ "SikuliEyeAutomate { ");
        sikuli_eyeautomate.add("\n\n");

        //add the methods
        for(String test_name: testNames){
            method_interactions = 0;
            ToggleTranslator translator = new ToggleTranslator(starting_folder, package_name, class_name, test_name, ratioH, ratioW);

            //translator.readLogcatToFile(logcat_filename);

            List<String> filtered_logcat_interactions = translator.filterLogcat(logcat_filename,logcat_tool_tag);

            interactions = new ArrayList<>();

            for(String logInteraction : filtered_logcat_interactions){
                ToggleInteraction interaction = translator.readInteractionsFromLogcat(logInteraction);
                interactions.add(interaction);
                method_interactions++;
            }

            //never comment
            if(deviceWidth > 0 && actualWidth > 0)
                translator.saveCroppedScreenshots(interactions,deviceWidth,actualWidth);
            else
                translator.saveCroppedScreenshots(interactions);
            translator.createEyeStudioScript(interactions);
            translator.createSikuliScript(interactions);
            //translator.createSikuliJavaMethod(interactions);
            //translator.createCombinedJavaMethod(interactions);

            eyeautomate_only.addAll(translator.createEyeAutomateJavaMethod(interactions));
            sikuli_only.addAll(translator.createSikuliJavaMethod(interactions));
            eyeautomate_sikuli.addAll(translator.createCombinedJavaMethod(interactions));
            sikuli_eyeautomate.addAll(translator.createCombinedJavaMethodSikuliFirst(interactions));

            eyeautomate_only.add("\n\n\n");
            sikuli_only.add("\n\n\n");
            eyeautomate_sikuli.add("\n\n\n");
            sikuli_eyeautomate.add("\n\n\n");
        }
        //add the main function
        eyeautomate_only.add("\n\n\n");
        sikuli_only.add("\n\n\n");
        eyeautomate_sikuli.add("\n\n\n");
        sikuli_eyeautomate.add("\n\n\n");

        ArrayList<String> eyeAutomateOrSiculiMain = this.createEyeAutomateOrSikuliJavaMain();
        eyeautomate_only.addAll(eyeAutomateOrSiculiMain);
        sikuli_only.addAll(eyeAutomateOrSiculiMain);
        /*for (String main_line: this.createEyeAutomateOrSikuliJavaMain()) {
            eyeautomate_only.add(main_line);
            sikuli_only.add(main_line);
        }*/
        eyeautomate_sikuli.addAll(this.createCombinedMainEyeAutomateFirst());
        sikuli_eyeautomate.addAll(this.createCombinedMainSikuliFirst());

        //add closure of function
        eyeautomate_only.add("\n\n");
        eyeautomate_only.add("}");

        sikuli_only.add("\n\n");
        sikuli_only.add("}");

        eyeautomate_sikuli.add("\n\n");
        eyeautomate_sikuli.add("}");

        sikuli_eyeautomate.add("\n\n");
        sikuli_eyeautomate.add("}");


        writeOnFile(starting_folder + "\\JavaTranslatedProject\\src\\" + class_name + "EyeAutomate.java",eyeautomate_only);
        writeOnFile(starting_folder + "\\JavaTranslatedProject\\src\\" + class_name + "Sikuli.java",sikuli_only);
        writeOnFile(starting_folder + "\\JavaTranslatedProject\\src\\" + class_name + "EyeAutomateSikuli.java",eyeautomate_sikuli);
        writeOnFile(starting_folder + "\\JavaTranslatedProject\\src\\" + class_name + "SikuliEyeAutomate.java",sikuli_eyeautomate);

        return test_class_code;
    }

    public ArrayList<String> createClass(String logName) throws IOException, XPathExpressionException, SAXException, ParserConfigurationException, ToggleException {
        //FUNZIONAMENTO: si aggiunge al logger come primo parametro dopo TOGGLETOOL il nome del test;
        //createClass lancia per ogni nome di test ricevuto un toggle translator.
        //gli script sikuli ed eyeautomate vengono salvati direttamente all'interno della cartella con le giuste immagini
        //gli script java vengono salvati all'interno di una cartella dove:
        //1) si salvano le immagini (tutte)
        //2) si crea una classe Main.java, la classe main contiene un metodo per ogni test + nel main lancia tutti i test e fa girare statistiche su quanti hanno ritornato true e quanti null

        int method_interactions = 0;

        float ratioH = this.actualHeight / (float) this.deviceHeight;
        float ratioW = this.actualWidth / (float) this.deviceWidth;

        ArrayList<String> test_class_code = new ArrayList<>();

        ArrayList<String> eyeautomate_only = new ArrayList<>();
        ArrayList<String> sikuli_only = new ArrayList<>();
        ArrayList<String> eyeautomate_sikuli = new ArrayList<>();
        ArrayList<String> sikuli_eyeautomate = new ArrayList<>();

        //ADD HEADERS
        for(String header : createHeaders()){
            eyeautomate_only.add(header);
            sikuli_only.add(header);
            eyeautomate_sikuli.add(header);
            sikuli_eyeautomate.add(header);
        }

        //add class spec
        eyeautomate_only.add("\n\n");
        eyeautomate_only.add("public class " + class_name + "EyeAutomate { ");
        eyeautomate_only.add("\n\n");

        sikuli_only.add("\n\n");
        sikuli_only.add("public class " + class_name + "Sikuli { ");
        sikuli_only.add("\n\n");

        eyeautomate_sikuli.add("\n\n");
        eyeautomate_sikuli.add("public class " + class_name + "EyeAutomateSikuli { ");
        eyeautomate_sikuli.add("\n\n");

        sikuli_eyeautomate.add("\n\n");
        sikuli_eyeautomate.add("public class " + class_name+ "SikuliEyeAutomate { ");
        sikuli_eyeautomate.add("\n\n");

        //add the methods
        for(String test_name: testNames){
            method_interactions = 0;
            ToggleTranslator translator = new ToggleTranslator(starting_folder, package_name, class_name, test_name, ratioH, ratioW);

            //translator.readLogcatToFile(logcat_filename);

            List<String> filtered_logcat_interactions = translator.filterLogcat(logName,logcat_tool_tag);
            //List<String> filtered_logcat_interactions = translator.readLogFile(logName,logcat_tool_tag);

            interactions = new ArrayList<>();

            for(String logInteraction : filtered_logcat_interactions){
                ToggleInteraction interaction = translator.readInteractionsFromLogcat(logInteraction);
                interactions.add(interaction);
                method_interactions++;
            }

            //never comment
            if(deviceWidth > 0 && actualWidth > 0) {
                translator.saveCroppedScreenshots(interactions, deviceWidth, actualWidth);
            }else
                translator.saveCroppedScreenshots(interactions);
            translator.createEyeStudioScript(interactions);
            translator.createSikuliScript(interactions);

            eyeautomate_only.addAll(translator.createEyeAutomateJavaMethod(interactions));
            sikuli_only.addAll(translator.createSikuliJavaMethod(interactions));
            eyeautomate_sikuli.addAll(translator.createCombinedJavaMethod(interactions));
            sikuli_eyeautomate.addAll(translator.createCombinedJavaMethodSikuliFirst(interactions));

            eyeautomate_only.add("\n\n\n");
            sikuli_only.add("\n\n\n");
            eyeautomate_sikuli.add("\n\n\n");
            sikuli_eyeautomate.add("\n\n\n");
        }
        //add the main function
        eyeautomate_only.add("\n\n\n");
        sikuli_only.add("\n\n\n");
        eyeautomate_sikuli.add("\n\n\n");
        sikuli_eyeautomate.add("\n\n\n");

        //ArrayList<String> eyeAutomateOrSiculiMain = this.createEyeAutomateOrSikuliJavaMain();
        ArrayList<String> eyeAutomateOrSiculiMain = this.createEyeAutomateOrSikuliRun();
        eyeautomate_only.addAll(eyeAutomateOrSiculiMain);
        sikuli_only.addAll(eyeAutomateOrSiculiMain);

        //eyeautomate_sikuli.addAll(this.createCombinedMainEyeAutomateFirst());
        //sikuli_eyeautomate.addAll(this.createCombinedMainSikuliFirst());
        eyeautomate_sikuli.addAll(this.createCombinedRunEyeAutomateFirst());
        sikuli_eyeautomate.addAll(this.createCombinedRunSikuliFirst());

        //add closure of function
        eyeautomate_only.add("\n\n");
        eyeautomate_only.add("}");

        sikuli_only.add("\n\n");
        sikuli_only.add("}");

        eyeautomate_sikuli.add("\n\n");
        eyeautomate_sikuli.add("}");

        sikuli_eyeautomate.add("\n\n");
        sikuli_eyeautomate.add("}");


        writeOnFile(starting_folder + "\\JavaTranslatedProject\\src\\" + class_name + "EyeAutomate.java",eyeautomate_only);
        writeOnFile(starting_folder + "\\JavaTranslatedProject\\src\\" + class_name + "Sikuli.java",sikuli_only);
        writeOnFile(starting_folder + "\\JavaTranslatedProject\\src\\" + class_name + "EyeAutomateSikuli.java",eyeautomate_sikuli);
        writeOnFile(starting_folder + "\\JavaTranslatedProject\\src\\" + class_name + "SikuliEyeAutomate.java",sikuli_eyeautomate);

        return test_class_code;
    }

    public ArrayList<String> createClassMethodGranularity(String logName) throws IOException, XPathExpressionException, SAXException, ParserConfigurationException, ToggleException {
        //FUNZIONAMENTO: si aggiunge al logger come primo parametro dopo TOGGLETOOL il nome del test;
        //createClass lancia per ogni nome di test ricevuto un toggle translator.
        //gli script sikuli ed eyeautomate vengono salvati direttamente all'interno della cartella con le giuste immagini
        //gli script java vengono salvati all'interno di una cartella dove:
        //1) si salvano le immagini (tutte)
        //2) si crea una classe Main.java, la classe main contiene un metodo per ogni test + nel main lancia tutti i test e fa girare statistiche su quanti hanno ritornato true e quanti null

        int method_interactions = 0;

        float ratioH = this.actualHeight / (float) this.deviceHeight;
        float ratioW = this.actualWidth / (float) this.deviceWidth;

        ArrayList<String> test_class_code = new ArrayList<>();

        ArrayList<String> eyeautomate_only = new ArrayList<>();
        ArrayList<String> sikuli_only = new ArrayList<>();
        ArrayList<String> eyeautomate_sikuli = new ArrayList<>();
        ArrayList<String> sikuli_eyeautomate = new ArrayList<>();

        //ADD HEADERS
        for(String header : createHeaders()){
            eyeautomate_only.add(header);
            sikuli_only.add(header);
            eyeautomate_sikuli.add(header);
            sikuli_eyeautomate.add(header);
        }

        //add class spec
        eyeautomate_only.add("\n\n");
        eyeautomate_only.add("public class " + class_name + "EyeAutomate { ");
        eyeautomate_only.add("\n\n");

        sikuli_only.add("\n\n");
        sikuli_only.add("public class " + class_name + "Sikuli { ");
        sikuli_only.add("\n\n");

        eyeautomate_sikuli.add("\n\n");
        eyeautomate_sikuli.add("public class " + class_name + "EyeAutomateSikuli { ");
        eyeautomate_sikuli.add("\n\n");

        sikuli_eyeautomate.add("\n\n");
        sikuli_eyeautomate.add("public class " + class_name+ "SikuliEyeAutomate { ");
        sikuli_eyeautomate.add("\n\n");

        //add the methods
        for(String test_name: testNames){
            method_interactions = 0;
            ToggleTranslator translator = new ToggleTranslator(starting_folder, package_name, class_name, test_name, ratioH, ratioW);

            //translator.readLogcatToFile(logcat_filename);

            List<String> filtered_logcat_interactions = translator.filterLogcat(logName,logcat_tool_tag);

            interactions = new ArrayList<>();

            for(String logInteraction : filtered_logcat_interactions){
                ToggleInteraction interaction = translator.readInteractionsFromLogcatMethodGranularity(logInteraction);
                interactions.add(interaction);
                method_interactions++;
            }

            //never comment
            if(deviceWidth > 0 && actualWidth > 0) {
                translator.saveCroppedScreenshots(interactions, deviceWidth, actualWidth);
            }else
                translator.saveCroppedScreenshots(interactions);
            translator.createEyeStudioScript(interactions);
            translator.createSikuliScript(interactions);

            eyeautomate_only.addAll(translator.createEyeAutomateJavaMethod(interactions));
            sikuli_only.addAll(translator.createSikuliJavaMethod(interactions));
            eyeautomate_sikuli.addAll(translator.createCombinedJavaMethod(interactions));
            sikuli_eyeautomate.addAll(translator.createCombinedJavaMethodSikuliFirst(interactions));

            eyeautomate_only.add("\n\n\n");
            sikuli_only.add("\n\n\n");
            eyeautomate_sikuli.add("\n\n\n");
            sikuli_eyeautomate.add("\n\n\n");
        }
        //add the main function
        eyeautomate_only.add("\n\n\n");
        sikuli_only.add("\n\n\n");
        eyeautomate_sikuli.add("\n\n\n");
        sikuli_eyeautomate.add("\n\n\n");

        ArrayList<String> eyeAutomateOrSiculiMain = this.createEyeAutomateOrSikuliJavaMain();
        eyeautomate_only.addAll(eyeAutomateOrSiculiMain);
        sikuli_only.addAll(eyeAutomateOrSiculiMain);

        eyeautomate_sikuli.addAll(this.createCombinedMainEyeAutomateFirst());
        sikuli_eyeautomate.addAll(this.createCombinedMainSikuliFirst());

        //add closure of function
        eyeautomate_only.add("\n\n");
        eyeautomate_only.add("}");

        sikuli_only.add("\n\n");
        sikuli_only.add("}");

        eyeautomate_sikuli.add("\n\n");
        eyeautomate_sikuli.add("}");

        sikuli_eyeautomate.add("\n\n");
        sikuli_eyeautomate.add("}");


        writeOnFile(starting_folder + "\\JavaTranslatedProject\\src\\" + class_name + "EyeAutomate.java",eyeautomate_only);
        writeOnFile(starting_folder + "\\JavaTranslatedProject\\src\\" + class_name + "Sikuli.java",sikuli_only);
        writeOnFile(starting_folder + "\\JavaTranslatedProject\\src\\" + class_name + "EyeAutomateSikuli.java",eyeautomate_sikuli);
        writeOnFile(starting_folder + "\\JavaTranslatedProject\\src\\" + class_name + "SikuliEyeAutomate.java",sikuli_eyeautomate);

        return test_class_code;
    }

    private void writeOnFile(String filename, ArrayList<String> toBeWritten) throws IOException {
        File fout = new File(filename);
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        for(String instruction : toBeWritten){
            bw.write(instruction);
            bw.newLine();
        }
        bw.close();
    }
}
