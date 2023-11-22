/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package cn.dzc.dammitinterpreter.token;

/**
 * @author dzc
 * @version TokenType.java, v 0.1 2023年11月10日 17:19 dzc
 */
public enum TokenType {

    // +
    PLUS,
    // -
    MINUS,
    // *
    MUL,
    // /
    DIV,
    // num
    DIGIT,
    // ( or )
    L_PAREN,
    R_PAREN,
    // variable
    VAR,
    // >
    GREATER,
    // >=
    GREATER_OR_EQUAL,
    // <
    SMALLER,
    // <=
    SMALLER_OR_EQUAL,
    // ==
    EQUAL,
    // &&
    AND,
    // ||
    OR,
    // = 赋值语句
    ASSIGN,
    // ;
    LINE_BREAK,
    // ,
    COMMA,
    // String
    STRING,
    // {
    L_CODE_BLOCK,
    // }
    R_CODE_BLOCK,
    // for
    FOR_LOOP,
    // if
    IF,
    // else
    ELSE,
    // over
    NONE
}