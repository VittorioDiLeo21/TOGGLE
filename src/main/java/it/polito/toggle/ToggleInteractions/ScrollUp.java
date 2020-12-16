package it.polito.toggle.ToggleInteractions;

import it.polito.toggle.ToggleInteraction;
import it.polito.toggle.exceptions.ToggleException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ScrollUp extends ToggleInteraction {


    //TODO : SCREENSHOT DELL'ULTIMO NODO MOSTRATO SPECIFICANDO CHE STO CERCANDO IL NODO N. NEGLI ARGS LOGGARE ANCHE LE COORD TOP E LEFT DEL PRIMO NODO MOSTRATO
    //       SUCCESSIVAMENTE SI TROVA IL CENTRO E SI SCROLLA DAL CENTRO VERSO L'ALTO DI (TOPUltimo-TOPPrimo).
    //       SI CONTINUA FINO A RAGGIUNGERE IL NUMERO DI STEP NECESSARI.
    private int scrollStep;
    private int toBeScrolled;

    public ScrollUp(String packagename, String search_type, String search_keyword, String timestamp,
                      String interaction_type, String args, File screen_capture, File dump, double screenRatio)
            throws XPathExpressionException, SAXException, IOException, ParserConfigurationException, ToggleException {
        super(packagename, search_type, search_keyword, timestamp, interaction_type, args, screen_capture, dump);
        String[] coords = args.split(";");
        int from = Integer.parseInt(coords[0]);
        int to = Integer.parseInt(coords[1]);
        int offset = Integer.parseInt(coords[2]);
        int sizeTop = Integer.parseInt(coords[3]);
        int sizeBottom = Integer.parseInt(coords[4]);
        int deviceSize = sizeBottom - sizeTop;
        offset = (int)(offset*screenRatio);
        this.toBeScrolled = (int)((to-from)*screenRatio*(-1));
        if(-(to-from) > deviceSize)
            this.scrollStep = (int)(deviceSize*screenRatio) - offset;
        else{
            this.scrollStep = this.toBeScrolled - offset;
        }

    }

    @Override
    public ArrayList<String> generateSikuliLines() {
        ArrayList<String> res = new ArrayList<>();
        res.add("sleep(1)");
        res.add("wait(\"" + timestamp + "_cropped.png\", 30)");
        res.add("r = find(\"" + timestamp + "_cropped.png\")");
        res.add("stepY = "+this.scrollStep);
        res.add("start = r.getCenter()");
        res.add("run = start");
        int last = toBeScrolled%this.scrollStep;
        for(int i = 0; i < this.toBeScrolled/this.scrollStep; i++) {
            res.add("mouseMove(start); wait(0.2)");
            res.add("mouseDown(Button.LEFT); wait(0.2)");
            res.add("start = run.below(stepY)");
            res.add("mouseMove(run)");
            res.add("mouseUp()");
            res.add("wait(0.2)");
            res.add("run = run.above(stepY)");
        }
        if(last > 0){
            res.add("mouseMove(start); wait(0.2)");
            res.add("mouseDown(Button.LEFT); wait(0.2)");
            res.add("run = run.below("+last+")");
            res.add("mouseMove(run)");
            res.add("mouseUp()");
            res.add("wait(0.2)");
        }
        return res;
    }

    @Override
    public ArrayList<String> generateEyeStudioLines() {
        ArrayList<String> res = new ArrayList<>();
        res.add("Check \"{ImageFolder}\\" + timestamp + "_cropped.png\"");
        res.add("Move \"{ImageFolder}\\" + timestamp + "_cropped.png\"");
        res.add("Sleep 10");
        int last = toBeScrolled%this.scrollStep;
        for(int i = 0; i < this.toBeScrolled/this.scrollStep;i++){
            res.add("MouseLeftPress");
            res.add("MoveRelative \"0\" \"" + this.scrollStep + "\"");
            res.add("MouseLeftRelease");
            res.add("MoveRelative \"" + this.scrollStep + "\" \"0\"");
        }
        if(last > 0){
            res.add("MouseLeftPress");
            res.add("MoveRelative \"0\" \"" + last + "\"");
            res.add("MouseLeftRelease");
        }
        return res;
    }

    @Override
    public ArrayList<String> generateSikuliJavaLines(String starting_folder) {
        ArrayList<String> res = new ArrayList<>();
        res.add("try {");
        res.add("\tsikuli_screen.wait(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\", 30);");
        res.add("\torg.sikuli.script.Match sikuli_match = sikuli_screen.find(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\",  "\\\\") + "\");");
        res.add("\torg.sikuli.script.Location l = sikuli_match.getCenter();");
        int last = toBeScrolled%this.scrollStep;
        for(int i = 0; i < this.toBeScrolled/this.scrollStep; i++ ){
            res.add("\tsikuli_screen.mouseMove(l);");
            res.add("\tsikuli_screen.wait(0.2);");
            res.add("\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\tsikuli_screen.wait(0.2);");
            res.add("\tl = l.below("+this.scrollStep+");");
            res.add("\tsikuli_screen.mouseMove(l);");
            res.add("\tsikuli_screen.mouseUp();");
            res.add("\tsikuli_screen.wait(0.2);");
            res.add("\tl = l.above("+this.scrollStep+");");
        }
        if(last > 0){
            res.add("\tsikuli_screen.mouseMove(l);");
            res.add("\tsikuli_screen.wait(0.2);");
            res.add("\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\tsikuli_screen.wait(0.2);");
            res.add("\tl = l.below("+last+");");
            res.add("\tsikuli_screen.mouseMove(l);");
            res.add("\tsikuli_screen.mouseUp();");
            res.add("\tsikuli_screen.wait(0.2);");
        }
        res.add("\t}");
        res.add("catch (FindFailed ffe) {");
        res.add("\tffe.printStackTrace();");
        res.add("\treturn \"fail;\"+interactions;");
        res.add("}");

        return res;
    }

    @Override
    public ArrayList<String> generateEyeAutomateJavaLines(String starting_folder) {
        ArrayList<String> res = new ArrayList<>();
        res.add("image = eye.loadImage(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
        res.add("if (image != null) {");
        res.add("\tmatch = eye.findImage(image);");
        res.add("\tif (match == null) {");
        res.add("\t\tSystem.out.println(\"Test failed - " + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
        res.add("\t\treturn \"fail;\"+interactions;");
        res.add("\t}");
        int last = this.toBeScrolled%this.scrollStep;

        res.add("		eye.move(match.getCenterLocation());\r\n");
        res.add("		Thread.sleep(20);\r\n");
        for (int i = 0 ; i < (this.toBeScrolled/this.scrollStep); i++){
            res.add("		bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);\r\n");
            res.add("		Thread.sleep(20);\r\n");
            /*res.add("		for (int i=0; i<100; i++){  \r\n");
            res.add("		    int mov_y = (((int)(MouseInfo.getPointerInfo().getLocation().getY() +" + this.scrollStep + ") * i)/100) + ((int)MouseInfo.getPointerInfo().getLocation().getY()*(100-i)/100);\r\n");
            res.add("		    bot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX()),mov_y);\r\n");
            res.add("	        bot.delay(10);\r\n");
            res.add("	    }\r\n");*/
            res.add("       bot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX()),(int)(match.getCenterLocation().getY() + " + this.scrollStep +"));");
            res.add("		Thread.sleep(20);\r\n");
            res.add("		bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);");
            res.add("		Thread.sleep(20);\r\n");
            res.add("       bot.mouseMove((int)match.getCenterLocation().getX(),(int)match.getCenterLocation().getY());\r\n");
            res.add("		Thread.sleep(20);\r\n");
        }
        if(last > 0){
            res.add("		bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);\r\n");
            res.add("		Thread.sleep(20);\r\n");
            /*res.add("		for (int i=0; i<100; i++){  \r\n");
            res.add("		    int mov_y = (((int)(MouseInfo.getPointerInfo().getLocation().getY() +" + last + ") * i)/100) + ((int)MouseInfo.getPointerInfo().getLocation().getY()*(100-i)/100);\r\n");
            res.add("		    bot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX()),mov_y);\r\n");
            res.add("	        bot.delay(10);\r\n");
            res.add("	    }\r\n");*/
            res.add("       bot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX()),(int)(match.getCenterLocation().getY() + " + last +"));");
            res.add("		Thread.sleep(20);\r\n");
            res.add("		bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);");
            res.add("		Thread.sleep(20);\r\n");
        }
        res.add("}");
        res.add("else {");
        res.add("\tSystem.out.println(\"image not found\");");
        res.add("\treturn \"fail;\"+interactions;");
        res.add("}");
        return res;
    }

    @Override
    public ArrayList<String> generateCombinedJavaLines(String starting_folder) {
        ArrayList<String> res = new ArrayList<>();
        int last = this.toBeScrolled%this.scrollStep;
        res.add("image = eye.loadImage(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
        res.add("if (image != null) {");
        res.add("\tmatch = eye.findImage(image);");

        //try sikuli
        res.add("\tif (match == null) {");
        res.add("\t\teyeautomate_failures++;");

        res.add("\t\ttry {");
        res.add("\t\t\tsikuli_screen.wait(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\", 25);");
        res.add("\t\t\torg.sikuli.script.Match sikuli_match = sikuli_screen.find(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\",  "\\\\") + "\");");
        res.add("\t\t\torg.sikuli.script.Location l = sikuli_match.getCenter();");

        for(int i = 0; i < this.toBeScrolled/this.scrollStep; i++) {
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tsikuli_screen.wait(0.2);");
            res.add("\t\t\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\t\t\tsikuli_screen.wait(0.2);");
            res.add("\t\t\tl = l.below(" + this.scrollStep + ");");
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tsikuli_screen.mouseUp();");
            res.add("\t\t\tsikuli_screen.wait(0.2);");
            res.add("\t\t\tl = l.above(" + this.scrollStep + ");");
        }
        if(last > 0){
            res.add("\tsikuli_screen.mouseMove(l);");
            res.add("\tsikuli_screen.wait(0.2);");
            res.add("\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\tsikuli_screen.wait(0.2);");
            res.add("\tl = l.below("+last+");");
            res.add("\tsikuli_screen.mouseMove(l);");
            res.add("\tsikuli_screen.mouseUp();");
            res.add("\tsikuli_screen.wait(0.2);");
        }
        res.add("\t\t\t}");
        res.add("\t\tcatch (FindFailed ffe) {");
        res.add("\t\t\tffe.printStackTrace();");
        res.add("\t\t\treturn \"fail;\" + eyeautomate_failures + \";\" + interactions;");
        res.add("\t\t}");
        res.add("\t}");

        //eyeautomate
        res.add("\telse {");
        res.add("		eye.move(match.getCenterLocation());\r\n");
        res.add("		Thread.sleep(20);\r\n");
        for (int i = 0 ; i < (this.toBeScrolled/this.scrollStep); i++){
            res.add("		bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);\r\n");
            res.add("		Thread.sleep(20);\r\n");
            /*res.add("		for (int i=0; i<100; i++){  \r\n");
            res.add("		    int mov_y = (((int)(MouseInfo.getPointerInfo().getLocation().getY() +" + this.scrollStep + ") * i)/100) + ((int)MouseInfo.getPointerInfo().getLocation().getY()*(100-i)/100);\r\n");
            res.add("		    bot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX()),mov_y);\r\n");
            res.add("	        bot.delay(10);\r\n");
            res.add("	    }\r\n");*/
            res.add("       bot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX()),(int)(match.getCenterLocation().getY() + " + this.scrollStep +"));");
            res.add("		Thread.sleep(20);\r\n");
            res.add("		bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);");
            res.add("		Thread.sleep(20);\r\n");
            res.add("       bot.mouseMove((int)match.getCenterLocation().getX(),(int)match.getCenterLocation().getY());\r\n");
            res.add("		Thread.sleep(20);\r\n");
        }
        if(last > 0){
            res.add("		bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);\r\n");
            res.add("		Thread.sleep(20);\r\n");
            /*res.add("		for (int i=0; i<100; i++){  \r\n");
            res.add("		    int mov_y = (((int)(MouseInfo.getPointerInfo().getLocation().getY() +" + last + ") * i)/100) + ((int)MouseInfo.getPointerInfo().getLocation().getY()*(100-i)/100);\r\n");
            res.add("		    bot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX()),mov_y);\r\n");
            res.add("	        bot.delay(10);\r\n");
            res.add("	    }\r\n");*/
            res.add("       bot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX()),(int)(match.getCenterLocation().getY() + " + last +"));");
            res.add("		Thread.sleep(20);\r\n");
            res.add("		bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);");
            res.add("		Thread.sleep(20);\r\n");
        }
        res.add("\t}");
        res.add("}");
        res.add("else {");
        res.add("\tSystem.out.println(\"image not found\");");
        res.add("\treturn \"fail;\" + eyeautomate_failures + \";\" + interactions;");
        res.add("}");

        return res;
    }

    @Override
    public ArrayList<String> generateCombinedJavaLinesSikuliFirst(String starting_folder) {
        ArrayList<String> res = new ArrayList<>();
        int last = this.toBeScrolled%this.scrollStep;
        res.add("try {");
        res.add("\tsikuli_screen.wait(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\", 5);");
        res.add("\torg.sikuli.script.Match sikuli_match = sikuli_screen.find(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\",  "\\\\") + "\");");
        res.add("\torg.sikuli.script.Location l = sikuli_match.getCenter();");
        for(int i = 0; i < this.toBeScrolled/this.scrollStep; i++) {
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tsikuli_screen.wait(0.2);");
            res.add("\t\t\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\t\t\tsikuli_screen.wait(0.2);");
            res.add("\t\t\tl = l.below(" + this.scrollStep + ");");
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tsikuli_screen.mouseUp();");
            res.add("\t\t\tsikuli_screen.wait(0.2);");
            res.add("\t\t\tl = l.above(" + this.scrollStep + ");");
        }
        if(last > 0){
            res.add("\tsikuli_screen.mouseMove(l);");
            res.add("\tsikuli_screen.wait(0.2);");
            res.add("\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\tsikuli_screen.wait(0.2);");
            res.add("\tl = l.below("+last+");");
            res.add("\tsikuli_screen.mouseMove(l);");
            res.add("\tsikuli_screen.mouseUp();");
            res.add("\tsikuli_screen.wait(0.2);");
        }
        res.add("}");
        res.add("catch (FindFailed ffe) {");
        res.add("\tsikuli_failures++;");
        res.add("\timage = eye.loadImage(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
        res.add("\tif (image != null) {");
        res.add("\t\tmatch = eye.findImage(image);");
        res.add("\t\tif (match == null) {");		//test failed also with eyeautomate
        res.add("\t\t\treturn \"fail;\" + sikuli_failures + \";\" + interactions;");
        res.add("\t\t}");
        res.add("\t\telse {");						//test ok with eyeautomate
        res.add("		eye.move(match.getCenterLocation());\r\n");
        res.add("		Thread.sleep(20);\r\n");
        for (int i = 0 ; i < (this.toBeScrolled/this.scrollStep); i++){
            res.add("		bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);\r\n");
            res.add("		Thread.sleep(20);\r\n");
            /*res.add("		for (int i=0; i<100; i++){  \r\n");
            res.add("		    int mov_y = (((int)(MouseInfo.getPointerInfo().getLocation().getY() +" + this.scrollStep + ") * i)/100) + ((int)MouseInfo.getPointerInfo().getLocation().getY()*(100-i)/100);\r\n");
            res.add("		    bot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX()),mov_y);\r\n");
            res.add("	        bot.delay(10);\r\n");
            res.add("	    }\r\n");*/
            res.add("       bot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX()),(int)(match.getCenterLocation().getY() + " + this.scrollStep +"));");
            res.add("		Thread.sleep(20);\r\n");
            res.add("		bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);");
            res.add("		Thread.sleep(20);\r\n");
            res.add("       bot.mouseMove((int)match.getCenterLocation().getX(),(int)match.getCenterLocation().getY());\r\n");
            res.add("		Thread.sleep(20);\r\n");
        }
        if(last > 0){
            res.add("		bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);\r\n");
            res.add("		Thread.sleep(20);\r\n");
            /*res.add("		for (int i=0; i<100; i++){  \r\n");
            res.add("		    int mov_y = (((int)(MouseInfo.getPointerInfo().getLocation().getY() +" + last + ") * i)/100) + ((int)MouseInfo.getPointerInfo().getLocation().getY()*(100-i)/100);\r\n");
            res.add("		    bot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX()),mov_y);\r\n");
            res.add("	        bot.delay(10);\r\n");
            res.add("	    }\r\n");*/
            res.add("       bot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX()),(int)(match.getCenterLocation().getY() + " + last +"));");
            res.add("		Thread.sleep(20);\r\n");
            res.add("		bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);");
            res.add("		Thread.sleep(20);\r\n");
        }
        res.add("\t\t}");
        res.add("\t}");
        res.add("\telse {");
        res.add("\t\tSystem.out.println(\"image not found\");");
        res.add("\t\treturn \"fail;\" + sikuli_failures + \";\" + interactions;");
        res.add("\t}");
        res.add("}");
        return res;
    }
}
