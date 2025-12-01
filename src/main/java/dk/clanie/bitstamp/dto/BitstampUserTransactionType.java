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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * User transaction types.
 */
public enum BitstampUserTransactionType {

	DEPOSIT(0, "deposit"),
	WITHDRAWAL(1, "withdrawal"),
	MARKET_TRADE(2, "market_trade"),
	SUB_ACCOUNT_TRANSFER(14, "sub_account_transfer"),
	CREDITED_WITH_STAKING_REWARD(25, "credited_with_staking_reward"),
	SENDING_TOKENS_TO_STAKING(26, "sending_tokens_to_staking"),
	RETURNING_TOKENS_FROM_STAKING(27, "returning_tokens_from_staking"),
	TRANSFER(32, "transfer"),
	REFERRAL_REWARD(35, "referral_reward"),
	INTER_ACCOUNT_TRANSFER(40, "inter_account_transfer");

	private final int code;
	private final String name;

	BitstampUserTransactionType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	@JsonValue
	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	@JsonCreator
	public static BitstampUserTransactionType fromCode(int code) {
		for (BitstampUserTransactionType type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown user transaction type code: " + code);
	}

}
