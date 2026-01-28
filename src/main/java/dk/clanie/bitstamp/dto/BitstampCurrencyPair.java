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
package dk.clanie.bitstamp.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import tools.jackson.databind.annotation.JsonDeserialize;

import dk.clanie.bitstamp.jackson.BitstampCurrencyPairDeserializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Currency pair representation for Bitstamp.
 * <p>
 * Bitstamp uses two formats for currency pairs:
 * <ul>
 * <li>With slash (e.g., "BTC/USD") - used in API responses</li>
 * <li>Without slash (e.g., "btcusd") - used as URL parameters in API calls</li>
 * </ul>
 * This class handles both formats and provides methods to convert between them.
 */
@Getter
@EqualsAndHashCode
@JsonDeserialize(using = BitstampCurrencyPairDeserializer.class)
public class BitstampCurrencyPair {

	private final BitstampCurrencyCode baseCurrency;
	private final BitstampCurrencyCode quoteCurrency;

	/**
	 * Creates a currency pair from BitstampCurrencyCode enums.
	 *
	 * @param baseCurrency the base currency
	 * @param quoteCurrency the quote currency
	 */
	public BitstampCurrencyPair(BitstampCurrencyCode baseCurrency, BitstampCurrencyCode quoteCurrency) {
		if (baseCurrency == null) {
			throw new IllegalArgumentException("Base currency cannot be null");
		}
		if (quoteCurrency == null) {
			throw new IllegalArgumentException("Quote currency cannot be null");
		}
		this.baseCurrency = baseCurrency;
		this.quoteCurrency = quoteCurrency;
	}

	/**
	 * Parses a currency pair string with slash (e.g., "BTC/USD").
	 *
	 * @param pairWithSlash the pair string with slash
	 * @return the currency pair
	 * @throws IllegalArgumentException if the format is invalid
	 */
	@JsonCreator
	public static BitstampCurrencyPair fromString(String pairWithSlash) {
		if (pairWithSlash == null || pairWithSlash.isEmpty()) {
			throw new IllegalArgumentException("Pair string cannot be null or empty");
		}
		String[] parts = pairWithSlash.split("/");
		if (parts.length != 2) {
			throw new IllegalArgumentException(
					"Invalid pair format. Expected format: BASE/QUOTE, got: " + pairWithSlash);
		}
		BitstampCurrencyCode base = BitstampCurrencyCode.fromString(parts[0].toUpperCase());
		BitstampCurrencyCode quote = BitstampCurrencyCode.fromString(parts[1].toUpperCase());
		return new BitstampCurrencyPair(base, quote);
	}


	/**
	 * Parses a currency pair string with underscore (e.g., "btc_eur").
	 * This format is used in user transactions for exchange rates.
	 *
	 * @param pairWithUnderscore the pair string with underscore
	 * @return the currency pair
	 * @throws IllegalArgumentException if the format is invalid
	 */
	public static BitstampCurrencyPair fromUnderscoreFormat(String pairWithUnderscore) {
		if (pairWithUnderscore == null || pairWithUnderscore.isEmpty()) {
			throw new IllegalArgumentException("Pair string cannot be null or empty");
		}
		String[] parts = pairWithUnderscore.split("_");
		if (parts.length != 2) {
			throw new IllegalArgumentException(
					"Invalid pair format. Expected format: base_quote, got: " + pairWithUnderscore);
		}
		BitstampCurrencyCode base = BitstampCurrencyCode.fromString(parts[0].toUpperCase());
		BitstampCurrencyCode quote = BitstampCurrencyCode.fromString(parts[1].toUpperCase());
		return new BitstampCurrencyPair(base, quote);
	}

	/**
	 * Returns the pair in the format with slash (e.g., "BTC/USD").
	 * This format is used in API responses.
	 *
	 * @return the pair with slash
	 */
	@JsonValue
	public String toStringWithSlash() {
		return baseCurrency.getCode() + "/" + quoteCurrency.getCode();
	}

	/**
	 * Returns the pair in the format without slash (e.g., "btcusd").
	 * This format is used as URL parameter in API calls.
	 *
	 * @return the pair without slash in lowercase
	 */
	public String toUrlParameter() {
		return (baseCurrency.getCode() + quoteCurrency.getCode()).toLowerCase();
	}

	@Override
	public String toString() {
		return toStringWithSlash();
	}

}
