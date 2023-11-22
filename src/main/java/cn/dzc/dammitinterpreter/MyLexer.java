/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package cn.dzc.dammitinterpreter;


import cn.dzc.dammitinterpreter.token.DigitToken;
import cn.dzc.dammitinterpreter.token.StringToken;
import cn.dzc.dammitinterpreter.token.SymbolToken;
import cn.dzc.dammitinterpreter.token.Token;
import cn.dzc.dammitinterpreter.token.TokenType;
import cn.dzc.dammitinterpreter.token.VarToken;

import java.math.BigDecimal;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Stack;

/**
 * @author dzc
 * @version MyLexer.java, v 0.1 2023年11月10日 14:07 dzc
 */
public class MyLexer {

    private StringCharacterIterator iterator;
    private Stack<Integer>          backtrack;

    public MyLexer(String string) {
        this.iterator = new StringCharacterIterator(string);
        this.backtrack = new Stack<>();
    }

    /**
     * Next boolean.
     *
     * @return the boolean
     */
    public Token next() {
        StringBuilder sb = new StringBuilder();
        char c = iterator.current();
        if (c == ' ' || c == '\n' || c == '\t' || c == '\r') {
            iterator.next();
            return next();
        }
        if (c == CharacterIterator.DONE) {
            iterator.next();
            return new SymbolToken(TokenType.NONE);
        }
        if (c == '+') {
            iterator.next();
            return new SymbolToken(TokenType.PLUS);
        }
        if (c == '-') {
            iterator.next();
            return new SymbolToken(TokenType.MINUS);
        }
        if (c == '*') {
            iterator.next();
            return new SymbolToken(TokenType.MUL);
        }
        if (c == '/') {
            iterator.next();
            return new SymbolToken(TokenType.DIV);
        }
        if (c == '(') {
            iterator.next();
            return new SymbolToken(TokenType.L_PAREN);
        }
        if (c == ')') {
            iterator.next();
            return new SymbolToken(TokenType.R_PAREN);
        }
        if (c == '=') {
            if (iterator.next() == '=') {
                iterator.next();
                return new SymbolToken(TokenType.EQUAL);
            } else {
                return new SymbolToken(TokenType.ASSIGN);
            }
        }
        if (c == '>') {
            if (iterator.next() == '=') {
                iterator.next();
                return new SymbolToken(TokenType.GREATER_OR_EQUAL);
            } else {
                return new SymbolToken(TokenType.GREATER);
            }
        }
        if (c == '<') {
            if (iterator.next() == '=') {
                iterator.next();
                return new SymbolToken(TokenType.SMALLER_OR_EQUAL);
            } else {
                return new SymbolToken(TokenType.SMALLER);
            }
        }
        if (c == ',') {
            iterator.next();
            return new SymbolToken(TokenType.COMMA);
        }
        if (c == ';') {
            if (iterator.next() == '\n') {
                iterator.next();
            }
            return new SymbolToken(TokenType.LINE_BREAK);
        }
        if (c == '"') {
            iterator.next();
            Token nextToken = next();
            if (iterator.current() != '"') {
                throw new IllegalArgumentException("expect string end \"");
            }
            iterator.next();
            return new StringToken(((VarToken) nextToken).getText());
        }
        if (c == '{') {
            iterator.next();
            return new SymbolToken(TokenType.L_CODE_BLOCK);
        }
        if (c == '}') {
            iterator.next();
            return new SymbolToken(TokenType.R_CODE_BLOCK);
        }
        if (Character.isDigit(c)) {
            sb.append(c);
            while (true) {
                c = iterator.next();
                if (Character.isDigit(c)) {
                    sb.append(c);
                } else if (c == ' ') {
                    continue;
                } else {
                    break;
                }
            }
            return new DigitToken(new BigDecimal(Integer.parseInt(sb.toString())));
        } else if (Character.isLetter(c) || c == '.') {
            sb.append(c);
            while (true) {
                c = iterator.next();
                if (Character.isLetter(c) || c == '.') {
                    sb.append(c);
                } else if (c == ' ') {
                    continue;
                } else {
                    break;
                }
            }
            String ret = sb.toString();
            switch (ret) {
                case "for" : return new SymbolToken(TokenType.FOR_LOOP);
                case "if": return new SymbolToken(TokenType.IF);
                case "else": return new SymbolToken(TokenType.ELSE);
                default: return new VarToken(ret);
            }
        }
        throw new IllegalArgumentException("error expr");
    }

    public void mark() {
        this.backtrack.push(this.iterator.getIndex());
    }

    public void cancel() {
        this.backtrack.pop();
    }

    public void recover() {
        this.iterator.setIndex(this.backtrack.pop());
    }

}