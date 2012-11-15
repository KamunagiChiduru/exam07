package simulator.currency;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import simulator.util.Yen;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;

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
	
	/**
	 * origを、複数枚のorigよりも安いコインに両替する。
	 * 
	 * @param orig
	 * @return
	 */
	public static Collection<Coin> exchange(Coin orig){
		// 1円は両替不可
		if(orig.equals(oneYenCoin())){ return Arrays.asList(orig); }
		final Coin upperBounds= orig;
		// origよりも安いけれど、金額の高い順にコインを1種類ずつ保持
		ImmutableSortedSet<Coin> coinSet= ImmutableSortedSet.copyOf( //
				descComparator(), //
				Collections2.filter(getOneOfEach(), new Predicate<Coin>(){
					@Override
					public boolean apply(Coin o){
						return o.compareTo(upperBounds) < 0;
					}
				}));
		Collection<Coin> result= Lists.newArrayList();
		
		Yen remaining= orig.getAmount();
		for(Coin coin : coinSet){
			while(remaining.compareTo(coin.getAmount()) >= 0){
				remaining= remaining.subtract(coin.getAmount());
				
				if(remaining.compareTo(Yen.zero()) < 0){ throw new AssertionError(); }
				
				result.add(coin);
				
				if(remaining.compareTo(Yen.zero()) == 0){ return result; }
			}
		}
		throw new AssertionError();
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
