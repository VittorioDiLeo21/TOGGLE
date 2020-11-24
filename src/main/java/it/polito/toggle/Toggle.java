package it.polito.toggle;

import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;
import it.enhancer.Enhancer;
import it.polito.toggle.exceptions.ToggleException;
import it.polito.toggle.utils.Emulators;
import it.polito.toggle.utils.EspressoTestFinder;
import it.polito.toggle.utils.ToggleToolFinder;
import it.windowUtils.ResizeException;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.xml.sax.SAXException;

import javax.naming.NameNotFoundException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Toggle {
    Emulators device;

    private String logFilename = "toggleLog.txt";
    private String guiTestsPath;
    private String appPackageName;
    private Enhancer enhancer;
    private long startEspressoExecution;
    private long endEspressoExecution;
    private String testDirectoryPath;
    private String appProjectPath;
    private String toggleInjectionPath;

    private it.windowUtils.WindowUtils windowUtils;
    private static String adbPath = System.getenv("LOCALAPPDATA")+"\\Android\\Sdk\\platform-tools";

    public Toggle(String testDirectoryName,
                  String guiTestsPath,
                  String appPackageName,
                  String testDirectoryPath,
                  Emulators device){
        this.guiTestsPath = guiTestsPath;
        this.enhancer = new Enhancer(testDirectoryName);
        this.startEspressoExecution = 0;
        this.endEspressoExecution = 0;
        this.appPackageName = appPackageName;
        this.testDirectoryPath = testDirectoryPath;
        int indexProjectPath = testDirectoryPath.indexOf("\\app\\");
        int indexJava = testDirectoryPath.indexOf("\\java\\");
        this.toggleInjectionPath = testDirectoryPath.substring(indexJava+6,testDirectoryPath.length()-1).replace("\\",".");

        this.appProjectPath = testDirectoryPath.substring(0,indexProjectPath);
        this.windowUtils = new it.windowUtils.WindowUtils();
        this.device = device;
    }

    public Toggle(String testDirectoryName, //androidTest
                  String logFilename, //nonServe?
                  String guiTestsPath, //dove li vogliamo mettere
                  String appPackageName, //org.ligi.passandroid
                  String testDirectoryPath, //C:\Users\vitto\AndroidStudioProjects\PassAndroid\app\src\androidTest\java\org\ligi\passandroid\
                  Emulators device){
        this.logFilename = logFilename;
        this.guiTestsPath = guiTestsPath;
        int index = testDirectoryPath.indexOf("\\app\\");
        this.appProjectPath = testDirectoryPath.substring(0,index);
        this.enhancer = new Enhancer(testDirectoryName);
        this.startEspressoExecution = 0;
        this.endEspressoExecution = 0;
        this.appPackageName = appPackageName;
        this.windowUtils = new it.windowUtils.WindowUtils();
        this.testDirectoryPath = testDirectoryPath;
        this.device = device;
    }

    public void setEmulatedDevice(Emulators device){
        this.device = device;
    }

    public void injectToggleTool(String path){
        File folder = new File(path);
        if(!ToggleToolFinder.findToggleTools(folder)){
            ToggleToolFinder.copyToggleTools(path,toggleInjectionPath);
            ToggleToolFinder.copyBitmapSaver(path,toggleInjectionPath);
        }
    }

    public boolean executeFullProcess() throws IOException {
        //1

        Map<String,ClassData> tests = enhanceEspressoTestFolder(testDirectoryPath); //todo : fa l'enhance anche di altre classi di test nella directory
        //injectToggleTool(testDirectoryPath);
        //2 build and install the apk

        /*try {
            //buildProject(appProjectPath);
            installApp();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }*/
        //3 get the test Instrumentation

        /*String instrumentation = getInstrumentation();
        if(instrumentation.isEmpty())
            return false;*/
        //4
        //getDeviceDensity<-- da toggleGUI.EspressoGUI
        //4.5
        //eventually resize the emulator
        //5

        //executeAllEnhancedEspresso(new ArrayList<>(tests.keySet()),instrumentation);
        //6
        for(String testClassName : tests.keySet()){
            ToggleClassManager tcm = new ToggleClassManager(testClassName,appPackageName,guiTestsPath, new ArrayList<>(tests.get(testClassName).getTests()),getEmulatorResolution(),windowUtils.getEmulatorScreenPixelsWidth(this.device));
            //7
            try {
                tcm.createClass(testClassName+".txt");
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (ToggleException e) {
                e.printStackTrace();
            }
        }
        //8
        /*try {
            //todo
            windowUtils.resizeWindow(339,Emulators.NEXUS_5);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (ResizeException e) {
            e.printStackTrace();
        }*/
        return true;
    }


    public List<String> enhanceEspressoClass(String path) {
        return enhancer.generateEnhancedClassFrom(path);
    }

    public Map<String,ClassData> enhanceEspressoTestFolder(String testFolder){
        File folder = new File(testFolder);
        //todo test this
        List<String> files = EspressoTestFinder.getEspressoTests(folder);
        Map<String,ClassData> result = new HashMap<>();
        for(String test: files){
            List<String> methods = enhancer.generateEnhancedClassFrom(test,testFolder);
            String className = test+"Enhanced";
            ClassData cd = new ClassData(methods,className+".txt");
            result.put(className,cd);
        }

        /*FileFilter filter = new WildcardFileFilter("*.java", IOCase.INSENSITIVE);
        File[] tests = folder.listFiles(filter);
        if(tests == null)
            return result;

        for(File test : tests){
            String name = test.getName();
            int dotIndex = name.lastIndexOf('.');
            List<String> methods = enhancer.generateEnhancedClassFrom(name.substring(0,dotIndex),testFolder);
            String className = name.substring(0,dotIndex)+"Enhanced";
            ClassData cd = new ClassData(methods,className+".txt");
            result.put(className,cd);
        }*/
        return result;
    }

    public void executeAllEnhancedEspresso(List<String> testClasses, String instrumentation) throws IOException {
        //installApp();
        grantPermissions();
        resetLogFiles();
        ProcessBuilder builder;
        Process p;
        BufferedReader r;
        String line;
        startEspressoExecution = System.currentTimeMillis();
        for(String testName : testClasses){
            builder = new ProcessBuilder(
                    "cmd.exe", "/c\"", adbPath + "\\adb\" shell am instrument -w -e class "+/*appPackageName*/toggleInjectionPath+"."+testName+" "+instrumentation);
            builder.redirectErrorStream(true);
            p = builder.start();
            r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = r.readLine()) != null) {
                System.out.println(line);
            }
        }
        endEspressoExecution = System.currentTimeMillis();
        testClasses.forEach(testName -> {
            try {
                pullLogFile(testName+".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Method that builds the project with Gradle
     * @throws IOException
     */
    public static boolean buildProject(String androidProjectPath) throws IOException {

        System.out.println("BUILDING PROJECT");
		/*ProcessBuilder builder = new ProcessBuilder(
				"cmd.exe", "/c\"", projectPath + "\\gradlew\" assembleDebug");
		 */
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c\"", "gradlew\" build");
        builder.directory(new File(androidProjectPath));
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
            if(line.contains("OK")) {
                System.out.println(line);
                return true;
            }
            else if(line.contains("FAILED")) {
                System.out.println(line);
                return false;
            }
        }
        return false;
    }

    /**
     * Method that installs the APK in the selected device
     * @param apkPath path of the APK to be installed
     * @throws IOException
     */
    public static void installApk(String pack,String apkPath) throws IOException {
        ProcessBuilder builder;
        if(pack==null) {
            String cmd="adb\" install -r \""+apkPath+"\"";
            builder = new ProcessBuilder(
                    "cmd.exe", "/c\"", cmd);
        }
        else {
            builder = new ProcessBuilder(
                    "cmd.exe", "/c\"", "adb\" push \""+apkPath+"\" /data/local/tmp/\""+pack+"\"");
        }
        builder.redirectErrorStream(true);
        builder.directory(new File(adbPath));
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = r.readLine()) != null) {
            System.out.println("Install APK: "+line);
        }
    }


    private void installApp() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c\"", "gradlew\" installDebugAndroidTest");

        builder.redirectErrorStream(true);
        builder.directory(new File(this.appProjectPath));
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = r.readLine()) != null) {
            //if(line.contains("install")&&!line.contains("uninstall")&&!line.contains("tasks"))
            System.out.println(line);
        }
    }

    public void executeEnhancedEspresso( String testName, String instrumentation) throws IOException {
        installApp();
        grantPermissions();
        resetLogFile();
        startEspressoExecution = System.currentTimeMillis();
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c\"", adbPath + "\\adb\" shell am instrument -w -e class "+appPackageName+"."+testName+" "+instrumentation);

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

        //todo delete this --> debug
        String line;
        while ((line = r.readLine()) != null) {
            System.out.println(line);
        }
        endEspressoExecution = System.currentTimeMillis();
        pullLogFile();
    }

    public void pullLogFile() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe","/c\"",adbPath + "\\adb\" pull /sdcard/" + logFilename + " " + guiTestsPath + "\\" + logFilename
        );
        builder.redirectErrorStream(true);
        Process p = builder.start();

        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        //todo --> debug
        String line;
        System.out.println("pullLogFile.............");
        int i = 0;
        while((line = r.readLine()) != null){
            System.out.println( i++ + "->" + line);
        }
    }

    public void pullLogFile(String logFile ) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe","/c\"",adbPath + "\\adb\" pull /sdcard/" + logFile + " " + guiTestsPath + "\\" + logFile
        );
        builder.redirectErrorStream(true);
        Process p = builder.start();

        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        //todo --> debug
        String line;
        System.out.println("pullLogFile.............");
        int i = 0;
        while((line = r.readLine()) != null){
            System.out.println( i++ + "->" + line);
        }
    }

    public long getTimeToExecute(){
        if(startEspressoExecution>0 && endEspressoExecution>0)
            return endEspressoExecution-startEspressoExecution;
        return -1;
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public int getEmulatorWidth(){
        Rectangle rect = null;
        for (DesktopWindow desktopWindow : WindowUtils.getAllWindows(true)) {
            if (desktopWindow.getTitle().contains("Android Emulator")) {
                rect = desktopWindow.getLocAndSize();
                String device = desktopWindow.getTitle().split(" - ")[1];
                System.out.println(device + " : " + rect.width + " x " + rect.height);
                return rect.width;
            }
        }
        return -1;
    }

    public int getEmulatorResolution() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c\"", adbPath + "\\adb\" shell wm size");

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        int ret = -1;
        String line;
        while ((line = r.readLine()) != null) {
            System.out.println(line);
            String[] strings = line.split(": ");
            if(strings.length>=2){
                String[] resolution = strings[1].split("x");
                if(resolution.length==2 && isNumeric(resolution[0]) && isNumeric(resolution[1])){
                    ret = Integer.parseInt(resolution[0]);
                    break;
                }
            }
        }
        return ret;
    }

    public void resetLogFiles() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c\"", adbPath + "\\adb\" shell rm -f /sdcard/*Enhanced.txt");

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line;
        while ((line = r.readLine()) != null) {
            System.out.println(line);
        }
    }

    //funge!
    public void resetLogFile() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c\"", adbPath + "\\adb\" shell rm -f /sdcard/"+logFilename);

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line;
        while ((line = r.readLine()) != null) {
            System.out.println(line);
        }
    }

    public void grantPermissions() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c\"", adbPath + "\\adb\" shell pm grant " + appPackageName + " android.permission.WRITE_EXTERNAL_STORAGE");

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line;
        while ((line = r.readLine()) != null) {
            System.out.println(line);
        }
        builder = new ProcessBuilder(
                "cmd.exe", "/c\"", adbPath + "\\adb\" shell pm grant " + appPackageName + " android.permission.READ_EXTERNAL_STORAGE");

        builder.redirectErrorStream(true);
        p = builder.start();
        r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = r.readLine()) != null) {
            System.out.println(line);
        }
    }
    //todo
    public ArrayList<String> getGradlewTasks() throws IOException {
        ArrayList<String> apks=new ArrayList<>();
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c\"", "gradlew\" tasks");

        builder.redirectErrorStream(true);
        builder.directory(new File(this.appProjectPath));
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = r.readLine()) != null) {
            if(line.contains("install")&&!line.contains("uninstall")&&!line.contains("tasks"))
                apks.add(line.split("-")[0].trim());
            System.out.println(line);
        }

        return apks;
    }
    //todo
    public static void gradlewInstall(String task) throws IOException {

        String projectPath = "";//todo ??
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c\"", "gradlew\" "+task);

        builder.redirectErrorStream(true);
        builder.directory(new File(projectPath));
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }

    }


    /**
     * Get all the instrumentations
     * @return the instrumentation needed to run the espresso tests
     * @throws IOException
     */
    public /*ArrayList<String>*/ String getInstrumentation() throws IOException{


        String instrumentationRegex="instrumentation:";
        String targetRegex="[(]target=";
        String targetPackage;

        ArrayList<String> instrumentations=new ArrayList<>();
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c\"", adbPath + "\\adb\" shell pm list instrumentation");

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;

        while ((line = r.readLine()) != null) {
            if(line.contains(appPackageName)) {
                line=line.replaceAll("\\s+","");
                //System.out.println(line);
                //String[] substrings=line.split(targetRegex); <-- utile per ricavare il nome dell'app in automatico'

                //targetPackage=substrings[1].split("[)]")[0];
                //System.out.println(targetPackage);
                //targetPackages.add(targetPackage); <-- era scommentato
                //installationPackage=substrings[0].split(instrumentationRegex)[1].split("/")[0];

                System.out.println(line.split(instrumentationRegex)[1]);
                if(line.contains("AndroidJUnitRunner")) {
                    return line.split(instrumentationRegex)[1].split(targetRegex)[0];
                }
                //instrumentations.add(line.split(instrumentationRegex)[1]);

            }

        }

        //return instrumentations;
        return "";
    }

    /** todo
     * Checks if the selected has already been enhanced
     * @param test test to be checked
     * @return true if the the test has been enhanced
     */
    public boolean isEnhanced(File test) {
        try {
            @SuppressWarnings("resource")
            Scanner scanner=new Scanner(test);
            while(scanner.hasNextLine()) {
                String line=scanner.nextLine();
                if(line.matches("(.*)import " + this.appPackageName+".TOGGLETools(.*)")&&!line.matches("(.*)//(.*)")) {
                    return true;
                }

            }
            scanner.close();
        } catch (FileNotFoundException fnfe) {
            // TODO Auto-generated catch block
            fnfe.printStackTrace();
            return false;
        }
        return false;
    }

    /** todo
     * Method that gets the package of the selected test
     * @param test from which the package is taken
     */
    public static void getTestPackage(File test) {
        String packageStr="package";
        String javaStr=".java";
        String testPackage;
        String testName;
        try {
            Scanner scanner=new Scanner(test);
            while(scanner.hasNextLine()) {
                String line=scanner.nextLine();
                if(contains(line, "package")) {

                    testPackage=line.substring(packageStr.length(), line.length()-1).trim();
                    String name=test.getName();
                    testName=name.split(javaStr)[0];
                    break;
                }

            }
            scanner.close();
        } catch (FileNotFoundException fnfe) {
            // TODO Auto-generated catch block
            fnfe.printStackTrace();
        }
    }

    /** todo
     * Checks if a source string contains an exact word
     * @param source string in which the word is searched
     * @param subItem word to be searched
     * @return
     */
    private static boolean contains(String source, String subItem){
        String pattern = "\\b"+subItem+"\\b";
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(source);
        return m.find();
    }
    //todo non è che funzioni benissimo
    private static boolean isAndroidFolder(File file) {

        int fileCount=0;

        File[] list = file.listFiles();
        if(list!=null)
            for (File fil : list) {
                if(!fil.isDirectory()) {
                    String fileName=fil.getName();
                    //System.out.println(fileName);
                    if(fileName.compareToIgnoreCase("build.gradle")==0
                            || fileName.compareToIgnoreCase("gradle.properties")==0
                            || fileName.compareToIgnoreCase("gradlew")==0
                            || fileName.compareToIgnoreCase("gradlew.bat")==0
                            || fileName.compareToIgnoreCase("settings.gradle")==0)

                        fileCount++;
                }
            }

        //System.out.println(fileCount);
        if(fileCount==5) {
            System.out.println("Is an android folder");
            return true;
        }
        return false;

    }
    //todo
    private static boolean hasAndroidManifest(File file) {

        if(file.getName().compareToIgnoreCase("main")!=0) {

            File[] list = file.listFiles();
            if(list!=null)
                for (File fil : list) {
                    //System.out.println(fil.getName());
                    if(fil.isDirectory())
                        return hasAndroidManifest(fil);
                }
        } else {
            File[] list = file.listFiles();
            if(list!=null) {
                for (File fil : list) {
                    //System.out.println(file.getName());
                    if (!fil.isDirectory()) {
                        try {
                            Scanner scanner = new Scanner(fil);
                            while (scanner.hasNextLine()) {
                                String line = scanner.nextLine();
                                if (line.matches("(.*)manifest(.*)") && !line.matches("(.*)<!--(.*)")) {
                                    //System.out.println(line);
                                    return true;
                                }

                            }
                            scanner.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return false;
    }
}
