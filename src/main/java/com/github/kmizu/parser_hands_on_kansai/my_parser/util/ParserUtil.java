package com.github.kmizu.parser_hands_on_kansai.my_parser.util;

public class ParserUtil {
    public static boolean checkZeroStart(String input) {
        if (input.length() > 1 && input.charAt(0) == '0') {
            return false;
        }
        return true;
    }

    public static boolean checkDigit(char c) {
        if (!('0' <= c && c <= '9')) {
            return false;
        }
        return true;
    }
}
