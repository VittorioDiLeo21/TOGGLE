package it.enhancer;

public enum DataOptions {
    atPosition("atposition"),
    inAdapterView("inadapterview"),
    onChildView("onchildview");

    private String value;

    DataOptions(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getSearchType(String matcher) {
        Object[] va = DataOptions.values();

        int low = 0;
        int high = va.length - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;

            if (((DataOptions) va[mid]).name().compareTo(matcher) < 0) {
                low = mid + 1;
            } else if (((DataOptions) va[mid]).name().compareTo(matcher) > 0) {
                high = mid - 1;
            } else {
                return ((DataOptions) va[mid]).getValue();
            }
        }

        return "";
    }
}
