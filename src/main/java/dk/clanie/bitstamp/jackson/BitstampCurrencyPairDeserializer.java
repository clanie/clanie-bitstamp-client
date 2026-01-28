/*
 * Copyright (C) 2025, Claus Nielsen, clausn999@gmail.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package dk.clanie.bitstamp.jackson;

import java.util.HashSet;
import java.util.Set;

import dk.clanie.bitstamp.dto.BitstampCurrencyCode;
import dk.clanie.bitstamp.dto.BitstampCurrencyPair;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

/**
 * Custom deserializer for BitstampCurrencyPair.
 * <p>
 * This deserializer collects unknown currency codes into a thread-local set
 * rather than failing immediately. This allows us to collect all unknown codes
 * from a response before throwing an exception.
 */
public class BitstampCurrencyPairDeserializer extends ValueDeserializer<BitstampCurrencyPair> {

	private static final ThreadLocal<Set<String>> unknownCurrencyCodes = ThreadLocal.withInitial(HashSet::new);


	/**
	 * Gets the set of unknown currency codes collected during deserialization.
	 * 
	 * @return set of unknown currency codes
	 */
	public static Set<String> getUnknownCurrencyCodes() {
		return unknownCurrencyCodes.get();
	}


	/**
	 * Clears the collected unknown currency codes.
	 */
	public static void clearUnknownCurrencyCodes() {
		unknownCurrencyCodes.get().clear();
	}


	@Override
	public BitstampCurrencyPair deserialize(JsonParser p, DeserializationContext ctxt) throws DatabindException {
		String value = p.getString();
		if (value == null || value.isEmpty()) {
			throw DatabindException.from(p, "Pair string cannot be null or empty");
		}

		String[] parts = value.split("/");
		if (parts.length != 2) {
			throw DatabindException.from(p, 
					"Invalid pair format. Expected format: BASE/QUOTE, got: " + value);
		}

		BitstampCurrencyCode base = null;
		BitstampCurrencyCode quote = null;

		try {
			base = BitstampCurrencyCode.fromString(parts[0].toUpperCase());
		} catch (IllegalArgumentException e) {
			unknownCurrencyCodes.get().add(parts[0].toUpperCase());
		}

		try {
			quote = BitstampCurrencyCode.fromString(parts[1].toUpperCase());
		} catch (IllegalArgumentException e) {
			unknownCurrencyCodes.get().add(parts[1].toUpperCase());
		}

		// If either currency is unknown, we can't create a valid pair
		// But we continue to collect all unknown codes
		if (base == null || quote == null) {
			// Return a dummy pair - we'll throw exception later with all unknown codes
			// Using first available codes as fallback
			if (base == null) base = BitstampCurrencyCode.USD;
			if (quote == null) quote = BitstampCurrencyCode.USD;
		}

		return new BitstampCurrencyPair(base, quote);
	}


}
