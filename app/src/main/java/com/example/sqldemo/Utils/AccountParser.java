package com.example.sqldemo.Utils;

import java.math.BigDecimal;

public class AccountParser {

    public static BigDecimal parseBigDecimalFrom(String value){
        return new BigDecimal(value);
    }
}
