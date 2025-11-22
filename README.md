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

Add the following property to your `application.properties` or `application.yml`:

```properties
# Optional - defaults to https://www.bitstamp.net/api/v2
bitstamp.url=https://www.bitstamp.net/api/v2
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
