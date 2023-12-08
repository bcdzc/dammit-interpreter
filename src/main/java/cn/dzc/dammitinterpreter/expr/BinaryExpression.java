/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package cn.dzc.dammitinterpreter.expr;

import cn.dzc.dammitinterpreter.DigitExpressionCallback;
import cn.dzc.dammitinterpreter.Value;
import cn.dzc.dammitinterpreter.Value.ValueType;
import cn.dzc.dammitinterpreter.token.TokenType;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author dzc
 * @version BinaryExpression.java, v 0.1 2023年11月10日 16:59 dzc
 */
public class BinaryExpression implements Expression {

    private Expression         left;
    private Expression         right;
    private TokenType          tokenType;
    private Map<String, Value> context;

    public BinaryExpression(Expression left, Expression right, TokenType tokenType, Map<String, Value> dataMap) {
        this.left = left;
        this.right = right;
        this.tokenType = tokenType;
        this.context = dataMap;
    }

    @Override
    public Value eval() {
        if (tokenType == TokenType.PLUS) {
            Value lValue = left.eval();
            Value rValue = right.eval();
            if (lValue.isString() && rValue.isString()) {
                return new Value(lValue.getRefValue() + rValue.getRefValue(), ValueType.STRING, context);
            } else if (lValue.isDigit() && rValue.isDigit()) {
                return digit((n1, n2) -> n1.add(n2));
            } else if (lValue.isString() && rValue.isDigit()) {
                return new Value(lValue.getRefValue() + rValue.getRefDigit(), ValueType.STRING, context);
            } else {
                throw new IllegalArgumentException("invalid plus");
            }
        } else if (tokenType == TokenType.MINUS) {
            return digit((n1, n2) -> n1.subtract(n2));
        } else if (tokenType == TokenType.MUL) {
            return digit((n1, n2) -> n1.multiply(n2));
        } else if (tokenType == TokenType.DIV) {
            return digit((n1, n2) -> n1.divide(n2));
        } else if (tokenType == TokenType.GREATER) {
            return digit((n1, n2) -> {
                boolean ret = n1.doubleValue() > n2.doubleValue();
                return new BigDecimal(ret ? 1 : 0);
            });
        } else if (tokenType == TokenType.GREATER_OR_EQUAL) {
            return digit((n1, n2) -> {
                boolean ret = n1.doubleValue() >= n2.doubleValue();
                return new BigDecimal(ret ? 1 : 0);
            });
        } else if (tokenType == TokenType.SMALLER) {
            return digit((n1, n2) -> {
                boolean ret = n1.doubleValue() < n2.doubleValue();
                return new BigDecimal(ret ? 1 : 0);
            });
        } else if (tokenType == TokenType.SMALLER_OR_EQUAL) {
            return digit((n1, n2) -> {
                boolean ret = n1.doubleValue() <= n2.doubleValue();
                return new BigDecimal(ret ? 1 : 0);
            });
        } else if (tokenType == TokenType.EQUAL) {
            return digit((n1, n2) -> {
                boolean ret = n1.doubleValue() == n2.doubleValue();
                return new BigDecimal(ret ? 1 : 0);
            });
        } else if (tokenType == TokenType.AND) {
            return digit((n1, n2) -> {
                boolean ret = (n1.doubleValue() > 0) && (n2.doubleValue() > 0);
                return new BigDecimal(ret ? 1 : 0);
            });
        } else if (tokenType == TokenType.OR) {
            return digit((n1, n2) -> {
                boolean ret = (n1.doubleValue() > 0) || (n2.doubleValue() > 0);
                return new BigDecimal(ret ? 1 : 0);
            });
        } else if (tokenType == TokenType.ASSIGN) {
            Value l = left.eval();
            Value r = right.eval();
            checkParam(l, ValueType.VARIABLE);
            l.setElem(r);
            return l;
        }
        throw new IllegalArgumentException("unknown binary type");
    }

    private Value digit(DigitExpressionCallback callback) {
        Value l = left.eval();
        Value r = right.eval();
        if (!l.isDigit() || !r.isDigit()) {
            throw new IllegalArgumentException("unsupported non digit param");
        }
        return new Value(callback.eval(l.getRefDigit(), r.getRefDigit()), context);
    }

    private void checkParam(Value value, ValueType valueType) {
        if (value.getValueType() != valueType) {
            throw new IllegalArgumentException("invalid ValueType");
        }
    }

}