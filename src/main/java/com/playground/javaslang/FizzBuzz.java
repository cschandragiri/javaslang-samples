package com.playground.javaslang;

import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class FizzBuzz {
    static <R> IntFunction<R> ifmod(int m, R r, IntFunction<R> f) {
        return (int i) -> (i % m == 0) ? r : f.apply(i);
    }
            
    public static void main(String[] args) {
        IntStream.rangeClosed(1, 100)
                 .mapToObj(ifmod(15, "FizzBuzz",
                             ifmod(5, "Buzz", 
                               ifmod(3, "Fizz", Integer::toString))))
                 .forEach(System.out::println);
    }
}