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

import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.BTC;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.EUR;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.GBP;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.USD;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.XRP;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode._1INCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

class BitstampCurrencyPairTest {

	@Test
	void testConstructorWithEnums() {
		BitstampCurrencyPair pair = new BitstampCurrencyPair(BTC, EUR);
		
		assertThat(pair.getBaseCurrency()).isEqualTo(BTC);
		assertThat(pair.getQuoteCurrency()).isEqualTo(EUR);
		assertThat(pair.toStringWithSlash()).isEqualTo("BTC/EUR");
		assertThat(pair.toUrlParameter()).isEqualTo("btceur");
		assertThat(pair.toString()).isEqualTo("BTC/EUR");
	}

	@Test
	void testFromString() {
		BitstampCurrencyPair pair = BitstampCurrencyPair.fromString("BTC/USD");
		
		assertThat(pair.getBaseCurrency()).isEqualTo(BTC);
		assertThat(pair.getQuoteCurrency()).isEqualTo(USD);
	}

	@Test
	void testFromStringLowercase() {
		BitstampCurrencyPair pair = BitstampCurrencyPair.fromString("btc/usd");
		
		assertThat(pair.getBaseCurrency()).isEqualTo(BTC);
		assertThat(pair.getQuoteCurrency()).isEqualTo(USD);
	}

	@Test
	void testFromStringWithPerpetual() {
		BitstampCurrencyPair pair = BitstampCurrencyPair.fromString("BTC/USD-PERP");
		
		assertThat(pair.getBaseCurrency()).isEqualTo(BTC);
		assertThat(pair.getQuoteCurrency()).isEqualTo(BitstampCurrencyCode.USD_PERP);
		assertThat(pair.toUrlParameter()).isEqualTo("btcusd-perp");
	}

	@Test
	void testFromStringInvalidFormat() {
		assertThatThrownBy(() -> BitstampCurrencyPair.fromString("BTCUSD"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Invalid pair format");
	}

	@Test
	void testFromStringNull() {
		assertThatThrownBy(() -> BitstampCurrencyPair.fromString(null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("cannot be null");
	}

	@Test
	void testFromStringEmpty() {
		assertThatThrownBy(() -> BitstampCurrencyPair.fromString(""))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("cannot be null or empty");
	}

	@Test
	void testConstructorNullBaseCurrency() {
		assertThatThrownBy(() -> new BitstampCurrencyPair(null, USD))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Base currency cannot be null");
	}

	@Test
	void testConstructorNullQuoteCurrency() {
		assertThatThrownBy(() -> new BitstampCurrencyPair(BTC, null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Quote currency cannot be null");
	}

	@Test
	void testEquality() {
		BitstampCurrencyPair pair1 = new BitstampCurrencyPair(BTC, USD);
		BitstampCurrencyPair pair2 = BitstampCurrencyPair.fromString("BTC/USD");
		BitstampCurrencyPair pair3 = new BitstampCurrencyPair(BTC, USD);
		
		assertThat(pair1).isEqualTo(pair2);
		assertThat(pair1).isEqualTo(pair3);
		assertThat(pair2).isEqualTo(pair3);
	}

	@Test
	void testJsonSerialization() throws JacksonException {
		ObjectMapper objectMapper = new ObjectMapper();
		BitstampCurrencyPair pair = new BitstampCurrencyPair(BTC, USD);
		
		String json = objectMapper.writeValueAsString(pair);
		
		assertThat(json).isEqualTo("\"BTC/USD\"");
	}

	@Test
	void testJsonDeserialization() throws JacksonException {
		ObjectMapper objectMapper = new ObjectMapper();
		
		BitstampCurrencyPair pair = objectMapper.readValue("\"BTC/USD\"", BitstampCurrencyPair.class);
		
		assertThat(pair.getBaseCurrency()).isEqualTo(BTC);
		assertThat(pair.getQuoteCurrency()).isEqualTo(USD);
	}

	@Test
	void testSpecialCases() {
		// Test with 1INCH
		BitstampCurrencyPair pair1 = new BitstampCurrencyPair(_1INCH, USD);
		assertThat(pair1.getBaseCurrency()).isEqualTo(_1INCH);
		assertThat(pair1.toStringWithSlash()).isEqualTo("1INCH/USD");
		assertThat(pair1.toUrlParameter()).isEqualTo("1inchusd");
		
		// Test with XRP/GBP
		BitstampCurrencyPair pair2 = BitstampCurrencyPair.fromString("XRP/GBP");
		assertThat(pair2.getBaseCurrency()).isEqualTo(XRP);
		assertThat(pair2.getQuoteCurrency()).isEqualTo(GBP);
		assertThat(pair2.toUrlParameter()).isEqualTo("xrpgbp");
	}

}
