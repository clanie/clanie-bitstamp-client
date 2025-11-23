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
 * OHLC (Open, High, Low, Close) candlestick data.
 */
@Value
public class BitstampOhlcData {

	String pair;
	List<Candle> ohlc;


	@JsonCreator
	public BitstampOhlcData(
			@JsonProperty("pair") String pair,
			@JsonProperty("ohlc") List<Candle> ohlc) {
		this.pair = pair;
		this.ohlc = ohlc;
	}


	/**
	 * Represents a single OHLC candle.
	 */
	@Value
	public static class Candle {
		long timestamp;
		double open;
		double high;
		double low;
		double close;
		double volume;

		@JsonCreator
		public Candle(
				@JsonProperty("timestamp") long timestamp,
				@JsonProperty("open") double open,
				@JsonProperty("high") double high,
				@JsonProperty("low") double low,
				@JsonProperty("close") double close,
				@JsonProperty("volume") double volume) {
			this.timestamp = timestamp;
			this.open = open;
			this.high = high;
			this.low = low;
			this.close = close;
			this.volume = volume;
		}
	}

}
