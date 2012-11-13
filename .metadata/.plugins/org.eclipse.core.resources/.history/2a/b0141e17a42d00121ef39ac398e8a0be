package main;

import java.util.Collection;

import com.google.common.collect.Lists;

import main.io.Appender;

public class Customer{
	private final Collection<Coin> wallet;
	
	public Customer(){
		this.wallet= Lists.newArrayList(Coin.FIVE_HUNDRED, Coin.HUNDRED, Coin.HUNDRED, Coin.FIFTY,
				Coin.TEN, Coin.TEN, Coin.TEN);
	}
	
	public void giveCoins(Collection<Coin> coins){
		System.out.println("giveCoins " + coins);
	}
	
	public void showResult(Appender appender){
		System.out.println("showResult");
	}
}
