package simulator.customer;

import java.util.Collection;
import java.util.Queue;

import simulator.currency.Coin;
import simulator.currency.Coins;
import simulator.currency.Wallet;
import simulator.io.ConsoleIOManager;
import simulator.io.IOManager;
import simulator.product.Drink;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.Queues;

import static com.google.common.base.Preconditions.checkNotNull;

public class Customer{
	private final IOManager appender;
	private final Wallet wallet;
	private final Multiset<Drink> bucket;
	
	public Customer(){
		this.appender= new ConsoleIOManager();
		this.wallet= new Wallet( //
				Coins.fiveHundredYenCoin(), // 500円玉が1枚
				Coins.oneHundredYenCoin(), Coins.oneHundredYenCoin(), // 100円玉が2枚
				Coins.fiftyYenCoin(), // 50円玉が1枚
				Coins.tenYenCoin(), Coins.tenYenCoin(), Coins.tenYenCoin() // 10円玉が3枚
		);
		this.bucket= HashMultiset.create();
		;
	}
	
	public ImmutableSet<Coin> getUniqueCoinSet(){
		return this.wallet.getUniqueCoinSet();
	}
	
	public Coin pay(Coin paied){
		return this.wallet.get(paied);
	}
	
	public void buy(Drink bought){
		this.bucket.add(checkNotNull(bought));
	}
	
	public void giveCoins(Collection<Coin> coins){
		this.wallet.addAll(checkNotNull(coins));
	}
	
	public boolean hasCoin(){
		return !this.wallet.isEmpty();
	}
	
	public IOManager getIOManager(){
		return this.appender;
	}
	
	public void showResult(){
		StringBuffer buf= new StringBuffer();
		
		buf.append("〜お買いもの結果〜%n");
		
		if(this.bucket.isEmpty()){
			buf.append("あなたは何も買っていません。%n");
		}
		else{
			buf.append(String.format("あなたは%sを持っています。%n", this.formatBoughtProducts()));
		}
		
		if(this.wallet.isEmpty()){
			buf.append("あなたのお財布の中には何もありません。%n");
		}
		else{
			buf.append(String.format("あなたのお財布の中には%sあります。%n", this.formatWallet()));
		}
		
		this.appender.write(buf.toString());
	}
	
	private String formatBoughtProducts(){
		return "hogehoge";
	}
	
	private String formatWallet(){
		ImmutableSortedSet<Coin> descUniqueCoins= ImmutableSortedSet.copyOf( //
				Coins.descComparator(), //
				this.wallet.getUniqueCoinSet() //
				);
		return this.formatWallet(Queues.newPriorityQueue(descUniqueCoins));
	}
	
	private String formatWallet(Queue<Coin> kinds){
		if(kinds.isEmpty()){ return ""; }
		
		Coin coin= kinds.poll();
		
		return String.format("%sが%d枚、%s", coin, this.wallet.count(coin), this.formatWallet(kinds));
	}
}
