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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.ObjectMapper;

class BitstampOhlcDataTest {

	@Test
	void testJsonDeserialization() throws DatabindException, JacksonException {
		ObjectMapper objectMapper = new ObjectMapper();
		BitstampOhlcData ohlcData = objectMapper.readValue("""
				{
				  "pair": "BTC/USD",
				  "ohlc": [
				    {
				      "timestamp": "1637596800",
				      "open": "48500.00",
				      "high": "48850.00",
				      "low": "47200.00",
				      "close": "47800.00",
				      "volume": "1234.56789012"
				    },
				    {
				      "timestamp": "1637600400",
				      "open": "47800.00",
				      "high": "48200.00",
				      "low": "47500.00",
				      "close": "48000.00",
				      "volume": "987.65432109"
				    }
				  ]
				}
				""", BitstampOhlcData.class);
		
		assertThat(ohlcData.getPair()).isEqualTo("BTC/USD");
		assertThat(ohlcData.getOhlc()).hasSize(2);
		
		BitstampOhlcData.Candle candle1 = ohlcData.getOhlc().get(0);
		assertThat(candle1.getTimestamp()).isEqualTo(1637596800L);
		assertThat(candle1.getOpen()).isEqualTo(48500.00);
		assertThat(candle1.getHigh()).isEqualTo(48850.00);
		assertThat(candle1.getLow()).isEqualTo(47200.00);
		assertThat(candle1.getClose()).isEqualTo(47800.00);
		assertThat(candle1.getVolume()).isEqualTo(1234.56789012);
	}

}
