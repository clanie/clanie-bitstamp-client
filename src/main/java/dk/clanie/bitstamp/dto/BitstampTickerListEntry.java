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

import lombok.Value;

/**
 * Ticker data entry returned from listing all currency pairs.
 * <p>
 * This is returned from the /ticker endpoint (without a specific currency pair).
 * It contains the same fields as {@link BitstampTicker} plus additional fields:
 * side, marketType, pair, and market.
 */
@Value
public class BitstampTickerListEntry {

	Long timestamp;
	double open;
	double high;
	double low;
	double last;
	double volume;
	double vwap;
	double bid;
	double ask;
	String side;
	double open24;
	double percentChange24;
	String marketType;
	String pair;
	String market;


	@JsonCreator
	public BitstampTickerListEntry(
			@JsonProperty("timestamp") Long timestamp,
			@JsonProperty("open") double open,
			@JsonProperty("high") double high,
			@JsonProperty("low") double low,
			@JsonProperty("last") double last,
			@JsonProperty("volume") double volume,
			@JsonProperty("vwap") double vwap,
			@JsonProperty("bid") double bid,
			@JsonProperty("ask") double ask,
			@JsonProperty("side") String side,
			@JsonProperty("open_24") double open24,
			@JsonProperty("percent_change_24") double percentChange24,
			@JsonProperty("market_type") String marketType,
			@JsonProperty("pair") String pair,
			@JsonProperty("market") String market) {
		this.timestamp = timestamp;
		this.open = open;
		this.high = high;
		this.low = low;
		this.last = last;
		this.volume = volume;
		this.vwap = vwap;
		this.bid = bid;
		this.ask = ask;
		this.side = side;
		this.open24 = open24;
		this.percentChange24 = percentChange24;
		this.marketType = marketType;
		this.pair = pair;
		this.market = market;
	}

}
