package it.polito.toggle;

import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;
import it.enhancer.Enhancer;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Toggle {
    private String logFilename = "toggleLog.txt";
    private String guiTestsPath;
    private String appPackageName;
    private Enhancer enhancer;
    private long startEspressoExecution;
    private long endEspressoExecution;
    private static String adbPath = System.getenv("LOCALAPPDATA")+"\\Android\\Sdk\\platform-tools";

    public Toggle(String testDirectory, String guiTestsPath, String appPackageName){
        this.guiTestsPath = guiTestsPath;
        this.enhancer = new Enhancer(testDirectory);
        this.startEspressoExecution = 0;
        this.endEspressoExecution = 0;
        this.appPackageName = appPackageName;
    }

    public Toggle(String testDirectory, String logFilename, String guiTestsPath, String appPackageName){
        this.logFilename = logFilename;
        this.guiTestsPath = guiTestsPath;
        this.enhancer = new Enhancer(testDirectory);
        this.startEspressoExecution = 0;
        this.endEspressoExecution = 0;
        this.appPackageName = appPackageName;
    }

    public List<String> enhanceEspressoClass(String path) {
        return enhancer.generateEnhancedClassFrom(path);
    }

    public Map<String,List<String>> enhanceEspressoTestFolder(String testFolder){
        File folder = new File(testFolder);
        FileFilter filter = new WildcardFileFilter("*.java", IOCase.INSENSITIVE);
        File[] tests = folder.listFiles(filter);
        Map<String,List<String>> result = new HashMap<>();

        if(tests == null)
            return result;
        for(File test : tests){
            String name = test.getName();
            int dotIndex = name.lastIndexOf('.');
            List<String> methods = enhancer.generateEnhancedClassFrom(name.substring(0,dotIndex),testFolder);
            result.put(name.substring(0,dotIndex)+"Enhanced",methods);
            //filenames.add(name.substring(0,dotIndex)+"Enhanced");
        }
        //return filenames;
        return result;
    }

    public void executeAllEnhancedEspresso(List<String> testNames, String instrumentation) throws IOException {
        grantPermissions();
        ProcessBuilder builder;
        Process p;
        BufferedReader r;
        String line;
        startEspressoExecution = System.currentTimeMillis();
        for(String testName : testNames){
            builder = new ProcessBuilder(
                    "cmd.exe", "/c\"", adbPath + "\\adb\" shell am instrument -w -e class "+appPackageName+"."+testName+" "+instrumentation);
            builder.redirectErrorStream(true);
            p = builder.start();
            r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = r.readLine()) != null) {
                System.out.println(line);
            }
        }
        endEspressoExecution = System.currentTimeMillis();
        pullLogFile();
    }

    public void executeEnhancedEspresso( String testName, String instrumentation) throws IOException {
        grantPermissions();
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
}
