package it.polito.toggle.utils;

import java.io.*;
import java.util.Scanner;

public class ToggleToolFinder extends Thread {
	private static String toggleToolsPackage;

	public static void setToggleToolsPackage(String togglePackage) { toggleToolsPackage = togglePackage; }

	public static boolean findToggleTools(File testFolder) {
		File[] list = testFolder.listFiles();
		if(list!=null) {
			for (File fil : list) {
				if (! fil.isDirectory() && fil.getName().endsWith(".java")) {
					//System.out.println("File: "+fil.getName());
					if (fil.getName().matches("(.*)TOGGLETools(.*)")) {
						try {
							Scanner scanner = new Scanner(fil);
							while (scanner.hasNextLine()) {
								String line = scanner.nextLine();
								if (line.matches("(.*)package(.*)") && !line.matches("(.*)//(.*)")) {

									//System.out.println(line);
									toggleToolsPackage = line.split("package")[1].trim().replace(";", "");
									break;
								}

							}
							scanner.close();

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void copyToggleTools(String toggleToolsDestPath, String testClassPackage) {

		try {

			String toggleToolsContent="package "+testClassPackage+";\n";

			try(BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\src\\main\\java\\it\\polito\\toggle\\utils\\TOGGLETools.java"))) {
			    StringBuilder sb = new StringBuilder();
			    String line;


			    while ((line = br.readLine()) != null) {
					if(line.contains("package")) {
						continue;
					}
			        sb.append(line);
			        sb.append(System.lineSeparator());
			    }
			    toggleToolsContent += sb.toString();
			}

			FileWriter toggleTools=new FileWriter(toggleToolsDestPath+"\\TOGGLETools.java");
			toggleTools.write(toggleToolsContent);
			toggleTools.close();
			//Files.copy(Paths.get(toggleToolsSourcePath+"\\TOGGLETools.java"), Paths.get(toggleToolsDestPath+"\\TOGGLETools.java"), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
