package simulator.currency;

import java.util.Collections;
import java.util.Comparator;

import simulator.util.Yen;

import com.google.common.collect.ImmutableList;

public final class Coins{
	private Coins(){}
	
	public static ImmutableList<Coin> getOneOfEach(){
		return ImmutableList.of( //
				oneYenCoin(), //
				fiveYenCoin(), //
				tenYenCoin(), //
				fiftyYenCoin(), //
				oneHundredYenCoin(), //
				fiveHundredYenCoin() //
				);
	}
	
	public static Coin oneYenCoin(){
		return new Coin(Yen.one());
	}
	
	public static Coin fiveYenCoin(){
		return new Coin(Yen.five());
	}
	
	public static Coin tenYenCoin(){
		return new Coin(Yen.ten());
	}
	
	public static Coin fiftyYenCoin(){
		return new Coin(Yen.fifty());
	}
	
	public static Coin oneHundredYenCoin(){
		return new Coin(Yen.oneHundred());
	}
	
	public static Coin fiveHundredYenCoin(){
		return new Coin(Yen.fiveHundred());
	}
	
	public static Comparator<Coin> ascComparator(){
		return Collections.reverseOrder(descComparator());
	}
	
	public static Comparator<Coin> descComparator(){
		return new Comparator<Coin>(){
			@Override
			public int compare(Coin lhs, Coin rhs){
				return -lhs.compareTo(rhs);
			}
		};
	}
}
