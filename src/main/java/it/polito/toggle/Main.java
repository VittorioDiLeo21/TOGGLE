package it.polito.toggle;

import it.polito.toggle.utils.Emulators;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        /*Toggle toggle = new Toggle(
                "androidTest",
                "C:\\Users\\vitto\\OneDrive\\Desktop\\toggleTests",
                "org.ligi.passandroid",
                "C:\\Users\\vitto\\AndroidStudioProjects\\PassAndroid\\app\\src\\androidTest\\java\\org\\ligi\\passandroid\\tests\\",
                Emulators.NEXUS_5);*/
        Toggle toggle = new Toggle(
                "androidTest",
                "C:\\Users\\vitto\\OneDrive\\Desktop\\onDataTests",
                "com.example.ondatatestapp",
                "C:\\Users\\vitto\\AndroidStudioProjects\\OnDataTestApp\\app\\src\\androidTest\\java\\com\\example\\ondatatestapp\\finalTest\\",
                Emulators.NEXUS_5);
        try {
            toggle.executeFullProcess();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
