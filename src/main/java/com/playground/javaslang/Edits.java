package com.playground.javaslang;

import io.vavr.collection.Seq;
import static io.vavr.API.List;
class Edits {
    static <Text> Seq<Text> editAllCharsAndReturnHistory(Text text, int length, Replacer<Text> replacer) {
        Seq<Text> history = List(text);
        for (int i = 0; i < length; i++) {
            text = replacer.apply(text, i);
            history = history.prepend(text);
        }
        return history;
    }
}
