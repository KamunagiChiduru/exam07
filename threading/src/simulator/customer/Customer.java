package simulator.customer;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Queue;

import simulator.currency.Coin;
import simulator.currency.Coins;
import simulator.currency.Wallet;
import simulator.io.ConsoleIOManager;
import simulator.io.IOManager;
import simulator.product.Drink;
import simulator.product.Product;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.Queues;

public class Customer{
	/** 定数宣言のためのインタフェイス */
	private static interface Message{
		String RESULT_TITLE= "〜お買いもの結果〜";
		String I_HAVE_NO_PRODUCTS= "あなたは何も買っていません。";
		String I_HAVE_PRODUCTS= "あなたは%s持っています。";
		String I_HAVE_NO_MONEY= "あなたのお財布の中には何もありません。";
		String I_HAVE_MONEY= "あなたのお財布の中には%sあります。";
		String PRODUCT_FORMAT= "%sを%d本";
		String PRODUCT_SEPARATOR= "と、";
		String WALLET_FORMAT= "%sが%d枚";
		String WALLET_SEPARATOR= "、";
	}
	
	private final IOManager io;
	private final Wallet wallet;
	private final Multiset<Product> bucket;
	
	public Customer(){
		this.io= new ConsoleIOManager();
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
		return this.io;
	}
	
	public void showResult(){
		this.io.writeln(Message.RESULT_TITLE);
		
		if(this.bucket.isEmpty()){
			this.io.writeln(Message.I_HAVE_NO_PRODUCTS);
		}
		else{
			this.io.writeln(Message.I_HAVE_PRODUCTS, this.formatBoughtProducts());
		}
		
		if(this.wallet.isEmpty()){
			this.io.writeln(Message.I_HAVE_NO_MONEY);
		}
		else{
			this.io.writeln(Message.I_HAVE_MONEY, this.formatWallet());
		}
	}
	
	private String formatBoughtProducts(){
		Queue<Product> things= Queues.newPriorityQueue(this.bucket);
		
		return this.formatBoughtProducts(things);
	}
	
	private String formatBoughtProducts(Queue<? extends Product> things){
		if(things.isEmpty()){ return null; }
		
		Product thing= things.poll();
		String formatted= String.format( //
				Message.PRODUCT_FORMAT, thing.getName(), this.bucket.count(thing));
		
		Joiner joiner= Joiner.on(Message.PRODUCT_SEPARATOR).skipNulls();
		
		return joiner.join(formatted, this.formatBoughtProducts(things));
	}
	
	private String formatWallet(){
		ImmutableSortedSet<Coin> descUniqueCoins= ImmutableSortedSet.copyOf( //
				Coins.descComparator(), //
				this.wallet.getUniqueCoinSet() //
				);
		return this.formatWallet(Queues.newPriorityQueue(descUniqueCoins));
	}
	
	private String formatWallet(Queue<Coin> kinds){
		if(kinds.isEmpty()){ return null; }
		
		Coin coin= kinds.poll();
		String formatted= String.format(Message.WALLET_FORMAT, coin, this.wallet.count(coin));
		
		Joiner joiner= Joiner.on(Message.WALLET_SEPARATOR).skipNulls();
		return joiner.join(formatted, this.formatWallet(kinds));
	}
}
