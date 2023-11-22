/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package cn.dzc.dammitinterpreter;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author dzc
 * @version Value.java, v 0.1 2023年11月17日 16:47 dzc
 */
public class Value {

    private String             text;
    private BigDecimal         num;
    private ValueType          valueType;
    private Map<String, Value> context;

    public Value(String value, ValueType valueType, Map<String, Value> context) {
        this.text = value;
        this.valueType = valueType;
        this.context = context;
    }

    public Value(BigDecimal num, Map<String, Value> context) {
        this.num = num;
        this.valueType = ValueType.DIGIT;
        this.context = context;
    }

    public boolean isString() {
        if (this.valueType == ValueType.VARIABLE) {
            return this.context.get(text).isString();
        }
        return this.valueType == ValueType.STRING;
    }

    public String getText() {
        return text;
    }

    public BigDecimal getRefDigit() {
        if (this.valueType == ValueType.DIGIT) {
            return this.num;
        }
        if (this.valueType == ValueType.VARIABLE) {
            Value value = this.context.get(text);
            return value.getRefDigit();
        }
        throw new IllegalArgumentException("unsupported ValueType");
    }

    public String getRefValue() {
        if (this.valueType == ValueType.STRING) {
            return this.text;
        }
        if (this.valueType == ValueType.VARIABLE) {
            Value value = this.context.get(text);
            return value.getRefValue();
        }
        throw new IllegalArgumentException("unsupported ValueType");
    }

    public boolean isDigit() {
        if (this.valueType == ValueType.VARIABLE) {
            return this.context.get(text).isDigit();
        }
        return this.valueType == ValueType.DIGIT;
    }

    @Deprecated
    public BigDecimal getNum() {
        return getRefDigit();
    }

    public ValueType getValueType() {
        return valueType;
    }

    public enum ValueType {
        STRING,
        DIGIT,
        BOOL,
        VARIABLE
    }
}