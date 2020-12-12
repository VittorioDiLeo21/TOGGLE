package it.windowUtils;

import javax.naming.NameNotFoundException;

import com.sun.jna.Native;
import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;
import it.polito.toggle.utils.Emulators;

import java.awt.*;

public class WindowUtils {
    private Toolkit t = null;
    private double dotsPerInch = 0;
    private double dotsPerCm = 0;
    private int widthPxs;
    private int heightPxs;
    private double diagonalPxs;
    private double pxPerInch;
    private double pxPerCm;
    private double dotPerPx;
    private double pxPerDot;
    private double zoom;

    private double diagonalInInches = 15.513451; // the only thing needed from the user

    private static final int SWP_SHOWWINDOW = 0x0040;

    public WindowUtils(){
        this.t = Toolkit.getDefaultToolkit();
        this.zoom = getScaleFactor();
        this.widthPxs = (int) (t.getScreenSize().getWidth()*this.zoom);
        this.heightPxs = (int) (t.getScreenSize().getHeight()*this.zoom);
        this.diagonalPxs = Math.sqrt(this.widthPxs*this.widthPxs + this.heightPxs*this.heightPxs);
        this.dotsPerInch = t.getScreenResolution();
        this.dotsPerCm = dotsPerInch/2.54;
        this.pxPerInch = diagonalPxs/diagonalInInches;
        this.pxPerCm = pxPerInch/2.54;
        this.dotPerPx = dotsPerInch/pxPerInch;
        this.pxPerDot = pxPerInch/dotsPerInch;
    }

    public double getScaleFactor() {
        WinDef.HDC hdc = GDI32.INSTANCE.CreateCompatibleDC(null);
        if (hdc != null) {
            float actual = GDI32.INSTANCE.GetDeviceCaps(hdc, 10 /* VERTRES */);
            float logical = GDI32.INSTANCE.GetDeviceCaps(hdc, 117 /* DESKTOPVERTRES */);
            GDI32.INSTANCE.DeleteDC(hdc);
            // JDK11 seems to always return 1, use fallback below
            if (logical != 0 && logical/actual > 1) {
                return logical/actual;
            }
        }
        return /*(int)*/(Toolkit.getDefaultToolkit().getScreenResolution() / 96.0);
    }

    public int getEmulatorScreenPixelsWidth(Emulators DEVICE){
        int totalWidth;
        try {
            totalWidth = getWindowWidth("Android Emulator");
        } catch (NameNotFoundException | ResizeException e) {
            e.printStackTrace();
            return -1;
        }
        double ratio;
        switch (DEVICE){
            case NEXUS_5:
                //ratio = 0.6626315940449212;

                //ratio = 0.7964376590330788;
                ratio = 0.7947154471544715;
                break;
            case NEXUS_5X:
                //ratio = 0.6714876033057852;
                ratio = 0.7954545454545454;
                break;
            case NEXUS_6:
                //ratio = 0.6436567164179104;
                ratio = 0.7611940298507462;
                break;
            case NEXUS_6P:
                //ratio = 0.6646216768916156;
                ratio = 0.787321063394683;
                break;
            case NEXUS_4:
                //ratio = 0.6795131845841785;
                ratio = 0.8032454361054767;
                break;
            case NEXUS_S:
                //ratio = 0.5695970695970696;
                ratio = 0.6758241758241759;
                break;
            case NEXUS_ONE:
                //ratio = 0.5597014925373134;
                ratio = 0.6623134328358209;
                break;
            case GALAXY_NEXUS:
                //ratio = 0.5547576301615799;
                ratio = 0.6570915619389587;
                break;
            case PIXEL:
                //ratio = 0.6659528907922913;
                ratio = 0.6659528907922913;
                break;
            case PIXEL_3:
                //ratio = 0.7714987714987716;
                ratio = 0.9115479115479116;
                break;
            case PIXEL_XL:
                //ratio = 0.6597510373443983;
                ratio = 0.7821576763485477;
                break;
            default :
                ratio = -1;
        }
        if(ratio == -1)
            return -1;
        double screenPx = ratio*totalWidth;
        return (int) screenPx;
    }

