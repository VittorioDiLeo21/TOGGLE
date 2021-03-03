package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppStarter {

    private static String adbPath = System.getenv("LOCALAPPDATA")+"\\Android\\Sdk\\platform-tools";
    private static String activityMain = "";
    private static String appPackage = "";

    public static void stop() throws IOException {
        ProcessBuilder builder;
        Process p;
        BufferedReader r;
        builder = new ProcessBuilder(
                "cmd.exe", "/c", "adb shell am force-stop " + appPackage);

        builder.redirectErrorStream(true);
        builder.directory(new File(adbPath));
        p = builder.start();
        r = new BufferedReader(new InputStreamReader(p.getInputStream()));


        String line;
        while ((line = r.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static void start() throws IOException {
        ProcessBuilder builder;
        Process p;
        BufferedReader r;
        builder = new ProcessBuilder(
                "cmd.exe", "/c", "adb shell am start -n " + appPackage + "/" + activityMain);

        builder.redirectErrorStream(true);
        builder.directory(new File(adbPath));
        p = builder.start();
        r = new BufferedReader(new InputStreamReader(p.getInputStream()));


        String line;
        while ((line = r.readLine()) != null) {
            System.out.println(line);
        }
    }
}
