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

import static dk.clanie.bitstamp.dto.BitstampCurrencyType.CRYPTO;
import static dk.clanie.bitstamp.dto.BitstampCurrencyType.FIAT;

import org.jspecify.annotations.Nullable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Currency code enumeration for Bitstamp.
 * <p>
 * Includes both fiat currencies and cryptocurrencies traded on Bitstamp.
 */
@Getter
@RequiredArgsConstructor
public enum BitstampCurrencyCode {

	// Fiat currencies
	EUR("Euro", "Euro", FIAT),
	GBP("British Pound Sterling", "British Pound Sterling", FIAT),
	SGD("Singapore Dollar", "Singapore Dollar", FIAT),
	USD("US Dollar", "United States Dollar", FIAT),

	// Stablecoins
	DAI("Dai", "DAI Stablecoin", CRYPTO),
	EURC("Euro Coin", "Circle Euro Coin", CRYPTO),
	EURCV("Euro Coin V", "Euro Coin V", CRYPTO),
	GUSD("Gemini Dollar", "Gemini Dollar Stablecoin", CRYPTO),
	GYEN("GYEN", "GMO JPY Stablecoin", CRYPTO),
	PYUSD("PayPal USD", "PayPal USD Stablecoin", CRYPTO),
	RLUSD("Ripple USD", "Ripple USD Stablecoin", CRYPTO),
	USDC("USD Coin", "USD Coin Stablecoin", CRYPTO),
	USDT("Tether", "Tether USD Stablecoin", CRYPTO),
	UST("TerraUSD", "TerraUSD Stablecoin (deprecated)", CRYPTO),
	VCHF("VCHF", "VCHF Stablecoin", CRYPTO),
	VEUR("VEUR", "VEUR Stablecoin", CRYPTO),
	XSGD("XSGD", "Xfers Singapore Dollar Stablecoin", CRYPTO),
	ZUSD("ZUSD", "ZUSD Stablecoin", CRYPTO),

	// Major cryptocurrencies
	BTC("Bitcoin", "Bitcoin", CRYPTO),
	ETH("Ethereum", "Ethereum", CRYPTO),
	ETH2("Ethereum 2.0", "Staked Ether", CRYPTO),
	ETH2R("Ethereum 2.0 Rewards", "Staked Ether Rewards", CRYPTO),
	XRP("Ripple", "Ripple", CRYPTO),
	LTC("Litecoin", "Litecoin", CRYPTO),
	BCH("Bitcoin Cash", "Bitcoin Cash", CRYPTO),
	SOL("Solana", "Solana", CRYPTO),
	ADA("Cardano", "Cardano", CRYPTO),
	DOGE("Dogecoin", "Dogecoin", CRYPTO),
	DOT("Polkadot", "Polkadot", CRYPTO),
	AVAX("Avalanche", "Avalanche", CRYPTO),
	NEAR("NEAR Protocol", "NEAR Protocol", CRYPTO),
	ICP("Internet Computer", "Internet Computer", CRYPTO),
	SUI("Sui", "Sui", CRYPTO),
	TON("Toncoin", "The Open Network", CRYPTO),
	ETC("Ethereum Classic", "Ethereum Classic", CRYPTO),
	EGLD("Elrond", "Elrond (MultiversX)", CRYPTO),

	// DeFi tokens
	AAVE("Aave", "Aave", CRYPTO),
	UNI("Uniswap", "Uniswap", CRYPTO),
	LINK("Chainlink", "Chainlink", CRYPTO),
	CRV("Curve", "Curve DAO Token", CRYPTO),
	COMP("Compound", "Compound", CRYPTO),
	MKR("Maker", "Maker", CRYPTO),
	SNX("Synthetix", "Synthetix", CRYPTO),
	SUSHI("SushiSwap", "SushiSwap", CRYPTO),
	YFI("Yearn Finance", "Yearn.finance", CRYPTO),
	LDO("Lido DAO", "Lido DAO", CRYPTO),
	ONDO("Ondo Finance", "Ondo Finance", CRYPTO),
	ENA("Ethena", "Ethena", CRYPTO),

	// Layer 2 & Scaling
	ARB("Arbitrum", "Arbitrum", CRYPTO),
	OP("Optimism", "Optimism", CRYPTO),
	MATIC("Matic", "Matic)", CRYPTO),
	POL("Polygon", "Polygon (formerly MATIC)", CRYPTO),
	STRK("Starknet", "Starknet", CRYPTO),

	// Meme coins
	SHIB("Shiba Inu", "Shiba Inu", CRYPTO),
	PEPE("Pepe", "Pepe", CRYPTO),
	WIF("Dogwifhat", "dogwifhat", CRYPTO),
	BONK("Bonk", "Bonk", CRYPTO),
	FLOKI("Floki Inu", "Floki Inu", CRYPTO),
	BOME("Book of Meme", "Book of Meme", CRYPTO),
	MEW("Cat in a Dogs World", "Cat in a Dogs World", CRYPTO),
	POPCAT("Popcat", "Popcat", CRYPTO),
	PNUT("Peanut the Squirrel", "Peanut the Squirrel", CRYPTO),
	WEN("Wen", "Wen", CRYPTO),
	MOG("Mog Coin", "Mog Coin", CRYPTO),
	FARTCOIN("Fartcoin", "Fartcoin", CRYPTO),
	PENGU("Pengu", "Pudgy Penguins", CRYPTO),
	MOODENG("Moo Deng", "Moo Deng", CRYPTO),

	// Political & Celebrity tokens
	TRUMP("TRUMP", "TRUMP Meme Token", CRYPTO),
	MELANIA("MELANIA", "MELANIA Meme Token", CRYPTO),

