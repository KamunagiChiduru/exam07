package simulator.customer;

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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * お客さんクラス。
 * @author E.Sekito
 * @since 2012/11/16
 */
public class Customer {
    /** 定数宣言のためのインタフェイス */
    private static interface Message {
        /** 〜お買いもの結果〜 */
        String RESULT_TITLE = "〜お買いもの結果〜";

        /** あなたは何も買っていません。 */
        String I_HAVE_NO_PRODUCTS = "あなたは何も買っていません。";

        /** あなたは%s持っています。 */
        String I_HAVE_PRODUCTS = "あなたは%s持っています。";

        /** あなたのお財布の中には何もありません。 */
        String I_HAVE_NO_MONEY = "あなたのお財布の中には何もありません。";

        /** あなたのお財布の中には%sあります。 */
        String I_HAVE_MONEY = "あなたのお財布の中には%sあります。";

        /** %sを%d本 */
        String PRODUCT_FORMAT = "%sを%d本";

        /** と、 */
        String PRODUCT_SEPARATOR = "と、";

        /** %sが%d枚 */
        String WALLET_FORMAT = "%sが%d枚";

        /** 、 */
        String WALLET_SEPARATOR = "、";
    }

    /** IOマネージャ */
    private final IOManager io;

    /** 財布 */
    private final Wallet wallet;

    /** 買い物カゴ */
    private final Multiset<Product> bucket;

    /**
     * コンストラクタ
     */
    public Customer() {
        this.io = new ConsoleIOManager();
        this.wallet = new Wallet( //
            Coins.fiveHundredYenCoin(), // 500円玉が1枚
            Coins.oneHundredYenCoin(), Coins.oneHundredYenCoin(), // 100円玉が2枚
            Coins.fiftyYenCoin(), // 50円玉が1枚
            Coins.tenYenCoin(), Coins.tenYenCoin(), Coins.tenYenCoin() // 10円玉が3枚
        );
        this.bucket = HashMultiset.create();
    }

    /**
     * 自分の財布に入っている、全種類のコインを報告する。
     * @return 財布に入っている全種類のコイン
     */
    public ImmutableSet<Coin> getUniqueCoinSet() {
        return this.wallet.getUniqueCoinSet();
    }

    /**
     * paidと同じコインを財布から出して払う。
     * @param paied 払うコイン
     * @return 自分の財布から出したコイン
     */
    public Coin pay( Coin paied) {
        return this.wallet.get( paied);
    }

    /**
     * boughtを購入し、自分の買い物カゴに入れる。
     * @param bought 買ったもの
     */
    public void buy( Drink bought) {
        this.bucket.add( checkNotNull( bought));
    }

    /**
     * coinsを受け取って自分の財布に入れる。
     * @param coins 受け取るコイン
     */
    public void giveCoins( Collection<Coin> coins) {
        this.wallet.addAll( checkNotNull( coins));
    }

    /**
     * 今お金を持っているかを返す。
     * @return 持っていればtrue、持っていなければfalse
     */
    public boolean hasCoin() {
        return !this.wallet.isEmpty();
    }

    /**
     * 自分との通信に使うIOマネージャを返す。
     * @return IOマネージャ 非null
     */
    public IOManager getIOManager() {
        return this.io;
    }

    /**
     * お買い物結果を報告する。
     */
    public void showResult() {
        this.io.writeln( Message.RESULT_TITLE);

        if ( this.bucket.isEmpty()) {
            this.io.writeln( Message.I_HAVE_NO_PRODUCTS);
        }
        else {
            this.io.writeln( Message.I_HAVE_PRODUCTS, this.formatBoughtProducts());
        }

        if ( this.wallet.isEmpty()) {
            this.io.writeln( Message.I_HAVE_NO_MONEY);
        }
        else {
            this.io.writeln( Message.I_HAVE_MONEY, this.formatWallet());
        }
    }

    private String formatBoughtProducts() {
        Queue<Product> things = Queues.newPriorityQueue( this.bucket);

        return this.formatBoughtProducts( things);
    }

    private String formatBoughtProducts( Queue<? extends Product> things) {
        if ( things.isEmpty()) {
            return null;
        }

        Product thing = things.poll();
        String formatted = String.format( //
            Message.PRODUCT_FORMAT, thing.getName(), this.bucket.count( thing));

        Joiner joiner = Joiner.on( Message.PRODUCT_SEPARATOR).skipNulls();

        return joiner.join( formatted, this.formatBoughtProducts( things));
    }

    private String formatWallet() {
        ImmutableSortedSet<Coin> descUniqueCoins = ImmutableSortedSet.copyOf( //
            Coins.descComparator(), //
            this.wallet.getUniqueCoinSet() //
            );
        return this.formatWallet( Queues.newPriorityQueue( descUniqueCoins));
    }

    private String formatWallet( Queue<Coin> kinds) {
        if ( kinds.isEmpty()) {
            return null;
        }

        Coin coin = kinds.poll();
        String formatted = String.format( Message.WALLET_FORMAT, coin, this.wallet.count( coin));

        Joiner joiner = Joiner.on( Message.WALLET_SEPARATOR).skipNulls();
        return joiner.join( formatted, this.formatWallet( kinds));
    }
}
