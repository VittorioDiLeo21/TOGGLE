package it.enhancer;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.*;

public class Statistic {
    public static Map<String, Integer> populateInitialMap() {
        Map<String, Integer> statistic = new TreeMap<String, Integer>();

        statistic.put("actionOnHolderItem", 0);
        statistic.put("actionOnItem", 0);
        statistic.put("actionOnItemAtPosition", 0);
        statistic.put("actionWithAssertions", 0);
        statistic.put("addGlobalAssertion", 0);
        statistic.put("allOf", 0);
        statistic.put("anyIntent", 0);
        statistic.put("anyOf", 0);
        statistic.put("assertThat", 0);
        statistic.put("childAtPosition", 0);
        statistic.put("clearGlobalAssertions", 0);
        statistic.put("clearText", 0);
        statistic.put("click", 0);
        statistic.put("clickBetweenToTitles", 0);
        statistic.put("close", 0);
        statistic.put("closeDrawer", 0);
        statistic.put("closeSoftKeyboard", 0);
        statistic.put("doesNotExist", 0);
        statistic.put("doubleClick", 0);
        statistic.put("endsWith", 0);
        statistic.put("hasAction", 0);
        statistic.put("hasBackground", 0);
        statistic.put("hasCategories", 0);
        statistic.put("hasChildCount", 0);
        statistic.put("hasClassName", 0);
        statistic.put("hasComponent", 0);
        statistic.put("hasContentDescription", 0);
        statistic.put("hasData", 0);
        statistic.put("hasDescendant", 0);
        statistic.put("hasEllipsizedText", 0);
        statistic.put("hasEntry", 0);
        statistic.put("hasErrorText", 0);
        statistic.put("hasExtra", 0);
        statistic.put("hasExtras", 0);
        statistic.put("hasExtraWithKey", 0);
        statistic.put("hasFlag", 0);
        statistic.put("hasFlags", 0);
        statistic.put("hasFocus", 0);
        statistic.put("hasHost", 0);
        statistic.put("hasIMEAction", 0);
        statistic.put("hasKey", 0);
        statistic.put("hasLinks", 0);
        statistic.put("hasMinimumChildCount", 0);
        statistic.put("hasMultilineTest", 0);
        statistic.put("hasMyPackageName", 0);
        statistic.put("hasPackage", 0);
        statistic.put("hasPackageName", 0);
        statistic.put("hasParamWithName", 0);
        statistic.put("hasParamWithValue", 0);
        statistic.put("hasPath", 0);
        statistic.put("hasResultCode", 0);
        statistic.put("hasResultData", 0);
        statistic.put("hasScheme", 0);
        statistic.put("hasSchemeSpecificPart", 0);
        statistic.put("hasShortClassName", 0);
        statistic.put("hasSibling", 0);
        statistic.put("hasTextColor", 0);
        statistic.put("hasToString", 0);
        statistic.put("hasType", 0);
        statistic.put("hasValue", 0);
        statistic.put("hasWindowLayoutParams", 0);
        statistic.put("inAdapterView", 0);
        statistic.put("instanceOf", 0);
        statistic.put("is", 0);
        statistic.put("isAbove", 0);
        statistic.put("isAssignableFrom", 0);
        statistic.put("isBelow", 0);
        statistic.put("isBottomAlignedWith", 0);
        statistic.put("isChecked", 0);
        statistic.put("isClickable", 0);
        statistic.put("isClosed", 0);
        statistic.put("isCompletelyAbove", 0);
        statistic.put("isCompletelyBelow", 0);
        statistic.put("isCompletelyDisplayed", 0);
        statistic.put("isCompletelyLeftOf", 0);
        statistic.put("isCompletelyRightOf", 0);
        statistic.put("isDescendantOfA", 0);
        statistic.put("isDialog", 0);
        statistic.put("isDisplayed", 0);
        statistic.put("isDisplayingAtLeast", 0);
        statistic.put("isEnabled", 0);
        statistic.put("isFocusable", 0);
        statistic.put("isInternal", 0);
        statistic.put("isJavascriptEnabled", 0);
        statistic.put("isLeftAlignedWith", 0);
        statistic.put("isLeftOf", 0);
        statistic.put("isNotChecked", 0);
        statistic.put("isOpen", 0);
        statistic.put("isPartiallyAbove", 0);
        statistic.put("isPartiallyBelow", 0);
        statistic.put("isPartiallyLeftOf", 0);
        statistic.put("isPartiallyRightOf", 0);
        statistic.put("isPlatformPopup", 0);
        statistic.put("isRightAlignedWith", 0);
        statistic.put("isRightOf", 0);
        statistic.put("isRoot", 0);
        statistic.put("isSelected", 0);
        statistic.put("isSystemAlertWindow", 0);
        statistic.put("isTopAlignedWith", 0);
        statistic.put("isTouchable", 0);
        statistic.put("longClick", 0);
        statistic.put("matches", 0);
        statistic.put("navigateTo", 0);
        statistic.put("noEllipseizedText", 0);
        statistic.put("noMultilineButtons", 0);
        statistic.put("noOverlaps", 0);
        statistic.put("not", 0);
        statistic.put("onData", 0);
        statistic.put("onView", 0);
        statistic.put("open", 0);
        statistic.put("openActionBarOverflowOrOptionsMenu", 0);
        statistic.put("openContextualActionModeOverflowMenu", 0);
        statistic.put("openDrawer", 0);
        statistic.put("openLink", 0);
        statistic.put("openLinkWithText", 0);
        statistic.put("openLinkWithUri", 0);
        statistic.put("pressBack", 0);
        statistic.put("pressBackUnconditionally", 0);
        statistic.put("pressImeActionButton", 0);
        statistic.put("pressKey", 0);
        statistic.put("pressMenuKey", 0);
        statistic.put("removeGlobalAssertion", 0);
        statistic.put("repeatedlyUntil", 0);
        statistic.put("replaceText", 0);
        statistic.put("scrollLeft", 0);
        statistic.put("scrollRight", 0);
        statistic.put("scrollTo", 0);
        statistic.put("scrollTo", 0);
        statistic.put("scrollToFirst", 0);
        statistic.put("scrollToHolder", 0);
        statistic.put("scrollToLast", 0);
        statistic.put("scrollToPage", 0);
        statistic.put("scrollToPosition", 0);
        statistic.put("selectedDescendantsMatch", 0);
        statistic.put("setData", 0);
        statistic.put("setTime", 0);
        statistic.put("startsWith", 0);
        statistic.put("supportsInputMethods", 0);
        statistic.put("swipeDown", 0);
        statistic.put("swipeLeft", 0);
        statistic.put("swipeRight", 0);
        statistic.put("swipeUp", 0);
        statistic.put("toPackage", 0);
        statistic.put("typeText", 0);
        statistic.put("typeTextIntoFocusedView", 0);
        statistic.put("withAlpha", 0);
        statistic.put("withChild", 0);
        statistic.put("withClassName", 0);
        statistic.put("withContentDescription", 0);
        statistic.put("withDecorView", 0);
        statistic.put("withEffectiveVisibility", 0);
        statistic.put("withHint", 0);
        statistic.put("withId", 0);
        statistic.put("withInputType", 0);
        statistic.put("withKey", 0);
        statistic.put("withParent", 0);
        statistic.put("withParentIndex", 0);
        statistic.put("withResourceName", 0);
        statistic.put("withRowBlob", 0);
        statistic.put("withRowDouble", 0);
        statistic.put("withRowFloat", 0);
        statistic.put("withRowInt", 0);
        statistic.put("withRowLong", 0);
        statistic.put("withRowShort", 0);
        statistic.put("withRowString", 0);
        statistic.put("withSpinnerText", 0);
        statistic.put("withSubstring", 0);
        statistic.put("withSummary", 0);
        statistic.put("withSummaryText", 0);
        statistic.put("withTagKey", 0);
        statistic.put("withTagValue", 0);
        statistic.put("withText", 0);
        statistic.put("withTitle", 0);
        statistic.put("withTitleText", 0);

        return statistic;
    }

