package simulator.product;

import java.util.Formatter;

import simulator.util.Yen;

/**
 * 飲料。
 * @author E.Sekito
 * @since 2012/11/16
 */
public enum Drink implements Product {
    /** 珈琲 */
    COFFEE( "コーヒー", Yen.of( 120)),
    /** コーラ */
    BOTTLE_COLA( "ボトルのコーラ", Yen.of( 150)),
    /** 栄養ドリンク */
    ENERGY_DRINK( "栄養ドリンク", Yen.of( 200)),
    /** お茶 */
    TEE_OF_TOKUHO( "トクホのお茶", Yen.of( 190));

    /** 飲料名 */
    private final String name;

    /** 価格 */
    private final Yen price;

    /**
     * @param name 飲料名
     * @param price 価格
     */
    private Drink( String name, Yen price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Yen getPrice() {
        return this.price;
    }

    @Override
    public void formatTo( Formatter fmt, int flags, int width, int precision) {
        fmt.format( "%s %s円", this.name, this.price);
    }

    @Override
    public String toString() {
        return String.format( "%s", this);
    }
}
