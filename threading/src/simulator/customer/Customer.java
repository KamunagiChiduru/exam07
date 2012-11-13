package simulator.customer;

import java.util.Collection;

import simulator.currency.Coin;
import simulator.io.Appender;
import simulator.io.ConsoleAppender;
import simulator.io.DefaultLayout;
import simulator.product.Product;


import com.google.common.collect.Lists;

public class Customer{
	private final Appender appender;
	private final Collection<Coin> wallet;
	private final Collection<Product> bucket;
	
	public Customer(){
		this.appender= new ConsoleAppender(new DefaultLayout());
		this.wallet= Lists.newArrayList(Coin.FIVE_HUNDRED, Coin.HUNDRED, Coin.HUNDRED, Coin.FIFTY,
				Coin.TEN, Coin.TEN, Coin.TEN);
		this.bucket= Lists.newArrayList();
	}
	
	public void giveCoins(Collection<Coin> coins){
		System.out.println("giveCoins " + coins);
	}
	
	public void showResult(Appender appender){
		this.appender.writeln("〜お買いもの結果〜");
		
		if(this.bucket.isEmpty()){
			this.appender.writeln("あなたは何も買っていません。");
		}
		else{
			this.appender.writeln("あなたは%sを持っています。", this.formatBoughtProducts());
		}
		
		if(this.wallet.isEmpty()){
			this.appender.writeln("あなたのお財布の中には何もありません。");
		}
		else{
			this.appender.writeln("あなたのお財布の中には%sあります。", this.formatWallet());
		}
	}
	
	private String formatBoughtProducts(){
		return "hogehoge";
	}
	
	private String formatWallet(){
		return "piyopiyo";
	}
}
