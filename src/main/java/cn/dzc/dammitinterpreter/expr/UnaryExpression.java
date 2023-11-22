/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package cn.dzc.dammitinterpreter.expr;

import cn.dzc.dammitinterpreter.Value;
import cn.dzc.dammitinterpreter.Value.ValueType;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author dzc
 * @version UnaryExpression.java, v 0.1 2023年11月10日 16:56 dzc
 */
public class UnaryExpression implements Expression {

    private Value value;

    public UnaryExpression(Value value) {
        this.value = value;
    }

    public UnaryExpression(BigDecimal num, Map<String, Value> context) {
        this.value = new Value(num, context);
    }

    public UnaryExpression(String text, Map<String, Value> context) {
        this.value = new Value(text, ValueType.STRING, context);
    }

    public UnaryExpression(String text, ValueType valueType, Map<String, Value> context) {
        this.value = new Value(text, valueType, context);
    }

    public Value eval() {
        return this.value;
    }
}