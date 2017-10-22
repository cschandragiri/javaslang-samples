package com.playground.javaslang;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.vavr.CheckedFunction0;
import io.vavr.control.Either;
import io.vavr.control.Try;

public class VavrRateLimiterTest {

	RemoteService remoteService = null;
	RateLimiterConfig config = null;
	ExecutorService executor = null;
	final int ITERATIONS = 100;

	@Before
	public void init() {
		remoteService = new RemoteService();
		/**
		 * Allow only 5 calls per second
		 */
		config = RateLimiterConfig
				.custom()
				.limitRefreshPeriod(Duration.ofSeconds(1))
				.limitForPeriod(5)
				.timeoutDuration(Duration.ofMillis(500))
				.build();
		executor = Executors.newFixedThreadPool(5);
	}

	@Test
	public void test() throws Exception {

		System.out.println("******************test()**********************");
		RateLimiter rateLimiter = RateLimiter.of("customLimiter", config);

		CheckedFunction0<String> rateLimitedFunction = RateLimiter
				.decorateCheckedSupplier(rateLimiter, () -> remoteService.getData());

		List<Future<String>> result = Lists.newArrayList();
		Stopwatch watch = Stopwatch.createStarted();
		for (int i = 1; i <= ITERATIONS; i++) {
			Future<String> future = executor.submit(() ->
			{
				try {
					return rateLimitedFunction.apply();
				} catch (Throwable e) {
					System.out.println(
							"Error:" + e.getMessage() + " time elapsed:" + watch.elapsed(TimeUnit.MILLISECONDS));
					return null;
				}
			});
			result.add(future);
		}
		assertEquals(ITERATIONS, result.size());
		executor.shutdown();
		executor.awaitTermination(2, TimeUnit.MINUTES);
		System.out.println(
				"Finished all threads. Invocation Count: " + remoteService.getInvocationCount() + " time elapsed:"
						+ watch.elapsed(TimeUnit.MILLISECONDS));
	}

	/**
	 * Evaluate result using Future, unnecessarily messy code!
	 * 
	 * @throws Exception
	 */
	@Test
	public void testWithTry() throws Exception {
		System.out.println("******************testWithTry()**********************");
		RateLimiter rateLimiter = RateLimiter.of("customLimiter", config);

		Callable<Try<String>> rateLimitedFunction = RateLimiter
				.decorateCallable(rateLimiter, () -> remoteService.getTryData());

		List<Future<Try<String>>> result = Lists.newArrayList();
		Stopwatch watch = Stopwatch.createStarted();
		for (int i = 1; i <= ITERATIONS; i++) {
			result.add(executor.submit(rateLimitedFunction));
		}
		executor.shutdown();
		executor.awaitTermination(2, TimeUnit.MINUTES);

		assertEquals(ITERATIONS, result.size());

		result.stream().map(f ->
		{
			try {
				return f.get().toEither();
			} catch (Exception e) {
				return Either.left(e);
			}
		}).forEach(either ->
		{
			if (either.isLeft()) {
				System.out.println(either.getLeft().getMessage());
			} else {
				System.out.println(either.get());
			}
		});

		System.out.println(
				"Finished all threads. Invocation Count: " + remoteService.getInvocationCount() + " time:"
						+ watch.elapsed(TimeUnit.MILLISECONDS));
	}

	@Test
	public void testWithTryCompletableFuture() throws Exception {
		System.out.println("******************testWithTryCompletableFuture()**********************");
		RateLimiter rateLimiter = RateLimiter.of("customLimiter", config);

		Supplier<Try<String>> rateLimitedSupplier = RateLimiter
				.decorateSupplier(rateLimiter, () -> remoteService.getTryData());
		Stopwatch watch = Stopwatch.createStarted();

		IntStream.rangeClosed(1, ITERATIONS).forEach(iter ->
		{
			CompletableFuture.supplyAsync(rateLimitedSupplier, executor).exceptionally(ex ->
			{
				return Try.failure(ex);
			}).thenAccept(result ->
			{
				Either<Throwable, String> either = result.toEither();
				if (either.isLeft()) {
					System.out.println(
							iter + ":" + either.getLeft().getMessage() + " time elapsed:"
									+ watch.elapsed(TimeUnit.MILLISECONDS));
				} else {
					System.out.println(iter + ":" + either.get());
				}
			});
		});

		executor.shutdown();
		executor.awaitTermination(2, TimeUnit.MINUTES);
		System.out.println(
				"Finished all threads. Invocation Count: " + remoteService.getInvocationCount() + " time:"
						+ watch.elapsed(TimeUnit.MILLISECONDS));
	}
}
