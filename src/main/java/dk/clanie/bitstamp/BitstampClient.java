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
package dk.clanie.bitstamp;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import dk.clanie.bitstamp.dto.BitstampOhlcData;
import dk.clanie.bitstamp.dto.BitstampOrderBook;
import dk.clanie.bitstamp.dto.BitstampTicker;
import dk.clanie.bitstamp.dto.BitstampTradingPair;
import dk.clanie.bitstamp.dto.BitstampTransaction;
import dk.clanie.web.RestClientFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BitstampClient {

	private final RestClientFactory restClientFactory;

	@Value("${bitstamp.url:https://www.bitstamp.net/api/v2}")
	private String baseUrl;

	@Value("${bitstamp.wiretap:false}")
	private boolean wiretap;

	private RestClient restClient;


	@PostConstruct
	public void init() {
		restClient = restClientFactory.newRestClient(baseUrl, wiretap);
	}


	/**
	 * Gets ticker data for a specific currency pair.
	 * <p/>
	 * Returns ticker data for the specified currency pair.
	 * 
	 * @param currencyPair the currency pair (e.g., "btcusd", "ethusd")
	 * @return ticker data
	 */
	public BitstampTicker getTicker(String currencyPair) {
		return restClient.get()
				.uri("/ticker/{currencyPair}/", currencyPair)
				.retrieve()
				.body(BitstampTicker.class);
	}


	/**
	 * Gets hourly ticker data for a specific currency pair.
	 * <p/>
	 * Returns hourly ticker data for the specified currency pair.
	 * 
	 * @param currencyPair the currency pair (e.g., "btcusd", "ethusd")
	 * @return ticker data
	 */
	public BitstampTicker getHourlyTicker(String currencyPair) {
		return restClient.get()
				.uri("/ticker_hour/{currencyPair}/", currencyPair)
				.retrieve()
				.body(BitstampTicker.class);
	}


	/**
	 * Gets order book for a specific currency pair.
	 * <p/>
	 * Returns a list of bids and asks for the specified currency pair.
	 * 
	 * @param currencyPair the currency pair (e.g., "btcusd", "ethusd")
	 * @return order book data
	 */
	public BitstampOrderBook getOrderBook(String currencyPair) {
		return restClient.get()
				.uri("/order_book/{currencyPair}/", currencyPair)
				.retrieve()
				.body(BitstampOrderBook.class);
	}


	/**
	 * Gets recent transactions for a specific currency pair.
	 * <p/>
	 * Returns a list of recent transactions. By default, returns the last 100 transactions.
	 * 
	 * @param currencyPair the currency pair (e.g., "btcusd", "ethusd")
	 * @param time time frame: "minute", "hour", or "day"
	 * @return list of transactions
	 */
	public List<BitstampTransaction> getTransactions(String currencyPair, String time) {
		return restClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/transactions/{currencyPair}/")
						.queryParam("time", time)
						.build(currencyPair))
				.retrieve()
				.body(new ParameterizedTypeReference<List<BitstampTransaction>>() {});
	}


	/**
	 * Gets recent transactions for a specific currency pair.
	 * <p/>
	 * Returns a list of recent transactions. By default, returns the last 100 transactions.
	 * 
	 * @param currencyPair the currency pair (e.g., "btcusd", "ethusd")
	 * @return list of transactions
	 */
	public List<BitstampTransaction> getTransactions(String currencyPair) {
		return restClient.get()
				.uri("/transactions/{currencyPair}/", currencyPair)
				.retrieve()
				.body(new ParameterizedTypeReference<List<BitstampTransaction>>() {});
	}


	/**
	 * Gets all trading pairs info.
	 * <p/>
	 * Returns info for all available trading pairs.
	 * 
	 * @return list of trading pairs
	 */
	public List<BitstampTradingPair> getTradingPairsInfo() {
		return restClient.get()
				.uri("/trading-pairs-info/")
				.retrieve()
				.body(new ParameterizedTypeReference<List<BitstampTradingPair>>() {});
	}


	/**
	 * Gets OHLC (candlestick) data for a specific currency pair.
	 * <p/>
	 * Returns OHLC data for the specified currency pair.
	 * 
	 * @param currencyPair the currency pair (e.g., "btcusd", "ethusd")
	 * @param step the time interval in seconds (60, 180, 300, 900, 1800, 3600, 7200, 14400, 21600, 43200, 86400, 259200)
	 * @param limit the number of data points (default: 1000)
	 * @param start start timestamp (Unix timestamp)
	 * @param end end timestamp (Unix timestamp)
	 * @return OHLC data
	 */
	public BitstampOhlcData getOhlcData(String currencyPair, int step, Integer limit, Long start, Long end) {
		return restClient.get()
				.uri(uriBuilder -> {
					var builder = uriBuilder
							.path("/ohlc/{currencyPair}/")
							.queryParam("step", step);
					if (limit != null) {
						builder.queryParam("limit", limit);
					}
					if (start != null) {
						builder.queryParam("start", start);
					}
					if (end != null) {
						builder.queryParam("end", end);
					}
					return builder.build(currencyPair);
				})
				.retrieve()
				.body(BitstampOhlcData.class);
	}


	/**
	 * Gets OHLC (candlestick) data for a specific currency pair with default settings.
	 * <p/>
	 * Returns OHLC data for the specified currency pair.
	 * 
	 * @param currencyPair the currency pair (e.g., "btcusd", "ethusd")
	 * @param step the time interval in seconds (60, 180, 300, 900, 1800, 3600, 7200, 14400, 21600, 43200, 86400, 259200)
	 * @return OHLC data
	 */
	public BitstampOhlcData getOhlcData(String currencyPair, int step) {
		return getOhlcData(currencyPair, step, null, null, null);
	}


}
