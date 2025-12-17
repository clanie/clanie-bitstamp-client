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

import tools.jackson.databind.ObjectMapper;

class BitstampUserTransactionTest {

	private final ObjectMapper objectMapper = new ObjectMapper();


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
		assertEquals(-100.50, transaction.getAmount(BitstampCurrencyCode.USD));
		assertEquals(0.0025, transaction.getAmount(BitstampCurrencyCode.BTC));
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


	@Test
	void testDeserializeMultipleCurrencies() throws Exception {
		String json = """
			{
				"id": 555555555,
				"datetime": "2025-01-15 13:29:19.362050",
				"type": 2,
				"usd": -5000.00,
				"eur": 1000.00,
				"btc": 0.125,
				"eth": 2.5,
				"usdt": 500.00,
				"fee": 10.00,
				"order_id": 111222333,
				"market": "BTC/USD"
			}
		""";
		
		BitstampUserTransaction transaction = objectMapper.readValue(json, BitstampUserTransaction.class);
		
		assertNotNull(transaction);
		assertEquals(555555555L, transaction.getId());
		
		// Verify all currency amounts are captured in the map
		assertEquals(-5000.00, transaction.getAmount(BitstampCurrencyCode.USD));
		assertEquals(1000.00, transaction.getAmount(BitstampCurrencyCode.EUR));
		assertEquals(0.125, transaction.getAmount(BitstampCurrencyCode.BTC));
		assertEquals(2.5, transaction.getAmount(BitstampCurrencyCode.ETH));
		assertEquals(500.00, transaction.getAmount(BitstampCurrencyCode.USDT));
		
		// Verify currency not present returns null
		assertEquals(null, transaction.getAmount(BitstampCurrencyCode.XRP));
		
		// Verify the map contains exactly 5 currencies
		assertEquals(5, transaction.getCurrencyAmounts().size());
	}


	@Test
	void testDeserializeStringCurrencyAmounts() throws Exception {
		String json = """
			{
				"id": 777777777,
				"datetime": "2025-01-15 13:29:19.362050",
				"type": 2,
				"usd": "-1234.56",
				"btc": "0.00012345",
				"eth": "1.5",
				"fee": "5.50",
				"order_id": 999888777
			}
		""";
		
		BitstampUserTransaction transaction = objectMapper.readValue(json, BitstampUserTransaction.class);
		
		assertNotNull(transaction);
		assertEquals(777777777L, transaction.getId());
		
		// Verify string currency amounts are parsed correctly
		assertEquals(-1234.56, transaction.getAmount(BitstampCurrencyCode.USD));
		assertEquals(0.00012345, transaction.getAmount(BitstampCurrencyCode.BTC));
		assertEquals(1.5, transaction.getAmount(BitstampCurrencyCode.ETH));
		assertEquals(5.50, transaction.getFee());
		
		// Verify the map contains exactly 3 currencies
		assertEquals(3, transaction.getCurrencyAmounts().size());
	}


	@Test
	void testDeserializeMixedNumberAndStringAmounts() throws Exception {
		String json = """
			{
				"id": 888888888,
				"datetime": "2025-01-15 13:29:19.362050",
				"type": 2,
				"usd": -100.50,
				"btc": "0.0025",
				"eth": 2.5,
				"usdt": "500.00",
				"fee": 0.50
			}
		""";
		
		BitstampUserTransaction transaction = objectMapper.readValue(json, BitstampUserTransaction.class);
		
		assertNotNull(transaction);
		
		// Verify mixed number and string values are handled correctly
		assertEquals(-100.50, transaction.getAmount(BitstampCurrencyCode.USD));
		assertEquals(0.0025, transaction.getAmount(BitstampCurrencyCode.BTC));
		assertEquals(2.5, transaction.getAmount(BitstampCurrencyCode.ETH));
		assertEquals(500.00, transaction.getAmount(BitstampCurrencyCode.USDT));
		
		assertEquals(4, transaction.getCurrencyAmounts().size());
	}


