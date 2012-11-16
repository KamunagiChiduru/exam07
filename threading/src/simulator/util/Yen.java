package simulator.util;

import java.math.BigInteger;
import java.util.Formattable;
import java.util.FormattableFlags;
import java.util.Formatter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 単位「円」の値クラス。
 * @author E.Sekito
 * @since 2012/11/16
 */
public final class Yen implements Comparable<Yen>, Formattable {
    /** メッセージ定数 */
    private static interface Message {
        /** {@link Yen#toString()}で使用する、thisのフォーマット */
        String TO_STRING_FORMAT = "%#s";

        /** ～円の円 */
        String YEN = "円";
    }

    /** クラス内定数 */
    private static interface Constant {
        /** 0円 */
        Yen ZERO = new Yen( 0);

        /** 1円 */
        Yen ONE = new Yen( 1);

        /** 5円 */
        Yen FIVE = new Yen( 5);

        /** 10円 */
        Yen TEN = new Yen( 10);

        /** 50円 */
        Yen FIFTY = new Yen( 50);

        /** 100円 */
        Yen ONE_HUNDRED = new Yen( 100);

        /** 500円 */
        Yen FIVE_HUNDRED = new Yen( 500);

        /** 1000円 */
        Yen ONE_THOUSAND = new Yen( 1000);

        /** 2000円 */
        Yen TWO_THOUSAND = new Yen( 2000);

        /** 5000円 */
        Yen FIVE_THOUSAND = new Yen( 5000);

        /** 10000円 */
        Yen TEN_THOUSAND = new Yen( 10000);
    }

    /** ～円の～ */
    private final BigInteger amount;

    /**
     * 0円を返す。
     * @return 0円
     */
    public static Yen zero() {
        return Constant.ZERO;
    }

    /**
     * 1円を返す。
     * @return 1円
     */
    public static Yen one() {
        return Constant.ONE;
    }

    /**
     * 5円を返す。
     * @return 5円
     */
    public static Yen five() {
        return Constant.FIVE;
    }

    /**
     * 10円を返す。
     * @return 10円
     */
    public static Yen ten() {
        return Constant.TEN;
    }

    /**
     * 50円を返す。
     * @return 50円
     */
    public static Yen fifty() {
        return Constant.FIFTY;
    }

    /**
     * 100円を返す。
     * @return 100円
     */
    public static Yen oneHundred() {
        return Constant.ONE_HUNDRED;
    }

    /**
     * 500円を返す。
     * @return 500円
     */
    public static Yen fiveHundred() {
        return Constant.FIVE_HUNDRED;
    }

    /**
     * 1000円を返す。
     * @return 1000円
     */
    public static Yen oneThousand() {
        return Constant.ONE_THOUSAND;
    }

    /**
     * 2000円を返す。
     * @return 2000円
     */
    public static Yen twoThousand() {
        return Constant.TWO_THOUSAND;
    }

    /**
     * 5000円を返す。
     * @return 5000円
     */
    public static Yen fiveThousand() {
        return Constant.FIVE_THOUSAND;
    }

    /**
     * 10000円を返す。
     * @return 10000円
     */
    public static Yen tenThousand() {
        return Constant.TEN_THOUSAND;
    }

    /**
     * amount円を返す。
     * @param amount 値
     * @return amount円
     */
    public static Yen of( int amount) {
        return new Yen( amount);
    }

    /**
     * amount円を返す。
     * @param amount 値
     * @return amount円
     */
    public static Yen of( long amount) {
        return new Yen( amount);
    }

    /**
     * amountを数値にパースし、パースした数値円を返す。
     * @param amount 数値を表す文字列
     * @return amount円
     */
    public static Yen of( String amount) {
        return new Yen( amount);
    }

    /**
     * amount円を作成する。
     * @param amount 値
     */
    public Yen( int amount) {
        this.amount = BigInteger.valueOf( amount);
    }

    /**
     * amount円を作成する。
     * @param amount 値
     */
    public Yen( long amount) {
        this.amount = BigInteger.valueOf( amount);
    }

    /**
     * amountを数値にパースし、パースした数値円を返す。
     * @param amount 数値を表す文字列
     */
    public Yen( String amount) {
        this.amount = new BigInteger( checkNotNull( amount));
    }

    /**
     * amount円を作成する
     * @param amount 値
     */
    Yen( BigInteger amount) {
        this.amount = checkNotNull( amount);
    }

    public Yen add( Yen rhs) {
        return new Yen( this.amount.add( checkNotNull( rhs).amount));
    }

    public Yen subtract( Yen rhs) {
        return new Yen( this.amount.subtract( checkNotNull( rhs).amount));
    }

    @Override
    public int compareTo( Yen o) {
        return this.amount.compareTo( checkNotNull( o).amount);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        return result;
    }

    @Override
    public boolean equals( Object obj) {
        if ( this == obj) {
            return true;
        }
        if ( obj == null) {
            return false;
        }
        if ( getClass() != obj.getClass()) {
            return false;
        }
        Yen other = ( Yen) obj;
        if ( amount == null) {
            if ( other.amount != null) {
                return false;
            }
        }
        else if ( !amount.equals( other.amount)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format( Message.TO_STRING_FORMAT, this);
    }

    /**
     * ～円の～の部分のみが、通常は表示される。<br>
     * '#'つきでフォーマットが指定されたときは、～円と表示される。<br>
     * @see java.util.Formattable#formatTo(java.util.Formatter, int, int, int)
     */
    @Override
    public void formatTo( Formatter formatter, int flags, int width, int precision) {
        StringBuffer buf = new StringBuffer();

        final int decimalRadix = 10;
        buf.append( this.amount.toString( decimalRadix));
        // '#'つきなら、'円'の文字
        if ( (flags & FormattableFlags.ALTERNATE) != 0) {
            buf.append( Message.YEN);
        }

        formatter.format( buf.toString());
    }
}
