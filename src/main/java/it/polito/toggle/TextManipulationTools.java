package it.polito.toggle;

public class TextManipulationTools {
    public final static String translateKeyCodeToEyeAutomateJava(String keycode) {
        int num_keycode = Integer.parseInt(keycode);
        switch(num_keycode) {

            case KeyCodeConstants.KEYCODE_0: return "eye.type(\"0\");";
            case KeyCodeConstants.KEYCODE_1: return "eye.type(\"1\");";
            case KeyCodeConstants.KEYCODE_2: return "eye.type(\"2\");";
            case KeyCodeConstants.KEYCODE_3: return "eye.type(\"3\");";
            case KeyCodeConstants.KEYCODE_4: return "eye.type(\"4\");";
            case KeyCodeConstants.KEYCODE_5: return "eye.type(\"5\");";
            case KeyCodeConstants.KEYCODE_6: return "eye.type(\"6\");";
            case KeyCodeConstants.KEYCODE_7: return "eye.type(\"7\");";
            case KeyCodeConstants.KEYCODE_8: return "eye.type(\"8\");";
            case KeyCodeConstants.KEYCODE_9: return "eye.type(\"9\");";
            case KeyCodeConstants.KEYCODE_11: return "eye.type(\"11\");";
            case KeyCodeConstants.KEYCODE_12: return "eye.type(\"12\");";

            case KeyCodeConstants.KEYCODE_NUMPAD_0: return "eye.type(\"0\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_1: return "eye.type(\"1\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_2: return "eye.type(\"2\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_3: return "eye.type(\"3\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_4: return "eye.type(\"4\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_5: return "eye.type(\"5\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_6: return "eye.type(\"6\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_7: return "eye.type(\"7\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_8: return "eye.type(\"8\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_9: return "eye.type(\"9\");";


            //TODO capire come gestire i casi maiuscoli

            case KeyCodeConstants.KEYCODE_A: return "eye.type(\"a\");";
            case KeyCodeConstants.KEYCODE_B: return "eye.type(\"b\");";
            case KeyCodeConstants.KEYCODE_C: return "eye.type(\"c\");";
            case KeyCodeConstants.KEYCODE_D: return "eye.type(\"d\");";
            case KeyCodeConstants.KEYCODE_E: return "eye.type(\"e\");";
            case KeyCodeConstants.KEYCODE_F: return "eye.type(\"f\");";
            case KeyCodeConstants.KEYCODE_G: return "eye.type(\"g\");";
            case KeyCodeConstants.KEYCODE_H: return "eye.type(\"h\");";
            case KeyCodeConstants.KEYCODE_I: return "eye.type(\"i\");";
            case KeyCodeConstants.KEYCODE_J: return "eye.type(\"j\");";
            case KeyCodeConstants.KEYCODE_K: return "eye.type(\"k\");";
            case KeyCodeConstants.KEYCODE_L: return "eye.type(\"l\");";
            case KeyCodeConstants.KEYCODE_M: return "eye.type(\"m\");";
            case KeyCodeConstants.KEYCODE_N: return "eye.type(\"n\");";
            case KeyCodeConstants.KEYCODE_O: return "eye.type(\"o\");";
            case KeyCodeConstants.KEYCODE_P: return "eye.type(\"p\");";
            case KeyCodeConstants.KEYCODE_Q: return "eye.type(\"q\");";
            case KeyCodeConstants.KEYCODE_R: return "eye.type(\"r\");";
            case KeyCodeConstants.KEYCODE_S: return "eye.type(\"s\");";
            case KeyCodeConstants.KEYCODE_T: return "eye.type(\"t\");";
            case KeyCodeConstants.KEYCODE_U: return "eye.type(\"u\");";
            case KeyCodeConstants.KEYCODE_V: return "eye.type(\"v\");";
            case KeyCodeConstants.KEYCODE_W: return "eye.type(\"w\");";
            case KeyCodeConstants.KEYCODE_X: return "eye.type(\"x\");";
            case KeyCodeConstants.KEYCODE_Y: return "eye.type(\"y\");";
            case KeyCodeConstants.KEYCODE_Z: return "eye.type(\"z\");";


            case KeyCodeConstants.KEYCODE_APOSTROPHE: return "eye.type(\"'\");";
            case KeyCodeConstants.KEYCODE_AT: return "eye.type(\"@\");";
            case KeyCodeConstants.KEYCODE_BACKSLASH: return "eye.type(\"\\\");";
            case KeyCodeConstants.KEYCODE_EQUALS: return "eye.type(\"=\");";
            case KeyCodeConstants.KEYCODE_MINUS: return "eye.type(\"-\");";
            case KeyCodeConstants.KEYCODE_PERIOD: return "eye.type(\".\");";
            case KeyCodeConstants.KEYCODE_PLUS: return "eye.type(\"+\");";
            case KeyCodeConstants.KEYCODE_POUND: return "eye.type(\"#\");";
            case KeyCodeConstants.KEYCODE_LEFT_BRACKET: return "eye.type(\"[\");";
            case KeyCodeConstants.KEYCODE_RIGHT_BRACKET: return "eye.type(\"]\");";
            case KeyCodeConstants.KEYCODE_SEMICOLON: return "eye.type(\";\");";
            case KeyCodeConstants.KEYCODE_SLASH: return "eye.type(\"/\");";
            case KeyCodeConstants.KEYCODE_STAR: return "eye.type(\"*\");";

            case KeyCodeConstants.KEYCODE_NUMPAD_ADD: return "eye.type(\"+\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_COMMA: return "eye.type(\",\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_DIVIDE: return "eye.type(\"/\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_DOT: return "eye.type(\".\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_EQUALS: return "eye.type(\"=\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_LEFT_PAREN: return "eye.type(\"(\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_RIGHT_PAREN: return "eye.type(\")\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_MULTIPLY: return "eye.type(\"*\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_SUBTRACT: return "eye.type(\"-\");";


            case KeyCodeConstants.KEYCODE_ENTER: return "eye.type(\"[ENTER]\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_ENTER: return "eye.type(\"[ENTER]\");";
            case KeyCodeConstants.KEYCODE_DEL: return "eye.type(\"[BACKSPACE]\");";
            case KeyCodeConstants.KEYCODE_SPACE: return "eye.type(\"[SPACE]\");";
            case KeyCodeConstants.KEYCODE_TAB: return "eye.type(\"[TAB]\");";


            case KeyCodeConstants.KEYCODE_F1: return "eye.type(\"[F1]\");";
            case KeyCodeConstants.KEYCODE_F2: return "eye.type(\"[F2]\");";
            case KeyCodeConstants.KEYCODE_F3: return "eye.type(\"[F3]\");";
            case KeyCodeConstants.KEYCODE_F4: return "eye.type(\"[F4]\");";
            case KeyCodeConstants.KEYCODE_F5: return "eye.type(\"[F5]\");";
            case KeyCodeConstants.KEYCODE_F6: return "eye.type(\"[F6]\");";
            case KeyCodeConstants.KEYCODE_F7: return "eye.type(\"[F7]\");";
            case KeyCodeConstants.KEYCODE_F8: return "eye.type(\"[F8]\");";
            case KeyCodeConstants.KEYCODE_F9: return "eye.type(\"[F9]\");";
            case KeyCodeConstants.KEYCODE_F10: return "eye.type(\"[F10]\");";
            case KeyCodeConstants.KEYCODE_F11: return "eye.type(\"[F11]\");";
            case KeyCodeConstants.KEYCODE_F12: return "eye.type(\"[F12]\");";

        }

        return "";
    }

    public final static String translateKeyCodeToSikuliJava(String keycode) {
        int num_keycode = Integer.parseInt(keycode);

        switch (num_keycode){


            case KeyCodeConstants.KEYCODE_0: return "sikuli_screen.type(\"0\");";
            case KeyCodeConstants.KEYCODE_1: return "sikuli_screen.type(\"1\");";
            case KeyCodeConstants.KEYCODE_2: return "sikuli_screen.type(\"2\");";
            case KeyCodeConstants.KEYCODE_3: return "sikuli_screen.type(\"3\");";
            case KeyCodeConstants.KEYCODE_4: return "sikuli_screen.type(\"4\");";
            case KeyCodeConstants.KEYCODE_5: return "sikuli_screen.type(\"5\");";
            case KeyCodeConstants.KEYCODE_6: return "sikuli_screen.type(\"6\");";
            case KeyCodeConstants.KEYCODE_7: return "sikuli_screen.type(\"7\");";
            case KeyCodeConstants.KEYCODE_8: return "sikuli_screen.type(\"8\");";
            case KeyCodeConstants.KEYCODE_9: return "sikuli_screen.type(\"9\");";
            case KeyCodeConstants.KEYCODE_11: return "sikuli_screen.type(\"11\");";
            case KeyCodeConstants.KEYCODE_12: return "sikuli_screen.type(\"12\");";

            case KeyCodeConstants.KEYCODE_NUMPAD_0: return "sikuli_screen.type(\"0\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_1: return "sikuli_screen.type(\"1\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_2: return "sikuli_screen.type(\"2\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_3: return "sikuli_screen.type(\"3\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_4: return "sikuli_screen.type(\"4\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_5: return "sikuli_screen.type(\"5\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_6: return "sikuli_screen.type(\"6\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_7: return "sikuli_screen.type(\"7\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_8: return "sikuli_screen.type(\"8\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_9: return "sikuli_screen.type(\"9\");";


            //TODO capire come gestire i casi maiuscoli

            case KeyCodeConstants.KEYCODE_A: return "sikuli_screen.type(\"a\");";
            case KeyCodeConstants.KEYCODE_B: return "sikuli_screen.type(\"b\");";
            case KeyCodeConstants.KEYCODE_C: return "sikuli_screen.type(\"c\");";
            case KeyCodeConstants.KEYCODE_D: return "sikuli_screen.type(\"d\");";
            case KeyCodeConstants.KEYCODE_E: return "sikuli_screen.type(\"e\");";
            case KeyCodeConstants.KEYCODE_F: return "sikuli_screen.type(\"f\");";
            case KeyCodeConstants.KEYCODE_G: return "sikuli_screen.type(\"g\");";
            case KeyCodeConstants.KEYCODE_H: return "sikuli_screen.type(\"h\");";
            case KeyCodeConstants.KEYCODE_I: return "sikuli_screen.type(\"i\");";
            case KeyCodeConstants.KEYCODE_J: return "sikuli_screen.type(\"j\");";
            case KeyCodeConstants.KEYCODE_K: return "sikuli_screen.type(\"k\");";
            case KeyCodeConstants.KEYCODE_L: return "sikuli_screen.type(\"l\");";
            case KeyCodeConstants.KEYCODE_M: return "sikuli_screen.type(\"m\");";
            case KeyCodeConstants.KEYCODE_N: return "sikuli_screen.type(\"n\");";
            case KeyCodeConstants.KEYCODE_O: return "sikuli_screen.type(\"o\");";
            case KeyCodeConstants.KEYCODE_P: return "sikuli_screen.type(\"p\");";
            case KeyCodeConstants.KEYCODE_Q: return "sikuli_screen.type(\"q\");";
            case KeyCodeConstants.KEYCODE_R: return "sikuli_screen.type(\"r\");";
            case KeyCodeConstants.KEYCODE_S: return "sikuli_screen.type(\"s\");";
            case KeyCodeConstants.KEYCODE_T: return "sikuli_screen.type(\"t\");";
            case KeyCodeConstants.KEYCODE_U: return "sikuli_screen.type(\"u\");";
            case KeyCodeConstants.KEYCODE_V: return "sikuli_screen.type(\"v\");";
            case KeyCodeConstants.KEYCODE_W: return "sikuli_screen.type(\"w\");";
            case KeyCodeConstants.KEYCODE_X: return "sikuli_screen.type(\"x\");";
            case KeyCodeConstants.KEYCODE_Y: return "sikuli_screen.type(\"y\");";
            case KeyCodeConstants.KEYCODE_Z: return "sikuli_screen.type(\"z\");";


            case KeyCodeConstants.KEYCODE_APOSTROPHE: return "sikuli_screen.type(\"'\");";
            case KeyCodeConstants.KEYCODE_AT: return "sikuli_screen.type(\"@\");";
            case KeyCodeConstants.KEYCODE_BACKSLASH: return "sikuli_screen.type(\"\\\");";
            case KeyCodeConstants.KEYCODE_EQUALS: return "sikuli_screen.type(\"=\");";
            case KeyCodeConstants.KEYCODE_MINUS: return "sikuli_screen.type(\"-\");";
            case KeyCodeConstants.KEYCODE_PERIOD: return "sikuli_screen.type(\".\");";
            case KeyCodeConstants.KEYCODE_PLUS: return "sikuli_screen.type(\"+\");";
            case KeyCodeConstants.KEYCODE_POUND: return "sikuli_screen.type(\"#\");";
            case KeyCodeConstants.KEYCODE_LEFT_BRACKET: return "sikuli_screen.type(\"[\");";
            case KeyCodeConstants.KEYCODE_RIGHT_BRACKET: return "sikuli_screen.type(\"]\");";
            case KeyCodeConstants.KEYCODE_SEMICOLON: return "sikuli_screen.type(\";\");";
            case KeyCodeConstants.KEYCODE_SLASH: return "sikuli_screen.type(\"/\");";
            case KeyCodeConstants.KEYCODE_STAR: return "sikuli_screen.type(\"*\");";

            case KeyCodeConstants.KEYCODE_NUMPAD_ADD: return "sikuli_screen.type(\"+\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_COMMA: return "sikuli_screen.type(\",\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_DIVIDE: return "sikuli_screen.type(\"/\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_DOT: return "sikuli_screen.type(\".\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_EQUALS: return "sikuli_screen.type(\"=\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_LEFT_PAREN: return "sikuli_screen.type(\"(\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_RIGHT_PAREN: return "sikuli_screen.type(\")\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_MULTIPLY: return "sikuli_screen.type(\"*\");";
            case KeyCodeConstants.KEYCODE_NUMPAD_SUBTRACT: return "sikuli_screen.type(\"-\");";

            case KeyCodeConstants.KEYCODE_ENTER: return "sikuli_screen.type(Key.ENTER);";

            case KeyCodeConstants.KEYCODE_NUMPAD_ENTER: return "sikuli_screen.type(Key.ENTER);";
            case KeyCodeConstants.KEYCODE_DEL: return "sikuli_screen.type(Key.BACKSPACE);";
            case KeyCodeConstants.KEYCODE_SPACE: return "sikuli_screen.type(Key.SPACE);";
            case KeyCodeConstants.KEYCODE_TAB: return "sikuli_screen.type(Key.TAB);";


            case KeyCodeConstants.KEYCODE_F1: return "sikuli_screen.type(Key.F1);";
            case KeyCodeConstants.KEYCODE_F2: return "sikuli_screen.type(Key.F2);";
            case KeyCodeConstants.KEYCODE_F3: return "sikuli_screen.type(Key.F3);";
            case KeyCodeConstants.KEYCODE_F4: return "sikuli_screen.type(Key.F4);";
            case KeyCodeConstants.KEYCODE_F5: return "sikuli_screen.type(Key.F5);";
            case KeyCodeConstants.KEYCODE_F6: return "sikuli_screen.type(Key.F6);";
            case KeyCodeConstants.KEYCODE_F7: return "sikuli_screen.type(Key.F7);";
            case KeyCodeConstants.KEYCODE_F8: return "sikuli_screen.type(Key.F8);";
            case KeyCodeConstants.KEYCODE_F9: return "sikuli_screen.type(Key.F9);";
            case KeyCodeConstants.KEYCODE_F10: return "sikuli_screen.type(Key.F10);";
            case KeyCodeConstants.KEYCODE_F11: return "sikuli_screen.type(Key.F11);";
            case KeyCodeConstants.KEYCODE_F12: return "sikuli_screen.type(Key.F12);";
        }
        return "";
    }

    public final static String translateKeyCodeToSikuli(String keycode) {
        int num_keycode = Integer.parseInt(keycode);
        switch(num_keycode) {

            case KeyCodeConstants.KEYCODE_0: return "type(\"0\")";
            case KeyCodeConstants.KEYCODE_1: return "type(\"1\")";
            case KeyCodeConstants.KEYCODE_2: return "type(\"2\")";
            case KeyCodeConstants.KEYCODE_3: return "type(\"3\")";
            case KeyCodeConstants.KEYCODE_4: return "type(\"4\")";
            case KeyCodeConstants.KEYCODE_5: return "type(\"5\")";
            case KeyCodeConstants.KEYCODE_6: return "type(\"6\")";
            case KeyCodeConstants.KEYCODE_7: return "type(\"7\")";
            case KeyCodeConstants.KEYCODE_8: return "type(\"8\")";
            case KeyCodeConstants.KEYCODE_9: return "type(\"9\")";
            case KeyCodeConstants.KEYCODE_11: return "type(\"11\")";
            case KeyCodeConstants.KEYCODE_12: return "type(\"12\")";

            case KeyCodeConstants.KEYCODE_NUMPAD_0: return "type(\"0\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_1: return "type(\"1\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_2: return "type(\"2\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_3: return "type(\"3\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_4: return "type(\"4\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_5: return "type(\"5\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_6: return "type(\"6\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_7: return "type(\"7\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_8: return "type(\"8\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_9: return "type(\"9\")";


            //TODO capire come gestire i casi maiuscoli

            case KeyCodeConstants.KEYCODE_A: return "type(\"a\")";
            case KeyCodeConstants.KEYCODE_B: return "type(\"b\")";
            case KeyCodeConstants.KEYCODE_C: return "type(\"c\")";
            case KeyCodeConstants.KEYCODE_D: return "type(\"d\")";
            case KeyCodeConstants.KEYCODE_E: return "type(\"e\")";
            case KeyCodeConstants.KEYCODE_F: return "type(\"f\")";
            case KeyCodeConstants.KEYCODE_G: return "type(\"g\")";
            case KeyCodeConstants.KEYCODE_H: return "type(\"h\")";
            case KeyCodeConstants.KEYCODE_I: return "type(\"i\")";
            case KeyCodeConstants.KEYCODE_J: return "type(\"j\")";
            case KeyCodeConstants.KEYCODE_K: return "type(\"k\")";
            case KeyCodeConstants.KEYCODE_L: return "type(\"l\")";
            case KeyCodeConstants.KEYCODE_M: return "type(\"m\")";
            case KeyCodeConstants.KEYCODE_N: return "type(\"n\")";
            case KeyCodeConstants.KEYCODE_O: return "type(\"o\")";
            case KeyCodeConstants.KEYCODE_P: return "type(\"p\")";
            case KeyCodeConstants.KEYCODE_Q: return "type(\"q\")";
            case KeyCodeConstants.KEYCODE_R: return "type(\"r\")";
            case KeyCodeConstants.KEYCODE_S: return "type(\"s\")";
            case KeyCodeConstants.KEYCODE_T: return "type(\"t\")";
            case KeyCodeConstants.KEYCODE_U: return "type(\"u\")";
            case KeyCodeConstants.KEYCODE_V: return "type(\"v\")";
            case KeyCodeConstants.KEYCODE_W: return "type(\"w\")";
            case KeyCodeConstants.KEYCODE_X: return "type(\"x\")";
            case KeyCodeConstants.KEYCODE_Y: return "type(\"y\")";
            case KeyCodeConstants.KEYCODE_Z: return "type(\"z\")";


            case KeyCodeConstants.KEYCODE_APOSTROPHE: return "type(\"'\")";
            case KeyCodeConstants.KEYCODE_AT: return "type(\"@\")";
            case KeyCodeConstants.KEYCODE_BACKSLASH: return "type(\"\\\")";
            case KeyCodeConstants.KEYCODE_EQUALS: return "type(\"=\")";
            case KeyCodeConstants.KEYCODE_MINUS: return "type(\"-\")";
            case KeyCodeConstants.KEYCODE_PERIOD: return "type(\".\")";
            case KeyCodeConstants.KEYCODE_PLUS: return "type(\"+\")";
            case KeyCodeConstants.KEYCODE_POUND: return "type(\"#\")";
            case KeyCodeConstants.KEYCODE_LEFT_BRACKET: return "type(\"[\")";
            case KeyCodeConstants.KEYCODE_RIGHT_BRACKET: return "type(\"]\")";
            case KeyCodeConstants.KEYCODE_SEMICOLON: return "type(\";\")";
            case KeyCodeConstants.KEYCODE_SLASH: return "type(\"/\")";
            case KeyCodeConstants.KEYCODE_STAR: return "type(\"*\")";

            case KeyCodeConstants.KEYCODE_NUMPAD_ADD: return "type(\"+\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_COMMA: return "type(\",\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_DIVIDE: return "type(\"/\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_DOT: return "type(\".\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_EQUALS: return "type(\"=\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_LEFT_PAREN: return "type(\"(\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_RIGHT_PAREN: return "type(\")\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_MULTIPLY: return "type(\"*\")";
            case KeyCodeConstants.KEYCODE_NUMPAD_SUBTRACT: return "type(\"-\")";

            case KeyCodeConstants.KEYCODE_ENTER: return "type(Key.ENTER)";

            case KeyCodeConstants.KEYCODE_NUMPAD_ENTER: return "type(Key.ENTER)";
            case KeyCodeConstants.KEYCODE_DEL: return "type(Key.BACKSPACE)";
            case KeyCodeConstants.KEYCODE_SPACE: return "type(Key.SPACE)";
            case KeyCodeConstants.KEYCODE_TAB: return "type(Key.TAB)";


            case KeyCodeConstants.KEYCODE_F1: return "type(Key.F1)";
            case KeyCodeConstants.KEYCODE_F2: return "type(Key.F2)";
            case KeyCodeConstants.KEYCODE_F3: return "type(Key.F3)";
            case KeyCodeConstants.KEYCODE_F4: return "type(Key.F4)";
            case KeyCodeConstants.KEYCODE_F5: return "type(Key.F5)";
            case KeyCodeConstants.KEYCODE_F6: return "type(Key.F6)";
            case KeyCodeConstants.KEYCODE_F7: return "type(Key.F7)";
            case KeyCodeConstants.KEYCODE_F8: return "type(Key.F8)";
            case KeyCodeConstants.KEYCODE_F9: return "type(Key.F9)";
            case KeyCodeConstants.KEYCODE_F10: return "type(Key.F10)";
            case KeyCodeConstants.KEYCODE_F11: return "type(Key.F11)";
            case KeyCodeConstants.KEYCODE_F12: return "type(Key.F12)";

        }

        return "";
    }

    public final static String translateKeyCodeToEyeAutomate(String keycode){
        int num_keycode = Integer.parseInt(keycode);
        switch(num_keycode) {

            case KeyCodeConstants.KEYCODE_0: return "Type \"0\"";
            case KeyCodeConstants.KEYCODE_1: return "Type \"1\"";
            case KeyCodeConstants.KEYCODE_2: return "Type \"2\"";
            case KeyCodeConstants.KEYCODE_3: return "Type \"3\"";
            case KeyCodeConstants.KEYCODE_4: return "Type \"4\"";
            case KeyCodeConstants.KEYCODE_5: return "Type \"5\"";
            case KeyCodeConstants.KEYCODE_6: return "Type \"6\"";
            case KeyCodeConstants.KEYCODE_7: return "Type \"7\"";
            case KeyCodeConstants.KEYCODE_8: return "Type \"8\"";
            case KeyCodeConstants.KEYCODE_9: return "Type \"9\"";
            case KeyCodeConstants.KEYCODE_11: return "Type \"11\"";
            case KeyCodeConstants.KEYCODE_12: return "Type \"12\"";

            case KeyCodeConstants.KEYCODE_NUMPAD_0: return "Type \"0\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_1: return "Type \"1\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_2: return "Type \"2\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_3: return "Type \"3\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_4: return "Type \"4\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_5: return "Type \"5\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_6: return "Type \"6\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_7: return "Type \"7\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_8: return "Type \"8\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_9: return "Type \"9\"";


            //TODO capire come gestire i casi maiuscoli

            case KeyCodeConstants.KEYCODE_A: return "Type \"a\"";
            case KeyCodeConstants.KEYCODE_B: return "Type \"b\"";
            case KeyCodeConstants.KEYCODE_C: return "Type \"c\"";
            case KeyCodeConstants.KEYCODE_D: return "Type \"d\"";
            case KeyCodeConstants.KEYCODE_E: return "Type \"e\"";
            case KeyCodeConstants.KEYCODE_F: return "Type \"f\"";
            case KeyCodeConstants.KEYCODE_G: return "Type \"g\"";
            case KeyCodeConstants.KEYCODE_H: return "Type \"h\"";
            case KeyCodeConstants.KEYCODE_I: return "Type \"i\"";
            case KeyCodeConstants.KEYCODE_J: return "Type \"j\"";
            case KeyCodeConstants.KEYCODE_K: return "Type \"k\"";
            case KeyCodeConstants.KEYCODE_L: return "Type \"l\"";
            case KeyCodeConstants.KEYCODE_M: return "Type \"m\"";
            case KeyCodeConstants.KEYCODE_N: return "Type \"n\"";
            case KeyCodeConstants.KEYCODE_O: return "Type \"o\"";
            case KeyCodeConstants.KEYCODE_P: return "Type \"p\"";
            case KeyCodeConstants.KEYCODE_Q: return "Type \"q\"";
            case KeyCodeConstants.KEYCODE_R: return "Type \"r\"";
            case KeyCodeConstants.KEYCODE_S: return "Type \"s\"";
            case KeyCodeConstants.KEYCODE_T: return "Type \"t\"";
            case KeyCodeConstants.KEYCODE_U: return "Type \"u\"";
            case KeyCodeConstants.KEYCODE_V: return "Type \"v\"";
            case KeyCodeConstants.KEYCODE_W: return "Type \"w\"";
            case KeyCodeConstants.KEYCODE_X: return "Type \"x\"";
            case KeyCodeConstants.KEYCODE_Y: return "Type \"y\"";
            case KeyCodeConstants.KEYCODE_Z: return "Type \"z\"";


            case KeyCodeConstants.KEYCODE_APOSTROPHE: return "Type \"\'\"";
            case KeyCodeConstants.KEYCODE_AT: return "Type \"@\"";
            case KeyCodeConstants.KEYCODE_BACKSLASH: return "Type \"\\\"";
            case KeyCodeConstants.KEYCODE_EQUALS: return "Type \"=\"";
            case KeyCodeConstants.KEYCODE_MINUS: return "Type \"-\"";
            case KeyCodeConstants.KEYCODE_PERIOD: return "Type \".\"";
            case KeyCodeConstants.KEYCODE_PLUS: return "Type \"+\"";
            case KeyCodeConstants.KEYCODE_POUND: return "Type \"#\"";
            case KeyCodeConstants.KEYCODE_LEFT_BRACKET: return "Type \"[\"";
            case KeyCodeConstants.KEYCODE_RIGHT_BRACKET: return "Type \"]\"";
            case KeyCodeConstants.KEYCODE_SEMICOLON: return "Type \";\"";
            case KeyCodeConstants.KEYCODE_SLASH: return "Type \"/\"";
            case KeyCodeConstants.KEYCODE_STAR: return "Type \"*\"";

            case KeyCodeConstants.KEYCODE_NUMPAD_ADD: return "Type \"+\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_COMMA: return "Type \",\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_DIVIDE: return "Type \"/\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_DOT: return "Type \".\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_EQUALS: return "Type \"=\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_LEFT_PAREN: return "Type \"(\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_RIGHT_PAREN: return "Type \")\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_MULTIPLY: return "Type \"*\"";
            case KeyCodeConstants.KEYCODE_NUMPAD_SUBTRACT: return "Type \"-\"";

            case KeyCodeConstants.KEYCODE_ENTER: return "Type [ENTER]";

            case KeyCodeConstants.KEYCODE_NUMPAD_ENTER: return "Type [ENTER]";
            case KeyCodeConstants.KEYCODE_DEL: return "Type [BACKSPACE]";
            case KeyCodeConstants.KEYCODE_SPACE: return "Type [SPACE]";
            case KeyCodeConstants.KEYCODE_TAB: return "Type [TAB]";


            case KeyCodeConstants.KEYCODE_F1: return "Type [F1]";
            case KeyCodeConstants.KEYCODE_F2: return "Type [F2]";
            case KeyCodeConstants.KEYCODE_F3: return "Type [F3]";
            case KeyCodeConstants.KEYCODE_F4: return "Type [F4]";
            case KeyCodeConstants.KEYCODE_F5: return "Type [F5]";
            case KeyCodeConstants.KEYCODE_F6: return "Type [F6]";
            case KeyCodeConstants.KEYCODE_F7: return "Type [F7]";
            case KeyCodeConstants.KEYCODE_F8: return "Type [F8]";
            case KeyCodeConstants.KEYCODE_F9: return "Type [F9]";
            case KeyCodeConstants.KEYCODE_F10: return "Type [F10]";
            case KeyCodeConstants.KEYCODE_F11: return "Type [F11]";
            case KeyCodeConstants.KEYCODE_F12: return "Type [F12]";

        }

        return "";
    }
}
