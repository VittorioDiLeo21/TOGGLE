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

	public static void copyToggleTools(String toggleToolsDestPath, String toggleToolsSourcePath, String testClassPackage) {

		try {

			String toggleToolsContent=testClassPackage+"\n";

			try(BufferedReader br = new BufferedReader(new FileReader(toggleToolsSourcePath+"\\TOGGLETools.java"))) {
			    StringBuilder sb = new StringBuilder();
			    String line = br.readLine();

			    while (line != null) {
			        sb.append(line);
			        sb.append(System.lineSeparator());
			        line = br.readLine();
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
