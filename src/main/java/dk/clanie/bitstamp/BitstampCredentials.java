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
package dk.clanie.bitstamp;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.jspecify.annotations.NonNull;

import lombok.Value;

/**
 * Container for Bitstamp API credentials.
 * <p/>
 * This class holds the API key and secret required for authenticated
 * Bitstamp API requests. It is designed to support multi-user and
 * multi-tenant systems where different users/tenants may have different
 * credentials.
 */
@Value
public class BitstampCredentials {
	
	/**
	 * The Bitstamp API key.
	 */
	String apiKey;
	
	/**
	 * The Bitstamp API secret.
	 */
	String apiSecret;
	
	/**
	 * Creates a new BitstampCredentials instance.
	 * 
	 * @param apiKey the API key
	 * @param apiSecret the API secret
	 * @throws IllegalArgumentException if apiKey or apiSecret is null or empty
	 */
	public BitstampCredentials(@NonNull String apiKey, @NonNull String apiSecret) {
		if (isBlank(apiKey)) {
			throw new IllegalArgumentException("API key cannot be null or empty");
		}
		if (isBlank(apiSecret)) {
			throw new IllegalArgumentException("API secret cannot be null or empty");
		}
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
	}
	
}
