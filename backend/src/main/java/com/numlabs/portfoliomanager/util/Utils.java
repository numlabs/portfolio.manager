package com.numlabs.portfoliomanager.util;

public class Utils {

    /**
     * 70.34
     * @param value
     * @return
     */
    public static String normalizeNumbericValue(String value) {
        if(value == null || value.isEmpty() ) {
            return "0";
        }

        if(!value.contains(".")) {
            return value;
        }

        int lastNumberLength = value.length() - value.lastIndexOf(".") - 1;

        if(lastNumberLength == 2) {
            value += "0";
        } else if(lastNumberLength == 1) {
            value += "00";
        }

        return value.replace(".", "");
    }
}
