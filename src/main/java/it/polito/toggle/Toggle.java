package it.polito.toggle;

import it.enhancer.Enhancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Toggle {
    private String logFilename = "toggleLog.txt";
    private String guiTestsPath;
    private Enhancer enhancer;
    private long startEspressoExecution;
    private long endEspressoExecution;
    private static String adbPath = System.getenv("LOCALAPPDATA")+"\\Android\\Sdk\\platform-tools";

    public Toggle(String testDirectory, String guiTestsPath){
        this.guiTestsPath = guiTestsPath;
        this.enhancer = new Enhancer(testDirectory);
        this.startEspressoExecution = 0;
        this.endEspressoExecution = 0;
    }

    public Toggle(String testDirectory, String logFilename, String guiTestsPath){
        this.logFilename = logFilename;
        this.guiTestsPath = guiTestsPath;
        this.enhancer = new Enhancer(testDirectory);
        this.startEspressoExecution = 0;
        this.endEspressoExecution = 0;
    }

    public void enhanceEspressoClass(String path) {
        enhancer.generateEnhancedClassFrom(path);
    }

    public void executeEnhancedEspresso(String testPackage, String testName, String instrumentation) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c\"", adbPath + "\\adb\" shell am instrument -w -e class "+testPackage+"."+testName+" "+instrumentation);

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

        //todo delete this --> debug
        String line;
        while ((line = r.readLine()) != null) {
            System.out.println(line);
        }
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
        Files.lines( Paths.get(guiTestsPath + "\\" + logFilename), StandardCharsets.UTF_8).forEach(System.out::println);
    }

    public long getTimeToExecute(){
        if(startEspressoExecution>0 && endEspressoExecution>0)
            return endEspressoExecution-startEspressoExecution;
        return -1;
    }

}
