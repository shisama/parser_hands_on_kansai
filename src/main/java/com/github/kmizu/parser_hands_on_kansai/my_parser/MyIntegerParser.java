package com.github.kmizu.parser_hands_on_kansai.my_parser;

import com.github.kmizu.parser_hands_on_kansai.AbstractParser;
import com.github.kmizu.parser_hands_on_kansai.ParseFailure;

import static com.github.kmizu.parser_hands_on_kansai.my_parser.util.ParserUtil.checkDigit;
import static com.github.kmizu.parser_hands_on_kansai.my_parser.util.ParserUtil.checkZeroStart;

public class MyIntegerParser extends AbstractParser<Integer> {
    @Override
    public Integer parse(String input) {
        if (!checkZeroStart(input)) throw new ParseFailure("first char is zero");
        int result = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (!checkDigit(c)) throw new ParseFailure("input is not digit");
            result = result * 10;
            result += c - '0';
        }
        return result;
    }
}
