/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package cn.dzc.dammitinterpreter;

import java.math.BigDecimal;

/**
 * @author dzc
 * @version DigitExpressionCallback.java, v 0.1 2023年11月17日 17:02 dzc
 */
@FunctionalInterface
public interface DigitExpressionCallback {

    BigDecimal eval(BigDecimal n1, BigDecimal n2);
}