package it.enhancer;

public class EspressoCommands {
    private static String[] commands = { "actionOnHolderItem", "actionOnItem", "actionOnItemAtPosition",
            "actionWithAssertions", "addGlobalAssertion", "allOf", "anyIntent", "anyOf", "assertThat", "childAtPosition",
            "clearGlobalAssertions", "clearText", "click", "clickBetweenToTitles", "close", "closeDrawer",
            "closeSoftKeyboard", "doesNotExist", "doubleClick", "endsWith", "hasAction", "hasBackground",
            "hasCategories", "hasChildCount", "hasClassName", "hasComponent", "hasContentDescription", "hasData",
            "hasDescendant", "hasEllipsizedText", "hasEntry", "hasErrorText", "hasExtra", "hasExtras",
            "hasExtraWithKey", "hasFlag", "hasFlags", "hasFocus", "hasHost", "hasIMEAction", "hasKey", "hasLinks",
            "hasMinimumChildCount", "hasMultilineTest", "hasMyPackageName", "hasPackage", "hasPackageName",
            "hasParamWithName", "hasParamWithValue", "hasPath", "hasResultCode", "hasResultData", "hasScheme",
            "hasSchemeSpecificPart", "hasShortClassName", "hasSibling", "hasTextColor", "hasToString", "hasType",
            "hasValue", "hasWindowLayoutParams","inAdapterView", "instanceOf", "is", "isAbove", "isAssignableFrom", "isBelow",
            "isBottomAlignedWith", "isChecked", "isClickable", "isClosed", "isCompletelyAbove", "isCompletelyBelow",
            "isCompletelyDisplayed", "isCompletelyLeftOf", "isCompletelyRightOf", "isDescendantOfA", "isDialog",
            "isDisplayed", "isDisplayingAtLeast", "isEnabled", "isFocusable", "isInternal", "isJavascriptEnabled",
            "isLeftAlignedWith", "isLeftOf", "isNotChecked", "isOpen", "isPartiallyAbove", "isPartiallyBelow",
            "isPartiallyLeftOf", "isPartiallyRightOf", "isPlatformPopup", "isRightAlignedWith", "isRightOf", "isRoot",
            "isSelected", "isSystemAlertWindow", "isTopAlignedWith", "isTouchable", "longClick", "matches",
            "navigateTo", "noEllipseizedText", "noMultilineButtons", "noOverlaps", "not", "onData", "onView", "open",
            "openActionBarOverflowOrOptionsMenu", "openContextualActionModeOverflowMenu", "openDrawer", "openLink",
            "openLinkWithText", "openLinkWithUri", "pressBack", "pressBackUnconditionally", "pressImeActionButton",
            "pressKey", "pressMenuKey", "removeGlobalAssertion", "repeatedlyUntil", "replaceText", "scrollLeft",
            "scrollRight", "scrollTo", "scrollToFirst", "scrollToHolder", "scrollToLast", "scrollToPage",
            "scrollToPosition", "selectedDescendantsMatch", "setData", "setTime", "startsWith", "supportsInputMethods",
            "swipeDown", "swipeLeft", "swipeRight", "swipeUp", "toPackage", "typeText", "typeTextIntoFocusedView",
            "withAlpha", "withChild", "withClassName", "withContentDescription", "withDecorView",
            "withEffectiveVisibility", "withHint", "withId", "withInputType", "withKey", "withParent",
            "withParentIndex", "withResourceName", "withRowBlob", "withRowDouble", "withRowFloat", "withRowInt",
            "withRowLong", "withRowShort", "withRowString", "withSpinnerText", "withSubstring", "withSummary",
            "withSummaryText", "withTagKey", "withTagValue", "withText", "withTitle", "withTitleText" };

    public static String[] getCommands() {
        return commands;
    }
}
