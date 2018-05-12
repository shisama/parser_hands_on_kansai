package com.github.kmizu.parser_hands_on_kansai.my_parser;

import com.github.kmizu.parser_hands_on_kansai.AbstractParser;
import com.github.kmizu.parser_hands_on_kansai.ParseFailure;
import com.github.kmizu.parser_hands_on_kansai.expression.AbstractExpressionParser;
import com.github.kmizu.parser_hands_on_kansai.expression.ExpressionNode;
import com.github.kmizu.parser_hands_on_kansai.expression.ExpressionNode.ValueNode;

import javax.security.auth.login.FailedLoginException;

public class MyExpressionParser extends AbstractExpressionParser{
    private String input;
    private int position = 0;

    @Override
    public ExpressionNode parse(String input) {
        this.input = input;
        this.position = 0;
        ExpressionNode result = expression();
        if (position != input.length()){
            throw new ParseFailure("error parse");
        }
        return result;
    }

    private ExpressionNode expression() {
        return additive();
    }

    private ExpressionNode additive() {
        ExpressionNode result = multitive();
        while(true) {
            try {
                checkExp('+');
                result = new ExpressionNode.Addition(result, multitive());
            } catch (ParseFailure e1) {
                try {
                    checkExp('-');
                    result = new ExpressionNode.Subtraction(result, multitive());
                } catch (ParseFailure e2) {
                    return result;
                }
            }
        }
    }

    private ExpressionNode multitive() {
        ExpressionNode result = new ValueNode(integer());
        while(true) {
            try {
                checkExp('*');
                result = new ExpressionNode.Multiplication(result, primary());
            } catch (ParseFailure e) {
                try {
                    checkExp('/');
                    return new ExpressionNode.Division(result, primary());
                } catch (ParseFailure e2) {
                    return result;
                }
            }
        }
    }

    private ExpressionNode primary() {
        int tmp = position;
        try {
            checkExp('(');
            ExpressionNode result = expression();
            checkExp(')');
            return result;
        } catch (ParseFailure e) {
            position = tmp;
            return new ValueNode(integer());
        }
    }

    private int integer() {
        int result = (checkNumChar() - '0');
        if(result == 0) {
            if(position >= input.length()){
                return result;
            } else {
                char ch = input.charAt(position);
                if('0' <= ch && ch <= '9') {
                    throw new ParseFailure("if number starts with 0, it cannot be follow by any digit");
                }
                return result;
            }
        }
        while(true) {
            try {
                result = result * 10 + (checkNumChar() - '0');
            } catch (ParseFailure e) {
                return result;
            }
        }
    }

    private void checkExp(Character check) {
        if (input.length() <= position) {
            throw new ParseFailure("over");
        }
        if (check != input.charAt(position)) {
            throw new ParseFailure("expression error");
        }
        position++;
    }

    private char checkNumChar() {
        if (input.length() <= position) {
            throw new ParseFailure("over");
        }
        char c = input.charAt(position);
        if (!('0' <= c && c <= '9')) {
            throw new ParseFailure("parse int error");
        }
        position++;
        return c;
    }
}
