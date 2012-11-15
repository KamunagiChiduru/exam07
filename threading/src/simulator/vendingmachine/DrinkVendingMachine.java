package simulator.vendingmachine;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Formattable;
import java.util.Formatter;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import simulator.currency.Coin;
import simulator.currency.Coins;
import simulator.currency.Wallet;
import simulator.customer.Customer;
import simulator.io.IOManager;
import simulator.product.Drink;
import simulator.util.Yen;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

public class DrinkVendingMachine implements VendingMachine<Drink>{
	private static interface Message{
		String THIS_NAME= "ドリンク";
		String TO_STRING_FORMAT= "%s";
		String NO_PAYBACK_COINS= "現在0円なので、返却するコインはありません。";
		String PAYBACK_FORMAT= "%s円の返却です。%sを返却します。";
		String PAYBACK_COIN_FORMAT= "%s玉%d枚";
		String PAYBACK_SEPARATOR= "と";
		String EXIT_OPERATION_NAME= "終了";
		String PLEASE_SELECT_OPERATION= "操作を選択してください。";
		String SELECT_OPERATION_NAME= "操作の選択";
		String YOU_HAVE_NO_MONEY= "あなたはお金を持っていません。";
		String PLEASE_SELECT_COIN= "入れるコインを選択してください。";
		String CURRENT_MONEY= "現在%s円入っています。";
		String SELECT_COIN_NAME= "コインを入れる";
		String PAYBACK_COIN_NAME= "コインを戻す";
		String PLEASE_SELECT_PRODUCT= "商品を選択してください。";
		String I_CANT_SELL_ITS_PRODUCT= "現在の投入金額は%#sなので、%#sの商品はお売りできません。";
		String SELECT_PRODUCT_NAME= "買う";
	}
	
	private final IOManager io;
	private final BlockingQueue<Customer> customerQueue= new LinkedBlockingQueue<>();
	private final Wallet coinPool;
	private final Wallet safeBox;
	
	public DrinkVendingMachine(IOManager appender){
		this.io= appender;
		this.coinPool= new Wallet();
		this.safeBox= new Wallet();
	}
	
	/**
	 * @see simulator.vendingmachine.VendingMachine#comeNewCustomer(simulator.customer.Customer)
	 */
	@Override
	public void comeNewCustomer(Customer newCustomer){
		this.customerQueue.add(newCustomer);
	}
	
	/**
	 * @see simulator.vendingmachine.VendingMachine#call()
	 */
	@Override
	public Customer call() throws Exception{
		Customer customer= this.customerQueue.take();
		
		Operation op= new SelectOperation();
		while(op != null){
			op= op.perform(customer);
		}
		
		return customer;
	}
	
	/**
	 * @see simulator.vendingmachine.VendingMachine#putInto(simulator.currency.Coin)
	 */
	@Override
	public void putInto(Coin coin){
		this.coinPool.add(coin);
	}
	
	/**
	 * @see simulator.vendingmachine.VendingMachine#sell(simulator.product.Drink)
	 */
	@Override
	public Drink sell(Drink product){
		// XXX: 在庫を気にしないで良いので、そのまま返す
		return product;
	}
	
	/**
	 * @see simulator.vendingmachine.VendingMachine#payback()
	 */
	@Override
	public Collection<Coin> payback(){
		// 金額の高い順にコインを1種類ずつ保持
		ImmutableSortedSet<Coin> coinSet= ImmutableSortedSet.copyOf( //
				Coins.descComparator(), //
				Coins.getOneOfEach() //
				);
		Collection<Coin> payback= Lists.newArrayList();
		
		Yen remain= this.coinPool.getTotalAmount();
		for(Coin coin : coinSet){
			while(remain.compareTo(coin.getAmount()) >= 0){
				remain= remain.subtract(coin.getAmount());
				
				if(remain.compareTo(Yen.zero()) < 0){ throw new AssertionError(); }
				
				payback.add(coin);
				
				if(remain.compareTo(Yen.zero()) == 0){
					this.coinPool.clear();
					this.io.writeln(this.formatPayback(payback));
					return payback;
				}
			}
		}
		
		// おつりなし
		this.io.writeln(Message.NO_PAYBACK_COINS);
		return Collections.emptyList();
	}
	
