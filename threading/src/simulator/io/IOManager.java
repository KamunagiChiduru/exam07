package simulator.io;

import java.util.Collection;

/**
 * 入出力マネージャインタフェイス。
 * @author E.Sekitp
 * @since 2012/11/16
 */
public interface IOManager {
    /**
     * candidatesから1つを選び、返す。
     * @param msg プロンプトメッセージ
     * @param candidates 選択候補
     * @param <T> 選択させる型
     * @return
     */
    <T> T select( String msg, Collection<T> candidates);

    /**
     * @see {@link Formatter#format(String, Object...)}
     * @param format 表示フォーマット
     * @param params パラメータ
     */
    void write( String format, Object... params);

    /**
     * formatを改行つきで出力する。
     * @see {@link Formatter#format(String, Object...)}
     * @param format 表示フォーマット
     * @param params パラメータ
     */
    void writeln( String format, Object... params);
}
