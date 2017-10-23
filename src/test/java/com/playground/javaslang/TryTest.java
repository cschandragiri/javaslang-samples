package com.playground.javaslang;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.Test;

import io.vavr.control.Try;

public class TryTest {

	private final static String INPUT = "src/test/resources/in.txt";
	private final static String OUTPUT = "src/test/resources/out.txt";

	@Test
	public void testTryJava8() {
		Path path = Paths.get(OUTPUT);
		try (Stream<String> stream = Files.lines(Paths.get(INPUT));
				BufferedWriter writer = Files.newBufferedWriter(path)) {
			stream.filter(line -> !line.isEmpty()).forEach(line ->
			{
				try {
					writer.write(line + "\n");
				} catch (IOException ex) {
					System.err.println("An IOException was caught: " + ex.getMessage());
				}
			});
			// Make sure that every data is written to the output file.
			writer.flush();
		} catch (IOException ex) {
			System.err.println("An IOException was caught: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testTryVavr() {
		Path path = Paths.get(OUTPUT);
		Try.withResources(() -> Files.lines(Paths.get("dsa")), () -> Files.newBufferedWriter(path)).of((inputStream, writer) -> {
			inputStream.filter(line -> !line.isEmpty()).forEach(line ->
			{
				Try.run(() -> writer.write(line + "\n")).onFailure(System.err::println);
			});
			return null;
		}).onFailure(System.err::println);
	}
}
