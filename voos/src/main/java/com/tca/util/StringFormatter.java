package com.tca.util;

public class StringFormatter {
    public static String capitalize(String string) {
        string = string.trim().toLowerCase();
        for (int i=0; i<string.length(); i++) {
            if (i == 0 || string.charAt(i-1) == ' ') {
                string = string.substring(0, i) + string.substring(i, i+1).toUpperCase() + string.substring(i+1);
            }
        }
        return string;
    }

    public static String formatNumericData(String string) {
        return string.trim().replace(" ", "").replace(".", "").replace("-", "").replace("/", "").replace("(", "").replace(")", "").replace(":", "").replace(";", "");
    }
}
