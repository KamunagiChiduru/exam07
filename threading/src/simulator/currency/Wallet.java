package simulator.currency;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

import simulator.util.Yen;

import com.google.common.base.Predicates;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Queues;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * お財布
 * @author E.Sekito.
 * @since 2012/11/16
 */
public class Wallet extends AbstractCollection<Coin> {
    /** 実装クラス */
    private final Multiset<Coin> impl;

    /**
     * コンストラクタ
     */
    public Wallet() {
        this( ImmutableList.<Coin> of());
    }

    /**
     * coinsが入った財布を作成する。
     * @param coins 初期コイン
     */
    public Wallet( Coin... coins) {
        this( ImmutableList.copyOf( coins));
    }

    /**
     * coinsでイテレートできるすべてが入った財布を作成する。
     * @param coins 初期コイン
     */
    public Wallet( Iterable<Coin> coins) {
        this.impl = ConcurrentHashMultiset.create( coins);
    }

    /**
     * 全財産の金額を返す。
     * @return 非負、非nullの金額
     */
    public Yen getTotalAmount() {
        Yen total = Yen.zero();

        for ( Coin coin : this.impl) {
            total = total.add( coin.getAmount());
        }

        return total;
    }

    /**
     * oが今財布に何枚入っているかを返す。
     * @param o 調べるコイン
     * @return 何枚入っていたか 非負
     */
    public int count( Coin o) {
        return this.impl.count( checkNotNull( o));
    }

    /**
     * お財布に入っている全種類のコインを返す。
     * @return 全種類のコイン、非null
     */
    public ImmutableSet<Coin> getUniqueCoinSet() {
        return ImmutableSet.copyOf( this.impl.elementSet());
    }

    /**
     * coinを財布から取り出す。
     * @param coin 取り出すコイン
     * @return 取り出したコイン
     * @throws IllegalArgumentException 財布の中に存在しないコインを指定したとき
     */
    public Coin get( Coin coin) {
        if ( this.impl.contains( checkNotNull( coin))) {
            Coin found = Iterables.find( this.impl, Predicates.equalTo( coin));
            this.impl.remove( found);
            return found;
        }
        throw new IllegalArgumentException( String.format( "%sは、%sの中に存在しない。", coin, this));
    }

    /**
     * amountの分だけ、財布からお金を取り出す。<br>
     * 取り出した分のお金は財布からなくなる。<br>
     * 持っている小銭だけではamount円ちょうど払えないときには、お金を崩してくる。<br>
     * このメソッドが呼ばれた後には、thisに含まれるコインの種類が変化する可能性がある。<br>
     * @param amount お財布から取り出すコインの総額
     * @return amount円分だけのコインたち
     * @throws IllegalArgumentException amountがthis{@link #getTotalAmount()}よりも高額だったとき
     */
    public ImmutableCollection<Coin> get( Yen amount) {
        checkArgument( amount.compareTo( this.getTotalAmount()) > 0, "%sは高すぎて%sしか持っていない私には払えません。", amount, this.getTotalAmount());
        final Yen preAmount = this.getTotalAmount();

        // 安いコインから順に移動させる
        Deque<Coin> coins = Queues.newArrayDeque( //
            ImmutableSortedMultiset.copyOf( Coins.ascComparator(), this));
        Yen remaining = amount;
        Wallet result = new Wallet();
        while ( remaining.compareTo( Yen.zero()) > 0) {
            // ループ中にcoinsが空になるのはありえない
            if ( coins.isEmpty()) {
                throw new AssertionError();
            }
            Coin coin = coins.poll();

            // moveAmountより高いお金が来たら、崩してDequeの最初に入れる
            while ( coin.getAmount().compareTo( remaining) > 0) {
                // 安いコインから使いたい
                // 降順ソートのコインたちをDequeの先頭要素に挿入していくと、逆順になる
                ImmutableSortedMultiset<Coin> tmps = ImmutableSortedMultiset.copyOf( //
                    Coins.descComparator(), //
                    Coins.exchange( coin) //
                    );
                for ( Coin tmp : tmps) {
                    coins.addFirst( tmp);
                }
                coin = coins.poll();
            }

            remaining = remaining.subtract( coin.getAmount());
            result.add( coin);
        }
        checkState( result.getTotalAmount().compareTo( amount) == 0, "result=%s, coins=%s, amount=%s, this=%s", result, coins, amount, this);

        // 正しく計算が行われたので、お金を取り出す
        this.clear();
        this.addAll( coins);

        final Yen postAmount = this.getTotalAmount().add( result.getTotalAmount());
        checkState( //
            preAmount.compareTo( postAmount) == 0, //
            "投入金額から%sを金庫にしまった結果、%sから%sに投入金額と金庫の金額の合計値が変化しました。", //
            amount, preAmount, postAmount //
        );

        return ImmutableList.copyOf( result);
    }

    @Override
    public boolean add( Coin e) {
        return this.impl.add( e);
    }

    @Override
    public boolean addAll( Collection<? extends Coin> c) {
        return this.impl.addAll( c);
    }

    @Override
    public boolean remove( Object o) {
        return this.impl.remove( o);
    }

    @Override
    public boolean removeAll( Collection<?> c) {
        return this.impl.removeAll( c);
    }

    @Override
    public Iterator<Coin> iterator() {
        return this.impl.iterator();
    }

    @Override
    public int size() {
        return this.impl.size();
    }
}
