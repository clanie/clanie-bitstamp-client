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

import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.AAVE;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.ARB;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.BTC;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.DAI;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.DOGE;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.ETH;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.EUR;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.GBP;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.LINK;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.MELANIA;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.OP;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.PEPE;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.POL;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.SGD;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.SHIB;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.TRUMP;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.UNI;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.USD;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.USDC;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.USDT;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.USD_PERP;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode.XRP;
import static dk.clanie.bitstamp.dto.BitstampCurrencyCode._1INCH;
import static dk.clanie.bitstamp.dto.BitstampCurrencyType.CRYPTO;
import static dk.clanie.bitstamp.dto.BitstampCurrencyType.FIAT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class BitstampCurrencyCodeTest {

	@Test
	void testFiatCurrencies() {
		assertThat(USD.getCurrencyType()).isEqualTo(FIAT);
		assertThat(EUR.getCurrencyType()).isEqualTo(FIAT);
		assertThat(GBP.getCurrencyType()).isEqualTo(FIAT);
		assertThat(SGD.getCurrencyType()).isEqualTo(FIAT);
	}

	@Test
	void testCryptoCurrencies() {
		assertThat(BTC.getCurrencyType()).isEqualTo(CRYPTO);
		assertThat(ETH.getCurrencyType()).isEqualTo(CRYPTO);
		assertThat(XRP.getCurrencyType()).isEqualTo(CRYPTO);
	}

	@Test
	void testStablecoins() {
		assertThat(USDT.getCurrencyType()).isEqualTo(CRYPTO);
		assertThat(USDC.getCurrencyType()).isEqualTo(CRYPTO);
		assertThat(DAI.getCurrencyType()).isEqualTo(CRYPTO);
	}

	@Test
	void testGetCode() {
		assertThat(BTC.getCode()).isEqualTo("BTC");
		assertThat(USD.getCode()).isEqualTo("USD");
		assertThat(_1INCH.getCode()).isEqualTo("1INCH");
		assertThat(USD_PERP.getCode()).isEqualTo("USD-PERP");
	}

	@Test
	void testFromString() {
		assertThat(BitstampCurrencyCode.fromString("BTC")).isEqualTo(BTC);
		assertThat(BitstampCurrencyCode.fromString("USD")).isEqualTo(USD);
		assertThat(BitstampCurrencyCode.fromString("1INCH")).isEqualTo(_1INCH);
		assertThat(BitstampCurrencyCode.fromString("USD-PERP")).isEqualTo(USD_PERP);
	}

	@Test
	void testFromStringInvalid() {
		assertThatThrownBy(() -> BitstampCurrencyCode.fromString("INVALID"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Unknown currency code: INVALID");
	}

	@Test
	void testDescriptions() {
		assertThat(BTC.getName()).isEqualTo("Bitcoin");
		assertThat(BTC.getDescription()).isEqualTo("Bitcoin");
		
		assertThat(USD.getName()).isEqualTo("US Dollar");
		assertThat(USD.getDescription()).isEqualTo("United States Dollar");
		
		assertThat(USDT.getName()).isEqualTo("Tether");
		assertThat(USDT.getDescription()).isEqualTo("Tether USD Stablecoin");
	}

	@Test
	void testMemeCoinsCoverage() {
		assertThat(PEPE.getCurrencyType()).isEqualTo(CRYPTO);
		assertThat(SHIB.getCurrencyType()).isEqualTo(CRYPTO);
		assertThat(DOGE.getCurrencyType()).isEqualTo(CRYPTO);
		assertThat(BitstampCurrencyCode.BONK.getCurrencyType()).isEqualTo(CRYPTO);
	}

	@Test
	void testPoliticalTokens() {
		assertThat(TRUMP.getCurrencyType()).isEqualTo(CRYPTO);
		assertThat(MELANIA.getCurrencyType()).isEqualTo(CRYPTO);
	}

	@Test
	void testDeFiTokens() {
		assertThat(AAVE.getCurrencyType()).isEqualTo(CRYPTO);
		assertThat(UNI.getCurrencyType()).isEqualTo(CRYPTO);
		assertThat(LINK.getCurrencyType()).isEqualTo(CRYPTO);
	}

	@Test
	void testLayer2Tokens() {
		assertThat(ARB.getCurrencyType()).isEqualTo(CRYPTO);
		assertThat(OP.getCurrencyType()).isEqualTo(CRYPTO);
		assertThat(POL.getCurrencyType()).isEqualTo(CRYPTO);
	}

}
