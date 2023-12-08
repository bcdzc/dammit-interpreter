/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package cn.dzc.dammitinterpreter.expr;

import cn.dzc.dammitinterpreter.Value;
import cn.dzc.dammitinterpreter.Value.ValueType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author dzc
 * @version MethodExpression.java, v 0.1 2023年11月17日 16:29 dzc
 */
public class MethodExpression implements Expression {

    private String             methodName;
    private List<Expression>   expressions;
    private Map<String, Value> context;

    public MethodExpression(Value method, List<Expression> expressions, Map<String, Value> context) {
        if (null == method || method.getValueType() != ValueType.VARIABLE || null == method.getText()) {
            throw new IllegalArgumentException("supposed VARIABLE Value");
        }
        this.methodName = method.getText();
        this.expressions = expressions;
        this.context = context;
    }

    @Override
    public Value eval() {
        switch (methodName) {
            case "sum": return sum();
            case "subString": return subString();
            case "len": return len();
            case "print": return print();
        }
        throw new IllegalArgumentException("unsupported method");
    }

    private Value print() {
        if (expressions.size() != 1) {
            throw new IllegalArgumentException("len() arguments size must be 1");
        }
        Value value = expressions.get(0).eval();
        if (value.isDigit()) {
            System.out.println(value.getRefDigit());
        } else if (value.isString()) {
            System.out.println(value.getRefValue());
        }
        return null;
    }

    private Value len() {
        if (expressions.size() != 1) {
            throw new IllegalArgumentException("len() arguments size must be 1");
        }
        Value value = expressions.get(0).eval();
        if (value.getValueType() != ValueType.VARIABLE) {
            throw new IllegalArgumentException("len() arguments must be array variable");
        }
        return new Value(new BigDecimal(value.getArraySize()), context);
    }

    private Value subString() {
        List<Value> values = expressions.stream()
                .map(Expression::eval)
                .collect(Collectors.toList());
        boolean isString = values.get(0).getValueType() == ValueType.STRING || values.get(0).getValueType() == ValueType.VARIABLE;
        if (!isString || values.get(1).getValueType() != ValueType.DIGIT || values.get(2).getValueType() != ValueType.DIGIT) {
            throw new IllegalArgumentException("unsupported method param");
        }
        String ret = values.get(0).getRefValue().substring(values.get(1).getNum().intValue(), values.get(2).getNum().intValue());
        return new Value(ret, ValueType.STRING, context);
    }

    private Value sum() {
        BigDecimal sum = new BigDecimal(0);
        for (Expression expression : expressions) {
            Value value = expression.eval();
            if (!value.isDigit()) {
                throw new IllegalArgumentException("unsupported method param type");
            }
            sum = sum.add(value.getRefDigit());
        }
        return new Value(sum, context);
    }


}