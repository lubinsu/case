package com.maiya.lambda;

/**
 * Created by lubinsu
 * Date: 2018/7/12 10:49
 * Desc:
 */
@FunctionalInterface
public interface Converter<T, F> {
    T convert(F from);
}
