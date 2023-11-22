/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package cn.dzc.dammitinterpreter.token;

import java.math.BigDecimal;

/**
 * @author dzc
 * @version ValueToken.java, v 0.1 2023年11月10日 17:32 dzc
 */
public interface ValueToken extends Token {

    BigDecimal getValue();
}