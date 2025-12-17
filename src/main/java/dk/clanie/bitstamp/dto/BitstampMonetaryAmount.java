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
package dk.clanie.bitstamp.dto;

import tools.jackson.databind.annotation.JsonDeserialize;

import dk.clanie.bitstamp.jackson.BitstampMonetaryAmountDeserializer;
import lombok.Value;

/**
 * Represents a monetary amount with currency.
 * <p/>
 * This class is used to deserialize Bitstamp API responses that return
 * amounts as strings with currency (e.g., "10 USD").
 */
@Value
@JsonDeserialize(using = BitstampMonetaryAmountDeserializer.class)
public class BitstampMonetaryAmount {

	double amount;
	BitstampCurrencyCode currencyCode;


	public BitstampMonetaryAmount(double amount, BitstampCurrencyCode currencyCode) {
		this.amount = amount;
		this.currencyCode = currencyCode;
	}


	public BitstampMonetaryAmount(String amount, String currencyCode) {
		this(Double.parseDouble(amount), BitstampCurrencyCode.fromString(currencyCode));
	}


	@Override
	public String toString() {
		return amount + " " + currencyCode;
	}

}
