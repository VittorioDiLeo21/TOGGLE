package it.polito.toggle.ToggleInteractions;

import it.polito.toggle.ToggleInteraction;
import it.polito.toggle.exceptions.ToggleException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TypeIntoFocused extends ToggleInteraction {
	private String text;
	public TypeIntoFocused(String packagename, String search_type, String search_keyword, String timestamp,
			String interaction_type, String args, File screen_capture, File dump)
			throws XPathExpressionException, SAXException, IOException, ParserConfigurationException, ToggleException {
		super(packagename, search_type, search_keyword, timestamp, interaction_type, args, screen_capture, dump);
		this.need_screenshot = false;
		text = args;
	}

	@Override
	public ArrayList<String> generateSikuliLines() {
		ArrayList<String> res = new ArrayList<>();
		res.add("type(\"" + text + "\")");
		return res;
	}

	@Override
	public ArrayList<String> generateEyeStudioLines() {
		ArrayList<String> res = new ArrayList<>();
		res.add("Type \"" + text + "\"");
		return res;
	}


	@Override
	public void extractBounds() {
		return;
	}


	@Override
	public ArrayList<String> generateEyeAutomateJavaLines(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();
		res.add("eye.type(\"" + text + "\");");
		return res;
	}

	@Override
	public ArrayList<String> generateSikuliJavaLines(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();
		res.add("sikuli_screen.type(\"" + text + "\");");
		return res;
	}

	@Override
	public ArrayList<String> generateCombinedJavaLines(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();
		res.add("interactionName = \"TypeIntoFocused\";");
		//IS IT POSSIBLE TO HAVE EXCEPTIONS IN THIS SIMPLE OPERATIONS WITH EYEAUTOMATE??? CHECK
		res.add("try {");
		res.add("\teye.type(\"" + text + "\");");
		res.add("}");
		res.add("catch (Exception e) {");
		res.add("\teyeautomate_failures++;");
		res.add("\tSystem.out.println(\"Exception with EyeAutomate\");");
		res.add("\ttry {");
		res.add("\t\tsikuli_screen.type(\"" + text + "\");");
		res.add("\t}");
		res.add("\tcatch (Exception e2) {");
		res.add("\t\tSystem.out.println(\"Exception with Sikuli\");");
		res.add("\tf.write(testName+\";\"+interactions+\";f;\"+interactionName+\"\\n\");");
		res.add("\treturn \"fail;\" + eyeautomate_failures + \";\" + interactions+\";\"+totSize;");
		res.add("\t}");
		res.add("}");
		return res;
	}

	@Override
	public ArrayList<String> generateCombinedJavaLinesSikuliFirst(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();
		res.add("interactionName = \"TypeIntoFocused\";");
		res.add("try {");
		res.add("\tsikuli_screen.type(\"" + text + "\");");
		res.add("}");
		res.add("catch (Exception e) {");
		res.add("\tsikuli_failures++;");
		res.add("\tSystem.out.println(\"Exception with Sikuli\");");
		res.add("\ttry {");
		res.add("\t\teye.type(\"" + text + "\");");
		res.add("\t}");
		res.add("\tcatch (Exception e2) {");
		res.add("\t\tSystem.out.println(\"Exception with Eyeautomate\");");
		res.add("\t\t\tf.write(testName+\";\"+interactions+\";f;\"+interactionName+\"\\n\");");
		res.add("\t\t\treturn \"fail;\" + sikuli_failures + \";\" + interactions+\";\"+totSize;");
		res.add("\t}");
		res.add("}");

		return res;
	}
}