	private String formatPayback(Collection<Coin> payback){
		// コインの種類
		ImmutableSortedSet<Coin> kinds= ImmutableSortedSet.copyOf(Coins.descComparator(), payback);
		// コインの枚数と合計金額
		Wallet workWallet= new Wallet(payback);
		// 高い順に表示
		return String.format(Message.PAYBACK_FORMAT, //
				workWallet.getTotalAmount(), //
				this.formatPaybackDetails( //
						Queues.newPriorityQueue(kinds), //
						ImmutableMultiset.copyOf(workWallet)) //
				);
	}
	
	private String formatPaybackDetails(Queue<Coin> kinds, ImmutableMultiset<Coin> ncoins){
		if(kinds.isEmpty()){ return null; }
		
		Coin coin= kinds.poll();
		String formatted= String.format(Message.PAYBACK_COIN_FORMAT, coin, ncoins.count(coin));
		
		Joiner joiner= Joiner.on(Message.PAYBACK_SEPARATOR).skipNulls();
		
		return joiner.join(formatted, this.formatPaybackDetails(kinds, ncoins));
	}
	
	/**
	 * @see simulator.vendingmachine.VendingMachine#getTotalAmount()
	 */
	@Override
	public Yen getTotalAmount(){
		return this.coinPool.getTotalAmount();
	}
	
	@Override
	public int getCountOfCoins(){
		return this.coinPool.size();
	}
	
	@Override
	public void formatTo(Formatter formatter, int flags, int width, int precision){
		formatter.format(Message.THIS_NAME);
	}
	
	@Override
	public String toString(){
		return String.format(Message.TO_STRING_FORMAT, this);
	}
	
	/**
	 * 自動販売機に対する操作。.<br>
	 * {@link #perform(Customer)}の返り値は、次に行われる操作となる。<br>
	 * また、次に行われる操作が存在しないときは、nullを返す。<br>
	 * 
	 * @author E.Sekito
	 * @since 2012/11/15
	 */
	private static interface Operation extends Formattable{
		/**
		 * ある操作を、customerに対して実行する。
		 * 
		 * @param customer
		 *            お客さん
		 * @return 次に行われる操作。そんな操作が存在しなければnull。
		 * @throws NullPointerException
		 *             customerがnullのとき
		 */
		Operation perform(Customer customer);
	}
	
	private class Exit implements Operation{
		private final Operation beforeExit;
		
		Exit(){
			this.beforeExit= null;
		}
		
		Exit(Operation performBeforeExit){
			this.beforeExit= performBeforeExit;
		}
		
		@Override
		public Operation perform(Customer customer){
			checkNotNull(customer);
			// 終了前に呼ぶ操作が指定されていたときには、操作を呼んでから終了する
			if(this.beforeExit != null){
				this.beforeExit.perform(customer);
			}
			return null;
		}
		
		@Override
		public void formatTo(Formatter fmt, int flags, int width, int precision){
			fmt.format(Message.EXIT_OPERATION_NAME);
		}
	}
	
	private class SelectOperation implements Operation{
		@Override
		public Operation perform(Customer customer){
			checkNotNull(customer);
			ImmutableList<Operation> operationList= ImmutableList.of( //
					new SelectCoin(), //
					new SelectProduct(), //
					new Payback(this), //
					new Exit(new Payback()) //
					);
			
			Operation selected= io.select(Message.PLEASE_SELECT_OPERATION, operationList);
			
			return selected;
		}
		
		@Override
		public void formatTo(Formatter fmt, int flags, int width, int precision){
			fmt.format(Message.SELECT_OPERATION_NAME);
		}
	}
	
	private class SelectCoin implements Operation{
		@Override
		public Operation perform(Customer customer){
			checkNotNull(customer);
			
			// お客さんがコインを持っていない場合
			if( !customer.hasCoin()){
				io.writeln(Message.YOU_HAVE_NO_MONEY);
				
				return new SelectOperation();
			}
			
			ImmutableSortedSet<Coin> coinList= ImmutableSortedSet.copyOf( //
					Coins.descComparator(), //
					customer.getUniqueCoinSet() //
					);
			
			Coin selected= io.select(Message.PLEASE_SELECT_COIN, coinList);
			
			putInto(customer.pay(selected));
			io.writeln(Message.CURRENT_MONEY, getTotalAmount());
			
			return new SelectOperation();
		}
		
