/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package cn.dzc.dammitinterpreter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dzc
 * @version Executor.java, v 0.1 2023年11月10日 16:44 dzc
 */
public class Main {

    public static void main(String[] args) {
        //String script = "pa = ( 1 + 2 )*5;"
        //        + "pb = ( 1 + 3 )*5;"
        //        + "pc = sum(pa, sum(pb, pa));"
        //        + "pc;";

        //String script = "a=\"abc\";\n"
        //        + "b=a + \"defg\";\n"
        //        + "c=subString(b, 1,5);";

        String script = "{\n"
                + "\tsum = 1;\n"
                + "\tfor (i  =0; i < 10; i = i + 1) {\n"
                + "\t\tsum = 5 * (sum(sum, 2) + 1);\n"
                + "\t}\n"
                + "\tif (sum > 100) {\n"
                + "\t\t\"pass\";\n"
                + "\t} else {\n"
                + "\t\t\"fail\";\n"
                + "\t}\n"
                + "}";

        Map<String, Value> dataMap = new HashMap<>();
        dataMap.put("a.b.c", new Value(new BigDecimal(123), dataMap));

        MyLexer lexer = new MyLexer(script);
        MyParser parser = new MyParser(lexer, dataMap);

        Value ret = parser.program().eval();
        System.out.println(ret.isDigit() ? ret.getRefDigit() : ret.getRefValue());
    }
}