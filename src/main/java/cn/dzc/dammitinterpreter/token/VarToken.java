/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package cn.dzc.dammitinterpreter.token;

/**
 * @author dzc
 * @version VarToken.java, v 0.1 2023年11月10日 17:17 dzc
 */
public class VarToken implements Token {

    private String               text;

    public VarToken(String text) {
        this.text = text;
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.VAR;
    }

    public String getText() {
        return this.text;
    }
}