package vendingmachine;

public class Drink implements Product{
	private final long id;
	private final String name;
	private final Yen price;
	
	public Drink(long id, String name, Yen price){
		this.id= id;
		this.name= name;
		this.price= price;
	}
	
	@Override
	public long getId(){
		return this.id;
	}

	@Override
	public String getName(){
		return this.name;
	}

	@Override
	public Yen getPrice(){
		return this.price;
	}
}
