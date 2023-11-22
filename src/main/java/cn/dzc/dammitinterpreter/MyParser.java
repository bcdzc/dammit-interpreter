/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package cn.dzc.dammitinterpreter;

import cn.dzc.dammitinterpreter.Value.ValueType;
import cn.dzc.dammitinterpreter.expr.BinaryExpression;
import cn.dzc.dammitinterpreter.expr.Expression;
import cn.dzc.dammitinterpreter.expr.MethodExpression;
import cn.dzc.dammitinterpreter.expr.UnaryExpression;
import cn.dzc.dammitinterpreter.token.DigitToken;
import cn.dzc.dammitinterpreter.token.StringToken;
import cn.dzc.dammitinterpreter.token.Token;
import cn.dzc.dammitinterpreter.token.TokenType;
import cn.dzc.dammitinterpreter.token.VarToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static cn.dzc.dammitinterpreter.token.TokenType.*;

/**
 * @author dzc
 * @version MyParser2.java, v 0.1 2023年11月17日 14:51 dzc
 */
public class MyParser {

    private MyLexer            lexer;
    private Token              head;
    private Map<String, Value> context;
    private Stack<Token>       prevTokens;

    public MyParser(MyLexer lexer, Map<String, Value> dataMap) {
        this.lexer = lexer;
        this.context = dataMap;
        this.prevTokens = new Stack<>();
        next();
    }

    private boolean expect(TokenType type) {
        return head.getTokenType() == type;
    }

    private void next() {
        head = lexer.next();
    }

    private void skipTill(TokenType target) {
        while (head.getTokenType() != target) {
            next();
        }
    }

    private void mark() {
        lexer.mark();
        prevTokens.push(head);
    }

    private void cancel() {
        lexer.cancel();
        prevTokens.pop();
    }

    private void recover() {
        lexer.recover();
        head = prevTokens.pop();
    }

    public Expression program() {
        return codeBlock();
    }

    public Expression codeBlock() {
        if (!expect(L_CODE_BLOCK)) {
            throw new IllegalArgumentException("expect {");
        }
        next();
        Expression last = null;
        while (!expect(R_CODE_BLOCK)) {
            if (null != last) {
                last.eval();
            }
            last = statement();
        }
        next();
        return last;
    }

    public Expression statement() {
        Expression ret;
        if (expect(FOR_LOOP)) {
            next();
            // for code block finish
            return forLoop();
        } else if (expect(IF)) {
            next();
            return _if();
        } else {
            mark();
            next();
            if (expect(ASSIGN)) {
                recover();
                ret = assign();
            } else {
                recover();
                ret = expr();
            }
            if (!expect(LINE_BREAK)) {
                throw new IllegalArgumentException("expect LINE_BREAK");
            }
            next();
            return ret;
        }
    }

    public Expression method() {
        Expression left = new UnaryExpression(((VarToken) head).getText(), ValueType.VARIABLE, context);
        next();
        if (!expect(L_PAREN)) {
            throw new IllegalArgumentException("expect method start (");
        }
        next();
        List<Expression> paramList = new ArrayList<>();

        if (!expect(R_PAREN)) {
            paramList.add(expr());
            while (expect(COMMA)) {
                next();
                paramList.add(expr());
            }
        }
        if (!expect(R_PAREN)) {
            throw new IllegalArgumentException("expect method end )");
        }
        next();
        return new MethodExpression(left.eval(), paramList, context);
    }

    public Expression _if() {
        if (!expect(L_PAREN)) {
            throw new IllegalArgumentException("expect (");
        }
        next();

        boolean run = expr().eval().getRefDigit().doubleValue() > 0;
        if (!expect(R_PAREN)) {
            throw new IllegalArgumentException("expect (");
        }
        next();
        if (run) {
            Expression ret = codeBlock();
            // skip else code block
            if (expect(ELSE)) {
                skipTill(R_CODE_BLOCK);
                next();
            }
            return ret;
        } else {
            skipTill(R_CODE_BLOCK);
            next();
            // exec else code block
            if (expect(ELSE)) {
                next();
                return codeBlock();
            } else {
                return null;
            }
        }
    }

    public Expression forLoop() {
        if (!expect(L_PAREN)) {
            throw new IllegalArgumentException("expect (");
        }
        next();
        assign().eval();
        if (!expect(LINE_BREAK)) {
            throw new IllegalArgumentException("expect ;");
        }
        next();

        Value lastValue = null;
        while (true) {
            //compare expression mark
            mark();
            Expression compare = expr();
            if (compare.eval().getNum().intValue() == 0) {
                //exit
                cancel();
                skipTill(R_CODE_BLOCK);
                next();
                break;
            }
            if (!expect(LINE_BREAK)) {
                throw new IllegalArgumentException("expect ;");
            }
            next();
            //post assignment mark
            mark();
            skipTill(L_CODE_BLOCK);
            lastValue = codeBlock().eval();
            //post assignment recover
            recover();
            assign().eval();
            if (!expect(R_PAREN)) {
                throw new IllegalArgumentException("expect )");
            }
            //compare expression recover
            recover();
        }

        return null == lastValue ? null : new UnaryExpression(lastValue);
    }

    public Expression assign() {
        Expression left = expr();
        if (expect(ASSIGN)) {
            TokenType op = head.getTokenType();
            next();
            Expression right = assign();
            return new BinaryExpression(left, right, op, this.context);
        }
        return left;
    }

    public Expression expr() {
        Expression left = cmp();
        if (expect(AND) || expect(OR)) {
            TokenType op = head.getTokenType();
            next();
            Expression right = expr();
            return new BinaryExpression(left, right, op, this.context);
        }
        return left;
    }

    public Expression cmp() {
        Expression left = addOrMinus();
        if (expect(GREATER) || expect(GREATER_OR_EQUAL) || expect(SMALLER) || expect(SMALLER_OR_EQUAL) || expect(EQUAL)) {
            TokenType op = head.getTokenType();
            next();
            Expression right = cmp();
            return new BinaryExpression(left, right, op, this.context);
        }
        return left;
    }

    public Expression addOrMinus() {
        Expression left = mulOrDivide();
        if (expect(PLUS) || expect(MINUS)) {
            TokenType op = head.getTokenType();
            next();
            Expression right = addOrMinus();
            return new BinaryExpression(left, right, op, this.context);
        }
        return left;
    }

    public Expression mulOrDivide() {
        Expression left = factor();
        if (expect(MUL) || expect(DIV)) {
            TokenType op = head.getTokenType();
            next();
            Expression right = mulOrDivide();
            return new BinaryExpression(left, right, op, this.context);
        }
        return left;
    }

    public Expression factor() {
        Token cur = head;
        if (cur.getTokenType() == L_PAREN) {
            next();
            Expression factor = expr();
            if (head.getTokenType() != R_PAREN) {
                throw new IllegalArgumentException("need R_PAREN");
            }
            next();
            return factor;
        } else {
            mark();
            next();
            // method
            if (cur instanceof VarToken && expect(L_PAREN)) {
                recover();
                return method();
            }
            // digit || String || variable
            else {
                cancel();
                if (cur instanceof DigitToken) {
                    return new UnaryExpression(((DigitToken) cur).getValue(), context);
                } else if (cur instanceof StringToken) {
                    return new UnaryExpression(((VarToken) cur).getText(), ValueType.STRING, context);
                } else if (cur instanceof VarToken) {
                    return new UnaryExpression(((VarToken) cur).getText(), ValueType.VARIABLE, context);
                } else {
                    throw new IllegalArgumentException("not support factor");
                }
            }
        }
    }



}