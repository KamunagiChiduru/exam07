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

/**
 * Coinクラス用のユーティリティ。
 * @author E.Sekito
 * @since 2012/11/16
 */
public final class Coins {
    /** 継承もインスタンス化も意味がないので不許可 */
    private Coins() {
    }

    /**
     * 全種類のコインを1枚ずつ返す。
     * @return 金額によって昇順にソートされたコインのList
     */
    public static ImmutableList<Coin> getOneOfEach() {
        return ImmutableList.of( //
            oneYenCoin(), //
            fiveYenCoin(), //
            tenYenCoin(), //
            fiftyYenCoin(), //
            oneHundredYenCoin(), //
            fiveHundredYenCoin() //
            );
    }

    /**
     * 1円玉を返す。
     * @return 1円玉
     */
    public static Coin oneYenCoin() {
        return new Coin( Yen.one());
    }

    /**
     * 5円玉を返す。
     * @return 5円玉
     */
    public static Coin fiveYenCoin() {
        return new Coin( Yen.five());
    }

    /**
     * 10円玉を返す。
     * @return 10円玉
     */
    public static Coin tenYenCoin() {
        return new Coin( Yen.ten());
    }

    /**
     * 50円玉を返す。
     * @return 50円玉
     */
    public static Coin fiftyYenCoin() {
        return new Coin( Yen.fifty());
    }

    /**
     * 100円玉を返す。
     * @return 100円玉
     */
    public static Coin oneHundredYenCoin() {
        return new Coin( Yen.oneHundred());
    }

    /**
     * 500円玉を返す。
     * @return 500円玉
     */
    public static Coin fiveHundredYenCoin() {
        return new Coin( Yen.fiveHundred());
    }

    /**
     * origを、複数枚のorigよりも安いコインに両替する。
     * @param orig 両替するコイン
     * @return origよりも安い複数枚のコイン。origが1円玉なら、1円玉のみ返される。
     */
    public static Collection<Coin> exchange( Coin orig) {
        // 1円は両替不可
        if ( orig.equals( oneYenCoin())) {
            return Arrays.asList( orig);
        }
        final Coin upperBounds = orig;
        // origよりも安いけれど、金額の高い順にコインを1種類ずつ保持
        ImmutableSortedSet<Coin> coinSet = ImmutableSortedSet.copyOf( //
            descComparator(), //
            Collections2.filter( getOneOfEach(), new Predicate<Coin>() {
                @Override
                public boolean apply( Coin o) {
                    if ( o == null) {
                        return false;
                    }
                    return o.compareTo( upperBounds) < 0;
                }
            }));
        Collection<Coin> result = Lists.newArrayList();

        Yen remaining = orig.getAmount();
        for ( Coin coin : coinSet) {
            while ( remaining.compareTo( coin.getAmount()) >= 0) {
                remaining = remaining.subtract( coin.getAmount());

                if ( remaining.compareTo( Yen.zero()) < 0) {
                    throw new AssertionError();
                }

                result.add( coin);

                if ( remaining.compareTo( Yen.zero()) == 0) {
                    return result;
                }
            }
        }
        throw new AssertionError();
    }

    /**
     * コインの金額を見る昇順比較器を返す。
     * @return 比較器
     */
    public static Comparator<Coin> ascComparator() {
        return new Comparator<Coin>() {
            @Override
            public int compare( Coin lhs, Coin rhs) {
                return lhs.compareTo( rhs);
            }
        };
    }

    /**
     * コインの金額を見る降順比較器を返す。
     * @return 比較器
     */
    public static Comparator<Coin> descComparator() {
        return Collections.reverseOrder( ascComparator());
    }
}
