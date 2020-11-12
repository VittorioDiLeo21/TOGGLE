package it.polito.toggle;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;

public class Utils {
    public static final String java_project_file = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<projectDescription>\r\n" +
            "	<name>JavaTranslatedProject</name>\r\n" +
            "	<comment></comment>\r\n" +
            "	<projects>\r\n" +
            "	</projects>\r\n" +
            "	<buildSpec>\r\n" +
            "		<buildCommand>\r\n" +
            "			<name>org.eclipse.jdt.core.javabuilder</name>\r\n" +
            "			<arguments>\r\n" +
            "			</arguments>\r\n" +
            "		</buildCommand>\r\n" +
            "	</buildSpec>\r\n" +
            "	<natures>\r\n" +
            "		<nature>org.eclipse.jdt.core.javanature</nature>\r\n" +
            "	</natures>\r\n" +
            "</projectDescription>\r\n";


    public static final String java_project_classpath = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<classpath>\r\n" +
            "	<classpathentry kind=\"src\" path=\"src\"/>\r\n" +
            "	<classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER\"/>\r\n" +
            "	<classpathentry kind=\"lib\" path=\"libs/eye2.jar\"/>\r\n" +
            "	<classpathentry kind=\"lib\" path=\"libs/EyeAutomate.jar\"/>\r\n" +
            "	<classpathentry kind=\"lib\" path=\"libs/sikulixapi.jar\"/>\r\n" +
            "	<classpathentry kind=\"output\" path=\"bin\"/>\r\n" +
            "</classpath>\r\n";

    public static void copyFile(File source, File dest) throws IOException {
        Files.deleteIfExists(dest.toPath());
        Files.copy(source.toPath(), dest.toPath());
    }

    public static boolean deleteDir(String path) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //todo
            assert children != null;
            for (String child : children) {
                boolean success = deleteDir(path + "\\" + child);
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }


    public static void deleteDirContents(String dirpath) {
        File dir = new File(dirpath);

        for(File file: Objects.requireNonNull(dir.listFiles())) {
            if (!file.isDirectory() && !file.getName().equals("logcat.txt"))
                file.delete();
        }
    }


    public static void createJavaProjectFolder(String starting_folder) throws IOException {
        //TODO aggiungere download delle librerie all'interno della cartella libs
        //TODO caricare le librerie sul server (ora come ora bisogna copiarle a manina)
        File dir = new File(starting_folder + "\\JavaTranslatedProject");
        dir.mkdirs();

        dir = new File(starting_folder + "\\JavaTranslatedProject\\src");
        dir.mkdirs();

        dir = new File(starting_folder + "\\JavaTranslatedProject\\libs");
        dir.mkdirs();


        File fout_project = new File(starting_folder + "\\JavaTranslatedProject\\.project");
        FileOutputStream fos = new FileOutputStream(fout_project);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(java_project_file);
        bw.close();

        File fout_classpath = new File(starting_folder + "\\JavaTranslatedProject\\.classpath");
        fos = new FileOutputStream(fout_classpath);
        bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(java_project_classpath);
        bw.close();
    }
}
