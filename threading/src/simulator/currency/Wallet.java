package simulator.currency;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

import simulator.util.Yen;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Queues;

public class Wallet extends AbstractCollection<Coin>{
	private final Multiset<Coin> impl;
	
	public Wallet(){
		this(ImmutableList.<Coin>of());
	}
	
	public Wallet(Coin... coins){
		this(ImmutableList.copyOf(coins));
	}
	
	public Wallet(Iterable<Coin> coins){
		this.impl= ConcurrentHashMultiset.create(coins);
	}
	
	public Yen getTotalAmount(){
		Yen total= Yen.zero();
		
		for(Coin coin : this.impl){
			total= total.add(coin.getAmount());
		}
		
		return total;
	}
	
	public int count(Coin o){
		return this.impl.count(checkNotNull(o));
	}
	
	public ImmutableSet<Coin> getUniqueCoinSet(){
		return ImmutableSet.copyOf(this.impl.elementSet());
	}
	
	public Coin get(Coin coin) throws IllegalArgumentException{
		if(this.impl.contains(checkNotNull(coin))){
			this.impl.remove(coin);
			// TODO: 外部から渡されたオブジェクトを返しているが、イテレートして探すべき
			return coin;
		}
		throw new IllegalArgumentException(String.format("%sは、%sの中に存在しない。", coin, this));
	}
	
	/**
	 * amountの分だけ、財布からお金を取り出す。<br>
	 * 取り出した分のお金は財布からなくなる。<br>
	 * 持っている小銭だけではamount円ちょうど払えないときには、お金を崩してくる。<br>
	 * このメソッドが呼ばれた後には、thisに含まれるコインの種類が変化する可能性がある。<br>
	 * 
	 * @param amount
	 *            お財布から取り出すコインの総額
	 * @return amount円分だけのコインたち
	 * @throws IllegalArgumentException
	 *             amountがthis{@link #getTotalAmount()}よりも高額だったとき
	 */
	public ImmutableCollection<Coin> get(Yen amount) throws IllegalArgumentException{
		checkArgument(amount.compareTo(this.getTotalAmount()) > 0,
				"%sは高すぎて%sしか持っていない私には払えません。", amount, this.getTotalAmount());
		final Yen preAmount= this.getTotalAmount();
		
		// 安いコインから順に移動させる
		Deque<Coin> coins= Queues.newArrayDeque( //
				ImmutableSortedMultiset.copyOf(Coins.ascComparator(), this));
		Yen remaining= amount;
		Wallet result= new Wallet();
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
			result.add(coin);
		}
		checkState(result.getTotalAmount().compareTo(amount) == 0,
				"result=%s, coins=%s, amount=%s, this=%s", result, coins, amount, this);
		
		// 正しく計算が行われたので、お金を取り出す
		this.clear();
		this.addAll(coins);
		
		final Yen postAmount= this.getTotalAmount().add(result.getTotalAmount());
		checkState( //
				preAmount.compareTo(postAmount) == 0, //
				"投入金額から%sを金庫にしまった結果、%sから%sに投入金額と金庫の金額の合計値が変化しました。", //
				amount, preAmount, postAmount //
		);
		
		return ImmutableList.copyOf(result);
	}
	
	@Override
	public boolean add(Coin e){
		return this.impl.add(e);
	}
	
	@Override
	public boolean addAll(Collection<? extends Coin> c){
		return this.impl.addAll(c);
	}
	
	@Override
	public boolean remove(Object o){
		return this.impl.remove(o);
	}
	
	@Override
	public boolean removeAll(Collection<?> c){
		return this.impl.removeAll(c);
	}
	
	@Override
	public Iterator<Coin> iterator(){
		return this.impl.iterator();
	}
	
	@Override
	public int size(){
		return this.impl.size();
	}
}
