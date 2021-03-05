package it.polito.toggle.ToggleInteractions;

import it.polito.toggle.ToggleInteraction;
import it.polito.toggle.exceptions.ToggleException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TypeText extends ToggleInteraction {
	private String text;
	public TypeText(String packagename, String search_type, String search_keyword, String timestamp,
			String interaction_type, String args, File screen_capture, File dump)
			throws XPathExpressionException, SAXException, IOException, ParserConfigurationException, ToggleException {
		super(packagename, search_type, search_keyword, timestamp, interaction_type, args, screen_capture, dump);
		text = args;
	}


	public ArrayList<String> generateSikuliLines() {
		ArrayList<String> res = new ArrayList<>();
		res.add("wait (\"" + timestamp + "_cropped.png\", 30)");
		res.add("click(\"" + timestamp + "_cropped.png\")");
		res.add("type(\"" + text + "\")");
		return res;
	}


	public ArrayList<String> generateEyeStudioLines() {
		ArrayList<String> res = new ArrayList<>();
		res.add("Check \"{ImageFolder}\\" + timestamp + "_cropped.png\"");
		res.add("Click \"{ImageFolder}\\" + timestamp + "_cropped.png\"");
		res.add("Type \"" + text + "\"");
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
		res.add("\tf.write(testName+\";\"+interactions+\";f\\n\");");
		res.add("\treturn \"fail;\"+interactions+\";\"+totSize;");
		res.add("\t}");
		res.add("\teye.click(match.getCenterLocation());");
		res.add("\teye.type(\"" + text + "\");");
		res.add("}");
		res.add("else {");
		res.add("\tSystem.out.println(\"image not found\");");
		res.add("\tf.write(testName+\";\"+interactions+\";f\\n\");");
		res.add("\treturn \"fail;\"+interactions+\";\"+totSize;");
		res.add("}");
		return res;
	}

	@Override
	public ArrayList<String> generateSikuliJavaLines(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();
		res.add("try {");
		res.add("\tsikuli_screen.wait(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\", 30);");
		res.add("\tsikuli_screen.click(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
		res.add("\tsikuli_screen.type(\"" + text + "\");");
		res.add("}");
		res.add("catch (FindFailed ffe) {");
		res.add("\tffe.printStackTrace();");
		res.add("\tf.write(testName+\";\"+interactions+\";f\\n\");");
		res.add("\treturn \"fail;\"+interactions+\";\"+totSize;");
		res.add("}");
		return res;
	}


	@Override
	public ArrayList<String> generateCombinedJavaLines(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();
		res.add("image = eye.loadImage(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
		res.add("if (image != null) {");
		res.add("\tmatch = eye.findImage(image);");
		//try sikuli
		res.add("\tif (match == null) {");
		res.add("\t\teyeautomate_failures++;");
		res.add("\t\ttry {");
		res.add("\t\t\tsikuli_screen.wait(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\", 25);");
		res.add("\t\t\tsikuli_screen.click(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
		res.add("\t\t\tsikuli_screen.type(\"" + text + "\");");
		res.add("\t\t}");
		res.add("\t\tcatch (FindFailed ffe) {");
		res.add("\t\t\tffe.printStackTrace();");
		res.add("\t\t\tf.write(testName+\";\"+interactions+\";f\\n\");");
		res.add("\t\t\treturn \"fail;\" + eyeautomate_failures + \";\" + interactions+\";\"+totSize;");
		res.add("\t\t}");
		res.add("\t}");

		//eyeautomate
		res.add("\telse {");
		res.add("\t\teye.click(match.getCenterLocation());");
		res.add("\t\teye.type(\"" + text + "\");");
		res.add("\t}");
		res.add("}");
		res.add("else {");
		res.add("\tSystem.out.println(\"image not found\");");
		res.add("\t\t\tf.write(testName+\";\"+interactions+\";f\\n\");");
		res.add("\t\t\treturn \"fail;\" + eyeautomate_failures + \";\" + interactions+\";\"+totSize;");
		res.add("}");
		return res;
	}


	@Override
	public ArrayList<String> generateCombinedJavaLinesSikuliFirst(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();
		res.add("try {");
		res.add("\tsikuli_screen.wait(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\", 5);");
		res.add("\tsikuli_screen.click(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
		res.add("\tsikuli_screen.type(\"" + text + "\");");
		res.add("}");
		res.add("catch (FindFailed ffe) {");
		res.add("\tsikuli_failures++;");
		res.add("\timage = eye.loadImage(\"" + new String(starting_folder + "\\" + timestamp + "_cropped.png").replace("\\", "\\\\") + "\");");
		res.add("\tif (image != null) {");
		res.add("\t\tmatch = eye.findImage(image);");
		res.add("\t\tif (match == null) {");		//test failed also with eyeautomate
		res.add("\t\t\tf.write(testName+\";\"+interactions+\";f\\n\");");
		res.add("\t\t\treturn \"fail;\" + sikuli_failures + \";\" + interactions+\";\"+totSize;");
		res.add("\t\t}");
		res.add("\t\telse {");						//test ok with eyeautomate
		res.add("\t\t\teye.click(match.getCenterLocation());");
		res.add("\t\t\teye.type(\"" + text + "\");");
		res.add("\t\t}");
		res.add("\t}");
		res.add("\telse {");
		res.add("\t\tSystem.out.println(\"image not found\");");
		res.add("\t\t\tf.write(testName+\";\"+interactions+\";f\\n\");");
		res.add("\t\t\treturn \"fail;\" + sikuli_failures + \";\" + interactions+\";\"+totSize;");
		res.add("\t}");
		res.add("}");
		return res;
	}
}
