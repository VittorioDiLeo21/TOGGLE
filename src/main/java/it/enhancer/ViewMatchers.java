package it.enhancer;

public enum ViewMatchers {
    blank("-"),
    //hasContentDescription("hascontentdescription"),
    //hasDescendant("hasdescendant"),
    //hasEllipsizedText("hasellipsizedtext"),
    //hasLinks("haslinks"),
    //hasMultilineTest("hasmultilinetest"),
    //hasSibling("hassibling"),
    //isDescendantOfA("isdescendantofa"),
    //isDisplayed("isdisplayed"),*
    //isCompletelyDisplayed("iscompletelydisplayed"),*
    //isEnabled("isenabled"),
    //isRoot("isroot"),
    //withChild("withchild"),*
    withContentDescription("content-desc"),
    withHint("text"),
    withId("id"),
    //withParent("withparent"),
    //withSpinnerText("withspinnertext")
    //withTagKey("withtagkey"),
    //withTagValue("withtagvalue"),
    withText("text");

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
    }
}
