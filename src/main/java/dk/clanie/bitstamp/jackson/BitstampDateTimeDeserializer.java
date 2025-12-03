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

import static java.time.ZoneOffset.UTC;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Custom deserializer for Bitstamp datetime strings.
 * <p/>
 * Parses datetime strings in the format "yyyy-MM-dd HH:mm:ss[.SSSSSS]" 
 * (microseconds are optional) and converts them to Instant.
 */
public class BitstampDateTimeDeserializer extends JsonDeserializer<Instant> {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSSSSS]");


	@Override
	public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		String value = p.getText();
		if (isEmpty(value)) return null;

		try {
			LocalDateTime dateTime = LocalDateTime.parse(value.trim(), FORMATTER);
			return dateTime.toInstant(UTC);
		} catch (Exception e) {
			throw new IOException("Invalid datetime format: " + value + ". Expected format: 'yyyy-MM-dd HH:mm:ss[.SSSSSS]' (microseconds optional)", e);
		}
	}


}
