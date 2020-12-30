package it.polito.toggle.ToggleInteractions;

import it.polito.toggle.ToggleInteraction;
import it.polito.toggle.exceptions.ToggleException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ScrollDownRight extends ToggleInteraction {


    //TODO : SCREENSHOT DELL'ULTIMO NODO MOSTRATO SPECIFICANDO CHE STO CERCANDO IL NODO N. NEGLI ARGS LOGGARE ANCHE LE COORD TOP E LEFT DEL PRIMO NODO MOSTRATO
    //       SUCCESSIVAMENTE SI TROVA IL CENTRO E SI SCROLLA DAL CENTRO VERSO L'ALTO DI (TOPUltimo-TOPPrimo).
    //       SI CONTINUA FINO A RAGGIUNGERE IL NUMERO DI STEP NECESSARI.
    private int scrollYStep;
    private int toBeScrolledY;
    private int scrollXStep;
    private int toBeScrolledX;

    public ScrollDownRight(String packagename, String search_type,
                           String search_keyword, String timestamp,
                           String interaction_type, String args,
                           File screen_capture, File dump,
                           double screenYRatio, double screenXRatio)
            throws XPathExpressionException, SAXException, IOException, ParserConfigurationException, ToggleException {
        super(packagename, search_type, search_keyword, timestamp, interaction_type, args, screen_capture, dump);
        String[] coords = args.split(";");
        int fromY = Integer.parseInt(coords[0]);
        int toY = Integer.parseInt(coords[1]);
        int fromX = Integer.parseInt(coords[2]);//
        int toX = Integer.parseInt(coords[3]);//
        //int singleItemH = Integer.parseInt(coords[4]);
        //int singleItemW = Integer.parseInt(coords[5]);//
        int singleItemH = this.bottom - this.top;
        int singleItemW = this.right - this.left;//

        int AVHeight = Integer.parseInt(coords[6]);
        if(AVHeight <= singleItemH){
            singleItemH = (AVHeight)/2;
            for(int i = 10; i >= 0; i--){
                if((singleItemH-i)>0){
                    singleItemH-=i;
                    break;
                }
            }
        }

        int AVWidth = Integer.parseInt(coords[7]);
        if(AVWidth <= singleItemW) {
            singleItemW = (AVWidth)/2;
            for(int i = 10; i >= 0; i--){
                if((singleItemW-i)>0){
                    singleItemW-=i;
                    break;
                }
            }
        }
        int AVLeft = Integer.parseInt(coords[8]);
        int AVTop = Integer.parseInt(coords[9]);
        int tmpStepY,tmpStepX;
        int offFromTop = (this.bottom-this.top)/2;
        int offFromStart = (this.right-this.left)/2;

        // Y computing
        if((toY-fromY) > AVHeight){
            tmpStepY = AVHeight - singleItemH;
        } else {
            tmpStepY = Math.max((toY-fromY) - singleItemW,0);
        }
        if((this.top + tmpStepY + offFromTop) > (AVTop + AVHeight)){
            //todo non hardoccare quel 10, potremmo ottenere dalla super classe l'ampiezza dell'ultimo nodo
            tmpStepY = tmpStepY-((this.top + tmpStepY + offFromTop) -(AVTop + AVHeight + 10));
        }
        tmpStepY = (int)(tmpStepY*screenYRatio);
        this.toBeScrolledY = (int)((toY-fromY)*screenYRatio);
        this.scrollYStep = tmpStepY;

        // X computing
        if((toX-fromX) > AVWidth){
            tmpStepX = AVWidth - singleItemW;
        } else {
            tmpStepX = Math.max((toX - fromX) - singleItemW, 0);
        }
        if((this.left + tmpStepX + offFromStart) > (AVLeft + AVWidth)){
            //todo non hardoccare quel 10, potremmo ottenere dalla super classe l'ampiezza dell'ultimo nodo
            tmpStepX = tmpStepX-((this.left + tmpStepX + offFromStart) - (AVLeft + AVWidth + 10));
        }
        tmpStepX = (int)(tmpStepX*screenXRatio);
        this.toBeScrolledX = (int)((toX-fromX)*screenXRatio);
        this.scrollXStep = tmpStepX;
    }

    @Override
    public ArrayList<String> generateSikuliLines() {
        ArrayList<String> res = new ArrayList<>();
        res.add("sleep(1)");
        res.add("wait(\"" + timestamp + "_cropped.png\", 30)");
        res.add("r = find(\"" + timestamp + "_cropped.png\")");
        res.add("stepY = "+this.scrollYStep);
        res.add("stepX = "+this.scrollXStep);
        res.add("start = r.getCenter()");

        res.add("start = start.below(stepY)");
        res.add("start = start.right(stepX)");

        res.add("run = start.above(stepY)");
        res.add("run = run.left(stepX)");
        int totToBeScrolled = (int) Math.sqrt((Math.pow(this.toBeScrolledY,2)) + Math.pow(this.toBeScrolledX,2));
        int totScrollStep = (int) Math.sqrt((Math.pow(this.scrollYStep,2)) + Math.pow(this.scrollXStep,2));
        int last = totToBeScrolled%totScrollStep;

        for(int i = 0; i < (totToBeScrolled/totScrollStep); i++) {
            res.add("mouseMove(start); wait(0.5)");
            res.add("mouseDown(Button.LEFT); wait(0.5)");
            //res.add("run = run.above(stepY)");
            //res.add("run = run.left(stepX)");
            res.add("wait(0.5)");//
            res.add("mouseMove(run)");
            res.add("wait(1)");//
            res.add("mouseUp()");
            res.add("wait(0.5)");
            //res.add("start = run.below(stepY)");
            //res.add("start = run.right(stepX)");
            //res.add("wait(0.5)");
        }
        if(last > 0){
            double ratio = (double)last/(double)totScrollStep;
            res.add("mouseMove(start); wait(0.5)");
            res.add("mouseDown(Button.LEFT); wait(0.5)");
            res.add("run = start.above("+(int)(this.scrollYStep*ratio)+")");
            res.add("run = run.left("+(int)(this.scrollXStep*ratio)+")");
            res.add("wait(0.5)");
            res.add("mouseMove(run)");
            res.add("wait(1)");//
            res.add("mouseUp()");
            res.add("wait(0.5)");
        }
        return res;
    }

    @Override
    public ArrayList<String> generateEyeStudioLines() {
        ArrayList<String> res = new ArrayList<>();
        res.add("Check \"{ImageFolder}\\" + timestamp + "_cropped.png\"");
        res.add("Move \"{ImageFolder}\\" + timestamp + "_cropped.png\"");
        res.add("Sleep 10");

        int totToBeScrolled = (int) Math.sqrt((Math.pow(this.toBeScrolledY,2)) + Math.pow(this.toBeScrolledX,2));
        int totScrollStep = (int) Math.sqrt((Math.pow(this.scrollYStep,2)) + Math.pow(this.scrollXStep,2));
        int last = totToBeScrolled%totScrollStep;
        //TODO riconosco il primo elemento, mi muovo di scrollStep e inizio a scrollare
        res.add("MoveRelative \""+ this.scrollXStep + "\" \"" + this.scrollYStep +"\"");
        res.add("Sleep 500");
        //int last = toBeScrolled%this.scrollStep;
        for(int i = 0; i < (totToBeScrolled/totScrollStep);i++){
            res.add("MouseLeftPress");
            res.add("Sleep 500");
            res.add("MoveRelative \"-"+ this.scrollXStep + "\" \"-" + this.scrollYStep + "\"");
            res.add("Sleep 1000");
            res.add("MouseLeftRelease");
            res.add("Sleep 500");
            res.add("MoveRelative \""+ this.scrollXStep + "\" \"" + this.scrollYStep +"\"");
            res.add("Sleep 500");
        }
        if(last > 0){
            double ratio = (double)last/(double)totScrollStep;
            res.add("MouseLeftPress");
            res.add("Sleep 500");
            res.add("MoveRelative \"-" + (int)(this.scrollXStep*ratio) + "\" \"-" + (int)(this.scrollYStep*ratio) + "\"");
            res.add("Sleep 1000");
            res.add("MouseLeftRelease");
            res.add("Sleep 500");
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

        res.add("\tsikuli_screen.wait(0.2);");
        res.add("\tl = l.below("+this.scrollYStep+");");
        res.add("\tl = l.right("+this.scrollXStep+");");

        res.add("\tsikuli_screen.wait(0.5);");
        int totToBeScrolled = (int) Math.sqrt((Math.pow(this.toBeScrolledY,2)) + Math.pow(this.toBeScrolledX,2));
        int totScrollStep = (int) Math.sqrt((Math.pow(this.scrollYStep,2)) + Math.pow(this.scrollXStep,2));
        int last = totToBeScrolled%totScrollStep;

        for(int i = 0; i < (totToBeScrolled/totScrollStep); i++ ){
            res.add("\tsikuli_screen.mouseMove(l);");
            res.add("\tsikuli_screen.wait(0.5);");
            res.add("\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\tsikuli_screen.wait(0.5);");
            res.add("\tl = l.above("+this.scrollYStep+");");
            res.add("\tl = l.left("+this.scrollXStep+");");
            res.add("\tsikuli_screen.mouseMove(l);");
            res.add("\tsikuli_screen.wait(1);");
            res.add("\tsikuli_screen.mouseUp();");
            res.add("\tsikuli_screen.wait(0.5);");
            res.add("\tl = l.below("+this.scrollYStep+");");
            res.add("\tl = l.right("+this.scrollXStep+");");
            res.add("\tsikuli_screen.wait(0.5);");
        }
        if(last > 0){
            double ratio = (double)last/(double)totScrollStep;
            res.add("\tsikuli_screen.mouseMove(l);");
            res.add("\tsikuli_screen.wait(0.5);");
            res.add("\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\tsikuli_screen.wait(0.5);");
            res.add("\tl = l.above("+(int)(this.scrollYStep*ratio)+");");
            res.add("\tl = l.left("+(int)(this.scrollXStep*ratio)+");");
            res.add("\tsikuli_screen.mouseMove(l);");
            res.add("\tsikuli_screen.wait(1);");
            res.add("\tsikuli_screen.mouseUp();");
            res.add("\tsikuli_screen.wait(0.5);");
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
        int totToBeScrolled = (int) Math.sqrt((Math.pow(this.toBeScrolledY,2)) + Math.pow(this.toBeScrolledX,2));
        int totScrollStep = (int) Math.sqrt((Math.pow(this.scrollYStep,2)) + Math.pow(this.scrollXStep,2));
        int last = totToBeScrolled%totScrollStep;
        res.add("image = eye.loadImage(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
        res.add("if (image != null) {");
        res.add("\tmatch = eye.findImage(image);");
        res.add("\tif (match == null) {");
        res.add("\t\tSystem.out.println(\"Test failed - " + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
        res.add("\t\treturn \"fail;\"+interactions;");
        res.add("\t}");

        res.add("\t\teye.move(match.getCenterLocation());\r\n");
        res.add("\t\tThread.sleep(500);\r\n");
        res.add("\t\tbot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX() + " + this.scrollXStep +"),(int)(MouseInfo.getPointerInfo().getLocation().getY() + " + this.scrollYStep +"));\r\n");
        res.add("\t\tThread.sleep(500);\r\n");
        for (int i = 0 ; i < (totToBeScrolled/totScrollStep); i++){
            res.add("\t\tbot.mousePress(InputEvent.BUTTON1_DOWN_MASK);\r\n");
            res.add("\t\tThread.sleep(500);\r\n");
            res.add("\t\tbot.mouseMove((int)(match.getCenterLocation().getX()),(int)(match.getCenterLocation().getY()));\r\n"); //from bottom to top
            res.add("\t\tThread.sleep(1000);\r\n");
            res.add("\t\tbot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);\r\n");
            res.add("\t\tThread.sleep(500);\r\n");
            res.add("\t\tbot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX() + " + this.scrollXStep +"),(int)(MouseInfo.getPointerInfo().getLocation().getY() + " + this.scrollYStep +"));\r\n");
            res.add("\t\tThread.sleep(500);\r\n");
        }
        if(last > 0){
            double ratio = (double)last/(double)totScrollStep;
            res.add("\t\tbot.mousePress(InputEvent.BUTTON1_DOWN_MASK);\r\n");
            res.add("\t\tThread.sleep(500);\r\n");
            res.add("\t\tbot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX() - " + (int)(this.scrollXStep*ratio) + "),(int)(MouseInfo.getPointerInfo().getLocation().getY() - "+(int)(this.scrollYStep*ratio)+"));");
            res.add("\t\tThread.sleep(1000);\r\n");
            res.add("\t\tbot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);");
            res.add("\t\tThread.sleep(500);\r\n");
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
        int totToBeScrolled = (int) Math.sqrt((Math.pow(this.toBeScrolledY,2)) + Math.pow(this.toBeScrolledX,2));
        int totScrollStep = (int) Math.sqrt((Math.pow(this.scrollYStep,2)) + Math.pow(this.scrollXStep,2));
        int last = totToBeScrolled%totScrollStep;
        double ratio = (double)last/(double)totScrollStep;
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

        res.add("\t\t\tsikuli_screen.wait(0.2);");
        res.add("\t\t\tl = l.below("+this.scrollYStep+");");
        res.add("\t\t\tl = l.right("+this.scrollXStep+");");

        for(int i = 0; i < (totToBeScrolled/totScrollStep); i++) {
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tsikuli_screen.wait(0.5);");
            res.add("\t\t\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\t\t\tsikuli_screen.wait(0.5);");
            res.add("\t\t\tl = l.above(" + this.scrollYStep + ");");
            res.add("\t\t\tl = l.left(" + this.scrollXStep + ");");
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tsikuli_screen.wait(1);");
            res.add("\t\t\tsikuli_screen.mouseUp();");
            res.add("\t\t\tsikuli_screen.wait(0.5);");
            res.add("\t\t\tl = l.below(" + this.scrollYStep + ");");
            res.add("\t\t\tl = l.right(" + this.scrollXStep + ");");
            res.add("\t\t\tsikuli_screen.wait(0.5);");
        }
        if(last > 0){
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tsikuli_screen.wait(0.5);");
            res.add("\t\t\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\t\t\tsikuli_screen.wait(0.5);");
            res.add("\t\t\tl = l.above("+(int)(this.scrollYStep*ratio)+");");
            res.add("\t\t\tl = l.left("+(int)(this.scrollXStep*ratio)+");");
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tsikuli_screen.wait(1);");
            res.add("\t\t\tsikuli_screen.mouseUp();");
            res.add("\t\t\tsikuli_screen.wait(0.5);");
        }
        res.add("\t\t\t}");
        res.add("\t\tcatch (FindFailed ffe) {");
        res.add("\t\t\tffe.printStackTrace();");
        res.add("\t\t\treturn \"fail;\" + eyeautomate_failures + \";\" + interactions;");
        res.add("\t\t}");
        res.add("\t}");

        //eyeautomate
        res.add("\telse {");
        res.add("\t\teye.move(match.getCenterLocation());\r\n");
        res.add("\t\tThread.sleep(500);\r\n");

        res.add("\t\tbot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX() + " + this.scrollXStep +"),(int)(MouseInfo.getPointerInfo().getLocation().getY() + " + this.scrollYStep +"));\r\n");

        res.add("\t\tThread.sleep(500);\r\n");
        for (int i = 0 ; i < (totToBeScrolled/totScrollStep); i++){
            res.add("\t\tbot.mousePress(InputEvent.BUTTON1_DOWN_MASK);\r\n");
            res.add("\t\tThread.sleep(500);\r\n");
            res.add("\t\tbot.mouseMove((int)(match.getCenterLocation().getX()),(int)(match.getCenterLocation().getY()));\r\n"); //from bottom to top
            res.add("\t\tThread.sleep(1000);\r\n");
            res.add("\t\tbot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);");
            res.add("\t\tThread.sleep(500);\r\n");
            res.add("\t\tbot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX() + " + this.scrollXStep +"),(int)(MouseInfo.getPointerInfo().getLocation().getY() + " + this.scrollYStep +"));\r\n");
            res.add("\t\tThread.sleep(500);\r\n");
        }
        if(last > 0){
            res.add("\t\tbot.mousePress(InputEvent.BUTTON1_DOWN_MASK);\r\n");
            res.add("\t\tThread.sleep(500);\r\n");
            res.add("\t\tbot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX() - " + (int)(this.scrollXStep*ratio) + "),(int)(MouseInfo.getPointerInfo().getLocation().getY() - " + (int)(this.scrollYStep*ratio) +"));");
            res.add("\t\tThread.sleep(1000);\r\n");
            res.add("\t\tbot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);");
            res.add("\t\tThread.sleep(500);\r\n");
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
        int totToBeScrolled = (int) Math.sqrt((Math.pow(this.toBeScrolledY,2)) + Math.pow(this.toBeScrolledX,2));
        int totScrollStep = (int) Math.sqrt((Math.pow(this.scrollYStep,2)) + Math.pow(this.scrollXStep,2));
        int last = totToBeScrolled%totScrollStep;
        double ratio = (double)last/(double)totScrollStep;
        res.add("try {");
        res.add("\tsikuli_screen.wait(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\", 5);");
        res.add("\torg.sikuli.script.Match sikuli_match = sikuli_screen.find(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\",  "\\\\") + "\");");
        res.add("\torg.sikuli.script.Location l = sikuli_match.getCenter();");

        res.add("\t\t\tsikuli_screen.wait(0.5);");
        res.add("\tl = l.below("+this.scrollYStep+");");
        res.add("\tl = l.right("+this.scrollXStep+");");
        for(int i = 0; i < (totToBeScrolled/totScrollStep); i++) {
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tsikuli_screen.wait(0.5);");
            res.add("\t\t\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\t\t\tsikuli_screen.wait(0.5);");
            res.add("\t\t\tl = l.above(" + this.scrollYStep + ");");
            res.add("\t\t\tl = l.left(" + this.scrollXStep + ");");
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tsikuli_screen.wait(1);");
            res.add("\t\t\tsikuli_screen.mouseUp();");
            res.add("\t\t\tsikuli_screen.wait(0.5);");
            res.add("\t\t\tl = l.below(" + this.scrollYStep + ");");
            res.add("\t\t\tl = l.right(" + this.scrollXStep + ");");
            res.add("\t\t\tsikuli_screen.wait(0.5);");
        }
        if(last > 0){
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tsikuli_screen.wait(0.5);");
            res.add("\t\t\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\t\t\tsikuli_screen.wait(0.5);");
            res.add("\t\t\tl = l.above(" + (int)(this.scrollYStep*ratio) + ");");
            res.add("\t\t\tl = l.left(" + (int)(this.scrollXStep*ratio) + ");");
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tsikuli_screen.wait(1);");
            res.add("\t\t\tsikuli_screen.mouseUp();");
            res.add("\t\t\tsikuli_screen.wait(0.5);");
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
        res.add("\t\t\teye.move(match.getCenterLocation());\r\n");
        res.add("\t\t\tThread.sleep(500);\r\n");
        res.add("\t\t\tbot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX() + " + this.scrollXStep +"),(int)(MouseInfo.getPointerInfo().getLocation().getY() + " + this.scrollYStep +"));\r\n");
        res.add("\t\t\tThread.sleep(500);\r\n");
        for (int i = 0 ; i < (totToBeScrolled/totScrollStep); i++){
            res.add("\t\t\tbot.mousePress(InputEvent.BUTTON1_DOWN_MASK);\r\n");
            res.add("\t\t\tThread.sleep(500);\r\n");
            res.add("\t\t\tbot.mouseMove((int)(match.getCenterLocation().getX()),(int)(match.getCenterLocation().getY()));\r\n"); //from bottom to top
            res.add("\t\t\tThread.sleep(1000);\r\n");
            res.add("\t\t\tbot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);");
            res.add("\t\t\tThread.sleep(500);\r\n");
            res.add("\t\t\tbot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX() + " + this.scrollXStep +"),(int)(MouseInfo.getPointerInfo().getLocation().getY() + " + this.scrollYStep +"));\r\n");
            res.add("\t\t\tThread.sleep(500);\r\n");
        }
        if(last > 0){
            res.add("\t\t\tbot.mousePress(InputEvent.BUTTON1_DOWN_MASK);\r\n");
            res.add("\t\t\tThread.sleep(500);\r\n");
            res.add("\t\t\tbot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX() - " + (int)(this.scrollXStep*ratio) +"),(int)(MouseInfo.getPointerInfo().getLocation().getY() - " + (int)(this.scrollYStep*ratio) +"));\r\n");
            res.add("\t\t\tThread.sleep(1000);\r\n");
            res.add("\t\t\tbot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);");
            res.add("\t\t\tThread.sleep(500);\r\n");
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
