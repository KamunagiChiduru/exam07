package simulator.vendingmachine;

import java.util.Collection;
import java.util.Formatter;

import simulator.currency.Coin;
import simulator.customer.Customer;
import simulator.product.Product;
import simulator.util.Yen;

enum ReturnTicketVendingMachine implements VendingMachine<Product>{
	/** このクラスは単なるタグとして使いたいので、シングルトンとする */
	INSTANCE;
	
	@Override
	public void formatTo(Formatter formatter, int flags, int width, int precision){
		formatter.format("終了");
	}
	
	@Override
	public void comeNewCustomer(Customer newCustomer){
		
	}
	
	@Override
	public Customer call() throws Exception{
		throw new AssertionError();
	}
	
	@Override
	public void putInto(Coin coin){
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Product sell(Product product){
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Collection<Coin> payback(){
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Yen getTotalAmount(){
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getCountOfCoins(){
		// TODO Auto-generated method stub
		return 0;
	}
}
