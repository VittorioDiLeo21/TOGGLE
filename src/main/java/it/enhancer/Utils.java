package it.enhancer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class Utils {
    public static void removeLogFiles() {

        try {
            Files.deleteIfExists(new File("log.txt").toPath());
            Files.deleteIfExists(new File("log_statfiles.txt").toPath());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void logFileException(Exception e, String file) {

        try {
            File logfile = new File("logfilesnotfound.txt");
            FileWriter fr = new FileWriter(logfile, true);
            fr.write("\n\nException in " + file);

            if (e.getMessage() != null)
                fr.write(e.getMessage().toString());
            //for (StackTraceElement ste : e.getStackTrace()) {
            //	fr.write("\n" + ste.toString());
            //}
            fr.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    public static void logException(Exception e, String place) {

        try {
            File logfile = new File("log.txt");
            FileWriter fr = new FileWriter(logfile, true);
            fr.write("\n\nException in " + place);

            if (e.getMessage() != null)
                fr.write(e.getMessage());
            //for (StackTraceElement ste : e.getStackTrace()) {
            //	fr.write("\n" + ste.toString());
            //}
            fr.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
