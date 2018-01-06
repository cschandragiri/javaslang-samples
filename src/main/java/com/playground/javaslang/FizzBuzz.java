package com.playground.javaslang;

import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FizzBuzz {
    static <R> IntFunction<R> ifmod(int m, R r, IntFunction<R> f) {
        return (int i) -> (i % m == 0) ? r : f.apply(i);
    }
    
    public static String getTriple(int m, int n) {   
        int a = m * m - n * n;
        int b = 2 * m * n;
        int c = m * m + n * n;
        System.out.println("m n: " + m+  " " + n);
        return String.format("%d %d %d", a, b, c);
      } 
      
      public static List<String> computeTriples(int numberOfValues) {
        return Stream.iterate(2, e -> e + 1)      
                     .flatMap(m -> IntStream.range(1, m)
                                            .mapToObj(n -> getTriple(m, n)))
                     .limit(numberOfValues)
                     .collect(Collectors.toList());
      }
      
      public static void main(String[] args) {
        //values of a, b, c where a**2 + b**2 == c**2.
        computeTriples(10).forEach(System.out::println); 
      }       
            
    /*public static void main(String[] args) {
        IntStream.rangeClosed(1, 100)
                 .mapToObj(ifmod(15, "FizzBuzz",
                             ifmod(5, "Buzz", 
                               ifmod(3, "Fizz", Integer::toString))))
                 .forEach(System.out::println);
    }*/
}