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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

/**
 * Represents a single transaction (trade).
 */
@Value
public class BitstampTransaction {

	Long date;
	String tid;
	double price;
	double amount;
	Integer type;


	@JsonCreator
	public BitstampTransaction(
			@JsonProperty("date") Long date,
			@JsonProperty("tid") String tid,
			@JsonProperty("price") double price,
			@JsonProperty("amount") double amount,
			@JsonProperty("type") Integer type) {
		this.date = date;
		this.tid = tid;
		this.price = price;
		this.amount = amount;
		this.type = type;
	}

}
