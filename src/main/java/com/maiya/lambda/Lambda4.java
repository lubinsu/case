package com.maiya.lambda;

import java.util.function.Supplier;

/**
 * Created by lubinsu
 * Date: 2018/7/12 13:59
 * Desc:
 */
class Lambda4 {
    static int outerStaticNum;
    int outerNum;

    void testScopes() {
        Converter<Integer, String> stringConverter1 = (from) ->
        {
            outerNum = 23;
            return Integer.valueOf(from);
        };
        Converter<String, Integer> stringConverter2 = (from) ->
        {
            outerStaticNum = 72;
            return String.valueOf(from);
        };
    }
}