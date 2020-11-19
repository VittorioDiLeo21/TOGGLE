package it;

import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;
import it.enhancer.Enhancer;
import it.enhancer.Utils;
import it.polito.toggle.ToggleClassManager;
import it.polito.toggle.ToggleInteraction;
import it.polito.toggle.ToggleTranslator;
import it.windowUtils.ResizeException;

import javax.naming.NameNotFoundException;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Main {
    private static String adbPath = System.getenv("LOCALAPPDATA")+"\\Android\\Sdk\\platform-tools";

    private static boolean isNumeric(String strNum) {
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
//458x804
    public static void resizeEmulator() throws NameNotFoundException, ResizeException {
        Rectangle rect = null;
        for (DesktopWindow desktopWindow : WindowUtils.getAllWindows(true)) {
            if (desktopWindow.getTitle().contains("Android Emulator")) {
                rect = desktopWindow.getLocAndSize();
                String device = desktopWindow.getTitle().split(" - ")[1];
                System.out.println(device + " : " + rect.width + " x " + rect.height);
                it.windowUtils.WindowUtils.resizeWindow(desktopWindow.getHWND(),rect.x,rect.y,600,1054,true);
            }
        }
    }

    public static double computeRatio(){
        Toolkit t = Toolkit.getDefaultToolkit();
        double pxPerCm = t.getScreenResolution()/2.54;
        return 0.6707317073170732;
    }

    public static int getEmulatorWidth(){
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

    public static int getEmulatorResolution() throws IOException {
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

    public static void grantWritePermission(String appPackage) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c\"", adbPath + "\\adb\" shell pm grant " + appPackage + " android.permission.WRITE_EXTERNAL_STORAGE");

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = r.readLine()) != null) {
            System.out.println(line);
        }
        builder = new ProcessBuilder(
                "cmd.exe", "/c\"", adbPath + "\\adb\" shell pm grant " + appPackage + " android.permission.READ_EXTERNAL_STORAGE");

        builder.redirectErrorStream(true);
        p = builder.start();
        r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = r.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static void executeEnhancedEspresso(String testPackage, String testName, String instrumentation) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c\"", adbPath + "\\adb\" shell am instrument -w -e class "+testPackage+"."+testName+" "+instrumentation);

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = r.readLine()) != null) {
            System.out.println(line);
        }
    }


    public static void pullLogFile(String start_folder) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe","/c\"",adbPath + "\\adb\" pull /sdcard/mylog.txt " + start_folder + "\\mylog.txt"
        );
        builder.redirectErrorStream(true);
        Process p = builder.start();

        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        System.out.println("pullLogFile.............");
        int i = 0;
        while((line = r.readLine()) != null){
            System.out.println( i++ + "->" + line);
        }
        Files.lines( Paths.get(start_folder+"\\mylog.txt"), StandardCharsets.UTF_8).forEach(System.out::println);
    }

    public static void main(String[] args) throws Exception {
        String logcat_filename = "mylog.txt";
        List<ToggleInteraction> interactions = new ArrayList<>();
        String starting_folder = "C:\\Users\\vitto\\OneDrive\\Desktop\\toggleTests";
        String testPackage = "org.ligi.passandroid";
        //String className = "TestHomeActivity";
        String className = "TestEditPassProperties";
        String enhancedClassName = "TestEditPassPropertiesEnhanced";
        String test_name = "orderActivityBasicTest";
        String logcat_tool_tag = "touchtest";
        String instrumentation="org.ligi.passandroid.test/androidx.test.runner.AndroidJUnitRunner";

        it.windowUtils.WindowUtils wu = new it.windowUtils.WindowUtils();
        /*int emWidth = wu.getWindowWidth("Android Emulator");
        wu.getPixelsFromCm(12.0);
        int cm = wu.getPixelsFromCm(8.0497);*/
        int emulatorWidth = wu.getEmulatorScreenPixelsWidth();
        System.out.println(emulatorWidth);

        //int windowWidth = getEmulatorWidth();
        //double ratio =((double) 389)/windowWidth;
        //double ratio = 0.7908496732026143; // NEXUS 5
        //PIXEL XL ratio 0.9211195928753181

        //int emulatorWidth = (int) (windowWidth*ratio);
        //int emulator = getEmulatorWidth();
        //int emulatorWidth = (int)(emulator*computeRatio());
        //System.out.println("Emulator : " + emulator + " - "+ emulatorWidth);
        /*if((1+1) == 2)
            return;*/
        /*try {
            resizeEmulator();
        }catch (ResizeException re){
            re.printStackTrace();
        }*/
        System.out.println("Original resolution: " +getEmulatorResolution()+" \nActual Width: " + emulatorWidth);
        //System.out.println("*************\n" + getEmulatorResolution() + "\n****************\n" + getEmulatorWidth() + "\n****************\n" + getEmulatorWidth()/getEmulatorResolution());
        Enhancer en = new Enhancer("androidTest");
        Utils.removeLogFiles();
        //en.generateEnhancedClassFrom("C:\\Users\\vitto\\AndroidStudioProjects\\Espresso\\app\\src\\androidTest\\java\\com\\example\\android\\teatime\\orderActivityBasicTest.java");
        //en.generateEnhancedClassFrom("C:\\Users\\vitto\\AndroidStudioProjects\\PassAndroid\\app\\src\\androidTest\\java\\org\\ligi\\passandroid\\TestHomeActivity.java");
        List<String> testNames = en.generateEnhancedClassFrom("C:\\Users\\vitto\\AndroidStudioProjects\\PassAndroid\\app\\src\\androidTest\\java\\org\\ligi\\passandroid\\TestEditPassProperties.java");

        //enhancement of a test class
        //en.generateEnhancedClassFrom("C:\\Users\\Riccardo Coppola\\MiMangaNu-master_oldgraphics\\MiMangaNu-master\\app\\src\\androidTest\\java\\ar\\rulosoft\\mimanganu\\TestAdvancedFeatures.java");
        //en.generateEnhancedClassFrom("C:\\Users\\Riccardo Coppola\\StudioProjects\\Travel-Mate-master\\Android\\app\\src\\androidTest\\java\\io\\github\\project_travel_mate\\TripsTest.java");
        //en.generateEnhancedClassFrom("C:\\Users\\Riccardo Coppola\\StudioProjects\\k-9-master\\app\\k9mail\\src\\androidTest\\java\\com\\fsck\\k9\\debug\\MessageOperationsTest.java");
        //en.generateEnhancedClassFrom("C:\\Omni-Notes-develop_oldgraphics\\Omni-Notes-develop\\omniNotes\\src\\androidTest\\java\\it\\feio\\android\\omninotes\\TestSearchChecklist.java");
        //en.generateEnhancedClassFrom("C:\\Users\\Riccardo Coppola\\StudioProjects\\PassAndroid-2.5.0_oldgraphics\\PassAndroid-2.5.0\\app\\src\\androidTest\\java\\org\\ligi\\passandroid\\TestCreatePassTabs.java");

		//execution of an enhanced test class
		long st = System.currentTimeMillis();
		grantWritePermission(testPackage);
		executeEnhancedEspresso(testPackage, enhancedClassName, instrumentation);
		pullLogFile(starting_folder);
		long et = System.currentTimeMillis();
		System.out.println("time to execute " + (et - st));

        //translation of instructions
        long time_for_script_creation_before = System.currentTimeMillis();
        ArrayList<String> tests = new ArrayList<>();
        //tests.add(test_name);
        /*tests.add("testCreatePassIsShown");
        tests.add("testScanIsShown");
        tests.add("testDemoPassIsShown");
        tests.add("testOpenFileIsShown");
        tests.add("testFabButtonGoesBackWithClick");
        tests.add("testWhatIsIt");
        tests.add("testLeftMenuIsShown");*/
        tests.add("testSetDescriptionWorks");
        tests.add("testCanSetToQR");
        tests.add("testCanSetToPDF417");
        tests.add("testCanSetToAZTEC");
        tests.add("testCanSetMessage");
        tests.add("testCanSetAltMessage");

        //tests.add("testTripInfoFields");
        //tests.add("testAddTwoTrips");
        //tests.add("testChecklist");
        //tests.add("testCompass");
        //tests.add("testRestaurantsSortRating");
        //tests.add("testHangoutContent");
        //tests.add("testSetUnsetStarTest");
        //ToggleClassManager tcm = new ToggleClassManager("TestAdditionalFeatures", "it.feio.android.omninotes.alpha", "C:\\Users\\Riccardo Coppola\\Desktop\\touchtest", tests);
        //ToggleClassManager tcm = new ToggleClassManager("TestInterfaceBasicTry", "ar.rulosoft.mimanganu", "C:\\Users\\Riccardo Coppola\\Desktop\\touchtest", tests);
        //ToggleClassManager tcm = new ToggleClassManager("TripsTest", "io.github.project_travel_mate", "C:\\Users\\Riccardo Coppola\\Desktop\\touchtest", tests);

        ToggleClassManager tcm = new ToggleClassManager(className, testPackage, starting_folder, tests,getEmulatorResolution(),emulatorWidth);

        //ToggleClassManager tcm = new ToggleClassManager("MessageOperationsTest", "com.fsck.k9.debug", "C:\\Users\\Riccardo Coppola\\Desktop\\touchtest", tests);
        //ToggleClassManager tcm = new ToggleClassManager("TestAdvancedFeatures", "ar.rulosoft.mimanganu", "C:\\Users\\Riccardo Coppola\\Desktop\\touchtest", tests);
        //it.polito.toggle.Utils.createJavaProjectFolder(starting_folder);

        ArrayList<String> result_class = tcm.createClass();
        /*for (String s: result_class) {
            System.out.println("******************");
            System.out.println(s);
            System.out.println("******************");
        }*/
        /*for(String test: tests) {
            ToggleTranslator tt = new ToggleTranslator(starting_folder, testPackage, className, test);
            //tt.readLogcatToFile(logcat_filename);
            List<String> filtered_logcat_interactions = tt.filterLogcat(logcat_filename, logcat_tool_tag);

            System.out.println("Main : filtered -> " +filtered_logcat_interactions.size());
            for (String s:filtered_logcat_interactions) {
                System.out.println("--> " + s);
                String[] separated = s.split(": ");
                String line_data = separated[1];
                String[] separated2 = line_data.split(", ");
                ToggleInteraction interaction = tt.readInteractionsFromLogcat(s);
                interactions.add(interaction);
            }

            tt.saveCroppedScreenshots(interactions,getEmulatorResolution(),emulatorWidth);
            tt.createEyeStudioScript(interactions);
            tt.createSikuliScript(interactions);
            tt.createEyeAutomateJavaMethod(interactions);
            tt.createSikuliJavaMethod(interactions);
            tt.createCombinedJavaMethod(interactions);
        }*/


        //tt.readLogcatToFile(logcat_filename);
        //List<String> filtered_logcat_interactions = tt.filterLogcat(logcat_filename, logcat_tool_tag);
        //for (String s:filtered_logcat_interactions) {
            //String[] separated = s.split(": ");
            //String line_data = separated[1];
            //String[] separated2 = line_data.split(", ");
            //ToggleInteraction interaction = tt.readInteractionsFromLogcat(s);
            //interactions.add(interaction);
        //}
        //tt.saveCroppedScreenshots(interactions);
        //tt.createEyeStudioScript(interactions);
        //tt.createSikuliScript(interactions);
        //tt.createEyeAutomateJavaMethod(interactions);
        //tt.createSikuliJavaMethod(interactions);
        //tt.createCombinedJavaMethod(interactions);
        //long time_for_script_creation_after = System.currentTimeMillis();
        //System.out.println("time to translate " + (time_for_script_creation_after - time_for_script_creation_before));
    }
}
