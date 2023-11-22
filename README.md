# dammit-interpreter
简易的解释器，支持for循环、if语句、变量赋值。运行case参照Main.java

```
{
    num = 1;
    for (i = 0; i < 2; i = i + 1) {
        num = 5 * (sum(num, 2) + 1);
    }
    if (num == 115) {
        "pass";
    } else {
        "fail";
    }
}
```