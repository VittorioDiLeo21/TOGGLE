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
    private int scrollYStep;
    private int toBeScrolledY;
    private int scrollXStep;
    private int toBeScrolledX;
    private int pxUp = 0;
    private int pxDown = 0;
    private int pxLeft = 0;
    private int pxRight = 0;

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
        int singleItemH = this.bottom - this.top;
        int singleItemW = this.right - this.left;//

        //***********************************************
        int locatorH = this.bottom - this.top;
        int locatorW = this.right - this.left;//
        //***********************************************

        int AVHeight = Integer.parseInt(coords[6]);
        int AVWidth = Integer.parseInt(coords[7]);
        int offFromTop = (this.bottom-this.top)/2;
        int offFromStart = (this.right-this.left)/2;

        int AVLeft = Integer.parseInt(coords[8]);
        int AVTop = Integer.parseInt(coords[9]);
        int tmpStepY,tmpStepX;

        if(search_type.contains("scrollTo")){
            //scrollTo
            if(toY != 0) {
                toY += singleItemH;
                fromY += singleItemH;
                if (offFromTop > 20) {
                    this.pxUp = offFromTop - 20;
                } else {
                    this.pxUp = 0;
                    for(int i = 18; i > 0; i--){
                        if(offFromTop > i){
                            this.pxUp = offFromTop-i;
                            break;
                        }
                    }
                }
                this.pxDown = 2*this.pxUp;
            }
            if(toX != 0) {
                toX += singleItemW;
                fromX += singleItemW;
                if(offFromStart > 20){
                    this.pxLeft = offFromStart-20;
                } else {
                    this.pxLeft = 0;
                    for(int i = 18; i > 0; i--){
                        if(offFromStart > i){
                            this.pxLeft = offFromStart-i;
                            break;
                        }
                    }
                }
                this.pxDown = 2*pxLeft;
            }
            this.toBeScrolledY = (int) ((toY - fromY) * screenYRatio);
            if(toBeScrolledY > 0)
                toBeScrolledY+=20;
            this.scrollYStep = (int) (this.pxDown*screenYRatio);
            this.toBeScrolledX = (int) ((toX - fromX) * screenXRatio);
            if(toBeScrolledX>0)
                toBeScrolledX+=20;
            this.scrollXStep = (int) (this.pxRight*screenXRatio);
        } else {
            //onData
            int coordItemInAVTop = AVTop - this.top;
            int coordItemInAVLeft = AVLeft - this.left;
            int baseY = AVHeight - coordItemInAVTop - offFromTop;
            int baseX = AVWidth - coordItemInAVLeft - offFromStart;
            if(toY > AVHeight){
                if( baseY > 20) {
                    this.pxUp = baseY - 20;
                } else {
                    this.pxUp = 0;
                    for(int i = 18; i > 0; i--){
                        if(baseY > i){
                            this.pxUp = baseY - i;
                            break;
                        }
                    }
                }
                if((offFromTop+coordItemInAVTop) > 20) {
                    this.pxDown = offFromTop+coordItemInAVTop - 20;
                } else {
                    this.pxDown = 0;
                }
            } else {
                this.pxUp = toY;
                this.pxDown = 0;
            }
            if(toX > AVWidth){
                if( baseX > 20) {
                    this.pxLeft = baseX - 20;
                } else {
                    this.pxLeft = 0;
                    for(int i = 18; i > 0; i--){
                        if(baseX > i){
                            this.pxLeft = baseX - i;
                            break;
                        }
                    }
                }
                if((offFromStart+coordItemInAVLeft) > 20) {
                    this.pxRight = offFromStart+coordItemInAVLeft - 20;
                } else {
                    this.pxRight = 0;
                }
            } else {
                this.pxLeft = toX;
                this.pxRight = 0;
            }
            this.toBeScrolledY = (int) ((toY - fromY) * screenYRatio);
            if(toBeScrolledY > 0)
                toBeScrolledY+=20;
            this.scrollYStep = (int) ((this.pxDown + this.pxUp)*screenYRatio);

            this.toBeScrolledX = (int) ((toX - fromX) * screenXRatio);
            if(toBeScrolledX>0)
                toBeScrolledX+=20;

            this.scrollXStep = (int) ((this.pxRight+this.pxLeft)*screenXRatio);
        }
        this.pxLeft = (int)(this.pxLeft*screenXRatio);
        this.pxUp = (int)(this.pxUp*screenYRatio);
        /*if(AVHeight <= singleItemH){
            singleItemH = (AVHeight)/2;
            for(int i = 10; i >= 0; i--){
                if((singleItemH+i)<AVHeight){
                    singleItemH+=i;
                    break;
                }
            }
        }

        if(AVWidth <= singleItemW) {
            singleItemW = (AVWidth)/2;
            for(int i = 10; i >= 0; i--){
                if((singleItemW+i)<AVWidth){
                    singleItemW+=i;
                    break;
                }
            }
        }*/

        // Y & X computing for scrollTo
        /*if(search_type.contains("scrollTo")){
            // Y computing for scrollTo
            if (toY > fromY) {
                tmpStepY = fromY - singleItemH;
            } else {
                tmpStepY = 0;
            }
            if ((this.top + tmpStepY + offFromTop) > (this.top + AVHeight)) {
                for (int i = 10; i >= 0; i--) {
                    int newtmp = tmpStepY - ((this.top + tmpStepY + offFromTop) - (this.top + AVHeight - i));
                    if (newtmp < tmpStepY) {
                        tmpStepY = newtmp;
                        break;
                    }
                }
            }
            tmpStepY -= 10;
            tmpStepY = (int) (tmpStepY * screenYRatio);
            this.toBeScrolledY = (int) ((toY - fromY) * screenYRatio);
            this.scrollYStep = tmpStepY;

            // X computing for scrollto
            if (toX > fromX) {
                tmpStepX = AVWidth - singleItemW;
            } else {
                tmpStepX = 0;
            }
            if ((this.left + tmpStepX + offFromStart) > (this.left + AVWidth)) {
                for (int i = 10; i >= 0; i--) {
                    int newtmp = tmpStepX - ((this.left + tmpStepX + offFromStart) - (this.left + AVWidth - i));
                    if (newtmp < tmpStepX) {
                        tmpStepX = newtmp;
                        break;
                    }
                }
                //tmpStepX = tmpStepX-((this.left + tmpStepX + offFromStart) - (AVLeft + AVWidth + 10));
            }
            tmpStepX = (int) (tmpStepX * screenXRatio);
            this.toBeScrolledX = (int) ((toX - fromX) * screenXRatio);
            this.scrollXStep = tmpStepX;
        } elseif(!search_type.contains("scrollTo")) {
            // Y computing for onData
            if ((toY - fromY) > AVHeight) {
                tmpStepY = AVHeight - singleItemH;
            } else {
                tmpStepY = Math.max((toY - fromY) - singleItemH, 0);
            }
            if ((this.top + tmpStepY + offFromTop) > (AVTop + AVHeight)) {
                for (int i = 10; i >= 0; i--) {
                    int newtmp = tmpStepY - ((this.top + tmpStepY + offFromTop) - (AVTop + AVHeight - i));
                    if (newtmp < tmpStepY) {
                        tmpStepY = newtmp;
                        break;
                    }
                }
            }
            tmpStepY = (int) (tmpStepY * screenYRatio);
            this.toBeScrolledY = (int) ((toY - fromY) * screenYRatio);
            this.scrollYStep = tmpStepY;

            // X computing for onData
            if ((toX - fromX) > AVWidth) {
                tmpStepX = AVWidth - singleItemW;
            } else {
                tmpStepX = Math.max((toX - fromX) - singleItemW, 0);
            }
            if ((this.left + tmpStepX + offFromStart) > (AVLeft + AVWidth)) {
                for (int i = 10; i >= 0; i--) {
                    int newtmp = tmpStepX - ((this.left + tmpStepX + offFromStart) - (AVLeft + AVWidth - i));
                    if (newtmp < tmpStepX) {
                        tmpStepX = newtmp;
                        break;
                    }
                }
                //tmpStepX = tmpStepX-((this.left + tmpStepX + offFromStart) - (AVLeft + AVWidth + 10));
            }
            tmpStepX = (int) (tmpStepX * screenXRatio);
            this.toBeScrolledX = (int) ((toX - fromX) * screenXRatio);
            this.scrollXStep = tmpStepX;
        }*/
    }

    @Override
    public ArrayList<String> generateSikuliLines() {
        ArrayList<String> res = new ArrayList<>();
        int totToBeScrolled = (int) Math.sqrt((Math.pow(this.toBeScrolledY,2)) + Math.pow(this.toBeScrolledX,2));
        int totScrollStep = (int) Math.sqrt((Math.pow(this.scrollYStep,2)) + Math.pow(this.scrollXStep,2));
        if(totScrollStep<=0)
            return res;
        int last = totToBeScrolled%totScrollStep;

        res.add("sleep(1)");
        res.add("wait(\"" + timestamp + "_cropped.png\", 30)");
        res.add("r = find(\"" + timestamp + "_cropped.png\")");
        res.add("stepY = "+this.scrollYStep);
        res.add("stepX = "+this.scrollXStep);
        res.add("start = r.getCenter()");
        res.add("start = start.below("+this.pxUp+")");
        res.add("start = start.right("+this.pxLeft+")");
        res.add("run = start.above(stepY)");
        res.add("run = run.left(stepX)");
        for(int i = 0; i < (totToBeScrolled/totScrollStep); i++) {
            res.add("mouseMove(start); wait(0.5)");
            res.add("mouseDown(Button.LEFT); wait(0.5)");
            res.add("mouseMove(run)");
            res.add("wait(1)");//
            res.add("mouseUp()");
            res.add("wait(0.5)");
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
        /*res.add("sleep(1)");
        res.add("wait(\"" + timestamp + "_cropped.png\", 30)");
        res.add("r = find(\"" + timestamp + "_cropped.png\")");
        res.add("stepY = "+this.scrollYStep);
        res.add("stepX = "+this.scrollXStep);
        res.add("start = r.getCenter()");

        res.add("start = start.below(stepY)");
        res.add("start = start.right(stepX)");

        res.add("run = start.above(stepY)");
        res.add("run = run.left(stepX)");

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
        }*/
        return res;
    }

    @Override
    public ArrayList<String> generateEyeStudioLines() {
        ArrayList<String> res = new ArrayList<>();

        int totToBeScrolled = (int) Math.sqrt((Math.pow(this.toBeScrolledY,2)) + Math.pow(this.toBeScrolledX,2));
        int totScrollStep = (int) Math.sqrt((Math.pow(this.scrollYStep,2)) + Math.pow(this.scrollXStep,2));
        if(totScrollStep <= 0)
            return res;
        int last = totToBeScrolled%totScrollStep;

        res.add("Check \"{ImageFolder}\\" + timestamp + "_cropped.png\"");
        res.add("Move \"{ImageFolder}\\" + timestamp + "_cropped.png\"");
        res.add("Sleep 10");
        res.add("MoveRelative \""+ this.pxLeft + "\" \"" + this.pxUp +"\"");
        res.add("Sleep 500");
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
        /*res.add("Check \"{ImageFolder}\\" + timestamp + "_cropped.png\"");
        res.add("Move \"{ImageFolder}\\" + timestamp + "_cropped.png\"");
        res.add("Sleep 10");
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
        }*/
        return res;
    }

    @Override
    public ArrayList<String> generateSikuliJavaLines(String starting_folder) {
        ArrayList<String> res = new ArrayList<>();
        int totToBeScrolled = (int) Math.sqrt((Math.pow(this.toBeScrolledY,2)) + Math.pow(this.toBeScrolledX,2));
        int totScrollStep = (int) Math.sqrt((Math.pow(this.scrollYStep,2)) + Math.pow(this.scrollXStep,2));
        if(totScrollStep<=0)
            return res;
        int last = totToBeScrolled%totScrollStep;

        res.add("try {");
        res.add("\tsikuli_screen.wait(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\", 30);");
        res.add("\torg.sikuli.script.Match sikuli_match = sikuli_screen.find(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\",  "\\\\") + "\");");
        res.add("\torg.sikuli.script.Location l = sikuli_match.getCenter();");

        res.add("\tThread.sleep(200);");
        res.add("\tl = l.below("+this.pxUp+");");
        res.add("\tl = l.right("+this.pxLeft+");");

        res.add("\tThread.sleep(500);");

        for(int i = 0; i < (totToBeScrolled/totScrollStep); i++ ){
            res.add("\tsikuli_screen.mouseMove(l);");
            res.add("\tThread.sleep(500);");
            res.add("\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\tThread.sleep(500);");
            res.add("\tl = l.above("+this.scrollYStep+");");
            res.add("\tl = l.left("+this.scrollXStep+");");
            res.add("\tsikuli_screen.mouseMove(l);");
            res.add("\tThread.sleep(1000);");
            res.add("\tsikuli_screen.mouseUp();");
            res.add("\tThread.sleep(500);");
            res.add("\tl = l.below("+this.scrollYStep+");");
            res.add("\tl = l.right("+this.scrollXStep+");");
            res.add("\tThread.sleep(500);");
        }
        if(last > 0){
            double ratio = (double)last/(double)totScrollStep;
            res.add("\tsikuli_screen.mouseMove(l);");
            res.add("\tThread.sleep(500);");
            res.add("\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\tThread.sleep(500);");
            res.add("\tl = l.above("+(int)(this.scrollYStep*ratio)+");");
            res.add("\tl = l.left("+(int)(this.scrollXStep*ratio)+");");
            res.add("\tsikuli_screen.mouseMove(l);");
            res.add("\tThread.sleep(1000);");
            res.add("\tsikuli_screen.mouseUp();");
            res.add("\tThread.sleep(500);");
        }
        res.add("\t}");
        res.add("catch (FindFailed ffe) {");
        res.add("\tffe.printStackTrace();");
        //TODO CLASSNAME
        res.add("\tf.write(\";\"+interactions+\";f\\n\");");
        //TODO #INTERACTIONS
        res.add("\treturn \"fail;\"+interactions+\";4\";");
        res.add("}");

        /*res.add("try {");
        res.add("\tsikuli_screen.wait(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\", 30);");
        res.add("\torg.sikuli.script.Match sikuli_match = sikuli_screen.find(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\",  "\\\\") + "\");");
        res.add("\torg.sikuli.script.Location l = sikuli_match.getCenter();");

        res.add("\tsikuli_screen.wait(0.2);");
        res.add("\tl = l.below("+this.scrollYStep+");");
        res.add("\tl = l.right("+this.scrollXStep+");");

        res.add("\tsikuli_screen.wait(0.5);");

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
        res.add("}");*/

        return res;
    }

    @Override
    public ArrayList<String> generateEyeAutomateJavaLines(String starting_folder) {
        ArrayList<String> res = new ArrayList<>();
        int totToBeScrolled = (int) Math.sqrt((Math.pow(this.toBeScrolledY,2)) + Math.pow(this.toBeScrolledX,2));
        int totScrollStep = (int) Math.sqrt((Math.pow(this.scrollYStep,2)) + Math.pow(this.scrollXStep,2));
        if(totScrollStep<=0)
            return res;
        int last = totToBeScrolled%totScrollStep;

        res.add("image = eye.loadImage(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
        res.add("if (image != null) {");
        res.add("\tmatch = eye.findImage(image);");
        res.add("\tif (match == null) {");
        res.add("\t\tSystem.out.println(\"Test failed - " + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
        //todo classname
        res.add("\t\tf.write(\";\"+interactions+\";f\\n\");");
        //todo int#
        res.add("\t\treturn \"fail;\"+interactions+\";4\";");
        res.add("\t}");

        res.add("\t\teye.move(match.getCenterLocation());\r\n");
        res.add("\t\tThread.sleep(500);\r\n");
        res.add("\t\tbot.mouseMove((int)(match.getCenterLocation().getX() + " + this.pxLeft +"),(int)(match.getCenterLocation().getY() + " + this.pxUp +"));\r\n");
        res.add("\t\tThread.sleep(500);\r\n");
        for (int i = 0 ; i < (totToBeScrolled/totScrollStep); i++){
            res.add("\t\tbot.mousePress(InputEvent.BUTTON1_DOWN_MASK);\r\n");
            res.add("\t\tThread.sleep(500);\r\n");
            res.add("\t\tbot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX() - " + this.scrollXStep + "),(int)(MouseInfo.getPointerInfo().getLocation().getY() - " + this.scrollYStep + "));\r\n"); //from bottom to top
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
        //todo classname
        res.add("f.write(\";\"+interactions+\";f\\n\");");
        //todo int#
        res.add("\treturn \"fail;\"+interactions+\";4\";");
        res.add("}");

        /*res.add("image = eye.loadImage(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
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
        res.add("}");*/
        return res;
    }

    @Override
    public ArrayList<String> generateCombinedJavaLines(String starting_folder) {
        ArrayList<String> res = new ArrayList<>();
        int totToBeScrolled = (int) Math.sqrt((Math.pow(this.toBeScrolledY,2)) + Math.pow(this.toBeScrolledX,2));
        int totScrollStep = (int) Math.sqrt((Math.pow(this.scrollYStep,2)) + Math.pow(this.scrollXStep,2));
        if(totScrollStep<=0)
            return res;
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

        res.add("\t\t\tThread.sleep(200);");
        res.add("\t\t\tl = l.below("+this.pxUp+");");
        res.add("\t\t\tl = l.right("+this.pxLeft+");");

        for(int i = 0; i < (totToBeScrolled/totScrollStep); i++) {
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tThread.sleep(500);");
            res.add("\t\t\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\t\t\tThread.sleep(500);");
            res.add("\t\t\tl = l.above(" + this.scrollYStep + ");");
            res.add("\t\t\tl = l.left(" + this.scrollXStep + ");");
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tThread.sleep(1000);");
            res.add("\t\t\tsikuli_screen.mouseUp();");
            res.add("\t\t\tThread.sleep(500);");
            res.add("\t\t\tl = l.below(" + this.scrollYStep + ");");
            res.add("\t\t\tl = l.right(" + this.scrollXStep + ");");
            res.add("\t\t\tThread.sleep(500);");
        }
        if(last > 0){
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tThread.sleep(500);");
            res.add("\t\t\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\t\t\tThread.sleep(500);");
            res.add("\t\t\tl = l.above("+(int)(this.scrollYStep*ratio)+");");
            res.add("\t\t\tl = l.left("+(int)(this.scrollXStep*ratio)+");");
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tThread.sleep(1000);");
            res.add("\t\t\tsikuli_screen.mouseUp();");
            res.add("\t\t\tThread.sleep(500);");
        }
        res.add("\t\t\t}");
        res.add("\t\tcatch (FindFailed ffe) {");
        res.add("\t\t\tffe.printStackTrace();");
        //todo classname
        res.add("\t\t\tf.write(\";\"+interactions+\";f\\n\");");
        //todo int#
        res.add("\t\t\treturn \"fail;\" + eyeautomate_failures + \";\" + interactions+\";4\";");
        res.add("\t\t}");
        res.add("\t}");

        //eyeautomate
        res.add("\telse {");
        res.add("\t\teye.move(match.getCenterLocation());\r\n");
        res.add("\t\tThread.sleep(500);\r\n");

        res.add("\t\tbot.mouseMove((int)(match.getCenterLocation().getX() + " + this.pxLeft +"),(int)(match.getCenterLocation().getY() + " + this.pxUp +"));\r\n");

        res.add("\t\tThread.sleep(500);\r\n");
        for (int i = 0 ; i < (totToBeScrolled/totScrollStep); i++){
            res.add("\t\tbot.mousePress(InputEvent.BUTTON1_DOWN_MASK);\r\n");
            res.add("\t\tThread.sleep(500);\r\n");
            res.add("\t\tbot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX() - " + this.scrollXStep +"),(int)(MouseInfo.getPointerInfo().getLocation().getY() - " + this.scrollYStep +"));\r\n"); //from bottom to top
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
        //todo classname
        res.add("\t\t\tf.write(\";\"+interactions+\";f\\n\");");
        //todo int#
        res.add("\t\t\treturn \"fail;\" + eyeautomate_failures + \";\" + interactions+\";4+\";");
        res.add("}");

        /*res.add("image = eye.loadImage(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
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
        res.add("}");*/

        return res;
    }

    @Override
    public ArrayList<String> generateCombinedJavaLinesSikuliFirst(String starting_folder) {
        ArrayList<String> res = new ArrayList<>();
        int totToBeScrolled = (int) Math.sqrt((Math.pow(this.toBeScrolledY,2)) + Math.pow(this.toBeScrolledX,2));
        int totScrollStep = (int) Math.sqrt((Math.pow(this.scrollYStep,2)) + Math.pow(this.scrollXStep,2));
        if(totScrollStep<=0)
            return res;
        int last = totToBeScrolled%totScrollStep;
        double ratio = (double)last/(double)totScrollStep;

        res.add("\ttry {");
        res.add("\t\tsikuli_screen.wait(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\", 5);");
        res.add("\t\torg.sikuli.script.Match sikuli_match = sikuli_screen.find(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\",  "\\\\") + "\");");
        res.add("\t\torg.sikuli.script.Location l = sikuli_match.getCenter();");

        res.add("\t\tThread.sleep(500);");
        res.add("\t\tl = l.below("+this.pxUp+");");
        res.add("\t\tl = l.right("+this.pxLeft+");");
        for(int i = 0; i < (totToBeScrolled/totScrollStep); i++) {
            res.add("\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\tThread.sleep(500);");
            res.add("\t\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\t\tThread.sleep(500);");
            res.add("\t\tl = l.above(" + this.scrollYStep + ");");
            res.add("\t\tl = l.left(" + this.scrollXStep + ");");
            res.add("\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\tThread.sleep(1000);");
            res.add("\t\tsikuli_screen.mouseUp();");
            res.add("\t\tThread.sleep(500);");
            res.add("\t\tl = l.below(" + this.scrollYStep + ");");
            res.add("\t\tl = l.right(" + this.scrollXStep + ");");
            res.add("\t\tThread.sleep(500);");
        }
        if(last > 0){
            res.add("\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\tThread.sleep(500);");
            res.add("\t\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\t\tThread.sleep(500);");
            int tmpY = 0,tmpX = 0;
            if((int)(this.scrollYStep*ratio) > 0){
                tmpY = (int)(this.scrollYStep*ratio + 10);
            }
            if((int)(this.scrollXStep*ratio) > 0){
                tmpX =(int)(this.scrollXStep*ratio + 10);
            }
            res.add("\t\tl = l.above(" + tmpY + ");");
            res.add("\t\tl = l.left(" + tmpX + ");");
            res.add("\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\tThread.sleep(1000);");
            res.add("\t\tsikuli_screen.mouseUp();");
            res.add("\t\tThread.sleep(500);");
        }
        res.add("\t}");
        res.add("catch (FindFailed ffe) {");
        res.add("\tsikuli_failures++;");
        res.add("\timage = eye.loadImage(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
        res.add("\tif (image != null) {");
        res.add("\t\tmatch = eye.findImage(image);");
        res.add("\t\tif (match == null) {");		//test failed also with eyeautomate
        //todo classname
        res.add("\t\t\tf.write(\";\"+interactions+\";f\\n\");");
        //todo int#
        res.add("\t\t\treturn \"fail;\" + sikuli_failures + \";\" + interactions+\";4\";");
        res.add("\t\t}");
        res.add("\t\telse {");						//test ok with eyeautomate
        res.add("\t\t\teye.move(match.getCenterLocation());\r\n");
        res.add("\t\t\tThread.sleep(500);\r\n");
        res.add("\t\t\tbot.mouseMove((int)(match.getCenterLocation().getX() + " + this.pxLeft +"),(int)(match.getCenterLocation().getY() + " + this.pxUp +"));\r\n");
        res.add("\t\t\tThread.sleep(500);\r\n");
        for (int i = 0 ; i < (totToBeScrolled/totScrollStep); i++){
            res.add("\t\t\tbot.mousePress(InputEvent.BUTTON1_DOWN_MASK);\r\n");
            res.add("\t\t\tThread.sleep(500);\r\n");
            res.add("\t\t\tbot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX() - " + this.scrollXStep + "),(int)(MouseInfo.getPointerInfo().getLocation().getY() - " + this.scrollYStep + "));\r\n"); //from bottom to top
            res.add("\t\t\tThread.sleep(1000);\r\n");
            res.add("\t\t\tbot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);");
            res.add("\t\t\tThread.sleep(500);\r\n");
            res.add("\t\t\tbot.mouseMove((int)(MouseInfo.getPointerInfo().getLocation().getX() + " + this.scrollXStep +"),(int)(MouseInfo.getPointerInfo().getLocation().getY() + " + this.scrollYStep + "));\r\n");
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
        //todo classname
        res.add("\t\t\tf.write(\";\"+interactions+\";f\\n\");");
        //todo int#
        res.add("\t\t\treturn \"fail;\" + sikuli_failures + \";\" + interactions+\";4\";");
        res.add("\t}");
        res.add("}");

        /*res.add("try {");
        res.add("\tsikuli_screen.wait(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\", 5);");
        res.add("\torg.sikuli.script.Match sikuli_match = sikuli_screen.find(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\",  "\\\\") + "\");");
        res.add("\torg.sikuli.script.Location l = sikuli_match.getCenter();");

        res.add("\t\t\tThread.sleep(500);");
        res.add("\tl = l.below("+this.scrollYStep+");");
        res.add("\tl = l.right("+this.scrollXStep+");");
        for(int i = 0; i < (totToBeScrolled/totScrollStep); i++) {
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tThread.sleep(500);");
            res.add("\t\t\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\t\t\tThread.sleep(500);");
            res.add("\t\t\tl = l.above(" + this.scrollYStep + ");");
            res.add("\t\t\tl = l.left(" + this.scrollXStep + ");");
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tThread.sleep(1000);");
            res.add("\t\t\tsikuli_screen.mouseUp();");
            res.add("\t\t\tThread.sleep(500);");
            res.add("\t\t\tl = l.below(" + this.scrollYStep + ");");
            res.add("\t\t\tl = l.right(" + this.scrollXStep + ");");
            res.add("\t\t\tThread.sleep(500);");
        }
        if(last > 0){
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tThread.sleep(500);");
            res.add("\t\t\tsikuli_screen.mouseDown(Button.LEFT);");
            res.add("\t\t\tThread.sleep(500);");
            res.add("\t\t\tl = l.above(" + (int)(this.scrollYStep*ratio) + ");");
            res.add("\t\t\tl = l.left(" + (int)(this.scrollXStep*ratio) + ");");
            res.add("\t\t\tsikuli_screen.mouseMove(l);");
            res.add("\t\t\tThread.sleep(1000);");
            res.add("\t\t\tsikuli_screen.mouseUp();");
            res.add("\t\t\tThread.sleep(500);");
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
        res.add("}");*/
        return res;
    }
}