	// Gaming & Metaverse
	AXS("Axie Infinity", "Axie Infinity", CRYPTO),
	SAND("The Sandbox", "The Sandbox", CRYPTO),
	MANA("Decentraland", "Decentraland", CRYPTO),
	GALA("Gala", "Gala", CRYPTO),
	ENJ("Enjin Coin", "Enjin Coin", CRYPTO),
	IMX("Immutable X", "Immutable X", CRYPTO),
	APE("ApeCoin", "ApeCoin", CRYPTO),

	// AI & Oracle tokens
	FET("Fetch.ai", "Fetch.ai", CRYPTO),
	RNDR("Render Token", "Render Token", CRYPTO),
	GRT("The Graph", "The Graph", CRYPTO),
	VIRTUAL("Virtual Protocol", "Virtual Protocol", CRYPTO),
	TAI("TAI", "TAI", CRYPTO),

	// Infrastructure & Utility
	XLM("Stellar", "Stellar Lumens", CRYPTO),
	ALGO("Algorand", "Algorand", CRYPTO),
	HBAR("Hedera", "Hedera Hashgraph", CRYPTO),
	FTM("Fantom", "Fantom", CRYPTO),
	XDC("XDC Network", "XDC Network", CRYPTO),
	COREUM("Coreum", "Coreum", CRYPTO),
	CSPR("Casper", "Casper Network", CRYPTO),
	XCN("Chain", "Onyxcoin (formerly Chain)", CRYPTO),
	BNB("Binance Coin", "Binance Coin", CRYPTO),
	ASTER("Astar", "Astar Network", CRYPTO),
	AVNT("Aventus", "Aventus", CRYPTO),

	// Solana ecosystem
	JUP("Jupiter", "Jupiter", CRYPTO),
	PYTH("Pyth Network", "Pyth Network", CRYPTO),

	// Exchange & Governance tokens
	UMA("UMA", "UMA", CRYPTO),
	BAT("Basic Attention Token", "Basic Attention Token", CRYPTO),
	ZRX("0x", "0x Protocol", CRYPTO),
	KNC("Kyber Network", "Kyber Network Crystal", CRYPTO),
	LRC("Loopring", "Loopring", CRYPTO),
	CHZ("Chiliz", "Chiliz", CRYPTO),
	_1INCH("1inch", "1inch Network", CRYPTO) {
		@Override
		public String getCode() {
			return "1INCH";
		}
	},
	ENS("Ethereum Name Service", "Ethereum Name Service", CRYPTO),
	INJ("Injective", "Injective Protocol", CRYPTO),
	SEI("Sei", "Sei", CRYPTO),
	HYPE("Hyperliquid", "Hyperliquid", CRYPTO),
	WOO("WOO Network", "WOO Network", CRYPTO),

	// Other tokens
	AMP("Amp", "Amp", CRYPTO),
	AUDIO("Audius", "Audius", CRYPTO),
	BOBA("Boba Network", "Boba Network", CRYPTO),
	SKL("SKALE", "SKALE Network", CRYPTO),
	FLR("Flare", "Flare Network", CRYPTO),
	STORJ("Storj", "Storj", CRYPTO),
	PERP("Perpetual Protocol", "Perpetual Protocol", CRYPTO),
	CTSI("Cartesi", "Cartesi", CRYPTO),
	BLUR("Blur", "Blur", CRYPTO),
	LMWR("LimeWire", "LimeWire Token", CRYPTO),
	RGT("Rari Governance Token", "Rari Governance Token", CRYPTO),
	SGB("Songbird", "Songbird", CRYPTO),
	VEGA("Vega Protocol", "Vega Protocol", CRYPTO),
	WECAN("WeCan", "WeCan", CRYPTO),
	TRAC("OriginTrail", "OriginTrail", CRYPTO),
	SYRUP("Syrup", "Syrup", CRYPTO),
	CTX("Cryptex Finance", "Cryptex Finance", CRYPTO),
	CXT("CXT", "CXT", CRYPTO),
	XCHNG("XCHNG", "XCHNG", CRYPTO),
	XPL("XPL", "XPL", CRYPTO),
	BIO("BIO Protocol", "BIO Protocol", CRYPTO),
	SPK("Spark", "Spark", CRYPTO),
	TRUF("Truefi", "Truefi", CRYPTO),
	SMT("SmartMesh", "SmartMesh", CRYPTO),
	ZETA("ZetaChain", "ZetaChain", CRYPTO),
	WLFI("WLFI", "WLFI", CRYPTO),

	// Wrapped tokens
	WBTC("Wrapped Bitcoin", "Wrapped Bitcoin", CRYPTO),

	// Special
	USD_PERP("USD Perpetual", "USD Perpetual Futures", CRYPTO) {
		@Override
		public String getCode() {
			return "USD-PERP";
		}
	};


	private final String name;
	private final String description;
	private final BitstampCurrencyType currencyType;


	/**
	 * Returns the currency code as string.
	 * <p>
	 * Note: BitstampCurrencyCode.name() is the default, but a few have custom implementations
	 * because their code differs from the enum name.
	 *
	 * @return the currency code
	 */
	public String getCode() {
		return name();
	}


	/**
	 * Finds a currency code by its string representation.
	 * <p>
	 * This method handles case-insensitive conversion and special cases.
	 *
	 * @param code the currency code string
	 * @return the currency code enum
	 * @throws IllegalArgumentException if the code is not found
	 */
	public static @Nullable BitstampCurrencyCode fromString(@Nullable String code) {
		if (code == null) return null;
		code = code.toUpperCase();
		try {
			return valueOf(code);
		} catch (IllegalArgumentException e) {
			if ("1INCH".equals(code)) return _1INCH;
			if ("USD-PERP".equals(code)) return USD_PERP;
			throw new IllegalArgumentException("Unknown currency code: " + code);
		}
	}


}
