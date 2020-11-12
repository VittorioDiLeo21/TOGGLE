package it.enhancer;

public enum ViewActions {
    check("check"), clearText("cleartext"), click("click"), closeSoftKeyboard("closekeyboard"), doubleClick("doubleclick"),
    fullcheck("fullcheck"), longClick("longclick"), openActionBarOverflowOrOptionsMenu("openactionbaroverfloworoptionsmenu"),
    openContextualActionModeOverflowMenu("opencontextualactionmodeoverflowmenu"), perform("perform"), pressBack("pressback"),
    pressBackUnconditionally("pressbackunconditionally"), pressImeActionButton("pressIme"), pressKey("presskey"),
    pressMenuKey("pressmenukey"), replaceText("replacetext"), scrollTo("scrollto"),swipeDown("swipedown"),
    swipeLeft("swipeleft"), swipeRight("swiperight"), swipeUp("swipeup"), typeText("typetext"),
    typeTextIntoFocusedView("typeintofocused");

    private String value;

    ViewActions(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getSearchType(String matcher) {
        Object[] va = ViewActions.values();

        int low = 0;
        int high = va.length - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;

            if (((ViewActions) va[mid]).name().compareTo(matcher) < 0) {
                low = mid + 1;
            } else if (((ViewActions) va[mid]).name().compareTo(matcher) > 0) {
                high = mid - 1;
            } else {
                return ((ViewActions) va[mid]).getValue();
            }
        }

        return "";

        /*
         * for (Object o : va) if (((ViewActions) o).name().equals(matcher)) return
         * ((ViewActions) o).getValue();
         *
         * return "";
         */
    }
}
