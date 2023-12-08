/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package cn.dzc.dammitinterpreter;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author dzc
 * @version Value.java, v 0.1 2023年11月17日 16:47 dzc
 */
public class Value {

    @Getter
    private String             text;
    private BigDecimal         num;
    private List<Value>        dataList;
    @Getter
    private int                arrIdx;
    @Getter
    private ValueType          valueType;
    private Map<String, Value> context;

    private Value(Map<String, Value> context) {
        this.arrIdx = -1;
        this.context = context;
    }

    public Value(List<Value> array, Map<String, Value> context) {
        this(context);
        this.dataList = array;
        this.valueType = ValueType.ARRAY;
    }

    public Value(String value, ValueType valueType, Map<String, Value> context) {
        this(context);
        this.text = value;
        this.valueType = valueType;
        this.context = context;
    }

    public Value(BigDecimal num, Map<String, Value> context) {
        this(context);
        this.num = num;
        this.valueType = ValueType.DIGIT;
        this.context = context;
    }

    public Value(String arrName, int idx, Map<String, Value> context) {
        this(context);
        Value array = context.get(arrName);
        if (null == array) {
            array = new Value(new ArrayList<>(), context);
            context.put(arrName, array);
        }
        this.text = arrName;
        this.arrIdx = idx;
        this.valueType = ValueType.VARIABLE;
    }

    public Value getElemAt(int idx) {
        if (this.valueType != ValueType.ARRAY) {
            throw new IllegalArgumentException("expect array");
        }
        if (idx >= this.dataList.size()) {
            throw new IllegalArgumentException("array bound");
        }
        return this.dataList.get(idx);
    }

    public void setElem(Value value) {
        setElem(arrIdx, value);
    }

    private void setElem(int idx, Value value) {
        if (this.valueType == ValueType.VARIABLE) {
            if (idx == -1) {
                context.put(text, value);
            } else {
                context.get(text).setElem(idx, value);
            }
            return;
        }
        if (this.valueType != ValueType.ARRAY) {
            throw new IllegalArgumentException("expect array");
        }
        if (idx > this.dataList.size()) {
            throw new IllegalArgumentException("array bound");
        }
        if (idx == dataList.size()) {
            this.dataList.add(value);
        } else {
            this.dataList.set(idx, value);
        }
    }

    public int getArraySize() {
        if (this.valueType == ValueType.VARIABLE) {
            return context.get(text).getArraySize();
        }
        if (this.valueType != ValueType.ARRAY) {
            throw new IllegalArgumentException("expect array");
        }
        return this.dataList.size();
    }

    public boolean isString() {
        //if (this.valueType == ValueType.ARRAY) {
        //    return this.context.get(text).getElemAt(arrIdx).isString();
        //}
        if (this.valueType == ValueType.VARIABLE) {
            if (arrIdx != -1) {
                return this.context.get(text).isString(arrIdx);
            } else {
                return this.context.get(text).isString();
            }
        }
        return this.valueType == ValueType.STRING;
    }

    public boolean isString(int arrIdx) {
        if (valueType == ValueType.ARRAY) {
            return this.dataList.get(arrIdx).isString();
        }
        if (this.valueType == ValueType.VARIABLE) {
            return this.context.get(text).isString(arrIdx);
        }
        throw new IllegalArgumentException("expect array");
    }

    public BigDecimal getRefDigit() {
        if (this.valueType == ValueType.DIGIT) {
            return this.num;
        }
        if (valueType == ValueType.ARRAY) {
            return this.dataList.get(arrIdx).getRefDigit();
        }
        if (this.valueType == ValueType.VARIABLE) {
            if (arrIdx != -1) {
                return this.context.get(text).getRefDigit(arrIdx);
            } else {
                return this.context.get(text).getRefDigit();
            }
        }
        throw new IllegalArgumentException("unsupported ValueType");
    }

    public BigDecimal getRefDigit(int arrIdx) {
        if (valueType == ValueType.ARRAY) {
            return this.dataList.get(arrIdx).getRefDigit();
        }
        if (this.valueType == ValueType.VARIABLE) {
            return this.context.get(text).getRefDigit(arrIdx);
        }
        throw new IllegalArgumentException("expect array");
    }

    public String getRefValue() {
        if (this.valueType == ValueType.STRING) {
            return this.text;
        }
        if (valueType == ValueType.ARRAY) {
            return this.dataList.get(arrIdx).getRefValue();
        }
        if (this.valueType == ValueType.VARIABLE) {
            if (arrIdx != -1) {
                return this.context.get(text).getRefValue(arrIdx);
            } else {
                return this.context.get(text).getRefValue();
            }
        }
        throw new IllegalArgumentException("unsupported ValueType");
    }

    public String getRefValue(int arrIdx) {
        if (valueType == ValueType.ARRAY) {
            return this.dataList.get(arrIdx).getRefValue();
        }
        if (this.valueType == ValueType.VARIABLE) {
            return this.context.get(text).getRefValue(arrIdx);
        }
        throw new IllegalArgumentException("expect array");
    }

    public boolean isDigit() {
        if (this.valueType == ValueType.VARIABLE) {
            if (arrIdx != -1) {
                return context.get(text).isDigit(arrIdx);
            } else {
                return context.get(text).isDigit();
            }
        }
        //if (this.valueType == ValueType.ARRAY) {
        //    return this.context.get(text).getElemAt(arrIdx).isDigit();
        //}
        return this.valueType == ValueType.DIGIT;
    }

    public boolean isDigit(int arrIdx) {
        if (valueType == ValueType.ARRAY) {
            return this.dataList.get(arrIdx).isDigit();
        }
        if (this.valueType == ValueType.VARIABLE) {
            return this.context.get(text).isDigit(arrIdx);
        }
        throw new IllegalArgumentException("expect array");
    }

    @Deprecated
    public BigDecimal getNum() {
        return getRefDigit();
    }

    public enum ValueType {
        STRING,
        DIGIT,
        BOOL,
        VARIABLE,
        ARRAY
    }
}