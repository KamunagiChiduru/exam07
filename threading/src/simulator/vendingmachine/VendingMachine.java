package simulator.vendingmachine;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Queue;
import java.util.SortedSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

import simulator.currency.Coin;
import simulator.customer.Customer;
import simulator.io.Appender;
import simulator.io.Formattable;
import simulator.product.Product;
import simulator.util.Yen;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Queues;

public class VendingMachine implements Callable<Customer>{
	private final Appender appender;
	private final BlockingQueue<Customer> customerQueue= new LinkedBlockingQueue<>();
	private Yen totalAmount= new Yen(0);
	
	public VendingMachine(Appender appender){
		this.appender= appender;
	}
	
	public void comeNewCustomer(Customer newCustomer){
		this.customerQueue.add(newCustomer);
	}
	
	/**
	 * 
	 * @return 買い物が終わったお客さん
	 * @throws Exception
	 */
	@Override
	public Customer call() throws Exception{
		Customer customer= this.customerQueue.take();
		
		Operation op= Operation.SELECT_OPERATION;
		while(op != Operation.EXIT_OPERATION){
			op.setAppender(appender);
			op.setVendingMachine(this);
			op= op.perform(customer);
		}
		
		return customer;
	}
	
	public void pay(Coin coin){
		this.totalAmount= this.totalAmount.add(coin.getAmount());
	}
	
	/**
	 * 返却するコインの枚数が最小になるように、おつりを返却する。
	 * 
	 * @return
	 */
	public Collection<Coin> payback(){
		SortedSet<Coin> coinSet= ImmutableSortedSet.orderedBy(new Comparator<Coin>(){
			@Override
			public int compare(Coin o1, Coin o2){
				return -o1.getAmount().compareTo(o2.getAmount());
			}
		}).add(Coin.TEN, Coin.FIFTY, Coin.HUNDRED, Coin.FIVE_HUNDRED).build();
		Collection<Coin> payback= Lists.newArrayList();
		
		Yen remain= this.totalAmount;
		for(Coin coin : coinSet){
			while(remain.compareTo(coin.getAmount()) >= 0){
				remain= remain.subtract(coin.getAmount());
				
				if(remain.compareTo(Yen.zero()) < 0){ throw new AssertionError(); }
				
				payback.add(coin);
				
				if(remain.compareTo(Yen.zero()) == 0){
					this.appender.writeln(this.formatPayback(payback));
					return payback;
				}
			}
		}
		
		// おつりなし
		this.appender.writeln("現在0円なので、返却するコインはありません。");
		
		return Collections.emptyList();
	}
	
	private String formatPayback(Collection<Coin> payback){
		// コインの種類
		ImmutableSortedSet<Coin> kinds= ImmutableSortedSet.orderedBy(new Comparator<Coin>(){
			@Override
			public int compare(Coin o1, Coin o2){
				return o1.compareTo(o2);
			}
		}).addAll(payback).build();
		// コインの枚数
		ImmutableMultiset<Coin> ncoins= ImmutableMultiset.copyOf(payback);
		// 合計金額
		Yen total= Yen.zero();
		for(Coin coin : payback){
			total= total.add(coin.getAmount());
		}
		// 高い順に表示
		return String.format("%s円の返却です。%sを返却します。", total,
				this.formatPaybackDetails(Queues.newPriorityQueue(kinds), ncoins));
	}
	
	private String formatPaybackDetails(Queue<Coin> kinds, Multiset<Coin> ncoins){
		if(kinds.isEmpty()){ return ""; }
		
		Coin coin= kinds.poll();
		int count= ncoins.count(coin);
		
		return String.format("%s玉%d枚と", coin.getDisplayText(), count)
				+ this.formatPaybackDetails(kinds, ncoins);
	}
	
	public Yen getTotalAmount(){
		return this.totalAmount;
	}
	
	/**
	 * 操作
	 * 
	 * @author kamichidu
	 */
	private enum Operation implements Formattable{
		EXIT_OPERATION{
			@Override
			Operation perform(Customer customer){
				return null;
			}
			
			@Override
			public String getDisplayText(){
				return "終了";
			}
		},
		SELECT_OPERATION{
			@Override
			Operation perform(Customer customer){
				ImmutableList<Operation> operationList= ImmutableList.of( //
						SELECT_COIN, //
						SELECT_PRODUCT, //
						PAYBACK_COIN, //
						EXIT_OPERATION //
						);
				
				Operation selected= appender.select("操作を選択してください。", operationList);
				
				return selected;
			}
			
			@Override
			public String getDisplayText(){
				return "操作の選択";
			}
		},
		SELECT_COIN{
			@Override
			Operation perform(Customer customer){
				ImmutableList<Coin> coinList= ImmutableList.of( //
						Coin.TEN, //
						Coin.FIFTY, //
						Coin.HUNDRED, //
						Coin.FIVE_HUNDRED //
						);
				
				Coin selected= appender.select("入れるコインを選択してください。", coinList);
				
				vendingMachine.pay(selected);
				appender.writeln("現在%s円入っています。", vendingMachine.getTotalAmount());
				
				return SELECT_OPERATION;
			}
			
			@Override
			public String getDisplayText(){
				return "コインを入れる";
			}
		},
		PAYBACK_COIN{
			@Override
			Operation perform(Customer customer){
				// コインの枚数が最小となるようにおつりを出す
				customer.giveCoins(vendingMachine.payback());
				
				return EXIT_OPERATION;
			}
			
			@Override
			public String getDisplayText(){
				return "コインを戻す";
			}
		},
		SELECT_PRODUCT{
			@Override
			Operation perform(Customer customer){
				ImmutableList<Product> productList= ImmutableList.of( //
						Product.COFFEE, //
						Product.BOTTLE_COLA, //
						Product.ENERGY_DRINK, //
						Product.TEE_OF_TOKUHO //
						);
				Product selected= appender.select("商品を選択してください。", productList);
				
				appender.writeln("selected -> %s", selected.getDisplayText());
				
				return PAYBACK_COIN;
			}
			
			@Override
			public String getDisplayText(){
				return "買う";
			}
		},
		;
		
		abstract Operation perform(Customer customer);
		
		VendingMachine vendingMachine;
		Appender appender;
		
		void setAppender(Appender appender){
			this.appender= appender;
		}
		
		void setVendingMachine(VendingMachine vendingMachine){
			this.vendingMachine= vendingMachine;
		}
	}
}
