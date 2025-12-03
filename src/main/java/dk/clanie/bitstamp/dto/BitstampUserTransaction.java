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
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a user transaction.
 * <p/>
 * User transactions include deposits, withdrawals, trades, and fees.
 * Currency amounts are stored in a map with BitstampCurrencyCode as keys.
 */
@Getter
@Slf4j
public class BitstampUserTransaction {

	private final long id;
	
	@JsonDeserialize(using = BitstampDateTimeDeserializer.class)
	private final Instant datetime;
	
	private final BitstampUserTransactionType type;
	
	private final Map<BitstampCurrencyCode, Double> currencyAmounts;
	
	private final Double fee;
	
	private final Long orderId;
	
	private final String market;


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
		
		// Try to parse as currency amount (Number or String)
		Double amount = null;
		if (value instanceof Number) {
			amount = ((Number) value).doubleValue();
		} else if (value instanceof String) {
			try {
				amount = Double.parseDouble((String) value);
			} catch (NumberFormatException e) {
				log.debug("Ignoring non-numeric string value for property '{}': {}", key, value);
				return;
			}
		} else {
			log.debug("Ignoring property '{}' with unsupported type {}: {}", key, value.getClass().getSimpleName(), value);
			return;
		}
		
		// Try to match to a known currency code
		try {
			BitstampCurrencyCode currencyCode = BitstampCurrencyCode.valueOf(key.toUpperCase());
			currencyAmounts.put(currencyCode, amount);
		} catch (IllegalArgumentException e) {
			log.debug("Ignoring unknown currency code '{}' with value: {}", key, amount);
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
