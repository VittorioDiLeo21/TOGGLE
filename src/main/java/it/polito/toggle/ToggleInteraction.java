package it.polito.toggle;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class ToggleInteraction {
    protected String packagename;       // dove metterlo?

    protected String search_type;
    protected String search_keyword;
    protected String timestamp;
    protected String interaction_type;
    protected String args;              //optional
    protected boolean need_screenshot;

    protected File screen_capture;
    protected File dump;

    protected int left;
    protected int top;
    protected int right;
    protected int bottom;

    protected BufferedImage cropped_image;
    protected File cropped_screenshot_file;

    protected int interaction_number;   //used for translations that require variable instances

    public ToggleInteraction(String packagename, String search_type, String search_keyword, String timestamp, String interaction_type, String args, File screen_capture, File dump) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException{
        this.packagename = packagename;
        this.search_type = search_type;
        this.search_keyword = search_keyword;
        this.screen_capture = screen_capture;
        this.timestamp = timestamp;
        this.interaction_type = interaction_type;
        this.args = args;
        this.dump = dump;
        this.need_screenshot = true;
        this.extractBounds();

        interaction_number = 1;
    }

    public boolean needScreenshot(){ return need_screenshot; }

    public String getSearchType() { return search_type; }

    public String getSearchKeyword() { return search_keyword; }

    public File getScreenCapture() { return screen_capture; }

    public String getTimestamp() { return timestamp; }

    public File getDump() {
        return dump;
    }

    public void setDump(File dump) {
        this.dump = dump;
    }

    public String toString() {
        return this.timestamp + ", " + this.search_type + ", " + this.search_keyword + ", " + this.interaction_type;
    }

    public BufferedImage getCropped_image() {
        return cropped_image;
    }

    public File getCropped_screenshot_file() {
        return cropped_screenshot_file;
    }

    public String getInteractionType () {
        return interaction_type;
    }

    public ArrayList<String> generateEyeAutomateJavaLines(String starting_folder){

        //translates operations to eyeautomate api calls in pure java

        ArrayList<String> instructions = new ArrayList<>();

        System.out.println("interaction type " + interaction_type + " not found");

        return instructions;
    }

    public ArrayList<String> generateSikuliJavaLines(String starting_folder) {

        //TRANSLATES OPERATIONS TO SIKULI API CALLS IN PURE JAVA

        ArrayList<String> res = new ArrayList<>();

        System.out.println("interaction type " + interaction_type + " not found");

        return res;
    }

    public ArrayList<String> generateCombinedJavaLines(String starting_folder) {

        //TRANSLATES OPERATIONS TO COMBINED (EYEAUTOMATE THEN SIKULI) JAVA CODE

        ArrayList<String> res = new ArrayList<>();

        System.out.println("interaction type " + interaction_type + " not found");

        return res;
    }

    public ArrayList<String> generateCombinedJavaLinesSikuliFirst(String starting_folder) {

        ArrayList<String> res = new ArrayList<>();

        System.out.println("interaction type " + interaction_type + " not found");

        return res;
    }

    public ArrayList<String> generateSikuliLines() {

        //TRANSLATES OPERATIONS TO SIKULI

        ArrayList<String> res = new ArrayList<>();

        System.out.println("interaction type " + interaction_type  + " not found");

        return res;
    }

    public ArrayList<String> generateEyeStudioLines() {

        //TRANSLATES OPERATIONS TO SCRIPTS TO BE LOADED IN EYESTUDIO

        ArrayList<String> res = new ArrayList<>();

        System.out.println("interaction type " + interaction_type  + " not found");

        return res;
    }

    //EXTRACT THE BOUNDS FROM THE IMAGE
    public void extractBounds() throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {

        if(interaction_type.equals("fullcheck")
                || interaction_type.equals("pressback")
                || interaction_type.equals("closekeyboard"))
            return;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        String bounds = "";

        Document document = builder.parse(dump);

        Element root = document.getDocumentElement();

        System.out.println(root);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expr = null;

        switch (search_type) {
            case "id":
                expr = xPath.compile(new StringBuilder().append("//node[@resource-id=\"").append(packagename).append(":id/").append(search_keyword).append("\"]").toString());
                //expr = xPath.compile("//node[@resource-id=\"" + packagename + ":id/" + search_keyword + "\"]");
                System.out.println("//node[@resource-id=\"" + packagename + ":id/" + search_keyword + "\"]");
                break;
            case "text":
                expr = xPath.compile(new StringBuilder().append("//node[@text=\"").append(search_keyword).append("\"]").toString());
                //expr = xPath.compile("//node[@text=\"" + search_keyword + "\"]");
                System.out.println("got with text");
                //TODO vedere come si comporta se il testo fosse preso da string resources
                break;
            case "content-desc":
                expr = xPath.compile(new StringBuilder().append("//node[@content-desc=\"").append(search_keyword).append("\"]").toString());
                //expr = xPath.compile("//node[@content-desc=\"" + search_keyword + "\"]");
                break;
            case "id-adapterView":
                String node="";
                if(interaction_type.equals("scrollup")){
                    node = "1";
                } else if( interaction_type.equals("scrolldown")){
                    node = "last()";
                }
                //expr = xPath.compile(new StringBuilder().append("//node[@resource-id=\"").append(packagename).append(":id/").append(search_keyword).append("\"]/node[last()]").toString());
                expr = xPath.compile(new StringBuilder().append("//node[@resource-id=\"").append(packagename).append(":id/").append(search_keyword).append("\"]/node[").append(node).append("]").toString());

                break;
        }

        assert expr != null;

        NodeList nodes = (NodeList) expr.evaluate(document,XPathConstants.NODESET);
        if(nodes != null){
            for(int i = 0; i < nodes.getLength(); i++){
                bounds = (nodes.item(i).getAttributes().getNamedItem("bounds").toString());
            }
        }
        System.out.println("bounds = " + bounds);
        String[] splitted_string = bounds.split("(\\[)|(\\])|((,))");
//todo debug this
        left = Integer.parseInt(splitted_string[1]);
        top = Integer.parseInt(splitted_string[2]);

        right = Integer.parseInt(splitted_string[4]);
        bottom = Integer.parseInt(splitted_string[5]);
    }

    public File manageScreenshot(String starting_folder) throws IOException {
        cropped_image = ImageManipulationTools.cutImage(screen_capture,left,top,right,bottom);
        //cropped_screenshot_file = ImageManipulationTools.saveCroppedScreenshotToFile(starting_folder,timestamp,ImageManipulationTools.resizeScreenshot(cropped_image,1080,363)); //nexus 5x, nexus 5
        //cropped_screenshot_file = ImageManipulationTools.saveCroppedScreenshotToFile(starting_folder, timestamp, ImageManipulationTools.resizeScreenshot(cropped_image,768,376)); // nexus 4
        cropped_screenshot_file = ImageManipulationTools.saveCroppedScreenshotToFile(starting_folder, timestamp, ImageManipulationTools.resizeScreenshot(cropped_image,1440,362)); // pixel xl
        //cropped_screenshot_file = ImageManipulationTools.saveCroppedScreenshotToFile(starting_folder, timestamp, ImageManipulationTools.resizeScreenshot(cropped_image,480,348)); // nexus s
        //cropped_screenshot_file = ImageManipulationTools.saveCroppedScreenshotToFile(starting_folder, timestamp, ImageManipulationTools.resizeScreenshot(cropped_image,480,337)); // nexus one
        //cropped_screenshot_file = ImageManipulationTools.saveCroppedScreenshotToFile(starting_folder, timestamp, ImageManipulationTools.resizeScreenshot(cropped_image,1440,389)); // nexus 6
        //cropped_screenshot_file = ImageManipulationTools.saveCroppedScreenshotToFile(starting_folder, timestamp, ImageManipulationTools.resizeScreenshot(cropped_image,1440,365)); // nexus 6p
        //cropped_screenshot_file = ImageManipulationTools.saveCroppedScreenshotToFile(starting_folder, timestamp, ImageManipulationTools.resizeScreenshot(cropped_image,720,347)); // galaxy nexus
        //cropped_screenshot_file = ImageManipulationTools.saveCroppedScreenshotToFile(starting_folder, timestamp, ImageManipulationTools.resizeScreenshot(cropped_image,1080,352)); // pixel
        return cropped_screenshot_file;
    }

    public File manageScreenshot(String starting_folder,int originalWidth,int actualWidth) throws IOException {
        cropped_image = ImageManipulationTools.cutImage(screen_capture,left,top,right,bottom);
        cropped_screenshot_file = ImageManipulationTools
                .saveCroppedScreenshotToFile(starting_folder,
                        timestamp,
                        ImageManipulationTools.resizeScreenshot(cropped_image,originalWidth,actualWidth));
        return cropped_screenshot_file;
    }

    public File manageScreenshotThumbnailator(String starting_folder,int originalWidth,int actualWidth) throws IOException {
        cropped_screenshot_file = ImageManipulationTools
                .resizeScreenshotThumbnailator(
                        screen_capture,
                        left,top,right,bottom,
                        originalWidth,actualWidth,
                        starting_folder);
        return cropped_screenshot_file;
    }
}
