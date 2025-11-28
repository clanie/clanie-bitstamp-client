# Bitstamp Client

BitstampClient is a Java client for Bitstamp's [bitstamp.net](https://bitstamp.net/) HTTP API.

It implements the Public Data Functions from the [Bitstamp API v2](https://www.bitstamp.net/api/).

## Features

This client supports the following Bitstamp public API endpoints:

- **Ticker** - Real-time price data for a currency pair
- **Hourly Ticker** - Hourly ticker data
- **Order Book** - Current buy and sell orders
- **Transactions** - Recent trades
- **Trading Pairs Info** - Information about available trading pairs
- **OHLC Data** - Historical candlestick data

## Usage

### Configuration

Add the following properties to your `application.properties` or `application.yml`:

```properties
# Optional - defaults to https://www.bitstamp.net/api/v2
bitstamp.url=https://www.bitstamp.net/api/v2

# Optional - enable request/response logging (defaults to false)
bitstamp.wiretap=false
```

### Currency Pairs

Bitstamp uses two formats for currency pairs:
- **With slash** (e.g., "BTC/USD") - used in API responses
- **Without slash** (e.g., "btcusd") - used as URL parameters in API calls

The client provides classes to handle both formats:

```java
// Create currency pair from string with slash
BitstampCurrencyPair pair = BitstampCurrencyPair.fromString("BTC/USD");

// Create from enum values
BitstampCurrencyPair pair = new BitstampCurrencyPair(
    BitstampCurrencyCode.BTC,
    BitstampCurrencyCode.USD
);

// Get URL parameter format (lowercase without slash)
String urlParam = pair.toUrlParameter(); // "btcusd"

// Get display format (uppercase with slash)
String display = pair.toStringWithSlash(); // "BTC/USD"

// Access individual currencies
BitstampCurrencyCode base = pair.getBaseCurrency();  // BitstampCurrencyCode.BTC
BitstampCurrencyCode quote = pair.getQuoteCurrency(); // BitstampCurrencyCode.USD

// To get currency codes as strings
String baseCode = pair.getBaseCurrency().getCode();  // "BTC"
String quoteCode = pair.getQuoteCurrency().getCode(); // "USD"
```

### Example

The client is automatically configured as a Spring Bean. Just inject it:

```java
@Service
public class MyService {
    
    @Autowired
    private BitstampClient bitstampClient;
    
    public void example() {
        // Get ticker data for Bitcoin/USD
        BitstampTicker ticker = bitstampClient.getTicker("btcusd");
        System.out.println("BTC Price: " + ticker.getLast());
        
        // Or use the currency pair helper
        BitstampCurrencyPair pair = new BitstampCurrencyPair(
            BitstampCurrencyCode.BTC,
            BitstampCurrencyCode.USD
        );
        ticker = bitstampClient.getTicker(pair.toUrlParameter());
        
        // Get order book
        BitstampOrderBook orderBook = bitstampClient.getOrderBook("btcusd");
        System.out.println("Bids: " + orderBook.getBids().size());
        
        // Get recent transactions
        List<BitstampTransaction> transactions = bitstampClient.getTransactions("btcusd");
        
        // Get all trading pairs
        List<BitstampTradingPair> pairs = bitstampClient.getTradingPairsInfo();
        
        // Get OHLC data (candlesticks)
        // Step values: 60, 180, 300, 900, 1800, 3600, 7200, 14400, 21600, 43200, 86400, 259200
        BitstampOhlcData ohlc = bitstampClient.getOhlcData("btcusd", 3600); // 1-hour candles
    }
}
```

## Implementation Notes

- Uses Spring's `RestClient` for HTTP communication (not WebClient)
- No authentication required for public endpoints
- All DTOs use `double` for price/amount fields (consistent with EODHD client style)
- Immutable DTOs using Lombok's `@Value` annotation
