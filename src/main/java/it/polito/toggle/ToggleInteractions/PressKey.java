package it.polito.toggle.ToggleInteractions;

import it.polito.toggle.TextManipulationTools;
import it.polito.toggle.ToggleInteraction;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PressKey extends ToggleInteraction {
	int keycode;

	public PressKey(String packagename, String search_type, String search_keyword, String timestamp,
                    String interaction_type, String args, File screen_capture, File dump)
			throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		super(packagename, search_type, search_keyword, timestamp, interaction_type, args, screen_capture, dump);

		this.need_screenshot = false;
		keycode = Integer.parseInt(args);
	}

	public ArrayList<String> generateSikuliLines() {
		ArrayList<String> res = new ArrayList<>();
		res.add(TextManipulationTools.translateKeyCodeToSikuli(args));
		return res;
	}


	public ArrayList<String> generateEyeStudioLines() {
		ArrayList<String> res = new ArrayList<>();
		res.add(TextManipulationTools.translateKeyCodeToEyeAutomate(args));
		return res;
	}

	@Override
	public void extractBounds() { }

	@Override
	public ArrayList<String> generateEyeAutomateJavaLines(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();
		res.add(TextManipulationTools.translateKeyCodeToEyeAutomateJava(args));
		return res;
	}

	@Override
	public ArrayList<String> generateSikuliJavaLines(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();
		res.add(TextManipulationTools.translateKeyCodeToSikuliJava(args));
		return res;
	}

	@Override
	public ArrayList<String> generateCombinedJavaLines(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();
		//IS IT POSSIBLE TO HAVE EXCEPTIONS IN THIS SIMPLE OPERATIONS WITH EYEAUTOMATE??? CHECK

		res.add("try {");
		res.add(TextManipulationTools.translateKeyCodeToEyeAutomateJava(args));
		res.add("}");
		res.add("catch (Exception e) {");
		res.add("\teyeautomate_failures++;");
		res.add("\ttry {");
		res.add("\t" + TextManipulationTools.translateKeyCodeToSikuliJava(args));
		res.add("\t}");
		res.add("\tcatch (Exception e2) {");
		res.add("\t\tSystem.out.println(\"catched exception with Sikuli\");");
		res.add("\t\treturn \"fail;\" + eyeautomate_failures + \";\" + interactions;");
		res.add("\t}");
		res.add("}");
		return res;
	}

	@Override
	public ArrayList<String> generateCombinedJavaLinesSikuliFirst(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();

		res.add("try {");
		res.add(TextManipulationTools.translateKeyCodeToSikuliJava(args));
		res.add("}");
		res.add("catch (Exception e) {");
		res.add("\tsikuli_failures++;");
		res.add("\tSystem.out.println(\"Exception with Sikuli\");");
		res.add("\ttry {");
		res.add("\t" + TextManipulationTools.translateKeyCodeToEyeAutomateJava(args));
		res.add("\t}");
		res.add("\tcatch (Exception e2) {");
		res.add("\t\tSystem.out.println(\"Exception with Eyeautomate\");");
		res.add("\t\treturn \"fail;\" + sikuli_failures + \";\" + interactions;");
		res.add("\t}");
		res.add("}");
		return res;
	}
}
