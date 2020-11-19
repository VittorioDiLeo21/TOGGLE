package it.polito.toggle.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EspressoTestFinder {
	public static List<String> getEspressoTests(File testFoler){
		File[] list = testFoler.listFiles();
		List<String> res = new ArrayList<>();
		if(list == null){
			return res;
		}
		for(File file : list){
			if(file.isDirectory())
				continue;
			if(file.getName().endsWith(".java")) {
				boolean containsEspressoImports = false;
				boolean isEnhanced = false;
				try {
					Scanner scanner = new Scanner(file);
					while (scanner.hasNextLine()) {
						String line = scanner.nextLine();
						if (line.matches("(.*)test.espresso(.*)")
								&& line.matches("(.*)import(.*)")
								&& !line.matches("(.*)//(.*)")) {
							containsEspressoImports = true;
						}

						if (line.matches("(.*)TOGGLETools(.*)")
								&& line.matches("(.*)import(.*)")
								&& !line.matches("(.*)//(.*)")) {
							isEnhanced = true;
						}
					}
					scanner.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (containsEspressoImports && !isEnhanced) {
					res.add(file.getPath());
					System.out.println(file.getPath());
				}
			}
		}
		return res;
	}
}
