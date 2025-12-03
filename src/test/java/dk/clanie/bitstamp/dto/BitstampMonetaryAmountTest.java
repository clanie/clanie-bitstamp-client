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

class BitstampMonetaryAmountTest {

	private final ObjectMapper objectMapper = new ObjectMapper();


	@Test
	void testDeserializeBitstampMonetaryAmount() throws Exception {
		String json = "\"10 USD\"";
		BitstampMonetaryAmount amount = objectMapper.readValue(json, BitstampMonetaryAmount.class);
		
		assertNotNull(amount);
		assertEquals(10.0, amount.getAmount());
		assertEquals(BitstampCurrencyCode.USD, amount.getCurrencyCode());
	}


	@Test
	void testDeserializeDecimalAmount() throws Exception {
		String json = "\"0.0001 BTC\"";
		BitstampMonetaryAmount amount = objectMapper.readValue(json, BitstampMonetaryAmount.class);
		
		assertNotNull(amount);
		assertEquals(0.0001, amount.getAmount());
		assertEquals(BitstampCurrencyCode.BTC, amount.getCurrencyCode());
	}


	@Test
	void testToString() {
		BitstampMonetaryAmount amount = new BitstampMonetaryAmount("10.5", "USD");
		assertEquals("10.5 USD", amount.toString());
	}

}
