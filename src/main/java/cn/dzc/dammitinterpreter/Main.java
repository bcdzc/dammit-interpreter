/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package cn.dzc.dammitinterpreter;

import cn.dzc.dammitinterpreter.expr.Expression;

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

        //String script =
        //        "{\n" +
        //        "    num = 1;\n" +
        //        "    for (i = 0; i < 2; i = i + 1) {\n" +
        //        "        num = 5 * (sum(num, 2) + 1);\n" +
        //        "    }\n" +
        //        "    if (num == 115) {\n" +
        //        "        \"pass\";\n" +
        //        "    } else {\n" +
        //        "        \"fail\";\n" +
        //        "    }\n" +
        //        "}";

        String script = ""
                + "{\n"
                + "\tarr[0] = \"a\";\n"
                + "\tarr[1] = 1;\n"
                + "\tarr[2] = \"b\";\n"
                + "\tarr[3] = 2;\n"
                + "\tfor (i = 0; i < len(arr); i = i + 1) {\n"
                + "\t\tprint(\"print each elem:\" + arr[i]);\n"
                + "\t}\n"
                + "\totherArr = arr;\n"
                + "\totherArr[0] = \"b\";\n"
                + "\tprint(\"test array reference passing: \" + otherArr[0] + otherArr[2]);\n"
                + "\tprint(\"test origin array reference: \" + arr[0] + arr[2]);\n"
                + "}";

        Map<String, Value> dataMap = new HashMap<>();

        MyLexer lexer = new MyLexer(script);
        MyParser parser = new MyParser(lexer, dataMap);

        Expression last = parser.program();
        if (null != last) {
            Value ret = last.eval();
            if (null != ret) {
                System.out.println(ret.isDigit() ? ret.getRefDigit() : ret.getRefValue());
            }
        }
    }
}