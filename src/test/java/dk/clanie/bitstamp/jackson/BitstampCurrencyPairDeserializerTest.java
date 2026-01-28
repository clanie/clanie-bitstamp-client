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
package dk.clanie.bitstamp.jackson;

import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.BTC;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.USD;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import dk.clanie.bitstamp.dto.BitstampCurrencyPair;
import tools.jackson.databind.ObjectMapper;

class BitstampCurrencyPairDeserializerTest {

	private final ObjectMapper objectMapper = new ObjectMapper();


	@AfterEach
	void cleanup() {
		BitstampCurrencyPairDeserializer.clearUnknownCurrencyCodes();
	}


	@Test
	void testDeserializeValidPair() throws Exception {
		BitstampCurrencyPair pair = objectMapper.readValue("\"BTC/USD\"", BitstampCurrencyPair.class);
		
		assertThat(pair.getBaseCurrency()).isEqualTo(BTC);
		assertThat(pair.getQuoteCurrency()).isEqualTo(USD);
		assertThat(BitstampCurrencyPairDeserializer.getUnknownCurrencyCodes()).isEmpty();
	}


	@Test
	void testDeserializeWithUnknownBaseCurrency() throws Exception {
		BitstampCurrencyPair pair = objectMapper.readValue("\"UNKNOWN1/USD\"", BitstampCurrencyPair.class);
		
		// Deserializer should continue with dummy values
		assertThat(pair).isNotNull();
		
		// But unknown code should be collected
		Set<String> unknownCodes = BitstampCurrencyPairDeserializer.getUnknownCurrencyCodes();
		assertThat(unknownCodes).containsExactly("UNKNOWN1");
	}


	@Test
	void testDeserializeWithUnknownQuoteCurrency() throws Exception {
		BitstampCurrencyPair pair = objectMapper.readValue("\"BTC/UNKNOWN2\"", BitstampCurrencyPair.class);
		
		assertThat(pair).isNotNull();
		
		Set<String> unknownCodes = BitstampCurrencyPairDeserializer.getUnknownCurrencyCodes();
		assertThat(unknownCodes).containsExactly("UNKNOWN2");
	}


	@Test
	void testDeserializeWithBothUnknown() throws Exception {
		BitstampCurrencyPair pair = objectMapper.readValue("\"UNKNOWN1/UNKNOWN2\"", BitstampCurrencyPair.class);
		
		assertThat(pair).isNotNull();
		
		Set<String> unknownCodes = BitstampCurrencyPairDeserializer.getUnknownCurrencyCodes();
		assertThat(unknownCodes).containsExactlyInAnyOrder("UNKNOWN1", "UNKNOWN2");
	}


	@Test
	void testClearUnknownCurrencyCodes() throws Exception {
		objectMapper.readValue("\"UNKNOWN1/USD\"", BitstampCurrencyPair.class);
		assertThat(BitstampCurrencyPairDeserializer.getUnknownCurrencyCodes()).isNotEmpty();
		
		BitstampCurrencyPairDeserializer.clearUnknownCurrencyCodes();
		assertThat(BitstampCurrencyPairDeserializer.getUnknownCurrencyCodes()).isEmpty();
	}


}
