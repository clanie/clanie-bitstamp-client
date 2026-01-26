#!/bin/bash

# Script to fetch all currencies from Bitstamp and compare with our enum

echo "Fetching all currencies from Bitstamp API..."
CURRENCIES_JSON=$(curl -s "https://www.bitstamp.net/api/v2/currencies/")

if [ $? -ne 0 ]; then
    echo "Error: Failed to fetch currencies from Bitstamp API"
    exit 1
fi

# Extract all currency codes
BITSTAMP_CURRENCIES=$(echo "$CURRENCIES_JSON" | jq -r '.[].currency' | sort -u)

echo "Found $(echo "$BITSTAMP_CURRENCIES" | wc -l | tr -d ' ') currencies on Bitstamp"
echo ""

# Get currencies from our enum
ENUM_FILE="src/main/java/dk/clanie/bitstamp/dto/BitstampCurrencyCode.java"
if [ ! -f "$ENUM_FILE" ]; then
    echo "Error: Could not find $ENUM_FILE"
    exit 1
fi

# Extract enum values (lines that look like: SYMBOL("Name", "Description", TYPE))
# We need to handle multi-line entries, so we'll look for lines that start with tab and capital letter
ENUM_CURRENCIES=$(grep -oE '^\s+[A-Z_0-9]+\(' "$ENUM_FILE" | sed -E 's/^[[:space:]]+//; s/\($//' | grep -v 'USD_PERP' | sort -u)

echo "Found $(echo "$ENUM_CURRENCIES" | wc -l | tr -d ' ') currencies in our enum"
echo ""

# Find currencies in Bitstamp but not in our enum
echo "Currencies on Bitstamp but MISSING in our enum:"
echo "================================================"
MISSING=0
for currency in $BITSTAMP_CURRENCIES; do
    if ! echo "$ENUM_CURRENCIES" | grep -q "^${currency}$"; then
        # Get the name from the JSON
        NAME=$(echo "$CURRENCIES_JSON" | jq -r ".[] | select(.currency == \"$currency\") | .name")
        echo "  $currency - $NAME"
        MISSING=$((MISSING + 1))
    fi
done

if [ $MISSING -eq 0 ]; then
    echo "  (none)"
fi

echo ""
echo "Currencies in our enum but NOT on Bitstamp:"
echo "============================================"
EXTRA=0
for currency in $ENUM_CURRENCIES; do
    if ! echo "$BITSTAMP_CURRENCIES" | grep -q "^${currency}$"; then
        echo "  $currency"
        EXTRA=$((EXTRA + 1))
    fi
done

if [ $EXTRA -eq 0 ]; then
    echo "  (none)"
fi

echo ""
echo "Summary:"
echo "  Bitstamp currencies: $(echo "$BITSTAMP_CURRENCIES" | wc -l | tr -d ' ')"
echo "  Our enum currencies: $(echo "$ENUM_CURRENCIES" | wc -l | tr -d ' ')"
echo "  Missing from enum:   $MISSING"
echo "  Extra in enum:       $EXTRA"
