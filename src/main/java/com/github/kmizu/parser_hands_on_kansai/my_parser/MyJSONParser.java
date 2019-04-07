package com.github.kmizu.parser_hands_on_kansai.my_parser;

import com.github.kmizu.parser_hands_on_kansai.ParseFailure;
import com.github.kmizu.parser_hands_on_kansai.json.AbstractJSONParser;
import com.github.kmizu.parser_hands_on_kansai.json.JSONNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.kmizu.parser_hands_on_kansai.my_parser.util.ParserUtil.checkDigit;
import static com.github.kmizu.parser_hands_on_kansai.my_parser.util.ParserUtil.checkZeroStart;

public class MyJSONParser extends AbstractJSONParser {
    int position = 0;
    String input;

    private char accept() {
        if (input.length() <= position) {
            throw new ParseFailure("over");
        }
        char c = input.charAt(position);
        position++;
        return c;
    }

    private char accept(char c) {
        if (input.length() <= position) {
            throw new ParseFailure("over");
        }
        if (c != input.charAt(position)) {
            throw new ParseFailure("disallow char");
        }
        return accept();
    }

    private char acceptExcept(char c) {
        if (input.length() <= position) {
            throw new ParseFailure("over");
        }
        if (c == input.charAt(position)) {
            throw new ParseFailure("disallow char");
        }
        return accept();
    }

    @Override
    public JSONNode parse(String input) {
        this.position = 0;
        this.input = input;
        return jvalue();
    }

    private JSONNode jvalue() {
        try {
            return jobjct();
        } catch (ParseFailure e) {
        }

        try {
            return jarray();
        } catch (ParseFailure e) {
        }

        try {
            return jboolean();
        } catch (ParseFailure e) {
        }

        try {
            return jnull();
        } catch (ParseFailure e) {
        }

        try {
            return jstring();
        } catch (ParseFailure e) {
        }
        return jnumber();
    }

    private JSONNode.JSONObject jobjct() {
        accept('{');
        Map<String, JSONNode> properties = new HashMap<>();
        try {
            String key = jstring().value;
            accept(':');
            JSONNode value = jvalue();
            properties.put(key, value);
        } catch (ParseFailure e) {
            accept('}');
            return new JSONNode.JSONObject(properties);
        }
        return new JSONNode.JSONObject(properties);
    }

    private JSONNode.JSONArray jarray() {
        accept('[');
        List<JSONNode> list = new ArrayList<>();
        try {
            list.add(jvalue());
        } catch (ParseFailure e) {
            accept(']');
            return new JSONNode.JSONArray(list);
        }
        return new JSONNode.JSONArray(list);
    }

    private JSONNode.JSONBoolean jboolean() {
        try {
            accept('t');
            accept('r');
            accept('u');
            accept('e');
            return new JSONNode.JSONBoolean(true);
        } catch (ParseFailure e) {
            accept('f');
            accept('a');
            accept('l');
            accept('s');
            accept('e');
            return new JSONNode.JSONBoolean(false);
        }
    }

    private JSONNode.JSONNull jnull() {
        accept('n');
        accept('u');
        accept('l');
        accept('l');
        return JSONNode.JSONNull.getInstance();
    }

    private JSONNode.JSONString jstring() {
        StringBuilder content =
                new StringBuilder();
        accept('"');
        while(true) {
            int current = position;
            try {
                accept('\\');
                char code = accept();
                code = escapeSequence(code);
                content.append(code);
            } catch (ParseFailure e1) {
                position = current;
                try {
                    char code = acceptExcept('"');
                    content.append(code);
                } catch (ParseFailure e2) {
                    break;
                }
            }
        }
        accept('"');
        return new JSONNode.JSONString(
                new String(content)
        );
    }

    private JSONNode.JSONNumber jnumber() {
        int result = 0;
        for (; position < input.length(); position++) {
            char c = input.charAt(position);
            if (!checkDigit(c)) throw new ParseFailure("input is not digit");
            result = result * 10;
            result += c - '0';
        }
        return new JSONNode.JSONNumber(result);
    }

    char escapeSequence(char ch) {
        switch (ch) {
            case 'r':
                return '\r';
            case 'n':
                return '\n';
            case 'b':
                return '\b';
            case 'f':
                return '\f';
            case '\\':
                return '\\';
            case '"':
                return '"';
            default:
                throw new ParseFailure("unknown escape sequence");
        }
    }
}
