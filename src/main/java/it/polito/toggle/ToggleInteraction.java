package it.polito.toggle;

import it.polito.toggle.exceptions.ToggleException;
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

    public ToggleInteraction(String packagename, String search_type, String search_keyword, String timestamp, String interaction_type, String args, File screen_capture, File dump) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException, ToggleException {
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
    public void extractBounds() throws XPathExpressionException, SAXException, IOException, ParserConfigurationException, ToggleException {

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
        String splitter = "&";
        String logicalCondition = " and ";
        if(search_type.contains("|")) {
            splitter = "\\|";
            logicalCondition = " or ";
        }
        String joiner = "";
        String[] searchTypes = search_type.split(splitter);
        String[] searchKeys = search_keyword.split(splitter);
        StringBuilder sb = new StringBuilder().append("//node[");
        for(int i = 0; i < searchTypes.length; i++){
            if(i == 1){
                joiner = logicalCondition;
            }
            switch (searchTypes[i]) {
                case "class":
                    sb.append(joiner).append("contains(@class,'").append(searchKeys[i]).append("')");
                    break;
                case "id":
                    sb.append(joiner).append("@resource-id=\"").append(packagename).append(":id/").append(searchKeys[i]).append("\"");
                    break;
                case "text":
                    sb.append(joiner).append("@text=\"").append(searchKeys[i]).append("\"");
                    //TODO vedere come si comporta se il testo fosse preso da string resources
                    break;
                case "content-desc":
                    sb.append(joiner).append("@content-desc=\"").append(searchKeys[i]).append("\"");
                    break;
                case "id-adapterView":
                    expr = xPath.compile(new StringBuilder().append("//node[@resource-id=\"").append(packagename).append(":id/").append(searchKeys[i]).append("\"]/node").toString());
                    handleIdAdapterView(expr,document);
                    return;
                case "position-adapterView":
                    String id = searchKeys[i].split("_;")[0];
                    String nodeCoords = searchKeys[i].split("_;")[1];
                    sb.append(joiner).append("@resource-id=\"").append(packagename).append(":id/").append(id).append("\"]/node[contains(@bounds,'").append(nodeCoords).append("')");
                    //expr = xPath.compile(new StringBuilder().append("//node[@resource-id=\"").append(packagename).append(":id/").append(id).append("\"]/node[contains(@bounds,'").append(nodeCoords).append("')]").toString());
                    break;
                case "atposition":
                    String[] parameters = searchKeys[i].split("_;");
                    Integer pos = Integer.parseInt(parameters[1]);
                    Integer offset = Integer.parseInt(parameters[2]);
                    pos = pos-offset;
                    sb.append(joiner).append("@resource-id=\"").append(packagename).append(":id/").append(parameters[0]).append("\"]/node[@index=\"").append(pos).append("\"");
                    //expr = xPath.compile(new StringBuilder().append("//node[@resource-id=\"").append(packagename).append(":id/").append(parameters[0]).append("\"]/node[@index=\"").append(pos).append("\"]").toString());
                    break;
                case "text_adapterView":
                    String[] p = searchKeys[i].split("_;");
                    sb.append(joiner).append("@resource-id=\"").append(packagename).append(":id/").append(p[0]).append("\"]//node[node//@text=\"").append(p[1]).append("\"");
                    //expr = xPath.compile(new StringBuilder().append("//node[@resource-id=\"").append(packagename).append(":id/").append(search_keyword.split("_")[0]).append("\"]//node[node//@text=\"").append(search_keyword.split("_")[1]).append("\"]").toString());
                    break;
                case "class-scrollTo" :
                    String[] p2 = searchKeys[i].split("_;");
                    sb.append(joiner).append("@class=\"android.widget.").append(p2[0]).append("\" and contains(@bounds,'").append(p2[1]).append("')");
                    //expr = xPath.compile(new StringBuilder().append("//node[@class=\"android.widget.").append(search_keyword.split("_")[0]).append("\" and contains(@bounds,'").append(search_keyword.split("_")[1]).append("')]").toString());
                    break;
            }
        }
        System.out.println(sb.toString() + "]");
        expr = xPath.compile(sb.append("]").toString());

        assert expr != null;

        NodeList nodes = (NodeList) expr.evaluate(document,XPathConstants.NODESET);
        if(nodes != null){
            for(int i = 0; i < nodes.getLength(); i++){
                bounds = (nodes.item(i).getAttributes().getNamedItem("bounds").toString());
            }
        }
        /*switch (search_type) {
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
                String node = "1";
                //expr = xPath.compile(new StringBuilder().append("//node[@resource-id=\"").append(packagename).append(":id/").append(search_keyword).append("\"]/node[last()]").toString());
                //expr = xPath.compile(new StringBuilder().append("//node[@resource-id=\"").append(packagename).append(":id/").append(search_keyword).append("\"]/node[").append(node).append("]").toString());
                expr = xPath.compile(new StringBuilder().append("//node[@resource-id=\"").append(packagename).append(":id/").append(search_keyword).append("\"]/node").toString());
                handleIdAdapterView(expr,document);
                return;
            case "position-adapterView":
                String id = search_keyword.split("_")[0];
                String nodeCoords = search_keyword.split("_")[1];
                expr = xPath.compile(new StringBuilder().append("//node[@resource-id=\"").append(packagename).append(":id/").append(id).append("\"]/node[contains(@bounds,'").append(nodeCoords).append("')]").toString());
                break;
            case "atposition":
                String[] parameters = search_keyword.split("_");
                Integer pos = Integer.parseInt(parameters[1]);
                Integer offset = Integer.parseInt(parameters[2]);
                pos = pos-offset;
                expr = xPath.compile(new StringBuilder().append("//node[@resource-id=\"").append(packagename).append(":id/").append(parameters[0]).append("\"]/node[@index=\"").append(pos).append("\"]").toString());
                break;
            case "text_adapterView":
                expr = xPath.compile(new StringBuilder().append("//node[@resource-id=\"").append(packagename).append(":id/").append(search_keyword.split("_")[0]).append("\"]//node[node//@text=\"").append(search_keyword.split("_")[1]).append("\"]").toString());
                break;
            case "class-scrollTo" :
                expr = xPath.compile(new StringBuilder().append("//node[@class=\"android.widget.").append(search_keyword.split("_")[0]).append("\" and contains(@bounds,'").append(search_keyword.split("_")[1]).append("')]").toString());
                break;
        }

        assert expr != null;

        NodeList nodes = (NodeList) expr.evaluate(document,XPathConstants.NODESET);
        if(nodes != null){
            for(int i = 0; i < nodes.getLength(); i++){
                bounds = (nodes.item(i).getAttributes().getNamedItem("bounds").toString());
            }
        }*/
        System.out.println("bounds = " + bounds);
        String[] splitted_string = bounds.split("(\\[)|(\\])|((,))");


        left = Integer.parseInt(splitted_string[1]);
        top = Integer.parseInt(splitted_string[2]);

        right = Integer.parseInt(splitted_string[4]);
        bottom = Integer.parseInt(splitted_string[5]);
    }

    protected void handleIdAdapterView(XPathExpression expr,Document document) throws XPathExpressionException, ToggleException {
        NodeList nodes = (NodeList) expr.evaluate(document,XPathConstants.NODESET);
        if(nodes != null){
            int top,left,right,bottom;
            int maxT = 0, maxL = 0, maxR = 0, maxB = 0;
            int area,maxA=-1;
            for(int i = 0; i < nodes.getLength();i++){
                String bounds = (nodes.item(i).getAttributes().getNamedItem("bounds").toString());
                String[] splitted_string = bounds.split("(\\[)|(\\])|((,))");
                left = Integer.parseInt(splitted_string[1]);
                top = Integer.parseInt(splitted_string[2]);

                right = Integer.parseInt(splitted_string[4]);
                bottom = Integer.parseInt(splitted_string[5]);
                area = (right-left)*(bottom-top);
                if(area > maxA){
                    maxA = area;
                    maxT = top;
                    maxL = left;
                    maxR = right;
                    maxB = bottom;
                }
            }
            this.left = maxL;
            this.right = maxR;
            this.top = maxT;
            this.bottom = maxB;
        } else {
            //ERROR CONDITION, SHOULD THROW AN EXCEPTION
            throw new ToggleException("Invalid xml dump, no node is present in adapter view");
        }
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
