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
package dk.clanie.bitstamp.jackson;

import static org.apache.commons.lang3.StringUtils.isBlank;

import dk.clanie.bitstamp.dto.BitstampCurrencyCode;
import dk.clanie.bitstamp.dto.BitstampMonetaryAmount;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

/**
 * Custom deserializer for BitstampMonetaryAmount.
 * <p/>
 * Parses strings in the format "amount currency" (e.g., "10 USD", "0.0001 BTC").
 */
public class BitstampMonetaryAmountDeserializer extends ValueDeserializer<BitstampMonetaryAmount> {

	@Override
	public BitstampMonetaryAmount deserialize(JsonParser p, DeserializationContext ctxt) throws DatabindException {
		String value = p.getString();
		if (isBlank(value)) {
			return null;
		}

		String[] parts = value.trim().split("\\s+");
		if (parts.length != 2) {
			throw DatabindException.from(p, "Invalid monetary amount format: " + value + ". Expected format: 'amount currency'");
		}

		try {
			double amount = Double.parseDouble(parts[0]);
			BitstampCurrencyCode currencyCode = BitstampCurrencyCode.fromString(parts[1]);
			return new BitstampMonetaryAmount(amount, currencyCode);
		} catch (NumberFormatException e) {
			throw DatabindException.from(p, "Invalid amount value: " + parts[0], e);
		} catch (IllegalArgumentException e) {
			throw DatabindException.from(p, "Invalid currency code: " + parts[1], e);
		}
	}

}
