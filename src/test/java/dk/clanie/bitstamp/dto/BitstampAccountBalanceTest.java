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

class BitstampAccountBalanceTest {

	private final ObjectMapper objectMapper = new ObjectMapper();


	@Test
	void testDeserializeAccountBalance() throws Exception {
		String json = """
			{
				"currency": "usd",
				"total": "100.00",
				"available": "90.00",
				"reserved": "10.00"
			}
		""";
		
		BitstampAccountBalance balance = objectMapper.readValue(json, BitstampAccountBalance.class);
		
		assertNotNull(balance);
		assertEquals(BitstampCurrencyCode.USD, balance.getCurrency());
		assertEquals(100.00, balance.getTotal());
		assertEquals(90.00, balance.getAvailable());
		assertEquals(10.00, balance.getReserved());
	}

}
