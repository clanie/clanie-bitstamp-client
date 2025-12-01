# BitstampClient - Authentication and User Transactions Implementation

## Summary

The BitstampClient has been successfully extended to support:
1. **Authentication** using Bitstamp's v2 API authentication protocol
2. **User Transactions endpoint** (`/api/v2/user_transactions/`)
3. **Multi-user and multi-tenant support** via BitstampCredentials objects

## Recent Changes (December 2025)

### Multi-Tenant Refactoring

The client has been refactored to support multi-user and multi-tenant systems:

**New:**
- **BitstampCredentials.java** - Immutable credentials container with API key and secret
- API credentials are now passed as parameters to authenticated methods
- Supports different credentials for different users/tenants in the same application

**Removed:**
- Configuration properties `bitstamp.api.key` and `bitstamp.api.secret`
- These are no longer suitable for multi-tenant systems

**Modified:**
- All `getUserTransactions()` methods now require a `BitstampCredentials` parameter
- Updated README with credentials management examples

## Changes Made

### New Files Created

1. **BitstampCredentials.java**
   - Immutable container for API key and secret
   - Validation to ensure credentials are not null or empty
   - Designed for multi-user/multi-tenant systems

2. **BitstampAuthHelper.java**
   - Helper class for generating authentication headers
   - Implements HMAC-SHA256 signature generation
   - Generates required headers: X-Auth, X-Auth-Signature, X-Auth-Nonce, X-Auth-Timestamp, X-Auth-Version
   - Follows Bitstamp's authentication specification exactly

3. **BitstampUserTransaction.java**
   - DTO for user transaction data
   - Supports all major cryptocurrencies and fiat currencies
   - Fields: id, datetime, type, currency amounts, fee, orderId, market
   - Uses Double for monetary amounts

4. **BitstampUserTransactionType.java**
   - Enum for user transaction types
   - Includes: DEPOSIT, WITHDRAWAL, MARKET_TRADE, SUB_ACCOUNT_TRANSFER, staking types, transfers, etc.
   - JSON serialization/deserialization support

5. **BitstampAuthHelperTest.java**
   - Unit tests for authentication header generation
   - Verifies signature format, UUID nonce, timestamp, and different inputs produce different signatures

### Modified Files

1. **BitstampClient.java**
   - Removed configuration properties for API key and secret
   - Added `BitstampCredentials` parameter to all `getUserTransactions()` methods
   - Proper authentication header generation using credentials
   - Support for pagination and filtering parameters

2. **README.md**
   - Updated authentication section to explain credentials-based approach
   - Added multi-tenant usage examples
   - Updated all authenticated endpoint examples to use BitstampCredentials
   - Removed references to bitstamp.api.key/secret configuration properties

## Configuration

Add to your application.properties:

```properties
# Required for private API endpoints
bitstamp.api.key=your-api-key
bitstamp.api.secret=your-api-secret
```

## Usage Examples

### Basic Usage
```java
// Get all user transactions (requires authentication)
List<BitstampUserTransaction> transactions = bitstampClient.getUserTransactions();
```

### Filter by Currency Pair
```java
// Get user transactions for BTC/USD only
List<BitstampUserTransaction> btcTransactions = bitstampClient.getUserTransactions("btcusd");
```

### Advanced Filtering
```java
// Get user transactions with pagination and filters
List<BitstampUserTransaction> filtered = bitstampClient.getUserTransactions(
    "btcusd",  // currency pair
    0,         // offset
    100,       // limit (max 1000)
    "desc",    // sort order
    null,      // since ID
    null       // since timestamp
);
```

## Authentication Details

The implementation follows Bitstamp's v2 authentication protocol:

1. **Message Construction**: Concatenates BITSTAMP prefix, API key, HTTP method, host, path, query params, content-type, nonce, timestamp, version, and payload
2. **Signature Generation**: HMAC-SHA256 hash of the message using the API secret
3. **Headers**: Includes all required authentication headers in the request

## Security Notes

- API credentials are only required for private endpoints
- Public endpoints (ticker, order book, etc.) continue to work without authentication
- API key and secret should be stored securely and never committed to version control
- Use environment variables or secure configuration management for production deployments

## Testing

All 34 tests pass, including:
- 3 new authentication tests
- All existing DTO and functionality tests

Build successfully compiles with no errors or warnings (except for Maven Unsafe warnings which are unrelated).

## API Documentation References

- Authentication: https://www.bitstamp.net/api/#section/Authentication
- User Transactions: https://www.bitstamp.net/api/#tag/Transactions-private
