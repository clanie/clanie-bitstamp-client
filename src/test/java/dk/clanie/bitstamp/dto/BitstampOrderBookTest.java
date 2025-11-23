/**
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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class BitstampOrderBookTest {

	@Test
	void testJsonDeserialization() throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		BitstampOrderBook orderBook = objectMapper.readValue("""
				{
				  "timestamp": "1637597234",
				  "microtimestamp": "1637597234123456",
				  "bids": [
				    ["47795.00", "1.23456789"],
				    ["47790.00", "0.50000000"]
				  ],
				  "asks": [
				    ["47805.00", "2.34567890"],
				    ["47810.00", "1.00000000"]
				  ]
				}
				""", BitstampOrderBook.class);
		
		assertThat(orderBook.getTimestamp()).isEqualTo(1637597234L);
		assertThat(orderBook.getMicrotimestamp()).isEqualTo(1637597234123456L);
		assertThat(orderBook.getBids()).hasSize(2);
		assertThat(orderBook.getBids().get(0).getPrice()).isEqualTo(47795.00);
		assertThat(orderBook.getBids().get(0).getAmount()).isEqualTo(1.23456789);
		assertThat(orderBook.getAsks()).hasSize(2);
		assertThat(orderBook.getAsks().get(0).getPrice()).isEqualTo(47805.00);
		assertThat(orderBook.getAsks().get(0).getAmount()).isEqualTo(2.34567890);
	}

}