	@Test
	void testDeserializeWithExchangeRate() throws Exception {
		String json = """
			{
				"id": 999999999,
				"datetime": "2025-01-15 13:29:19.362050",
				"type": 2,
				"usd": -94201.00,
				"btc": 1.0,
				"btc_usd": 94201.0,
				"fee": 50.00,
				"order_id": 123456789,
				"market": "BTC/USD"
			}
		""";
		
		BitstampUserTransaction transaction = objectMapper.readValue(json, BitstampUserTransaction.class);
		
		assertNotNull(transaction);
		assertEquals(999999999L, transaction.getId());
		
		// Verify currency amounts
		assertEquals(-94201.00, transaction.getAmount(BitstampCurrencyCode.USD));
		assertEquals(1.0, transaction.getAmount(BitstampCurrencyCode.BTC));
		
		// Verify exchange rate is captured
		assertNotNull(transaction.getExchangeRate());
		BitstampCurrencyPair btcUsd = new BitstampCurrencyPair(BitstampCurrencyCode.BTC, BitstampCurrencyCode.USD);
		assertEquals(btcUsd, transaction.getExchangeRate().getCurrencyPair());
		assertEquals(94201.0, transaction.getExchangeRate().getRate());
	}


	@Test
	void testDeserializeTransactionTypes() throws Exception {
		// Test DEPOSIT (type 0)
		String depositJson = """
			{
				"id": 1,
				"datetime": "2025-01-15 13:29:19.362050",
				"type": 0,
				"btc": 1.0
			}
		""";
		BitstampUserTransaction deposit = objectMapper.readValue(depositJson, BitstampUserTransaction.class);
		assertEquals(BitstampUserTransactionType.DEPOSIT, deposit.getType());
		
		// Test WITHDRAWAL (type 1)
		String withdrawalJson = """
			{
				"id": 2,
				"datetime": "2025-01-15 13:29:19.362050",
				"type": 1,
				"eur": -1000.0,
				"fee": 3.0
			}
		""";
		BitstampUserTransaction withdrawal = objectMapper.readValue(withdrawalJson, BitstampUserTransaction.class);
		assertEquals(BitstampUserTransactionType.WITHDRAWAL, withdrawal.getType());
		
		// Test MARKET_TRADE (type 2)
		String tradeJson = """
			{
				"id": 3,
				"datetime": "2025-01-15 13:29:19.362050",
				"type": 2,
				"btc": -0.1,
				"eur": 9000.0,
				"btc_eur": 90000.0,
				"fee": 5.0
			}
		""";
		BitstampUserTransaction trade = objectMapper.readValue(tradeJson, BitstampUserTransaction.class);
		assertEquals(BitstampUserTransactionType.MARKET_TRADE, trade.getType());
	}


	@Test
	void testDeserializeWithMultipleExchangeRates() throws Exception {
		String json = """
			{
				"id": 111111111,
				"datetime": "2025-01-15 13:29:19.362050",
				"type": 2,
				"eur": -94201.00,
				"btc": 1.0,
				"usd": 100000.00,
				"btc_eur": 94201.0,
				"fee": 50.00
			}
		""";
		
		BitstampUserTransaction transaction = objectMapper.readValue(json, BitstampUserTransaction.class);
		
		assertNotNull(transaction);
		
		// Verify currency amounts
		assertEquals(-94201.00, transaction.getAmount(BitstampCurrencyCode.EUR));
		assertEquals(1.0, transaction.getAmount(BitstampCurrencyCode.BTC));
		assertEquals(100000.00, transaction.getAmount(BitstampCurrencyCode.USD));
		
		// Verify exchange rate is captured (only one exchange rate field now)
		assertNotNull(transaction.getExchangeRate());
		BitstampCurrencyPair btcEur = new BitstampCurrencyPair(BitstampCurrencyCode.BTC, BitstampCurrencyCode.EUR);
		assertEquals(btcEur, transaction.getExchangeRate().getCurrencyPair());
		assertEquals(94201.0, transaction.getExchangeRate().getRate());
	}

}
