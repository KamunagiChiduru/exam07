package vendingmachine;

import java.util.Collection;

import vending_simu.tag.Coin;

import com.google.common.collect.ImmutableList;

public interface VendingMachine{
	/**
	 * お金を投入する。<br>
	 * @param x お金
	 */
	void intoCoin(Coin x);
	/**
	 * 投入された合計金額から、コインの枚数が最小となるようにおつりを返却する。<br>
	 * @return おつり
	 */
	Collection<Coin> payBack();
	/**
	 * この自動販売機で購入可能な商品の一覧を返す
	 * @return
	 */
	ImmutableList<Product> getProductList();
	Product buyIt();
	/**
	 * 投入されているコインの数を報告する。<br>
	 * @return コインの枚数
	 */
	int getCountOfCoins();
	/**
	 * 投入されているコインの合計金額を報告する。<br>
	 * @return
	 */
	Yen totalAmount();
}
