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

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import dk.clanie.bitstamp.jackson.BitstampDateTimeDeserializer;
import lombok.Value;

/**
 * Represents a user transaction.
 * <p/>
 * User transactions include deposits, withdrawals, trades, and fees.
 * All monetary amounts are represented as Double values.
 */
@Value
public class BitstampUserTransaction {

	long id;
	
	@JsonDeserialize(using = BitstampDateTimeDeserializer.class)
	Instant datetime;
	
	BitstampUserTransactionType type;
	
	// Currency amounts (can be null)
	Double usd;
	Double eur;
	Double btc;
	Double eth;
	Double ltc;
	Double xrp;
	Double bch;
	Double xlm;
	Double pax;
	Double link;
	Double usdc;
	Double aave;
	Double bat;
	Double uma;
	Double dai;
	Double knc;
	Double mkr;
	Double zrx;
	Double gusd;
	Double algo;
	Double audio;
	Double crv;
	Double snx;
	Double uni;
	Double yfi;
	Double comp;
	Double grt;
	Double usdt;
	Double eurt;
	Double matic;
	Double sushi;
	Double chz;
	Double enj;
	Double hbar;
	Double alpha;
	Double axs;
	Double ftt;
	Double sand;
	Double storj;
	Double ada;
	Double avax;
	Double dot;
	Double sol;
	Double ftm;
	Double mana;
	Double dydx;
	Double gala;
	Double shib;
	Double amp;
	Double sgb;
	Double wbtc;
	Double perp;
	Double ape;
	
	// Fee information
	Double fee;
	
	// Order ID (for trades)
	Long orderId;
	
	// Market/pair (for trades)
	String market;


	@JsonCreator
	public BitstampUserTransaction(
			@JsonProperty("id") long id,
			@JsonProperty("datetime") Instant datetime,
			@JsonProperty("type") BitstampUserTransactionType type,
			@JsonProperty("usd") Double usd,
			@JsonProperty("eur") Double eur,
			@JsonProperty("btc") Double btc,
			@JsonProperty("eth") Double eth,
			@JsonProperty("ltc") Double ltc,
			@JsonProperty("xrp") Double xrp,
			@JsonProperty("bch") Double bch,
			@JsonProperty("xlm") Double xlm,
			@JsonProperty("pax") Double pax,
			@JsonProperty("link") Double link,
			@JsonProperty("usdc") Double usdc,
			@JsonProperty("aave") Double aave,
			@JsonProperty("bat") Double bat,
			@JsonProperty("uma") Double uma,
			@JsonProperty("dai") Double dai,
			@JsonProperty("knc") Double knc,
			@JsonProperty("mkr") Double mkr,
			@JsonProperty("zrx") Double zrx,
			@JsonProperty("gusd") Double gusd,
			@JsonProperty("algo") Double algo,
			@JsonProperty("audio") Double audio,
			@JsonProperty("crv") Double crv,
			@JsonProperty("snx") Double snx,
			@JsonProperty("uni") Double uni,
			@JsonProperty("yfi") Double yfi,
			@JsonProperty("comp") Double comp,
			@JsonProperty("grt") Double grt,
			@JsonProperty("usdt") Double usdt,
			@JsonProperty("eurt") Double eurt,
			@JsonProperty("matic") Double matic,
			@JsonProperty("sushi") Double sushi,
			@JsonProperty("chz") Double chz,
			@JsonProperty("enj") Double enj,
			@JsonProperty("hbar") Double hbar,
			@JsonProperty("alpha") Double alpha,
			@JsonProperty("axs") Double axs,
			@JsonProperty("ftt") Double ftt,
			@JsonProperty("sand") Double sand,
			@JsonProperty("storj") Double storj,
			@JsonProperty("ada") Double ada,
			@JsonProperty("avax") Double avax,
			@JsonProperty("dot") Double dot,
			@JsonProperty("sol") Double sol,
			@JsonProperty("ftm") Double ftm,
			@JsonProperty("mana") Double mana,
			@JsonProperty("dydx") Double dydx,
			@JsonProperty("gala") Double gala,
			@JsonProperty("shib") Double shib,
			@JsonProperty("amp") Double amp,
			@JsonProperty("sgb") Double sgb,
			@JsonProperty("wbtc") Double wbtc,
			@JsonProperty("perp") Double perp,
			@JsonProperty("ape") Double ape,
			@JsonProperty("fee") Double fee,
			@JsonProperty("order_id") Long orderId,
			@JsonProperty("market") String market) {
		this.id = id;
		this.datetime = datetime;
		this.type = type;
		this.usd = usd;
		this.eur = eur;
		this.btc = btc;
		this.eth = eth;
		this.ltc = ltc;
		this.xrp = xrp;
		this.bch = bch;
		this.xlm = xlm;
		this.pax = pax;
		this.link = link;
		this.usdc = usdc;
		this.aave = aave;
		this.bat = bat;
		this.uma = uma;
		this.dai = dai;
		this.knc = knc;
		this.mkr = mkr;
		this.zrx = zrx;
		this.gusd = gusd;
		this.algo = algo;
		this.audio = audio;
		this.crv = crv;
		this.snx = snx;
		this.uni = uni;
		this.yfi = yfi;
		this.comp = comp;
		this.grt = grt;
		this.usdt = usdt;
		this.eurt = eurt;
		this.matic = matic;
		this.sushi = sushi;
		this.chz = chz;
		this.enj = enj;
		this.hbar = hbar;
		this.alpha = alpha;
		this.axs = axs;
		this.ftt = ftt;
		this.sand = sand;
		this.storj = storj;
		this.ada = ada;
		this.avax = avax;
		this.dot = dot;
		this.sol = sol;
		this.ftm = ftm;
		this.mana = mana;
		this.dydx = dydx;
		this.gala = gala;
		this.shib = shib;
		this.amp = amp;
		this.sgb = sgb;
		this.wbtc = wbtc;
		this.perp = perp;
		this.ape = ape;
		this.fee = fee;
		this.orderId = orderId;
		this.market = market;
	}

}
