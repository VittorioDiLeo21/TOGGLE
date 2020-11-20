package it.polito.toggle;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        Toggle toggle = new Toggle(
                "androidTest",
                "C:\\Users\\vitto\\OneDrive\\Desktop\\toggleTests",
                "org.ligi.passandroid",
                "C:\\Users\\vitto\\AndroidStudioProjects\\PassAndroid\\app\\src\\androidTest\\java\\org\\ligi\\passandroid\\");
        try {
            toggle.executeFullProcess();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
