/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package cn.dzc.dammitinterpreter.token;

/**
 * @author dzc
 * @version CharToken.java, v 0.1 2023年11月10日 17:27 dzc
 */
public class SymbolToken implements Token {

    private TokenType tokenType;

    public SymbolToken(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public TokenType getTokenType() {
        return this.tokenType;
    }
}