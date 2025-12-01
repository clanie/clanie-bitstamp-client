# Bitstamp Client

BitstampClient is a Java client for Bitstamp's [bitstamp.net](https://bitstamp.net/) HTTP API.

It implements the Public Data Functions from the [Bitstamp API v2](https://www.bitstamp.net/api/).

## Features

This client supports the following Bitstamp API endpoints:

### Public API Endpoints (no authentication required)

- **Ticker** - Real-time price data for a currency pair
- **Hourly Ticker** - Hourly ticker data
- **Order Book** - Current buy and sell orders
- **Transactions** - Recent trades
- **Trading Pairs Info** - Information about available trading pairs
- **OHLC Data** - Historical candlestick data

### Private API Endpoints (authentication required)

- **User Transactions** - Your account's transaction history (deposits, withdrawals, trades, fees)

## Usage

### Configuration

Add the following properties to your `application.properties` or `application.yml`:

```properties
# Optional - defaults to https://www.bitstamp.net/api/v2
bitstamp.url=https://www.bitstamp.net/api/v2

# Optional - enable request/response logging (defaults to false)
bitstamp.wiretap=false
```

**Authentication:**
- Public endpoints (ticker, order book, etc.) do not require authentication
- Private endpoints (user transactions, etc.) require API credentials passed as `BitstampCredentials` objects
- This design supports multi-user and multi-tenant systems where different users/tenants have different credentials
- To obtain API credentials, log into your Bitstamp account and create an API key in the API settings
- See [Bitstamp API Authentication Documentation](https://www.bitstamp.net/api/#section/Authentication) for details

**Credentials Management:**
```java
// Create credentials object for each user/tenant
BitstampCredentials credentials = new BitstampCredentials(apiKey, apiSecret);

// Pass credentials to authenticated methods
List<BitstampUserTransaction> transactions = bitstampClient.getUserTransactions(credentials);
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
        
        // Create credentials for authenticated requests
        // In a real application, retrieve these from your user/tenant database
        BitstampCredentials credentials = new BitstampCredentials(apiKey, apiSecret);
        
        // Get user transactions (requires authentication)
        List<BitstampUserTransaction> userTransactions = bitstampClient.getUserTransactions(credentials);
        
        // Get user transactions for a specific pair
        List<BitstampUserTransaction> btcTransactions = bitstampClient.getUserTransactions(credentials, "btcusd");
        
        // Get user transactions with advanced options
        List<BitstampUserTransaction> filteredTransactions = bitstampClient.getUserTransactions(
            credentials, // API credentials
            "btcusd",    // currency pair (optional)
            0,           // offset (optional)
            100,         // limit (optional, max 1000)
            "desc",      // sort order: "asc" or "desc" (optional)
            null,        // since ID (optional)
            null         // since timestamp (optional)
        );
        
        // Process user transactions
        for (BitstampUserTransaction tx : userTransactions) {
            System.out.println("Transaction ID: " + tx.getId());
            System.out.println("Type: " + tx.getType().getName());
            System.out.println("Datetime: " + tx.getDatetime());
            if (tx.getBtc() != null) {
                System.out.println("BTC Amount: " + tx.getBtc());
            }
            if (tx.getFee() != null) {
                System.out.println("Fee: " + tx.getFee());
            }
        }
    }
}
```

## Implementation Notes

- Uses Spring's `RestClient` for HTTP communication (not WebClient)
- Public endpoints do not require authentication
- Private endpoints use Bitstamp's v2 authentication with HMAC-SHA256 signatures
- Authentication headers are automatically generated and included for private endpoints
- All DTOs use `double` or `BigDecimal` for numeric fields depending on precision requirements
- Immutable DTOs using Lombok's `@Value` annotation

## User Transaction Types

User transactions can be of the following types:

- **DEPOSIT (0)** - Fiat or crypto deposit
- **WITHDRAWAL (1)** - Fiat or crypto withdrawal
- **MARKET_TRADE (2)** - Trade execution
- **SUB_ACCOUNT_TRANSFER (14)** - Transfer between sub-accounts
- **CREDITED_WITH_STAKING_REWARD (25)** - Staking rewards
- **SENDING_TOKENS_TO_STAKING (26)** - Tokens sent to staking
- **RETURNING_TOKENS_FROM_STAKING (27)** - Tokens returned from staking
- **TRANSFER (32)** - General transfer
- **REFERRAL_REWARD (35)** - Referral program reward
- **INTER_ACCOUNT_TRANSFER (40)** - Transfer between accounts
