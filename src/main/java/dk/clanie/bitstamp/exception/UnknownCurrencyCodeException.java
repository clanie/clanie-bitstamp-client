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
package dk.clanie.bitstamp.exception;

import static java.lang.String.join;

import java.util.List;

import lombok.Getter;

/**
 * Exception thrown when Bitstamp returns currency codes that are not in our enumeration.
 */
@SuppressWarnings("serial")
@Getter
public class UnknownCurrencyCodeException extends RuntimeException {

	private final List<String> unknownCurrencyCodes;


	public UnknownCurrencyCodeException(List<String> unknownCurrencyCodes) {
		super("Unknown currency code(s) received from Bitstamp: " + join(", ", unknownCurrencyCodes));
		this.unknownCurrencyCodes = unknownCurrencyCodes;
	}


}
