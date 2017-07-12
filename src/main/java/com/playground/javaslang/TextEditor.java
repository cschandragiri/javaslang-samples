package com.playground.javaslang;

import static java.lang.Character.toLowerCase;
import static java.lang.String.format;

import java.util.Random;

import io.vavr.collection.Iterator;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;

/**
 * https://github.com/paplorinc/JavaslangTemplate/blob/master/src/main/java/pap/lorinc/TextEditor.java#L34
 * @author cchandragiri
 *
 */
public class TextEditor {
	static Random random = new Random();
    private static final int SIZE = 1_0;
    static final String STRING_TEXT = Iterator.continually(Generator::randomUppercaseChar).take(SIZE).mkString();
    static final Vector<Character> VECTOR_TEXT = Vector.ofAll(STRING_TEXT.toCharArray());

    @SuppressWarnings("unused")
	public static void main(String... args) {
        final Seq<String> string = editString(STRING_TEXT);
        final Seq<Vector<Character>> vector = editVector(VECTOR_TEXT);
        if (SIZE < 100) { System.out.println(string); }
        System.out.println(format("for %d elements String is %.1f× larger than Vector!",
                SIZE,
                (float) Memory.byteSize(string) / Memory.byteSize(vector)));
    }

    static Seq<String> editString(String text) {
        final Replacer<String> stringReplacer = (t, i) -> t.substring(0, i) + toLowerCase(t.charAt(i)) + t.substring(i + 1);
        return Edits.editAllCharsAndReturnHistory(text, text.length(), stringReplacer);
    }

    static Seq<Vector<Character>> editVector(Vector<Character> text) {
        final Replacer<Vector<Character>> vectorReplacer = (t, i) -> t.update(i, Character::toLowerCase);
        return Edits.editAllCharsAndReturnHistory(text, text.length(), vectorReplacer);
    }
}
