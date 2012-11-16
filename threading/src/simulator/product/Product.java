package simulator.product;

import java.util.Formattable;

import simulator.util.Yen;

/**
 * 自動販売機で販売することのできる商品インタフェイス。
 * @author E.Sekito
 * @since 2012/11/16
 */
public interface Product extends Formattable {
    /**
     * 商品名を返す。
     * @return 非null
     */
    String getName();

    /**
     * 価格を返す。
     * @return 非null、非負
     */
    Yen getPrice();
}
