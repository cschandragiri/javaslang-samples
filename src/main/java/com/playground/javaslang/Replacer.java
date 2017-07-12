package com.playground.javaslang;

import io.vavr.Function2;

@FunctionalInterface
interface Replacer<Text> extends Function2<Text, Integer, Text> {}
