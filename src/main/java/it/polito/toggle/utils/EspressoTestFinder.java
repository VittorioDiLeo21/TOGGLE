package it.polito.toggle.utils;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EspressoTestFinder {
	public static List<String> getEspressoTests(File testFoler){
		FileFilter filter = new WildcardFileFilter("*.java", IOCase.INSENSITIVE);
		File[] list = testFoler.listFiles(filter);
		List<String> res = new ArrayList<>();
		if(list == null){
			return res;
		}
		for(File file : list){
			//if(file.isDirectory())
				//continue;
			//if(file.getName().endsWith(".java")) {
				boolean containsEspressoImports = false;
				boolean containsOnView = false;
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

						if(line.matches("(.*)onView(.*)") &&
							!line.matches("(.*)import(.*)")){
							containsOnView = true;
						}

						if (line.matches("(.*)TOGGLETools(.*)")
								&& !line.matches("(.*)//(.*)")) {
							isEnhanced = true;
						}
					}
					scanner.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (containsEspressoImports && !isEnhanced && containsOnView) {
					String name = file.getName();
					int dotIndex = name.lastIndexOf('.');
					res.add(name.substring(0,dotIndex));
					System.out.println(file.getPath());
				}
			//}
		}
		return res;
	}
}
