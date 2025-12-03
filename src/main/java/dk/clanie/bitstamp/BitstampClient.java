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
package dk.clanie.bitstamp;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import dk.clanie.bitstamp.dto.BitstampCurrency;
import dk.clanie.bitstamp.dto.BitstampOhlcData;
import dk.clanie.bitstamp.dto.BitstampOrderBook;
import dk.clanie.bitstamp.dto.BitstampTicker;
import dk.clanie.bitstamp.dto.BitstampTickerListEntry;
import dk.clanie.bitstamp.dto.BitstampTradingPair;
import dk.clanie.bitstamp.dto.BitstampTransaction;
import dk.clanie.bitstamp.dto.BitstampUserTransaction;
import dk.clanie.web.RestClientFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BitstampClient {

	private final RestClientFactory restClientFactory;

	@Value("${bitstamp.url:https://www.bitstamp.net}")
	private String baseUrl;

	@Value("${bitstamp.wiretap:false}")
	private boolean wiretap;

	private RestClient restClient;


	@PostConstruct
	public void init() {
		restClient = restClientFactory.newRestClient(baseUrl, wiretap);
	}


	/**
	 * Gets all available currencies.
	 * <p/>
	 * Returns info for all available currencies including their networks, deposit/withdrawal status, and other metadata.
	 * 
	 * @return list of currencies
	 */
	public List<BitstampCurrency> getCurrencies() {
		return restClient.get()
				.uri("/api/v2/currencies/")
				.retrieve()
				.body(new ParameterizedTypeReference<List<BitstampCurrency>>() {});
	}


	/**
	 * Gets ticker data for all currency pairs.
	 * <p/>
	 * Returns ticker data for all available currency pairs.
	 * 
	 * @return list of ticker data for all pairs
	 */
	public List<BitstampTickerListEntry> listTickers() {
		return restClient.get()
				.uri("/api/v2/ticker/")
				.retrieve()
				.body(new ParameterizedTypeReference<List<BitstampTickerListEntry>>() {});
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
				.uri("/api/v2/ticker/{currencyPair}/", currencyPair)
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
				.uri("/api/v2/ticker_hour/{currencyPair}/", currencyPair)
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
				.uri("/api/v2/order_book/{currencyPair}/", currencyPair)
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
						.path("/api/v2/transactions/{currencyPair}/")
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
				.uri("/api/v2/transactions/{currencyPair}/", currencyPair)
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
				.uri("/api/v2/trading-pairs-info/")
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
							.path("/api/v2/ohlc/{currencyPair}/")
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


	/**
	 * Gets user transactions (private API endpoint).
	 * <p/>
	 * Returns user transactions including deposits, withdrawals, and trades.
	 * Requires authentication with API key and secret.
	 * 
	 * @param credentials the Bitstamp API credentials
	 * @param currencyPair the currency pair (optional, e.g., "btcusd", "ethusd")
	 * @param offset offset for pagination (optional)
	 * @param limit number of transactions to return (optional, default: 100, max: 1000)
	 * @param sort sorting order: "asc" or "desc" (optional, default: "desc")
	 * @param sinceId return transactions since this ID (optional)
	 * @param sinceTimestamp return transactions since this timestamp (optional)
	 * @return list of user transactions
	 * @throws IllegalArgumentException if credentials is null
	 */
	public List<BitstampUserTransaction> getUserTransactions(
			BitstampCredentials credentials,
			String currencyPair, 
			Integer offset, 
			Integer limit, 
			String sort,
			Long sinceId,
			Long sinceTimestamp) {
		
		if (credentials == null) {
			throw new IllegalArgumentException("Credentials cannot be null");
		}

		// Build the path
		String path = "/api/v2/user_transactions/";
		if (currencyPair != null && !currencyPair.isEmpty()) {
			path = "/api/v2/user_transactions/" + currencyPair + "/";
		}

		// Build query string - used for both authentication signature and request URI
		StringBuilder queryParams = new StringBuilder();
		if (offset != null) {
			queryParams.append("offset=").append(offset).append("&");
		}
		if (limit != null) {
			queryParams.append("limit=").append(limit).append("&");
		}
		if (sort != null) {
			queryParams.append("sort=").append(sort).append("&");
		}
		if (sinceId != null) {
			queryParams.append("since_id=").append(sinceId).append("&");
		}
		if (sinceTimestamp != null) {
			queryParams.append("since_timestamp=").append(sinceTimestamp).append("&");
		}
		
		// Remove trailing '&' if present
		String queryString = queryParams.length() > 0 ? 
				queryParams.substring(0, queryParams.length() - 1) : "";

		// Generate authentication headers using the exact query string
		// Note: Content-Type must be empty string when request body is empty (per Bitstamp API docs)
		BitstampAuthHelper.AuthHeaders authHeaders = BitstampAuthHelper.generateAuthHeaders(
				credentials.getApiKey(),
				credentials.getApiSecret(),
				"POST",
				"www.bitstamp.net",
				path,
				queryString.isEmpty() ? "" : "?" + queryString,
				"",  // Empty content-type when body is empty
				""   // Empty payload
		);

		// Make the authenticated request using the same query string
		// Note: Do not set Content-Type header when body is empty (per Bitstamp API docs)
		String uri = queryString.isEmpty() ? path : path + "?" + queryString;
		return restClient.post()
				.uri(uri)
				.header("X-Auth", authHeaders.getXAuth())
				.header("X-Auth-Signature", authHeaders.getXAuthSignature())
				.header("X-Auth-Nonce", authHeaders.getXAuthNonce())
				.header("X-Auth-Timestamp", authHeaders.getXAuthTimestamp())
				.header("X-Auth-Version", authHeaders.getXAuthVersion())
				.retrieve()
				.body(new ParameterizedTypeReference<List<BitstampUserTransaction>>() {});
	}


	/**
	 * Gets user transactions with default parameters (private API endpoint).
	 * <p/>
	 * Returns the most recent 100 user transactions in descending order.
	 * Requires authentication with API key and secret.
	 * 
	 * @param credentials the Bitstamp API credentials
	 * @return list of user transactions
	 * @throws IllegalArgumentException if credentials is null
	 */
	public List<BitstampUserTransaction> getUserTransactions(BitstampCredentials credentials) {
		return getUserTransactions(credentials, null, null, null, null, null, null);
	}


	/**
	 * Gets user transactions for a specific currency pair (private API endpoint).
	 * <p/>
	 * Returns user transactions for the specified currency pair.
	 * Requires authentication with API key and secret.
	 * 
	 * @param credentials the Bitstamp API credentials
	 * @param currencyPair the currency pair (e.g., "btcusd", "ethusd")
	 * @return list of user transactions
	 * @throws IllegalArgumentException if credentials is null
	 */
	public List<BitstampUserTransaction> getUserTransactions(BitstampCredentials credentials, String currencyPair) {
		return getUserTransactions(credentials, currencyPair, null, null, null, null, null);
	}


}
