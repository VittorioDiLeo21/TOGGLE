package it.enhancer;

public enum ViewAssertions {
    isAbove("isabove"),
    isBelow("isbelow"),
    isBottomAlignedWith("isbottomalignedwith"),
    isLeftAlignedWith("isleftalignedwith"),
    isLeftOf("isleftof"),
    isRightAlignedWith("isrightalignedwith"),
    isRightOf("isrightof"),
    isTopAlignedWith("istopalignedwith"),
    //doesNotExist("doesnotexist"),
    //selectedDescendantsMatch("selecteddescendantsmatch"),
    //noEllipseizedText("noellipseizedtext"),
    //noMultilineButtons("nomultilinebuttons"),
    //noOverlaps("nooverlaps"),
    matches("matches");

    private String value;

    ViewAssertions(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getSearchType(String matcher) {
        Object[] va = ViewAssertions.values();

        int low = 0;
        int high = va.length - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;

            if (((ViewAssertions)va[mid]).name().compareTo(matcher) < 0) {
                low = mid + 1;
            } else if (((ViewAssertions)va[mid]).name().compareTo(matcher) > 0) {
                high = mid - 1;
            } else {
                return ((ViewAssertions) va[mid]).getValue();
            }
        }

        return "";

		/*for (Object o : va)
			if (((ViewAssertions) o).name().equals(matcher))
				return ((ViewAssertions) o).getValue();

		return "";*/
    }
}
