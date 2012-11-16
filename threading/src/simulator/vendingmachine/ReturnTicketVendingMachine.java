package simulator.vendingmachine;

import java.util.Collection;
import java.util.Formatter;

import simulator.currency.Coin;
import simulator.customer.Customer;
import simulator.product.Product;
import simulator.util.Yen;

/**
 * このクラスは、==、equals()で比較をするか、%sフォーマットで表示する以外の操作をすると例外を吐く。
 * @author E.Sekito
 * @since 2012/11/16
 */
enum ReturnTicketVendingMachine implements VendingMachine<Product> {
    /** このクラスは単なるタグとして使いたいので、シングルトンとする */
    INSTANCE;

    @Override
    public void formatTo( Formatter formatter, int flags, int width, int precision) {
        formatter.format( "終了");
    }

    @Override
    public void comeNewCustomer( Customer newCustomer) {
        throw new AssertionError();
    }

    @Override
    public Customer call() throws Exception {
        throw new AssertionError();
    }

    @Override
    public void putInto( Coin coin) {
        throw new AssertionError();
    }

    @Override
    public Product sell( Product product) {
        throw new AssertionError();
    }

    @Override
    public Collection<Coin> payback() {
        throw new AssertionError();
    }

    @Override
    public Yen getTotalAmount() {
        throw new AssertionError();
    }

    @Override
    public int getCountOfCoins() {
        throw new AssertionError();
    }
}
