# Unknown Currency Code Handling

## Overview

When Bitstamp introduces new currency codes that are not yet in our `BitstampCurrencyCode` enumeration, the cryptocurrency rate update job now handles this gracefully by:

1. **Collecting all unknown currency codes** during deserialization (not just the first one)
2. **Throwing a specific exception** (`UnknownCurrencyCodeException`) with all unknown codes
3. **Registering an issue** for manual intervention with clear instructions

## Implementation Details

### 1. Custom Exception

**`UnknownCurrencyCodeException`** - A new exception that contains a list of all unknown currency codes encountered during API response deserialization.

```java
public class UnknownCurrencyCodeException extends RuntimeException {
    private final List<String> unknownCurrencyCodes;
    // ...
}
```

### 2. Custom Deserializer

**`BitstampCurrencyPairDeserializer`** - A custom Jackson deserializer that:
- Collects unknown currency codes into a thread-local set during deserialization
- Allows deserialization to continue even when unknown codes are encountered
- Provides static methods to retrieve and clear collected unknown codes

```java
public class BitstampCurrencyPairDeserializer extends ValueDeserializer<BitstampCurrencyPair> {
    private static final ThreadLocal<Set<String>> unknownCurrencyCodes = ThreadLocal.withInitial(HashSet::new);
    
    public static Set<String> getUnknownCurrencyCodes() { ... }
    public static void clearUnknownCurrencyCodes() { ... }
    // ...
}
```

### 3. Updated BitstampClient

The `listTickers()` method now:
- Clears unknown currency codes before making the API call
- Checks for unknown codes after deserialization
- Throws `UnknownCurrencyCodeException` with all collected unknown codes
- Always clears the thread-local state in a finally block

```java
public List<BitstampTickerListEntry> listTickers() {
    BitstampCurrencyPairDeserializer.clearUnknownCurrencyCodes();
    try {
        List<BitstampTickerListEntry> result = restClient.get()
                .uri("/api/v2/ticker/")
                .retrieve()
                .body(new ParameterizedTypeReference<List<BitstampTickerListEntry>>() {});
        
        Set<String> unknownCodes = BitstampCurrencyPairDeserializer.getUnknownCurrencyCodes();
        if (!unknownCodes.isEmpty()) {
            throw new UnknownCurrencyCodeException(new ArrayList<>(unknownCodes));
        }
        return result;
    } finally {
        BitstampCurrencyPairDeserializer.clearUnknownCurrencyCodes();
    }
}
```

### 4. Updated Issue Details

**`BitstampCurrencyCodeMissing`** now supports multiple currency codes:

```java
public static final class BitstampCurrencyCodeMissing extends IssueDetails {
    private List<String> currencyCodes;  // Changed from single String currencyCode
    
    @Override
    public String getShortDescription() {
        if (currencyCodes.size() == 1) {
            return "Bitstamp currency code missing: " + currencyCodes.get(0);
        } else {
            return "Bitstamp currency codes missing: " + currencyCodes.size() + " code(s)";
        }
    }
    
    @Override
    public String getDetailedDescription() {
        // HTML formatted description listing all missing codes
    }
}
```

### 5. Updated CryptocurrencyRateUpdateJob

The job now catches `UnknownCurrencyCodeException` and registers an issue:

```java
try {
    tickers = bitstampClient.listTickers();
} catch (UnknownCurrencyCodeException e) {
    log.error("Unknown currency codes received from Bitstamp: {}", e.getUnknownCurrencyCodes());
    
    IssueDetails.BitstampCurrencyCodeMissing issueDetails = 
            IssueDetails.bitstampCurrencyCodeMissing(e.getUnknownCurrencyCodes());
    List<IssueFix> fixes = asList(
            IssueFix.manualIntervention(
                    "Add the missing currency code(s) to the BitstampCurrencyCode enumeration in bitstamp-client")
    );
    issueService.registerIssue(ADMIN_TENANT_ID, null, issueDetails, fixes);
    
    throw new RuntimeException("Error fetching cryptocurrency rates from Bitstamp: Unknown currency codes. Issue registered.", e);
}
```

## Benefits

1. **Better Error Messages**: Instead of seeing a generic Jackson deserialization error, you now get a clear exception message listing all unknown currency codes
2. **Complete Information**: All unknown codes are collected in a single run, not just the first one encountered
3. **Automatic Issue Registration**: An issue is automatically created in the system for manual intervention
4. **Clear Instructions**: The issue provides clear guidance on what needs to be done (add codes to BitstampCurrencyCode enum)
5. **Thread-Safe**: Uses ThreadLocal to ensure thread safety when processing multiple requests

## Example

When Bitstamp introduces new currency codes "TRAC" and "XYZ", the system will:

1. Collect both "TRAC" and "XYZ" during deserialization
2. Throw `UnknownCurrencyCodeException` with message: "Unknown currency code(s) received from Bitstamp: TRAC, XYZ"
3. Register an issue with details showing both missing codes
4. Provide a manual intervention fix with instructions to add them to `BitstampCurrencyCode` enum

## Testing

New tests were added:
- `UnknownCurrencyCodeExceptionTest` - Tests the exception class
- `BitstampCurrencyPairDeserializerTest` - Tests the custom deserializer behavior with unknown codes

All existing tests continue to pass.
