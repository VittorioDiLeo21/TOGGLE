package it.polito.toggle.ToggleInteractions;

import it.polito.toggle.ToggleInteraction;
import it.polito.toggle.exceptions.ToggleException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PressBack extends ToggleInteraction {
	public PressBack(String packagename, String search_type, String search_keyword, String timestamp,
			String interaction_type, String args, File screen_capture, File dump)
			throws XPathExpressionException, SAXException, IOException, ParserConfigurationException, ToggleException {
		super(packagename, search_type, search_keyword, timestamp, interaction_type, args, screen_capture, dump);
		// TODO Auto-generated constructor stub
		this.need_screenshot = false;
	}

	public ArrayList<String> generateSikuliLines() {
		ArrayList<String> res = new ArrayList<>();
		res.add("keyDown(Key.CTRL)");
		res.add("sleep(0.01)");
		res.add("type(Key.BACKSPACE)");
		res.add("sleep(0.01)");
		res.add("keyUp(Key.CTRL)");

		return res;
	}

	public ArrayList<String> generateEyeStudioLines() {
		ArrayList<String> res = new ArrayList<>();
		res.add("Type [CTRL_PRESS]");
		res.add("Sleep 10");
		res.add("Type [BACKSPACE]");
		res.add("Sleep 10");
		res.add("Type [CTRL_RELEASE]");

		return res;
	}

	@Override
	public void extractBounds() {
		return;
	}

	@Override
	public ArrayList<String> generateEyeAutomateJavaLines(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();

		res.add("\teye.type(\"[CTRL_PRESS]\");");
		res.add("\tThread.sleep(10);");
		res.add("\teye.type(\"[BACKSPACE]\");");
		res.add("\tThread.sleep(10);");
		res.add("\teye.type(\"[CTRL_RELEASE]\");");

		return res;
	}

	@Override
	public ArrayList<String> generateSikuliJavaLines(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();
		res.add("\tsikuli_screen.keyDown(Key.CTRL);");
		res.add("\tThread.sleep(10);");
		res.add("\tsikuli_screen.type(Key.BACKSPACE);");
		res.add("\tThread.sleep(10);");
		res.add("\tsikuli_screen.keyUp(Key.CTRL);");

		return res;
	}


	public ArrayList<String> generateCombinedJavaLines(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();
		//IS IT POSSIBLE TO HAVE EXCEPTIONS IN THIS SIMPLE OPERATIONS WITH EYEAUTOMATE??? CHECK
		res.add("try {");
		res.add("\teye.type(\"[CTRL_PRESS]\");");
		res.add("\tThread.sleep(10);");
		res.add("\teye.type(\"[BACKSPACE]\");");
		res.add("\tThread.sleep(10);");
		res.add("\teye.type(\"[CTRL_RELEASE]\");");
		res.add("}");
		res.add("catch (Exception e) {");
		res.add("\teyeautomate_failures++;");
		res.add("\tSystem.out.println(\"catched exception with EyeAutomate\");");
		res.add("\ttry{");
		res.add("\t\tsikuli_screen.keyDown(Key.CTRL);");
		res.add("\t\tThread.sleep(10);");
		res.add("\t\tsikuli_screen.type(Key.BACKSPACE);");
		res.add("\t\tThread.sleep(10);");
		res.add("\t\tsikuli_screen.keyUp(Key.CTRL);");
		res.add("\t}");
		res.add("\tcatch (Exception e2) {");
		res.add("\t\tSystem.out.println(\"catched exception with Sikuli\");");
		res.add("\t\t\tf.write(testName+\";\"+interactions+\";f\\n\");");
		res.add("\t\t\treturn \"fail;\" + eyeautomate_failures + \";\" + interactions+\";\"+totSize;");
		res.add("\t}");
		res.add("}");
		return res;
	}

	@Override
	public ArrayList<String> generateCombinedJavaLinesSikuliFirst(String starting_folder) {
		ArrayList<String> res = new ArrayList<>();
		res.add("try {");
		res.add("\t\tsikuli_screen.keyDown(Key.CTRL);");
		res.add("\t\tThread.sleep(10);");
		res.add("\t\tsikuli_screen.type(Key.BACKSPACE);");
		res.add("\t\tThread.sleep(10);");
		res.add("\t\tsikuli_screen.keyUp(Key.CTRL);");
		res.add("}");
		res.add("catch (Exception e) {");
		res.add("\tsikuli_failures++;");
		res.add("\tSystem.out.println(\"Exception with Sikuli\");");
		res.add("\ttry {");
		res.add("\teye.type(\"[CTRL_PRESS]\");");
		res.add("\tThread.sleep(10);");
		res.add("\teye.type(\"[BACKSPACE]\");");
		res.add("\tThread.sleep(10);");
		res.add("\teye.type(\"[CTRL_RELEASE]\");");
		res.add("\t}");
		res.add("\tcatch (Exception e2) {");
		res.add("\t\tSystem.out.println(\"Exception with Eyeautomate\");");
		res.add("\t\tf.write(testName+\";\"+interactions+\";f\\n\");");
		res.add("\t\treturn \"fail;\" + sikuli_failures + \";\" + interactions+\";\"+totSize;");
		res.add("\t}");
		res.add("}");

		return res;
	}
}
