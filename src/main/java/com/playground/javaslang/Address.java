package com.playground.javaslang;

import java.util.Optional;

/**
 * Step Builder pattern.
 * 
 * Enforcing constraint at creation time that address entity should be created
 * in a specific order.
 * 
 */
public class Address {

	// Optional
	Optional<String> ssn;
	Optional<String> phone;

	// 1. Mandatory
	String email;
	// 2. Mandatory
	String pinCode;
	// 3. Mandatory
	String city;

	public Address(Builder builder) {
		this.ssn = Optional.ofNullable(builder.ssn);
		this.phone = Optional.ofNullable(builder.phone);
		this.email = builder.email;
		this.pinCode = builder.pinCode;
		this.city = builder.city;
	}

	public static Email builder() {
		return new Builder();
	}

	public static class Builder implements Email, PinCode, City, OtherFields {
		String ssn;
		String phone;
		String email;
		String pinCode;
		String city;

		@Override
		public Builder city(String city) {
			this.city = city;
			return this;
		}

		@Override
		public City pinCode(String pinCode) {
			this.pinCode = pinCode;
			return this;
		}

		@Override
		public PinCode email(String email) {
			this.email = email;
			return this;
		}

		public OtherFields phone(String phone) {
			this.phone = phone;
			return this;
		}

		public OtherFields ssn(String ssn) {
			this.ssn = ssn;
			return this;
		}

		@Override
		public Address build() {
			return new Address(this);
		}
	}

	public interface Email {
		PinCode email(String email);
	}

	public interface PinCode {
		City pinCode(String pinCode);
	}

	public interface City {
		OtherFields city(String city);
	}

	public interface OtherFields {
		OtherFields phone(String phone);

		OtherFields ssn(String ssn);

		Address build();
	}

	// Test
	public static void main(String args[]) {
		Address address1 = Address.builder().email("abc.aol.com").pinCode("12345").city("Yuba").build();

		Address address2 = Address
				.builder()
				.email("abc.aol.com")
				.pinCode("12345")
				.city("Yuba")
				.ssn("1234567890")
				.build();
	}
}
