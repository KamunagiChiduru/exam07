package simulator.currency;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import simulator.util.Yen;

public class WalletTest{
	private Wallet obj;
	
	@Test
	public void add_Coin(){
		assertTrue(this.obj.isEmpty());
		
		this.obj.add(Coins.oneYenCoin());
		
		assertEquals(1, this.obj.size());
	}
	
	@Test
	public void count(){
		assertTrue(this.obj.getUniqueCoinSet().isEmpty());
		
		this.obj.add(Coins.oneYenCoin());
		this.obj.add(Coins.fiftyYenCoin());
		this.obj.add(Coins.fiftyYenCoin());
		this.obj.add(Coins.fiveHundredYenCoin());
		this.obj.add(Coins.fiveHundredYenCoin());
		this.obj.add(Coins.fiveHundredYenCoin());
		this.obj.add(Coins.fiveHundredYenCoin());
		this.obj.add(Coins.fiveHundredYenCoin());
		
		assertEquals(1, this.obj.count(Coins.oneYenCoin()));
		assertEquals(0, this.obj.count(Coins.fiveYenCoin()));
		assertEquals(0, this.obj.count(Coins.tenYenCoin()));
		assertEquals(2, this.obj.count(Coins.fiftyYenCoin()));
		assertEquals(0, this.obj.count(Coins.oneHundredYenCoin()));
		assertEquals(5, this.obj.count(Coins.fiveHundredYenCoin()));
	}
	
	@Test(expected=NullPointerException.class)
	public void count_invalidCase(){
		this.obj.count(null);
	}
	
	@Test
	public void getTotalAmount(){
		assertEquals(Yen.zero(), this.obj.getTotalAmount());
		this.obj.add(Coins.oneHundredYenCoin());
		assertEquals(Yen.oneHundred(), this.obj.getTotalAmount());
		this.obj.add(Coins.fiveHundredYenCoin());
		assertEquals(Yen.of(600), this.obj.getTotalAmount());
		this.obj.add(Coins.oneYenCoin());
		assertEquals(Yen.of(601), this.obj.getTotalAmount());
		this.obj.clear();
		assertEquals(Yen.zero(), this.obj.getTotalAmount());
	}
	
	@Before
	public void setup(){
		this.obj= new Wallet();
	}
	
	@After
	public void teardown(){
		this.obj= null;
	}
}
