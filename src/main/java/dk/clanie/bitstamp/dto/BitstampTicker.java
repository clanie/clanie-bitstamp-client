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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import dk.clanie.core.json.EmptyStringToNullDoubleDeserializer;
import lombok.Value;

/**
 * Ticker data for a currency pair.
 */
@Value
public class BitstampTicker {

	double high;
	double last;
	long timestamp;
	double bid;
	double vwap;
	double volume;
	double low;
	double ask;
	BitstampTradeSide side;
	double open;
	double open24;
	@JsonDeserialize(using = EmptyStringToNullDoubleDeserializer.class)
	Double percentChange24;
	BitstampMarketType marketType;
	// Optional field - only present for certain trading pairs (e.g., EURUSD)
	Double indexPrice;


	@JsonCreator
	public BitstampTicker(
			@JsonProperty("high") double high,
			@JsonProperty("last") double last,
			@JsonProperty("timestamp") long timestamp,
			@JsonProperty("bid") double bid,
			@JsonProperty("vwap") double vwap,
			@JsonProperty("volume") double volume,
			@JsonProperty("low") double low,
			@JsonProperty("ask") double ask,
			@JsonProperty("side") BitstampTradeSide side,
			@JsonProperty("open") double open,
			@JsonProperty("open_24") double open24,
			@JsonProperty("percent_change_24") Double percentChange24,
			@JsonProperty("market_type") BitstampMarketType marketType,
			@JsonProperty("index_price") Double indexPrice) {
		this.high = high;
		this.last = last;
		this.timestamp = timestamp;
		this.bid = bid;
		this.vwap = vwap;
		this.volume = volume;
		this.low = low;
		this.ask = ask;
		this.side = side;
		this.open = open;
		this.open24 = open24;
		this.percentChange24 = percentChange24;
		this.marketType = marketType;
		this.indexPrice = indexPrice;
	}

}
