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

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

class BitstampUserTransactionTest {

	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


	@Test
	void testDeserializeUserTransaction() throws Exception {
		String json = """
			{
				"id": 123456789,
				"datetime": "2025-01-15 13:29:19.362050",
				"type": 2,
				"usd": -100.50,
				"btc": 0.0025,
				"fee": 0.50,
				"order_id": 987654321,
				"market": "BTC/USD"
			}
		""";
		
		BitstampUserTransaction transaction = objectMapper.readValue(json, BitstampUserTransaction.class);
		
		assertNotNull(transaction);
		assertEquals(123456789L, transaction.getId());
		assertEquals(Instant.parse("2025-01-15T13:29:19.362050Z"), transaction.getDatetime());
		assertEquals(BitstampUserTransactionType.MARKET_TRADE, transaction.getType());
		assertEquals(-100.50, transaction.getUsd());
		assertEquals(0.0025, transaction.getBtc());
		assertEquals(0.50, transaction.getFee());
		assertEquals(987654321L, transaction.getOrderId());
		assertEquals("BTC/USD", transaction.getMarket());
	}


	@Test
	void testDeserializeUserTransactionMinimal() throws Exception {
		String json = """
			{
				"id": 123456789,
				"datetime": "2025-12-03 20:30:00.000000",
				"type": 0
			}
		""";
		
		BitstampUserTransaction transaction = objectMapper.readValue(json, BitstampUserTransaction.class);
		
		assertNotNull(transaction);
		assertEquals(123456789L, transaction.getId());
		assertEquals(Instant.parse("2025-12-03T20:30:00.000000Z"), transaction.getDatetime());
		assertEquals(BitstampUserTransactionType.DEPOSIT, transaction.getType());
	}


	@Test
	void testDeserializeDatetimeWithoutMicroseconds() throws Exception {
		String json = """
			{
				"id": 987654321,
				"datetime": "2018-01-15 11:56:24",
				"type": 1
			}
		""";
		
		BitstampUserTransaction transaction = objectMapper.readValue(json, BitstampUserTransaction.class);
		
		assertNotNull(transaction);
		assertEquals(987654321L, transaction.getId());
		assertEquals(Instant.parse("2018-01-15T11:56:24Z"), transaction.getDatetime());
		assertEquals(BitstampUserTransactionType.WITHDRAWAL, transaction.getType());
	}

}