    public static void writeDataToFile(Map<String, Integer> statistic, String statisticFilePath) {
        BufferedWriter bw;
        LinkedHashMap<String, Integer> sorted = (LinkedHashMap<String, Integer>) sortByValue(statistic);

        try {
            bw = new BufferedWriter(new FileWriter(statisticFilePath));
            for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
                bw.write(entry.getKey() + ":" + entry.getValue());
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Map<String, Integer> readDataFromFile(String statisticFilePath) {
        Map<String, Integer> statistic = populateInitialMap();

        try (BufferedReader br = new BufferedReader(
                new FileReader(FilenameUtils.separatorsToSystem(statisticFilePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String key = "";
                Integer value = 0;

                String[] splitted = line.split(":");
                key = splitted[0];
                value = Integer.valueOf(splitted[1]);
                statistic.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statistic;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }


    //*****
    //makes the csv
    //*****

    public static void createCSV(String starting_folder, String list_files_path) throws IOException {
        FileWriter result_csv = new FileWriter("stats_all.csv");

        result_csv.write(createCSVHeader());

        Scanner s = new Scanner(new File(list_files_path));
        ArrayList<String> list_files = new ArrayList<String>();
        while (s.hasNext()) {
            list_files.add(s.next());
        }
        s.close();

        int counter = 0;

        for (String filename : list_files) {

            System.out.println("doing" + (++counter) + " of " + list_files.size());
            result_csv.write("\n" + createCSVLine(starting_folder, filename));

        }

        result_csv.close();
    }

    // ********
    // Function that returns the header line of the csv resulting file with all the
    // statistics, based on the
    // different keywords that have been specified.
    // It uses the populateInitialMap function to obtain the set of keys ordered by
    // name
    // ********

    public static String createCSVHeader() {

        String header = "project_name;class_name";

        TreeMap<String, Integer> dummy_map = (TreeMap<String, Integer>) populateInitialMap();

        for (Map.Entry<String, Integer> entry : dummy_map.entrySet()) {

            header += ";" + entry.getKey();
        }

        return header;

    }

    // ******
    // Function that returns a single line of the CSV file for the analysis of
    // statistics.
    // It takes as input parameters the starting folders where the files are
    // located, and the name of the examined class
    // output is a string of the csv file in the form of
    // project_name;class_path;stats(ordered by name)
    // ******

    public static String createCSVLine(String starting_folder, String file_name) {

        String result = "";

        String[] filenameparts = FilenameUtils.separatorsToUnix(file_name).split("/");
        String project_name = filenameparts[0] + "/" + filenameparts[1];

        result += project_name;

        result += ";" + file_name;

        String statistics_file_path = FilenameUtils.separatorsToSystem(starting_folder) + FilenameUtils.separatorsToSystem(file_name) + "_stats.txt";
        //+ FilenameUtils.removeExtension(FilenameUtils.separatorsToSystem(file_name)) + "_Statistic.txt";

        //System.out.println("project: " + project_name);
        //System.out.println("statistics file path: " + statistics_file_path);

        try {
            TreeMap<String, Integer> statistic = (TreeMap<String, Integer>) readDataFromFile(statistics_file_path);

            for (Map.Entry<String, Integer> mapentry : statistic.entrySet()) {

                result += ";" + mapentry.getValue();

            }

        }

        catch (Exception e ) {

            Utils.logFileException(e, statistics_file_path);
        }


        //System.out.println(result);
        return result;

    }

    // ******
    // function for the creations of statistics only given a set of test classes
    // ******

    public static void statisticsOnListOfFiles(String projectlist, String starting_folder) throws IOException {


        Enhancer en = new Enhancer("dummy.project"); // not required the project name: in this case the Enhanced class
        // is not of interest;
        ArrayList<String> filepaths = new ArrayList<String>();

        File logfile = new File("log.txt");
        FileWriter fr = new FileWriter(logfile, true);

        Scanner s = new Scanner(new File(projectlist));
        ArrayList<String> list = new ArrayList<String>();
        while (s.hasNext()) {
            list.add(s.next());
        }
        s.close();

        int number_of_files = list.size();

        int n = 0;
        for (String filename : list) {

            n++;
            System.out.println("doing " + filename + " (" + n + " of " + number_of_files + ")");
            try {
                en.generateEnhancedClassFrom(starting_folder + FilenameUtils.separatorsToSystem(filename));
            } catch (Exception e) {



                Utils.logFileException(e, FilenameUtils.separatorsToSystem(filename));

            }

        }

    }
}
