package it.windowUtils;

import javax.naming.NameNotFoundException;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;

public class WindowUtils {

    private static final int SWP_SHOWWINDOW = 0x0040;

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

    public synchronized static void minimizeWindow(String lpWindowName, MimimizeOption mimimizeOption)
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
