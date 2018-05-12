package com.github.kmizu.parser_hands_on_kansai.my_parser;

import com.github.kmizu.parser_hands_on_kansai.AbstractParser;
import com.github.kmizu.parser_hands_on_kansai.ParseFailure;

import static com.github.kmizu.parser_hands_on_kansai.my_parser.util.ParserUtil.checkDigit;
import static com.github.kmizu.parser_hands_on_kansai.my_parser.util.ParserUtil.checkZeroStart;

public class MyLimitedExpressionParser extends AbstractParser<Integer> {
    @Override
    public Integer parse(String input) {
        if (!checkZeroStart(input)) throw new ParseFailure("first char is zero");
        int result = 0;
        int tmp = 0;
        String method = "";
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (!checkDigit(c)) {
                method = String.valueOf(c);
                tmp = result;
                result = 0;
            }
            result = result * 10;
            result += c - '0';
        }
        switch (method) {
            case "+":
                result += tmp;
                break;
            case "-":
                result -= tmp;
                break;
            case "*":
                result *= tmp;
                break;
            case "/":
                result /= tmp;
                break;
            default:
                if (!method.isEmpty()) throw new ParseFailure("unknown expression");
        }
        return result;
    }
}
