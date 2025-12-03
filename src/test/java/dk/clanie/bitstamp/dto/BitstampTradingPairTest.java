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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class BitstampTradingPairTest {

	private final ObjectMapper objectMapper = new ObjectMapper();


	@Test
	void testDeserializeTradingPair() throws Exception {
		String json = """
			{
				"name": "BTC/USD",
				"url_symbol": "btcusd",
				"base_decimals": 8,
				"counter_decimals": 2,
				"instant_order_counter_decimals": 2,
				"minimum_order": "10 USD",
				"trading": "Enabled",
				"instant_and_market_orders": "Enabled",
				"description": "Bitcoin / U.S. dollar"
			}
		""";
		
		BitstampTradingPair pair = objectMapper.readValue(json, BitstampTradingPair.class);
		
		assertNotNull(pair);
		assertEquals("BTC/USD", pair.getName());
		assertEquals("btcusd", pair.getUrlSymbol());
		assertEquals(8, pair.getBaseDecimals());
		assertEquals(2, pair.getCounterDecimals());
		assertEquals(2, pair.getInstantOrderCounterDecimals());
		
		assertNotNull(pair.getMinimumOrder());
		assertEquals(10.0, pair.getMinimumOrder().getAmount());
		assertEquals(BitstampCurrencyCode.USD, pair.getMinimumOrder().getCurrencyCode());
		
		assertEquals(BitstampTradingStatus.ENABLED, pair.getTrading());
		assertEquals(BitstampTradingStatus.ENABLED, pair.getInstantAndMarketOrders());
		assertEquals("Bitcoin / U.S. dollar", pair.getDescription());
	}

}
