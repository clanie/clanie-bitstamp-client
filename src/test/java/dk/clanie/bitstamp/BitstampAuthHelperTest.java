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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dk.clanie.bitstamp.BitstampAuthHelper.AuthHeaders;

/**
 * Tests for BitstampAuthHelper.
 */
class BitstampAuthHelperTest {

	@Test
	void testGenerateAuthHeaders() {
		// Test that auth headers are generated with all required fields
		String apiKey = "test-api-key";
		String apiSecret = "test-api-secret";
		String httpMethod = "POST";
		String host = "www.bitstamp.net";
		String path = "/api/v2/user_transactions/";
		String queryParams = "";
		String contentType = "application/x-www-form-urlencoded";
		String payload = "";
		
		AuthHeaders headers = BitstampAuthHelper.generateAuthHeaders(
				apiKey, apiSecret, httpMethod, host, path, queryParams, contentType, payload);
		
		assertNotNull(headers);
		assertEquals("BITSTAMP " + apiKey, headers.getXAuth());
		assertNotNull(headers.getXAuthSignature());
		assertNotNull(headers.getXAuthNonce());
		assertNotNull(headers.getXAuthTimestamp());
		assertEquals("v2", headers.getXAuthVersion());
		
		// Verify signature is hex string (64 characters for SHA256)
		assertTrue(headers.getXAuthSignature().matches("^[0-9a-f]{64}$"));
		
		// Verify nonce is a valid UUID
		assertTrue(headers.getXAuthNonce().matches(
				"^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"));
		
		// Verify timestamp is a valid number
		assertTrue(headers.getXAuthTimestamp().matches("^\\d+$"));
	}

	@Test
	void testGenerateAuthHeadersWithQueryParams() {
		// Test with query parameters
		String apiKey = "test-api-key";
		String apiSecret = "test-api-secret";
		String httpMethod = "POST";
		String host = "www.bitstamp.net";
		String path = "/api/v2/user_transactions/";
		String queryParams = "?offset=0&limit=100";
		String contentType = "application/x-www-form-urlencoded";
		String payload = "";
		
		AuthHeaders headers = BitstampAuthHelper.generateAuthHeaders(
				apiKey, apiSecret, httpMethod, host, path, queryParams, contentType, payload);
		
		assertNotNull(headers);
		assertNotNull(headers.getXAuthSignature());
		assertTrue(headers.getXAuthSignature().matches("^[0-9a-f]{64}$"));
	}

	@Test
	void testSignatureChangesWithDifferentInputs() {
		// Test that different inputs produce different signatures
		String apiKey = "test-api-key";
		String apiSecret = "test-api-secret";
		String httpMethod = "POST";
		String host = "www.bitstamp.net";
		String path1 = "/api/v2/user_transactions/";
		String path2 = "/api/v2/user_transactions/btcusd/";
		String queryParams = "";
		String contentType = "application/x-www-form-urlencoded";
		String payload = "";
		
		AuthHeaders headers1 = BitstampAuthHelper.generateAuthHeaders(
				apiKey, apiSecret, httpMethod, host, path1, queryParams, contentType, payload);
		
		// Add a small delay to ensure different timestamp
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			// Ignore
		}
		
		AuthHeaders headers2 = BitstampAuthHelper.generateAuthHeaders(
				apiKey, apiSecret, httpMethod, host, path2, queryParams, contentType, payload);
		
		// Different paths should produce different signatures
		assertNotEquals(headers1.getXAuthSignature(), headers2.getXAuthSignature());
	}

}
