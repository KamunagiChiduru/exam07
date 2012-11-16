package simulator.currency;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoinsTest {
    @Test
    public void exchange500() {
        Wallet result = new Wallet( Coins.exchange( Coins.fiveHundredYenCoin()));

        assertEquals( 5, result.size());
        assertEquals( 5, result.count( Coins.oneHundredYenCoin()));
    }

    @Test
    public void exchange100() {
        Wallet result = new Wallet( Coins.exchange( Coins.oneHundredYenCoin()));

        assertEquals( 2, result.size());
        assertEquals( 2, result.count( Coins.fiftyYenCoin()));
    }

    @Test
    public void exchange50() {
        Wallet result = new Wallet( Coins.exchange( Coins.fiftyYenCoin()));

        assertEquals( 5, result.size());
        assertEquals( 5, result.count( Coins.tenYenCoin()));
    }

    @Test
    public void exhange10() {
        Wallet result = new Wallet( Coins.exchange( Coins.tenYenCoin()));

        assertEquals( 2, result.size());
        assertEquals( 2, result.count( Coins.fiveYenCoin()));
    }

    @Test
    public void exchange5() {
        Wallet result = new Wallet( Coins.exchange( Coins.fiveYenCoin()));

        assertEquals( 5, result.size());
        assertEquals( 5, result.count( Coins.oneYenCoin()));
    }

    @Test
    public void exchange1() {
        Wallet result = new Wallet( Coins.exchange( Coins.oneYenCoin()));

        assertEquals( 1, result.size());
        assertEquals( 1, result.count( Coins.oneYenCoin()));
    }
}
