package it.polito.toggle;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

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

        for (File file : Objects.requireNonNull(dir.listFiles())) {
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

        dir = new File(starting_folder + "\\JavaTranslatedProject\\src\\Utils");
        dir.mkdirs();
        File tmp = new File(dir.toPath().toString()+"\\AppStarter.java");
        if(!tmp.exists()){
            /*System.out.println(tmp.getAbsolutePath());
            tmp.createNewFile();
            copyAppStarter(new File(System.getProperty("user.dir") + "\\src\\main\\java\\it\\polito\\toggle\\utils\\AppStarter.java"),tmp);*/
            Files.copy(new File(System.getProperty("user.dir") + "\\src\\main\\java\\it\\polito\\toggle\\utils\\AppStarter.java").toPath(),
                    tmp.toPath());
        }
        /*Files.copy(new File(System.getProperty("user.dir") + "\\src\\main\\java\\it\\polito\\toggle\\utils\\AppStarter.java").toPath(),
                dir.toPath().toString()+"\\AppStarter.java",
                StandardCopyOption.REPLACE_EXISTING);*/

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

        File lib = new File(dir.toPath().toString()+"\\eye2.jar");
        if(!lib.exists())
            copyJarFile(new JarFile(new File(System.getProperty("user.dir") + "\\src\\main\\java\\it\\polito\\toggle\\utils\\eye2.jar")), dir);

        lib = new File(dir.toPath().toString()+"\\EyeAutomate.jar");
        if(!lib.exists())
            copyJarFile(new JarFile(new File(System.getProperty("user.dir") + "\\src\\main\\java\\it\\polito\\toggle\\utils\\EyeAutomate.jar")), dir);
        lib = new File(dir.toPath().toString()+"\\sikulixapi.jar");
        if(!lib.exists())
            copyJarFile(new JarFile(new File(System.getProperty("user.dir") + "\\src\\main\\java\\it\\polito\\toggle\\utils\\sikulixapi.jar")), dir);
    }

    private static void copyAppStarter(File src, File dst) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(dst); // buffer size 1K
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buf)) > 0) {
                os.write(buf, 0, bytesRead);
            }
        } finally {
            if(is!= null && os!=null) {
                is.close();
                os.close();
            }
        }
    }

    private static void copyJarFile(JarFile jar, File dst) throws IOException {
        String fileName = jar.getName();
        String fileNameLastPart = fileName.substring(fileName.lastIndexOf(File.separator));
        File destFile = new File(dst, fileNameLastPart);

        JarOutputStream jos = new JarOutputStream(new FileOutputStream(destFile));
        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            InputStream is = jar.getInputStream(entry);

            //jos.putNextEntry(entry);
            //create a new entry to avoid ZipException: invalid entry compressed size
            jos.putNextEntry(new JarEntry(entry.getName()));
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = is.read(buffer)) != -1) {
                jos.write(buffer, 0, bytesRead);
            }
            is.close();
            jos.flush();
            jos.closeEntry();
        }
        jos.close();
    }

    public static void writeJavaProjectMain(String dst, ArrayList<String> classes) {
        try {
            StringBuilder content = new StringBuilder();
            content.append("import java.io.IOException;");
            content.append("\n");
            content.append("import java.io.File;");
            content.append("\n");
            content.append("import java.io.FileWriter;");
            content.append("\n\n");
            content.append("public class Main {");
            content.append("\n\n");
            content.append("private static FileWriter interactionsLog;");
            content.append("\n\n");
            content.append("private static FileWriter testLog;");
            content.append("\n\n");
            content.append("\tpublic static void main(String[] args) {");
            content.append("\n\n");
            content.append("\t\ttry {");
            content.append("\n");
            content.append("\t\t\tFile f = new File(System.getProperty(\"user.dir\")+\"\\\\interactions.txt\");");
            content.append("\n");
            content.append("\t\t\tif(!f.exists()) {");
            content.append("\n");
            content.append("\t\t\t\tf.createNewFile();");
            content.append("\n");
            content.append("\t\t\t}");
            content.append("\n");
            content.append("\t\t\tinteractionsLog = new FileWriter(f);");
            content.append("\n");
            content.append("\t\t\tf = new File(System.getProperty(\"user.dir\")+\"\\\\tests.txt\");");
            content.append("\n");
            content.append("\t\t\tif(!f.exists()) {");
            content.append("\n");
            content.append("\t\t\t\tf.createNewFile();");
            content.append("\n");
            content.append("\t\t\t}");
            content.append("\n");
            content.append("\t\t\ttestLog = new FileWriter(f);");
            content.append("\n");
            content.append("\t\t\tfor(int i = 0; i < 10; i++) {");
            content.append("\n");
            for (String className : classes) {
                content.append("\t\t\t\t").append(className).append(".run(interactionsLog,testLog);");
                content.append("\n");
            }
            content.append("\t\t\t}");
            content.append("\n");
            content.append("\t\t\tinteractionsLog.close();");
            content.append("\n");
            content.append("\t\t\ttestLog.close();");
            content.append("\n");
            content.append("\t\t} catch (InterruptedException | IOException e) {");
            content.append("\n\n");
            content.append("\t\t\te.printStackTrace();");
            content.append("\n");
            content.append("\t\t}");
            content.append("\n\n");
            content.append("\t}");
            content.append("\n\n");
            content.append("}");
            content.append("\n");
            FileWriter mainFile = new FileWriter(dst);
            mainFile.write(content.toString());
            mainFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
