package simulator.vendingmachine;

import java.util.Collection;
import java.util.Formattable;
import java.util.concurrent.Callable;

import simulator.currency.Coin;
import simulator.customer.Customer;
import simulator.product.Product;
import simulator.util.Yen;

/**
 * 自動販売機インタフェイス。
 * @author E.Sekito.
 * @since 2012/11/16
 * @param <T> 自動販売機が販売する商品型
 */
public interface VendingMachine<T extends Product> extends Callable<Customer>, Formattable {
    /**
     * 新しいお客さんを受け入れる。
     * @param newCustomer
     *            新しいお客さん
     */
    void comeNewCustomer( Customer newCustomer);

    /**
     * お客さんに対して、商品を販売する。
     * @return 買い物が終わったお客さん
     * @throws Exception
     */
    @Override
    Customer call() throws Exception;

    /**
     * コインを投入する。
     * @param coin 投入されるコイン
     */
    void putInto( Coin coin);

    /**
     * 商品を販売する。
     * @param product 販売する商品
     * @return productで指定された商品
     */
    T sell( T product);

    /**
     * 返却するコインの枚数が最小になるように、おつりを返却する。
     * @return おつり
     */
    Collection<Coin> payback();

    /**
     * 投入されたコインの合計金額を返す。
     * @return 合計金額
     */
    Yen getTotalAmount();

    /**
     * 投入されたコインの総数を返す。
     * @return コインの総数
     */
    int getCountOfCoins();
}
