package com.playground.javaslang;

import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;

import io.vavr.Tuple;
import net.minidev.json.JSONArray;

public class JSONPathTest
{

	private final static String INPUT = "src/main/java/resources/store.json";
	private String jsonText;

	@Before
	public void init() throws IOException
	{
		jsonText = Files.lines(Paths.get(INPUT))
				.collect(Collectors.joining());
	}

	@Test
	public void testFilter()
	{
		/**
		 * Predicates
		 */
		Filter cheapFictionFilterAndNonFiction = filter(
				where("category").in("non-fiction", "fiction")
						.and("price")
						.lte(10D));

		Filter thrillerOrWrittenByRevati = filter(where("author").eq("Revati B"));

		Filter titleShouldExist = filter(where("title").exists(true)
				.and("title")
				.ne(null));

		/** Test cases ***/
		JSONArray authors = JsonPath.read(jsonText, "$.store.book[*].author");
		assertEquals(7, authors.size());

		authors = JsonPath.read(jsonText, "$.store.book[?(@.price > 10)]");
		assertEquals(4, authors.size());

		authors = JsonPath.read(jsonText, "$.store.book[?].author",
				cheapFictionFilterAndNonFiction);
		assertEquals(2, authors.size());

		JSONArray books = JsonPath.read(jsonText, "$.store.book[?]",
				cheapFictionFilterAndNonFiction.or(thrillerOrWrittenByRevati));
		assertEquals(3, books.size());

		JSONArray expensive = JsonPath.parse(jsonText)
				.read("$..['book'][?]", context ->
				{
					String value = context.item(Map.class)
							.get("price")
							.toString();
					return Float.valueOf(value) > 20.00;
				});
		assertEquals(2, expensive.size());

		JSONArray booksWithTitle = JsonPath.read(jsonText, "$.store.book[?]", titleShouldExist);
		assertEquals(6, booksWithTitle.size());

		JSONArray audioBooks = JsonPath.read(jsonText, "$..book[?]", context ->
		{
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> formats = (List<Map<String, Object>>) context.item(Map.class)
					.get("format");
			return formats.stream()
					.map(entry -> Tuple.of(entry.getOrDefault("type", ""),
							entry.getOrDefault("availableCopies", 0)))
					.filter(tuple -> tuple._1.equals("audio-book") && (int) tuple._2 > 0)
					.findAny()
					.isPresent();
		});
		assertEquals(1, audioBooks.size());

	}

	@Test
	public void testFunction()
	{
		/**
		 * Predicates
		 */
		Filter cheapFictionFilterAndNonFiction = filter(
				where("category").in("non-fiction", "fiction")
						.and("price")
						.lte(30D));

		/** Avg ***/
		JSONArray authors = JsonPath.read(jsonText, "$.store.book[?].price",
				cheapFictionFilterAndNonFiction);
		assertEquals(10.30, authors.stream().mapToDouble(e -> (Double)e).average().orElse(0), 0.5);
		

	}
}
