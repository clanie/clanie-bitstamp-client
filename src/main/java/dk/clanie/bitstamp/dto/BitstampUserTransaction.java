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

import static dk.clanie.core.Utils.asString;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import dk.clanie.bitstamp.jackson.BitstampDateTimeDeserializer;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a user transaction.
 * <p/>
 * User transactions include deposits, withdrawals, trades, and fees.
 * Currency amounts are stored in a map with BitstampCurrencyCode as keys.
 */
@Getter
@ToString
@Slf4j
public class BitstampUserTransaction {

	private final long id;

	@JsonDeserialize(using = BitstampDateTimeDeserializer.class)
	private final Instant datetime;

	private final BitstampUserTransactionType type;

	private final Map<BitstampCurrencyCode, Double> currencyAmounts;

	private BitstampExchangeRate exchangeRate;

	private final Double fee;

	private final Long orderId;

	private final String market;

	private final Map<String, Object> additionalProperties = new HashMap<>();


	@JsonCreator
	public BitstampUserTransaction(
			@JsonProperty("id") long id,
			@JsonProperty("datetime") Instant datetime,
			@JsonProperty("type") BitstampUserTransactionType type,
			@JsonProperty("fee") Double fee,
			@JsonProperty("order_id") Long orderId,
			@JsonProperty("market") String market) {
		this.id = id;
		this.datetime = datetime;
		this.type = type;
		this.currencyAmounts = new HashMap<>();
		this.exchangeRate = null;
		this.fee = fee;
		this.orderId = orderId;
		this.market = market;
	}


	@JsonAnySetter
	@JsonIgnore
	public void setCurrencyAmount(String key, Object value) {
		if (value == null) {
			log.debug("Ignoring null value for property: {}", key);
			return;
		}

		// Try to parse as numeric value (Number or String)
		Double numericValue = null;
		try {
			if (value instanceof Number number) {
				numericValue = number.doubleValue();
			} else if (value instanceof String string) {
				numericValue = Double.parseDouble(string);
			} else {
				log.debug("Ignoring property '{}' with non-numeric type {}: {}", key, value.getClass().getSimpleName(), asString(value));
				additionalProperties.put(key, value);
				return;
			}
		} catch (NumberFormatException e) {
			log.debug("Ignoring property '{}' with non-numeric string value: {}", key, asString(value));
			additionalProperties.put(key, value);
			return;
		}

		// Check if it's a currency pair (contains underscore)
		if (key.contains("_")) {
			try {
				BitstampCurrencyPair currencyPair = BitstampCurrencyPair.fromUnderscoreFormat(key);
				this.exchangeRate = new BitstampExchangeRate(currencyPair, numericValue);
				log.trace("Captured exchange rate: {} = {}", currencyPair, numericValue);
				return;
			} catch (IllegalArgumentException e) {
				log.debug("Property '{}' contains underscore but is not a valid currency pair, value: {}", key, asString(value));
				additionalProperties.put(key, value);
				return;
			}
		}

		// Try to match to a known currency code
		try {
			BitstampCurrencyCode currencyCode = BitstampCurrencyCode.valueOf(key.toUpperCase());
			currencyAmounts.put(currencyCode, numericValue);
		} catch (IllegalArgumentException e) {
			log.debug("Ignoring unknown currency code or other property '{}' with value: {}", key, asString(value));
			additionalProperties.put(key, value);
		}
	}


	/**
	 * Get the amount for a specific currency.
	 * 
	 * @param currencyCode the currency code
	 * @return the amount, or null if not present
	 */
	public Double getAmount(BitstampCurrencyCode currencyCode) {
		return currencyAmounts.get(currencyCode);
	}

}
