package it.polito.toggle;

import it.polito.toggle.ToggleInteractions.*;
import it.polito.toggle.exceptions.ToggleException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ToggleTranslator {
    private String starting_folder;
    private String package_name;
    private String class_name;
    private String test_name;
    private int cur_height;
    private int cur_width;

    private int screen_size_height = 0;
    private int screen_size_width = 0;

    private float ratioH;

    public ToggleTranslator(String starting_folder, String package_name, String class_name, String test_name) {
        this.starting_folder = starting_folder;
        this.package_name = package_name;
        this.test_name = test_name;
        this.class_name = class_name;
        this.ratioH = 1;
    }

    public ToggleTranslator(String starting_folder, String package_name, String class_name, String test_name,float ratioH) {
        this.starting_folder = starting_folder;
        this.package_name = package_name;
        this.test_name = test_name;
        this.class_name = class_name;
        this.ratioH = ratioH;
    }

    public String getStarting_folder() { return starting_folder; }

    public final void clearLogcat() throws IOException {
        System.out.println("************************************");
        System.out.println("*           CLEAR LOGCAT           *");
        System.out.println("************************************");

        ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c","cd \""+starting_folder+"\\\" && adb logcat -c");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while((line = r.readLine()) != null){
            System.out.println(line);
        }
    }

    public final void readLogcatToFile(String filename) throws IOException {
        //TODO cambiare cartella su cui viene rediretto il log

        System.out.println("***********************************");
        System.out.println("*           READ LOGCAT           *");
        System.out.println("***********************************");

        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "cd \"" + starting_folder + "\\\" && adb logcat -d > " + filename);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while((line = r.readLine()) != null){
            System.out.println(line);
        }
        //todo aggiunto, ha senso?
        clearLogcat();
    }

    public final void pullFile(String filename) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "cd \"" + starting_folder + "\\\" && adb pull /sdcard/" + filename);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while((line = r.readLine()) != null){
            System.out.println(line);
        }
    }

    public final List<String> filterLogcat(String filename, String filter) throws IOException {

        System.out.println("*************************************");
        System.out.println("*           FILTER LOGCAT           *");
        System.out.println("*************************************");

        //Files.lines(Paths.get(starting_folder,filename),StandardCharsets.ISO_8859_1).forEach(System.out::println);

        return Files.lines(Paths.get(starting_folder,filename), StandardCharsets.ISO_8859_1)
                .filter(line -> line.contains(filter))
                .filter(line -> line.contains(test_name)) // alternativa = ricerca per campo esatto field[1].equals(test_name)
                .collect(Collectors.toList());
    }

    public final void readScreenSize(String filename, String filter) throws IOException {
        List<String> screenSizeSettings = Files.lines(Paths.get(starting_folder,filename))
                .filter(line -> line.contains(filter))
                .collect(Collectors.toList());

        if(screenSizeSettings.size() == 1){
            String line = screenSizeSettings.get(0);
            String[] separated = line.split(": ");
            String line_data = separated[1];
            String[] separated2 = line_data.split(", ");
            screen_size_width = Integer.parseInt(separated2[0]);
            screen_size_height = Integer.parseInt(separated2[1]);
        }
    }

    public final ToggleInteraction readInteractionsFromLogcat(String line) throws IOException, XPathExpressionException, SAXException, ParserConfigurationException, ToggleException {

        String[] separated = line.split(": ");

        String line_data = separated[1];

        String[] separated2 = line_data.split("; ");

        System.out.println(line);

        String time = separated2[1];
        String search_type = separated2[2];
        String searched = separated2[3];
        String interaction_type = separated2[4];
        String args;
        if (separated2.length==6) {
            args = separated2[5];
        } else {
            args = "";
        }

        pullFile(time + ".xml");
        pullFile(time + ".bmp"); //3001
        //pullFile(time + ".png"); //3001

        //File imageFile = new File(starting_folder + "\\" + time + ".png");  //3001

        File imageFile = new File(starting_folder + "\\" + time + ".bmp");  //3001
        File xmlFile = new File(starting_folder + "\\" + time + ".xml");

        ToggleInteraction res;

        switch (interaction_type) {
            case "click": return new Click(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "typetext": return new TypeText(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "longclick": return new LongClick(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "doubleclick": return new DoubleClick(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "typeintofocused": return new TypeIntoFocused(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "replacetext": return new ReplaceText(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "cleartext": return new ClearText(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "check": return new Check(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "fullcheck": return new FullCheck(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "presskey": return new PressKey(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "swipeleft": return new SwipeLeft(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "swiperight": return new SwipeRight(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "swipeup": return new SwipeUp(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "swipedown": return new SwipeDown(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "pressback": return new PressBack(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "pressbackunconditionally": return new PressBack(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "closekeyboard": return new PressBack(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "pressmenukey": return new PressMenuKey(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "openactionbaroverfloworoptionsmenu": return new OpenOptionsMenu(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "opencontextualactionmodeoverflowmenu": return new OpenOptionsMenu(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            case "dialogescape": return new DialogEscape(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);

            //*********************************************************************************
            case "scrolldown": return new ScrollDown(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile,ratioH);
            //case "scrollup": return new ScrollUp(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            //case "scrollleft": return new ScrollLeft(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            //case "scrollright": return new ScrollRight(package_name, search_type, searched, time, interaction_type, args, imageFile, xmlFile);
            //*********************************************************************************
            default: throw new ToggleException("Interaction not found: " + interaction_type);
        }
    }

    public final ArrayList<String> createEyeAutomateJavaMethod(List <ToggleInteraction> interactions) {
        ArrayList<String> res = new ArrayList<>();

        res.add("public static String " + test_name + "() throws Exception {");
        res.add("");

        res.add("\tSystem.out.println(\"Starting test + " + package_name + "/" + class_name + "/" + test_name + "(" + interactions.size() + "interactions)\");");
        res.add("\tEye eye = new Eye();");
        res.add("\teye.setTimeout(30);");
        res.add("\teye.setRecognitionMode(Eye.RecognitionMode.TOLERANT);");
        res.add("\tBufferedImage image = null;");
        res.add("\tMatch match = null;");
        res.add("\tRobot bot = new Robot();");
        res.add("\tint interactions = 0;");
        res.add("\n\n\n");

        for (ToggleInteraction i:interactions) {
            for (String s:i.generateEyeAutomateJavaLines(starting_folder)) {
                res.add("\t" + s);
            }
            res.add("\tThread.sleep(1000);");
            res.add("\tinteractions++;");
            res.add("");
        }

        res.add("");
        res.add("\treturn \"pass;\"+interactions;");
        res.add("");
        res.add("}");

        return res;
    }

    public final ArrayList<String> createSikuliJavaMethod(List <ToggleInteraction> interactions) {

        ArrayList<String> res = new ArrayList<>();

        res.add("public static String " + test_name + "() throws Exception {");
        res.add("");

        res.add("\tSystem.out.println(\"Starting test + " + package_name + "/" + class_name + "/" + test_name + "(" + interactions.size() + "interactions)\");");
        res.add("\tScreen sikuli_screen = new Screen();");
        res.add("\tsikuli_screen.setAutoWaitTimeout(30);");

        res.add("\tint interactions = 0;");

        for (ToggleInteraction i:interactions) {
            for (String s:i.generateSikuliJavaLines(starting_folder)) {
                res.add("\t" + s);
            }
            res.add("\tThread.sleep(1000);");
            res.add("\tinteractions++;");
            res.add("");
        }

        res.add("");
        res.add("\treturn \"pass;\"+ interactions;");
        res.add("");
        res.add("}");

        return res;
    }

    public final ArrayList<String> createCombinedJavaMethodSikuliFirst(List <ToggleInteraction> interactions) {

        ArrayList<String> res = new ArrayList<>();

        res.add("public static String " + test_name + "() throws Exception {");
        res.add("");

        res.add("\tSystem.out.println(\"Starting test + " + package_name + "/" + class_name + "/" + test_name + "(" + interactions.size() + "interactions)\");");
        res.add("\tScreen sikuli_screen = new Screen();");
        res.add("\tsikuli_screen.setAutoWaitTimeout(5);");
        res.add("\tEye eye = new Eye();");
        res.add("\teye.setTimeout(25);");
        res.add("\teye.setRecognitionMode(Eye.RecognitionMode.TOLERANT);");
        res.add("\tBufferedImage image = null;");
        res.add("\tMatch match = null;");
        res.add("\tRobot bot = new Robot();");
        res.add("\tint sikuli_failures = 0;");
        res.add("\tint interactions = 0;");

        res.add("\n\n\n");

        for (ToggleInteraction i:interactions) {
            for (String s:i.generateCombinedJavaLinesSikuliFirst(starting_folder)) {
                res.add("\t" + s);
            }
            res.add("\tThread.sleep(1000);");
            res.add("\tinteractions++;");
            res.add("");
        }

        res.add("");
        //res.add("\treturn true;");
        res.add("\treturn \"pass;\" + sikuli_failures + \";\" + interactions;");
        res.add("");
        res.add("}");

        return res;
    }

    public final ArrayList<String> createCombinedJavaMethod(List <ToggleInteraction> interactions) {

        ArrayList<String> res = new ArrayList<>();

        res.add("public static String " + test_name + "() throws Exception {");

        res.add("");

        res.add("\tSystem.out.println(\"Starting test + " + package_name + "/" + class_name + "/" + test_name + "(" + interactions.size() + "interactions)\");");
        res.add("\tScreen sikuli_screen = new Screen();");
        res.add("\tsikuli_screen.setAutoWaitTimeout(25);");
        res.add("\tEye eye = new Eye();");
        res.add("\teye.setTimeout(5);");
        res.add("\teye.setRecognitionMode(Eye.RecognitionMode.TOLERANT);");
        res.add("\tBufferedImage image = null;");
        res.add("\tMatch match = null;");
        res.add("\tRobot bot = new Robot();");
        res.add("\tint eyeautomate_failures = 0;");
        res.add("\tint interactions = 0;");

        res.add("\n\n\n");

        for (ToggleInteraction i:interactions) {
            for (String s:i.generateCombinedJavaLines(starting_folder)) {
                res.add("\t" + s);
            }
            res.add("\tThread.sleep(1000);");
            res.add("\tinteractions++;");
            res.add("");
        }
        res.add("");
        //res.add("\treturn true;");
        res.add("\treturn \"pass;\" + eyeautomate_failures + \";\" + interactions;");
        res.add("");
        res.add("}");

        return res;
    }

    public final void createEyeStudioScript(List<ToggleInteraction> interactions) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
        String current_eyescript_folder = starting_folder + "\\" + package_name + "\\" + class_name + "\\" + test_name + "\\eyeautomate_script\\";
        Utils.deleteDir(current_eyescript_folder);

        File fout = new File(current_eyescript_folder + "eyescript.txt");
        fout.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(fout);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write("Set ImageFolder \"" + current_eyescript_folder +"\"\n");
        bw.write("Timeout 30\n");
        bw.newLine();

        for (ToggleInteraction i:interactions) {
            for (String s:i.generateEyeStudioLines()) {
                bw.write(s);
                bw.newLine();
            }
            bw.write("Sleep 1000\n");

            if (i.needScreenshot()) {
                File copied_screen_for_eyestudio = new File(current_eyescript_folder + i.getTimestamp() + "_cropped.png");

                Utils.copyFile(i.getCropped_screenshot_file(), copied_screen_for_eyestudio);
            }
        }
        bw.close();
    }

    public final void createSikuliScript(List<ToggleInteraction> interactions) throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        String current_sikuli_folder = starting_folder + "\\" + package_name + "\\" + class_name + "\\" + test_name + "\\sikuli_script.sikuli";
        Utils.deleteDir(current_sikuli_folder);

        //tentativo di generazione script sikuli. Per farlo funzionare, bisogna copiare gli screenshot cropped nella cartella sikuli_script
        File fout = new File(current_sikuli_folder + "\\sikuli_script.py");
        fout.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for (ToggleInteraction i:interactions) {
            for (String s:i.generateSikuliLines()) {
                bw.write(s);
                bw.newLine();
            }
            bw.write("sleep(1)\n");

            if (i.needScreenshot()) {
                File copied_screen_for_sikuli = new File(current_sikuli_folder + "\\" + i.getTimestamp() + "_cropped.png");

                Utils.copyFile(i.getCropped_screenshot_file(), copied_screen_for_sikuli);
            }
        }
        bw.close();
    }

    public final void saveCroppedScreenshots(List<ToggleInteraction> interactions) throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        for (ToggleInteraction i:interactions) {
            //aggiungere campo booleano needs_cut_screenshot per gestire tutti questi casi
            if (i.needScreenshot()) {
                File cropped_screenshot_file = i.manageScreenshot(starting_folder);
            }
        }
    }

    public final void saveCroppedScreenshots(List<ToggleInteraction> interactions,int screenResolution, int actualWidth) throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        System.out.println("************************************************\n\n\n");
        System.out.println("saveCroppedScreenshot of " + interactions.size() + " with screenRes " + screenResolution + " and width " + actualWidth);
        System.out.println("\n\n************************************************\n\n\n");

        for (ToggleInteraction i:interactions) {
            //aggiungere campo booleano needs_cut_screenshot per gestire tutti questi casi
            if (i.needScreenshot()) {
                File cropped_screenshot_file = i.manageScreenshot(starting_folder,screenResolution,actualWidth);
            }
        }
    }

    public final void saveCroppedScreenshotsThumbnailator(List<ToggleInteraction> interactions,int screenResolution, int actualWidth) throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        /*System.out.println("************************************************\n\n\n");
        System.out.println("saveCroppedScreenshot of " + interactions.size() + " with screenRes " + screenResolution + " and width " + actualWidth);
        System.out.println("\n\n************************************************\n\n\n");
        */
        for (ToggleInteraction i:interactions) {
            //aggiungere campo booleano needs_cut_screenshot per gestire tutti questi casi
            if (i.needScreenshot()) {
                File cropped_screenshot_file = i.manageScreenshotThumbnailator(starting_folder,screenResolution,actualWidth);
            }
        }
    }
}
