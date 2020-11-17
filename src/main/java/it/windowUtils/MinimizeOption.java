package it.windowUtils;

import com.sun.jna.platform.win32.WinUser;

enum MimimizeOption {
    SW_MINIMIZE(WinUser.SW_MINIMIZE), SW_FORCEMINIMIZE(WinUser.SW_FORCEMINIMIZE);

    int code;

    MimimizeOption(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
