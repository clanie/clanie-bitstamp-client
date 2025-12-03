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

import static dk.clanie.core.Utils.asString;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import lombok.Value;

/**
 * Helper class for generating Bitstamp API authentication headers.
 */
public class BitstampAuthHelper {

	private static final String HMAC_SHA256 = "HmacSHA256";

	/**
	 * Generates authentication headers for Bitstamp API requests.
	 * <p/>
	 * According to Bitstamp API documentation, authentication requires:
	 * - X-Auth: BITSTAMP {apiKey}
	 * - X-Auth-Signature: HMAC-SHA256 signature
	 * - X-Auth-Nonce: UUID v4
	 * - X-Auth-Timestamp: Unix timestamp in milliseconds
	 * - X-Auth-Version: v2
	 * <p/>
	 * The signature is created by concatenating:
	 * BITSTAMP {apiKey}{HTTP method}{host}{path}{query parameters}{content-type}{nonce}{timestamp}{version}{payload}
	 * and signing with HMAC-SHA256 using the API secret.
	 * 
	 * @param apiKey the API key
	 * @param apiSecret the API secret
	 * @param httpMethod the HTTP method (e.g., "POST", "GET")
	 * @param host the host (e.g., "www.bitstamp.net")
	 * @param path the API path (e.g., "/api/v2/user_transactions/")
	 * @param queryParams the query parameters (empty string if none)
	 * @param contentType the content type (e.g., "application/x-www-form-urlencoded")
	 * @param payload the request payload (empty string if none)
	 * @return authentication headers
	 */
	public static AuthHeaders generateAuthHeaders(
			String apiKey,
			String apiSecret,
			String httpMethod,
			String host,
			String path,
			String queryParams,
			String contentType,
			String payload) {
		
		String nonce = asString(UUID.randomUUID());
		long timestamp = System.currentTimeMillis();
		String version = "v2";
		
		// Build the message to sign
		StringBuilder message = new StringBuilder();
		message.append("BITSTAMP ").append(apiKey);
		message.append(httpMethod);
		message.append(host);
		message.append(path);
		message.append(queryParams);
		message.append(contentType);
		message.append(nonce);
		message.append(timestamp);
		message.append(version);
		message.append(payload);
		
		// Generate HMAC-SHA256 signature
		String signature = generateSignature(message.toString(), apiSecret);
		
		return new AuthHeaders(
				"BITSTAMP " + apiKey,
				signature,
				nonce,
				String.valueOf(timestamp),
				version
		);
	}

	/**
	 * Generates HMAC-SHA256 signature.
	 */
	private static String generateSignature(String message, String secret) {
		try {
			Mac mac = Mac.getInstance(HMAC_SHA256);
			SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
			mac.init(secretKeySpec);
			byte[] hash = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
			return bytesToHex(hash);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new RuntimeException("Failed to generate signature", e);
		}
	}

	/**
	 * Converts byte array to hex string.
	 */
	private static String bytesToHex(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		for (byte b : bytes) {
			result.append(String.format("%02x", b));
		}
		return result.toString();
	}

	/**
	 * Container for authentication headers.
	 */
	@Value
	public static class AuthHeaders {
		String xAuth;
		String xAuthSignature;
		String xAuthNonce;
		String xAuthTimestamp;
		String xAuthVersion;
	}

}
