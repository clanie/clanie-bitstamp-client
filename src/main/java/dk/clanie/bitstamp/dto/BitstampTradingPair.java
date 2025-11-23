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
 * Information about a trading pair.
 */
@Value
public class BitstampTradingPair {

	String name;
	String urlSymbol;
	int baseDecimals;
	int counterDecimals;
	int instantOrderCounterDecimals;
	double minimumOrder;
	BitstampTradingStatus trading;
	BitstampTradingStatus instantAndMarketOrders;
	String description;


	@JsonCreator
	public BitstampTradingPair(
			@JsonProperty("name") String name,
			@JsonProperty("url_symbol") String urlSymbol,
			@JsonProperty("base_decimals") int baseDecimals,
			@JsonProperty("counter_decimals") int counterDecimals,
			@JsonProperty("instant_order_counter_decimals") int instantOrderCounterDecimals,
			@JsonProperty("minimum_order") double minimumOrder,
			@JsonProperty("trading") BitstampTradingStatus trading,
			@JsonProperty("instant_and_market_orders") BitstampTradingStatus instantAndMarketOrders,
			@JsonProperty("description") String description) {
		this.name = name;
		this.urlSymbol = urlSymbol;
		this.baseDecimals = baseDecimals;
		this.counterDecimals = counterDecimals;
		this.instantOrderCounterDecimals = instantOrderCounterDecimals;
		this.minimumOrder = minimumOrder;
		this.trading = trading;
		this.instantAndMarketOrders = instantAndMarketOrders;
		this.description = description;
	}

}
