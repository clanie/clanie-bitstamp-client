#!/bin/bash

# Script to fetch all individual tickers and analyze their fields

echo "Fetching all trading pairs..."
PAIRS=$(curl -s "https://www.bitstamp.net/api/v2/trading-pairs-info/" | jq -r '.[].url_symbol')

echo "Found $(echo "$PAIRS" | wc -l | tr -d ' ') trading pairs"
echo ""

# Create temp directory for storing responses
TEMP_DIR=$(mktemp -d)
echo "Storing responses in: $TEMP_DIR"

# Fetch each ticker
echo "Fetching individual tickers..."
COUNTER=0
for pair in $PAIRS; do
    COUNTER=$((COUNTER + 1))
    echo -ne "Progress: $COUNTER/$(echo "$PAIRS" | wc -l | tr -d ' ')\r"
    curl -s "https://www.bitstamp.net/api/v2/ticker/$pair/" > "$TEMP_DIR/$pair.json"
    sleep 0.1  # Be nice to the API
done
echo ""

echo ""
echo "Analyzing all responses for unique field names..."
echo ""

# Get all unique field names across all responses
ALL_FIELDS=$(cat "$TEMP_DIR"/*.json | jq -r 'keys[]' | sort -u)

echo "All unique fields found across all tickers:"
echo "=============================================="
for field in $ALL_FIELDS; do
    echo "  - $field"
done

echo ""
echo "Sample values for each field:"
echo "=============================================="
for field in $ALL_FIELDS; do
    echo ""
    echo "Field: $field"
    echo "  Sample values:"
    cat "$TEMP_DIR"/*.json | jq -r ".$field // empty" | grep -v '^$' | sort -u | head -10 | sed 's/^/    /'
done

echo ""
echo "Checking for different field combinations..."
echo "=============================================="

# Get unique field combinations
cat "$TEMP_DIR"/*.json | jq -c 'keys | sort' | sort -u > "$TEMP_DIR/field_combinations.txt"

NUM_COMBINATIONS=$(cat "$TEMP_DIR/field_combinations.txt" | wc -l | tr -d ' ')
echo "Found $NUM_COMBINATIONS different field combination(s):"
echo ""

COMBO_NUM=1
while IFS= read -r combo; do
    echo "Combination $COMBO_NUM:"
    echo "  Fields: $(echo "$combo" | jq -r '.[]' | tr '\n' ', ' | sed 's/,$//')"
    echo "  Examples:"
    for file in "$TEMP_DIR"/*.json; do
        if [ "$(jq -c 'keys | sort' "$file")" = "$combo" ]; then
            echo "    - $(basename "$file" .json)"
            # Only show first 3 examples
            COUNT=$((COUNT + 1))
            if [ $COUNT -ge 3 ]; then
                break
            fi
        fi
    done
    COUNT=0
    echo ""
    COMBO_NUM=$((COMBO_NUM + 1))
done < "$TEMP_DIR/field_combinations.txt"

echo ""
echo "Detailed comparison - fields present in each trading pair:"
echo "=============================================="
for file in "$TEMP_DIR"/*.json; do
    pair=$(basename "$file" .json)
    fields=$(jq -r 'keys | join(", ")' "$file")
    echo "$pair: $fields"
done

# Cleanup
# rm -rf "$TEMP_DIR"

echo ""
echo "Analysis complete! Temp files kept in: $TEMP_DIR"
