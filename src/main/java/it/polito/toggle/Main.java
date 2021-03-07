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

        /*Toggle toggle = new Toggle(
                "androidTest",
                "C:\\Users\\vitto\\OneDrive\\Desktop\\onDataTests",
                "com.example.ondatatestapp",
                "C:\\Users\\vitto\\AndroidStudioProjects\\OnDataTestApp\\app\\src\\androidTest\\java\\com\\example\\ondatatestapp\\finalTest\\",
                Emulators.NEXUS_5);*/

        /* COMPLETO!*/
        /*Toggle toggle = new Toggle(
                "androidTest",
                "C:\\Users\\vitto\\OneDrive\\Desktop\\BudgetWatch",
                "protect.budgetwatch",
                "D:\\AndroidStudioProject\\bw\\budget-watch-master\\app\\src\\androidTest\\java\\protect\\budgetwatch\\",
                Emulators.NEXUS_5);*/

        /* COMPLETO!*/
        Toggle toggle = new Toggle(
                "androidTest",
                "C:\\Users\\vitto\\OneDrive\\Desktop\\Stoic",
                "app.reading.stoic.stoicreading",
                "D:\\AndroidStudioProject\\StoicReading-master\\app\\src\\androidTest\\java\\app\\reading\\stoic\\stoicreading\\toggle\\",
                Emulators.NEXUS_5);

        /* COMPLETO!*/
        /*Toggle toggle = new Toggle(
                "androidTest",
                "C:\\Users\\vitto\\OneDrive\\Desktop\\Images-to-PDF",
                "swati4star.createpdf",
                "D:\\AndroidStudioProject\\Images-to-PDF-master\\app\\src\\androidTest\\java\\swati4star\\createpdf\\",
                Emulators.NEXUS_5);*/

        /* COMPLETO!*/
        /*Toggle toggle = new Toggle(
                "androidTest",
                "C:\\Users\\vitto\\OneDrive\\Desktop\\ContactBook",
                "de.hskl.contacts",
                "D:\\AndroidStudioProject\\Contactbook-master\\app\\src\\androidTest\\java\\de\\hskl\\contacts\\toggle\\",
                Emulators.NEXUS_5);*/

        /* COMPLETO!*/
        /*Toggle toggle = new Toggle(
                "androidTest",
                "C:\\Users\\vitto\\OneDrive\\Desktop\\Calendar",
                "com.simplemobiletools.calendar.pro.debug",
                "D:\\AndroidStudioProject\\Simple-Calendar-master\\app\\src\\androidTest\\java\\com\\simplemobiletools\\calendar\\pro\\",
                Emulators.NEXUS_5);*/
        try {
            toggle.translateTestsWithMethodGranularity();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
