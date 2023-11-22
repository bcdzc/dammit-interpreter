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
        if (methodName.equals("sum")) {
            BigDecimal sum = new BigDecimal(0);
            List<Value> values = evalAndCheckAllParam(ValueType.DIGIT);
            for (Value value : values) {
                sum = sum.add(value.getNum());
            }
            return new Value(sum, context);
        } else if (methodName.equals("subString")) {
            List<Value> values = expressions.stream()
                    .map(Expression::eval)
                    .collect(Collectors.toList());
            boolean isString = values.get(0).getValueType() == ValueType.STRING || values.get(0).getValueType() == ValueType.VARIABLE;
            if (!isString || values.get(1).getValueType() != ValueType.DIGIT || values.get(2).getValueType() != ValueType.DIGIT) {
                throw new IllegalArgumentException("unsupported method param");
            }
            String ret = values.get(0).getRefValue().substring(values.get(1).getNum().intValue(), values.get(2).getNum().intValue());
            return new Value(ret, ValueType.STRING, context);
        } else {
            throw new IllegalArgumentException("unsupported method");
        }
    }

    private List<Value> evalAndCheckAllParam(ValueType valueType) {
        List<Value> ret = new ArrayList<>(expressions.size());
        for (int i = 0; i < expressions.size(); i++) {
            Value v = expressions.get(i).eval();
            if (v.getValueType() != valueType) {
                throw new IllegalArgumentException("unsupported method param type");
            }
            ret.add(v);
        }
        return ret;
    }

}