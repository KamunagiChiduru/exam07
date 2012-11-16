package simulator.currency;

import java.util.Formattable;
import java.util.Formatter;

import simulator.util.Yen;

/**
 * 1枚のコインを表すクラス。
 * @author E.Sekito
 * @since 2012/11/16
 */
public class Coin implements Comparable<Coin>, Formattable {
    /** メッセージ文字列 */
    private static interface Message {
        /** toString()で使用するthisのフォーマット */
        String TO_STRING_FORMAT = "%s";

        /** 表示フォーマット */
        String FORMAT = "%s円";
    }

    /** コインの金額 */
    private final Yen amt;

    /**
     * amt円のコインを作成する。
     * @param amt ～円の～
     */
    Coin( Yen amt) {
        this.amt = amt;
    }

    /**
     * 金額を返す。
     * @return 0より大きい金額
     */
    public Yen getAmount() {
        return this.amt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amt == null) ? 0 : amt.hashCode());
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
        Coin other = ( Coin) obj;
        if ( amt == null) {
            if ( other.amt != null) {
                return false;
            }
        }
        else if ( !amt.equals( other.amt)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format( Message.TO_STRING_FORMAT, this);
    }

    @Override
    public void formatTo( Formatter formatter, int flags, int width, int precision) {
        formatter.format( Message.FORMAT, this.amt);
    }

    @Override
    public int compareTo( Coin o) {
        return this.amt.compareTo( o.amt);
    }
}
