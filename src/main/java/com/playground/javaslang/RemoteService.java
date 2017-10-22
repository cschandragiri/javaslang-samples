package com.playground.javaslang;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.vavr.control.Try;

/**
 * A very jittery and unreliable service.
 * 
 * @author cchandragiri
 */
public class RemoteService {

	ExecutorService executor = Executors.newFixedThreadPool(10);
	int invocationCount = 0;
	int successCount = 0;
	Random rand = new Random();

	public RemoteService() {

	}

	/**
	 * Unreliable service where every 5th call throws some exception and every
	 * second call takes ~100-150 ms.
	 * 
	 * @return
	 * @throws Exception
	 */
	public synchronized String getData() throws Exception {
		invocationCount++;
		if (invocationCount % 5 == 0) {
			throw new RuntimeException("Internal Server Error (500) ! We are working on it!");
		}
		if (invocationCount % 2 == 0) {
			Thread.sleep(rand.nextInt(101) + 50);
		}
		successCount++;
		return "Success Finally!!";
	}

	public synchronized Try<String> getTryData() {
		invocationCount++;
		if (invocationCount % 5 == 0) {
			return Try.failure(new RuntimeException("Internal Server Error (500) ! We are working on it!"));
		}
		if (invocationCount % 2 == 0) {
			try {
				Thread.sleep(rand.nextInt(101) + 50);
			} catch (InterruptedException e) {
				return Try.failure(e);
			}
		}
		successCount++;
		return Try.ofSupplier(() -> "Success Finally!!");
	}

	/**
	 * Return the invocation count made to this service till now
	 * 
	 * @return
	 */
	public synchronized int getInvocationCount() {
		return invocationCount;
	}
}
