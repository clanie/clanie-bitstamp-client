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

import static dk.clanie.bitstamp.dto.BitstampMarketType.SPOT;
import static dk.clanie.bitstamp.dto.BitstampTradeSide.BUY;
import static dk.clanie.bitstamp.dto.BitstampTradeSide.SELL;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class BitstampTickerTest {

	@Test
	void testJsonDeserialization() throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		BitstampTicker ticker = objectMapper.readValue("""
				{
				  "high": "48850.00",
				  "last": "47800.00",
				  "timestamp": "1637597234",
				  "bid": "47795.00",
				  "vwap": "48234.56",
				  "volume": "1234.56789012",
				  "low": "47200.00",
				  "ask": "47805.00",
				  "side": "1",
				  "open": "48500.00",
				  "open_24": "48000.00",
				  "percent_change_24": "-0.42",
				  "market_type": "SPOT"
				}
				""", BitstampTicker.class);
		
		assertThat(ticker.getHigh()).isEqualTo(48850.00);
		assertThat(ticker.getLast()).isEqualTo(47800.00);
		assertThat(ticker.getTimestamp()).isEqualTo(1637597234L);
		assertThat(ticker.getBid()).isEqualTo(47795.00);
		assertThat(ticker.getVwap()).isEqualTo(48234.56);
		assertThat(ticker.getVolume()).isEqualTo(1234.56789012);
		assertThat(ticker.getLow()).isEqualTo(47200.00);
		assertThat(ticker.getAsk()).isEqualTo(47805.00);
		assertThat(ticker.getSide()).isEqualTo(SELL);
		assertThat(ticker.getOpen()).isEqualTo(48500.00);
		assertThat(ticker.getOpen24()).isEqualTo(48000.00);
		assertThat(ticker.getPercentChange24()).isEqualTo(-0.42);
		assertThat(ticker.getMarketType()).isEqualTo(SPOT);
		assertThat(ticker.getIndexPrice()).isNull();
	}

	@Test
	void testJsonDeserializationWithIndexPrice() throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		BitstampTicker ticker = objectMapper.readValue("""
				{
				  "timestamp": "1763860037",
				  "open": "1.15083",
				  "high": "1.15160",
				  "low": "1.14933",
				  "last": "1.15072",
				  "volume": "289594.68039",
				  "vwap": "1.15057",
				  "bid": "1.15037",
				  "ask": "1.15057",
				  "side": "0",
				  "open_24": "1.15087",
				  "percent_change_24": "-0.01",
				  "market_type": "SPOT",
				  "index_price": "1.15055"
				}
				""", BitstampTicker.class);
		
		assertThat(ticker.getTimestamp()).isEqualTo(1763860037L);
		assertThat(ticker.getLast()).isEqualTo(1.15072);
		assertThat(ticker.getSide()).isEqualTo(BUY);
		assertThat(ticker.getMarketType()).isEqualTo(SPOT);
		assertThat(ticker.getIndexPrice()).isEqualTo(1.15055);
	}

}
