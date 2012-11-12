package vendingmachine;

import java.util.Collection;
import java.util.List;

import vending_simu.tag.Coin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class DrinkVendingMachine implements VendingMachine{
	private Yen totalAmount;
	private final List<StockSlot<Drink>> stocks;
	
	public DrinkVendingMachine(Class<? extends Drink> drink, Class<? extends Drink>... extDrinks){
		List<StockSlot<Drink>> stocks= Lists.newArrayList();
		
		stocks.add(new StockSlot<Drink>());
		for(Class<? extends Product> t : extDrinks){
			stocks.add(new StockSlot<Drink>());
		}
		
		this.stocks= ImmutableList.copyOf(stocks);
	}

	@Override
	public void intoCoin(Coin x){
	}

	@Override
	public Collection<Coin> payBack(){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product buyIt(){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCountOfCoins(){
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Yen totalAmount(){
		return this.totalAmount;
	}

	@Override
	public ImmutableList<Product> getProductList(){
		// TODO Auto-generated method stub
		return null;
	}
}