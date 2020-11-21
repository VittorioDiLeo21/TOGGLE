package it.windowUtils;

import javax.naming.NameNotFoundException;

import com.sun.jna.Native;
import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;
import it.polito.toggle.utils.Emulators;

import java.awt.*;

public class WindowUtils {
    private Toolkit t = null;
    private double pxPerInch = 0;
    private double pxPerCm = 0;

    private static final int SWP_SHOWWINDOW = 0x0040;

    public WindowUtils(){
        this.t = Toolkit.getDefaultToolkit();
        this.pxPerInch = t.getScreenResolution();
        this.pxPerCm = pxPerInch/2.54;
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
                ratio = 0.6707317073170732;
                break;
            case NEXUS_5X:
                ratio = 0.6714876033057852;
                break;
            case NEXUS_6:
                ratio = 0.6436567164179104;
                break;
            case NEXUS_6P:
                ratio = 0.6646216768916156;
                break;
            case NEXUS_4:
                ratio = 0.6795131845841785;
                break;
            case NEXUS_S:
                ratio = 0.5695970695970696;
                break;
            case NEXUS_ONE:
                ratio = 0.5597014925373134;
                break;
            case GALAXY_NEXUS:
                ratio = 0.5547576301615799;
                break;
            case PIXEL:
                ratio = 0.6659528907922913;
                break;
            case PIXEL_3:
                ratio = 0.7714987714987716;
                break;
            case PIXEL_XL:
                ratio = 0.6597510373443983;
                break;
            default :
                ratio = -1;
        }
        if(ratio == -1)
            return -1;
        double screenPx = ratio*totalWidth;
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
            if (desktopWindow.getTitle().contains("Android Emulator")) {
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
        return -1;
    }

    public synchronized static void resizeWindow(HWND hWnd, int x, int y, int width, int height, boolean show)
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

    public synchronized static void minimizeWindow(String lpWindowName, MinimizeOption mimimizeOption)
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
