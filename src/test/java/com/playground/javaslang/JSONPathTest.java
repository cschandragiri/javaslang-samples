package com.playground.javaslang;

import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;

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

	Filter cheapFictionFilterAndNonFiction = filter(where("category").in("non-fiction", "fiction")
			.and("price")
			.lte(10D));

	Filter thrillerOrWrittenByRevati = filter(where("author").eq("Revati B"));

	@Test
	public void test()
	{
		List<String> authors = JsonPath.read(jsonText, "$.store.book[*].author");
		assertEquals(7, authors.size());

		authors = JsonPath.read(jsonText, "$.store.book[?(@.price > 10)]");
		assertEquals(4, authors.size());

		authors = JsonPath.read(jsonText, "$.store.book[?].author",
				cheapFictionFilterAndNonFiction);
		assertEquals(2, authors.size());

		List<String> books = JsonPath.read(jsonText, "$.store.book[?]",
				cheapFictionFilterAndNonFiction.or(thrillerOrWrittenByRevati));
		assertEquals(3, books.size());
	}
}
