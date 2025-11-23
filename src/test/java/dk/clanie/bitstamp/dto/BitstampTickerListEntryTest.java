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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class BitstampTickerListEntryTest {

	@Test
	void testJsonDeserializationSpotMarket() throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		BitstampTickerListEntry ticker = objectMapper.readValue("""
				{
				  "timestamp": "1763854710",
				  "open": "85054",
				  "high": "85530",
				  "low": "83432",
				  "last": "84921",
				  "volume": "2426.07836092",
				  "vwap": "84433",
				  "bid": "84920",
				  "ask": "84921",
				  "side": "0",
				  "open_24": "84878",
				  "percent_change_24": "0.05",
				  "market_type": "SPOT",
				  "pair": "BTC/USD",
				  "market": "BTC/USD"
				}
				""", BitstampTickerListEntry.class);
		
		assertThat(ticker.getTimestamp()).isEqualTo(1763854710L);
		assertThat(ticker.getOpen()).isEqualTo(85054.0);
		assertThat(ticker.getHigh()).isEqualTo(85530.0);
		assertThat(ticker.getLow()).isEqualTo(83432.0);
		assertThat(ticker.getLast()).isEqualTo(84921.0);
		assertThat(ticker.getVolume()).isEqualTo(2426.07836092);
		assertThat(ticker.getVwap()).isEqualTo(84433.0);
		assertThat(ticker.getBid()).isEqualTo(84920.0);
		assertThat(ticker.getAsk()).isEqualTo(84921.0);
		assertThat(ticker.getSide()).isEqualTo(BitstampTradeSide.BUY);
		assertThat(ticker.getOpen24()).isEqualTo(84878.0);
		assertThat(ticker.getPercentChange24()).isEqualTo(0.05);
		assertThat(ticker.getMarketType()).isEqualTo(BitstampMarketType.SPOT);
		assertThat(ticker.getPair()).isEqualTo("BTC/USD");
		assertThat(ticker.getMarket()).isEqualTo("BTC/USD");
		// Optional fields should be null for SPOT markets
		assertThat(ticker.getIndexPrice()).isNull();
		assertThat(ticker.getMarkPrice()).isNull();
		assertThat(ticker.getOpenInterest()).isNull();
		assertThat(ticker.getOpenInterestValue()).isNull();
	}

	@Test
	void testJsonDeserializationWithEmptyPercentChange() throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		BitstampTickerListEntry ticker = objectMapper.readValue("""
				{
				  "timestamp": "1763854710",
				  "open": "0.00000",
				  "high": "0.00000",
				  "low": "0.00000",
				  "last": "1.20000",
				  "volume": "0.00000",
				  "vwap": "0.00000",
				  "bid": "0.95263",
				  "ask": "1.00000",
				  "side": "1",
				  "open_24": "0.00000",
				  "percent_change_24": "",
				  "market_type": "SPOT",
				  "pair": "DAI/USD",
				  "market": "DAI/USD"
				}
				""", BitstampTickerListEntry.class);
		
		assertThat(ticker.getTimestamp()).isEqualTo(1763854710L);
		assertThat(ticker.getOpen()).isEqualTo(0.0);
		assertThat(ticker.getLast()).isEqualTo(1.20000);
		assertThat(ticker.getSide()).isEqualTo(BitstampTradeSide.SELL);
		// Empty string should be deserialized to null
		assertThat(ticker.getPercentChange24()).isNull();
		assertThat(ticker.getMarketType()).isEqualTo(BitstampMarketType.SPOT);
		assertThat(ticker.getPair()).isEqualTo("DAI/USD");
		assertThat(ticker.getMarket()).isEqualTo("DAI/USD");
	}

	@Test
	void testJsonDeserializationPerpetualMarket() throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		BitstampTickerListEntry ticker = objectMapper.readValue("""
				{
				  "timestamp": "1763854710",
				  "open": "85094",
				  "high": "85546",
				  "low": "83473",
				  "last": "84927",
				  "volume": "19.55393",
				  "vwap": "84287",
				  "bid": "84927",
				  "ask": "84928",
				  "side": "1",
				  "open_24": "84913",
				  "percent_change_24": "0.02",
				  "market_type": "PERPETUAL",
				  "pair": "BTC/USD-PERP",
				  "market": "BTC/USD-PERP",
				  "index_price": "84911.57133333334",
				  "mark_price": "84904.06679064",
				  "open_interest": "8.99503",
				  "open_interest_value": "763714.6279038105192"
				}
				""", BitstampTickerListEntry.class);
		
		assertThat(ticker.getTimestamp()).isEqualTo(1763854710L);
		assertThat(ticker.getOpen()).isEqualTo(85094.0);
		assertThat(ticker.getHigh()).isEqualTo(85546.0);
		assertThat(ticker.getLow()).isEqualTo(83473.0);
		assertThat(ticker.getLast()).isEqualTo(84927.0);
		assertThat(ticker.getVolume()).isEqualTo(19.55393);
		assertThat(ticker.getVwap()).isEqualTo(84287.0);
		assertThat(ticker.getBid()).isEqualTo(84927.0);
		assertThat(ticker.getAsk()).isEqualTo(84928.0);
		assertThat(ticker.getSide()).isEqualTo(BitstampTradeSide.SELL);
		assertThat(ticker.getOpen24()).isEqualTo(84913.0);
		assertThat(ticker.getPercentChange24()).isEqualTo(0.02);
		assertThat(ticker.getMarketType()).isEqualTo(BitstampMarketType.PERPETUAL);
		assertThat(ticker.getPair()).isEqualTo("BTC/USD-PERP");
		assertThat(ticker.getMarket()).isEqualTo("BTC/USD-PERP");
		// PERPETUAL markets should have these optional fields
		assertThat(ticker.getIndexPrice()).isEqualTo(84911.57133333334);
		assertThat(ticker.getMarkPrice()).isEqualTo(84904.06679064);
		assertThat(ticker.getOpenInterest()).isEqualTo(8.99503);
		assertThat(ticker.getOpenInterestValue()).isEqualTo(763714.6279038105192);
	}

}
