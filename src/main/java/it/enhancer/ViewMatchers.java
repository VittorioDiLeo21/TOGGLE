package it.enhancer;

public enum ViewMatchers {
    blank("-"), withContentDescription("content-desc"), withHint("text"), withId("id"), withText("text");

    private String value;

    ViewMatchers(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getSearchType(String matcher) {
        Object[] vm = ViewMatchers.values();

        int low = 0;
        int high = vm.length - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;

            if (((ViewMatchers) vm[mid]).name().compareTo(matcher) < 0) {
                low = mid + 1;
            } else if (((ViewMatchers) vm[mid]).name().compareTo(matcher) > 0) {
                high = mid - 1;
            } else {
                return ((ViewMatchers) vm[mid]).getValue();
            }
        }

        return "";

        /*
         * for (Object o : vm) if (((ViewMatchers) o).name().equals(matcher)) return
         * ((ViewMatchers) o).getValue();
         *
         * return "";
         */
    }
}