		@Override
		public void formatTo(Formatter fmt, int flags, int width, int precision){
			fmt.format(Message.SELECT_COIN_NAME);
		}
	}
	
	private class Payback implements Operation{
		private final Operation nextOperation;
		
		Payback(){
			// おつりを出したら、その後は終了がデフォルト
			this.nextOperation= new Exit();
		}
		
		Payback(Operation nextOperation){
			this.nextOperation= nextOperation;
		}
		
		@Override
		public Operation perform(Customer customer){
			checkNotNull(customer);
			// コインの枚数が最小となるようにおつりを出す
			customer.giveCoins(payback());
			
			return this.nextOperation;
		}
		
		@Override
		public void formatTo(Formatter fmt, int flags, int width, int precision){
			fmt.format(Message.PAYBACK_COIN_NAME);
		}
	}
	
	private class SelectProduct implements Operation{
		@Override
		public Operation perform(Customer customer){
			checkNotNull(customer);
			ImmutableList<Drink> productList= ImmutableList.of( //
					Drink.COFFEE, //
					Drink.BOTTLE_COLA, //
					Drink.ENERGY_DRINK, //
					Drink.TEE_OF_TOKUHO //
					);
			Drink selected= io.select(Message.PLEASE_SELECT_PRODUCT, productList);
			
			// 売ることができない場合
			if( !this.canSellIt(selected)){
				io.writeln(Message.I_CANT_SELL_ITS_PRODUCT, //
						coinPool.getTotalAmount(), //
						selected.getPrice() //
				);
				return new SelectOperation();
			}
			
			// 取引が成立したので、商品価格分を金庫にしまう
			this.move(safeBox, coinPool, selected.getPrice());
			customer.buy(sell(selected));
			
			return new Payback(new SelectOperation());
		}
		
		private void move(Wallet dest, Wallet src, Yen moveAmount){
			final Yen preAmount= dest.getTotalAmount().add(src.getTotalAmount());
			
			// FIXME: 投入金額からお金を崩して、moveAmountちょうどの金額ができるようにする
			// 安いコインから順に移動させる
			Deque<Coin> coins= Queues.newArrayDeque( //
					ImmutableSortedMultiset.copyOf(Coins.ascComparator(), src));
			Yen remaining= moveAmount;
			Wallet workWallet= new Wallet();
			while(remaining.compareTo(Yen.zero()) > 0){
				// ループ中にcoinsが空になるのはありえない
				if(coins.isEmpty()){ throw new AssertionError(); }
				Coin coin= coins.poll();
				
				// moveAmountより高いお金が来たら、崩してDequeの最初に入れる
				while(coin.getAmount().compareTo(remaining) > 0){
					// 安いコインから使いたい
					// 降順ソートのコインたちをDequeの先頭要素に挿入していくと、逆順になる
					ImmutableSortedMultiset<Coin> tmps= ImmutableSortedMultiset.copyOf( //
							Coins.descComparator(), //
							Coins.exchange(coin) //
							);
					for(Coin tmp : tmps){
						coins.addFirst(tmp);
					}
					coin= coins.poll();
				}
				
				remaining= remaining.subtract(coin.getAmount());
				workWallet.add(coin);
			}
			checkState(workWallet.getTotalAmount().compareTo(moveAmount) == 0,
					"workWallet=%s, coins=%s, moveAmount=%s, dest=%s, src=%s", workWallet, coins,
					moveAmount, dest, src);
			
			// 計算が正しく行われたため、一気にお金を移動させる
			src.clear();
			src.addAll(coins);
			dest.addAll(workWallet);
			
			final Yen postAmount= dest.getTotalAmount().add(src.getTotalAmount());
			checkState( //
					preAmount.compareTo(postAmount) == 0, //
					"投入金額から%sを金庫にしまった結果、%sから%sに投入金額と金庫の金額の合計値が変化しました。", //
					moveAmount, preAmount, postAmount //
			);
		}
		
		private boolean canSellIt(Drink product){
			// 購入可能条件: 投入金額 >= 商品価格
			return coinPool.getTotalAmount().compareTo(product.getPrice()) >= 0;
		}
		
		@Override
		public void formatTo(Formatter fmt, int flags, int width, int precision){
			fmt.format(Message.SELECT_PRODUCT_NAME);
		}
	}
}
