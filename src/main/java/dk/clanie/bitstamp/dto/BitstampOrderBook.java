/**
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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

/**
 * Order book data showing bids and asks.
 */
@Value
public class BitstampOrderBook {

	Long timestamp;
	String microtimestamp;
	List<Order> bids;
	List<Order> asks;


	@JsonCreator
	public BitstampOrderBook(
			@JsonProperty("timestamp") Long timestamp,
			@JsonProperty("microtimestamp") String microtimestamp,
			@JsonProperty("bids") List<Order> bids,
			@JsonProperty("asks") List<Order> asks) {
		this.timestamp = timestamp;
		this.microtimestamp = microtimestamp;
		this.bids = bids;
		this.asks = asks;
	}


	/**
	 * Represents a single order in the order book.
	 * Bitstamp returns orders as arrays: [price, amount]
	 */
	@Value
	public static class Order {
		double price;
		double amount;

		@JsonCreator
		public Order(List<Double> values) {
			if (values == null || values.size() < 2) {
				throw new IllegalArgumentException("Order must have at least 2 values: [price, amount]");
			}
			this.price = values.get(0);
			this.amount = values.get(1);
		}
	}

}