    public int getEmulatorScreenPixelHeight(Emulators DEVICE){
        int totalHeight;
        try {
            totalHeight = getWindowHeight("Android Emulator");
        } catch (NameNotFoundException | ResizeException e) {
            e.printStackTrace();
            return -1;
        }
        double ratio;
        switch (DEVICE){
            case NEXUS_5:
                ratio = 0.8014492753623188;
                break;
            case NEXUS_5X:
                ratio = 0.7902665121668598;
                break;
            case NEXUS_6:
                ratio = 0.8402777777777778;
                break;
            case NEXUS_6P:
                ratio = 0.7916666666666666;
                break;
            case NEXUS_4:
                ratio = 0.7650462962962963;
                break;
            case NEXUS_S:
                ratio = 0.707514450867052;
                break;
            case NEXUS_ONE:
                ratio = 0.6825028968713789;
                break;
            case GALAXY_NEXUS:
                ratio = 0.7526011560693642;
                break;
            case PIXEL:
                ratio = 0.7592592592592593;
                break;
            case PIXEL_3:
                ratio = 0.8587962962962963;
                break;
            case PIXEL_XL:
                ratio = 0.7754629629629629;
                break;
            default :
                ratio = -1;
        }
        if(ratio == -1)
            return -1;
        double screenPx = ratio*totalHeight;
        return (int) screenPx;
    }

    public double getCmFromPixels(int pixels){
        return (double)pixels/pxPerCm;
    }

    public double getInFromPixels(int pixels){
        return (double)pixels/pxPerInch;
    }

    public int getPixelsFromCm(double cm){
        return (int) (cm*pxPerCm);
    }

    public int getPixelsFromIn(double in){
        return (int) (in*pxPerInch);
    }

    public double getRatioBetween(int px1,int px2){
        return (double)px1/(double)px2;
    }

    public int getWindowWidth(String windowName) throws NameNotFoundException, ResizeException {
        Rectangle rect = null;
        for (DesktopWindow desktopWindow : com.sun.jna.platform.WindowUtils.getAllWindows(true)) {
            if (desktopWindow.getTitle().contains(windowName)) {
                rect = desktopWindow.getLocAndSize();
                String device = desktopWindow.getTitle().split(" - ")[1];
                System.out.println(device + " : " + rect.width + " x " + rect.height + " " + rect.width + "-" + rect.height);
                /*if(rect.width<600){
                    resizeWindow(desktopWindow.getHWND(),rect.x,rect.y,600,1054,true);
                    return 600;
                }*/
                return rect.width;
            }
        }
        /*Rectangle rect = null;
        for (DesktopWindow desktopWindow : com.sun.jna.platform.WindowUtils.getAllWindows(true)) {
            if (desktopWindow.getTitle().contains(windowName)) {
                rect = desktopWindow.getLocAndSize();
                String device = desktopWindow.getTitle().split(" - ")[1];
                System.out.println(device + " : " + rect.width + " x " + rect.height + " " + rect.width + "-" + rect.height);
                /*if(rect.width<600){
                    resizeWindow(desktopWindow.getHWND(),rect.x,rect.y,600,1054,true);
                    return 600;
                }*/

                /*return (int) (rect.width/dotPerPx);
            }
        }*/
        return -1;
    }

    public int getWindowHeight(String windowName) throws NameNotFoundException, ResizeException {
        Rectangle rect = null;
        for (DesktopWindow desktopWindow : com.sun.jna.platform.WindowUtils.getAllWindows(true)) {
            if (desktopWindow.getTitle().contains(windowName)) {
                rect = desktopWindow.getLocAndSize();
                String device = desktopWindow.getTitle().split(" - ")[1];
                System.out.println(device + " : " + rect.width + " x " + rect.height + " " + rect.width + "-" + rect.height);
                return rect.height;
            }
        }
        return -1;
    }

    public synchronized void resizeWindow(HWND hWnd, int x, int y, int width, int height, boolean show)
            throws NameNotFoundException, ResizeException {
        final User32 user32 = User32.INSTANCE;
        if (hWnd == null) {
            throw new NameNotFoundException();
        }

        boolean moveWindow = user32.MoveWindow(hWnd, x, y, width, height, true);
        if (!moveWindow) {
            int lastError = Native.getLastError();
            throw new ResizeException(lastError);

        }
        if (show) {
            user32.ShowWindow(hWnd, WinUser.SW_SHOW);
            user32.SetForegroundWindow(hWnd);
        }
    }

    public synchronized void resizeWindow(int width, Emulators device)
            throws NameNotFoundException, ResizeException {
        final User32 user32 = User32.INSTANCE;
        Rectangle rect = null;
        for (DesktopWindow desktopWindow : com.sun.jna.platform.WindowUtils.getAllWindows(true)) {
            if (desktopWindow.getTitle().contains("Android Emulator")) {
                rect = desktopWindow.getLocAndSize();
                resizeWindow(desktopWindow.getHWND(),rect.x,rect.y,width,603,true);
            }
        }
    }

    public synchronized void minimizeWindow(String lpWindowName, MinimizeOption mimimizeOption)
            throws NameNotFoundException {
        final User32 user32 = User32.INSTANCE;
        HWND hWnd = user32.FindWindow(null, lpWindowName);
        if (hWnd == null) {
            throw new NameNotFoundException();
        }

        user32.SetForegroundWindow(hWnd);
        user32.ShowWindow(hWnd, mimimizeOption.getCode());
    }

}
