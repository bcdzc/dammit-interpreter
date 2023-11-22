/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package cn.dzc.dammitinterpreter.token;

import java.math.BigDecimal;

/**
 * @author dzc
 * @version DigitToken.java, v 0.1 2023年11月10日 17:24 dzc
 */
public class DigitToken implements ValueToken {

    private BigDecimal num;

    public DigitToken(BigDecimal num) {
        this.num = num;
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.DIGIT;
    }

    @Override
    public BigDecimal getValue() {
        return num;
    }
}