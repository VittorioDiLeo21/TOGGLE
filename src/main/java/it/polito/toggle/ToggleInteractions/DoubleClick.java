package it.polito.toggle.ToggleInteractions;

import it.polito.toggle.ToggleInteraction;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class DoubleClick extends ToggleInteraction {

	public DoubleClick(String packagename, String search_type, String search_keyword, String timestamp,
			String interaction_type, String args, File screen_capture, File dump)
			throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		super(packagename, search_type, search_keyword, timestamp, interaction_type, args, screen_capture, dump);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<String> generateSikuliLines() {
		ArrayList<String> res = new ArrayList<>();
		res.add("wait(\"" + timestamp + "_cropped.png\", 30)");
		res.add("hover(\"" + timestamp + "_cropped.png\")");
		res.add("mouseDown(Button.LEFT)");
		res.add("sleep(0.001)");
		res.add("mouseUp(Button.LEFT)");
		res.add("sleep(0.001)");
		res.add("mouseDown(Button.LEFT)");
		res.add("sleep(0.001)");
		res.add("mouseUp(Button.LEFT)");
		return res;
	}

	@Override
	public ArrayList<String> generateEyeStudioLines() {
		ArrayList<String> res = new ArrayList<>();
		res.add("Check \"{ImageFolder}\\" + timestamp + "_cropped.png\"");
		res.add("Move \"{ImageFolder}\\" + timestamp + "_cropped.png\"");
		res.add("MouseDoubleClick");
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
		res.add("\teye.doubleClick(match.getCenterLocation());");
		res.add("}");
		res.add("else {");
		res.add("\tSystem.out.println(\"image not found\");");
		res.add("\treturn \"fail;\"+interactions;");
		res.add("}");
		return res;
	}

	@Override
	public ArrayList<String> generateSikuliJavaLines(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();
		res.add("try {");
		res.add("\tsikuli_screen.wait(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\", 30);");
		res.add("\tsikuli_screen.mouseMove(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
		res.add("\tsikuli_screen.mouseDown(Button.LEFT);");
		res.add("\tsleep(0.001);");
		res.add("\tsikuli_screen.mouseUp(Button.LEFT);");
		res.add("\tsikuli_screen.mouseDown(Button.LEFT);");
		res.add("\tsleep(0.001);");
		res.add("\tsikuli_screen.mouseUp(Button.LEFT);");
		res.add("}");
		res.add("catch (FindFailed ffe) {");
		res.add("\tffe.printStackTrace();");
		res.add("\treturn \"fail;\"+interactions;");
		res.add("}");

		return res;
	}

	@Override
	public ArrayList<String> generateCombinedJavaLines(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();
		res.add("image = eye.loadImage(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
		res.add("if (image != null) {");
		res.add("\tmatch = eye.findImage(image);");

		//sikuli alternative
		res.add("\tif (match == null) {");
		res.add("\t\teyeautomate_failures++;");
		res.add("\t\ttry {");
		res.add("\t\t\tsikuli_screen.wait(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\", 25);");
		res.add("\t\t\tsikuli_screen.mouseMove(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
		res.add("\t\t\tsikuli_screen.mouseDown(Button.LEFT);");
		res.add("\t\t\tsleep(0.001);");
		res.add("\t\t\tsikuli_screen.mouseUp(Button.LEFT);");
		res.add("\t\t\tsikuli_screen.mouseDown(Button.LEFT);");
		res.add("\t\t\tsleep(0.001);");
		res.add("\t\t\tsikuli_screen.mouseUp(Button.LEFT);");
		res.add("\t\t}");
		res.add("\t\tcatch (FindFailed ffe) {");
		res.add("\t\t\tffe.printStackTrace();");
		res.add("\t\t\treturn \"fail;\" + eyeautomate_failures + \";\" + interactions;");
		res.add("\t\t}");
		res.add("\t}");

		//eyeautomate
		res.add("\telse {");
		res.add("\t\teye.doubleClick(match.getCenterLocation());");
		res.add("\t}");
		res.add("}");
		res.add("else {");
		res.add("\tSystem.out.println(\"image not found\");");
		res.add("\treturn \"fail;\" + eyeautomate_failures;");
		res.add("}");
		return res;
	}

	@Override
	public ArrayList<String> generateCombinedJavaLinesSikuliFirst(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();

		res.add("try {");
		res.add("\tsikuli_screen.wait(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\", 5);");
		res.add("\tsikuli_screen.mouseMove(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
		res.add("\tsikuli_screen.mouseDown(Button.LEFT);");
		res.add("\tsleep(0.001);");
		res.add("\tsikuli_screen.mouseUp(Button.LEFT);");
		res.add("\tsikuli_screen.mouseDown(Button.LEFT);");
		res.add("\tsleep(0.001);");
		res.add("\tsikuli_screen.mouseUp(Button.LEFT);");
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
		res.add("\t\t\teye.doubleClick(match.getCenterLocation());");
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
