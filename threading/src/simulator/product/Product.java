package simulator.product;

import java.util.Formattable;
import java.util.Formatter;

import simulator.util.Yen;

public enum Product implements Formattable{
	COFFEE("コーヒー", Yen.of(120)), 
	BOTTLE_COLA("ボトルのコーラ", Yen.of(150)), 
	ENERGY_DRINK("栄養ドリンク", Yen.of(200)), 
	TEE_OF_TOKUHO("トクホのお茶", Yen.of(190));

	private final String name;
	private final Yen price;
	private Product(String name, Yen price){
		this.name= name;
		this.price= price;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Yen getPrice(){
		return this.price;
	}
	
	@Override
	public void formatTo(Formatter fmt, int flags, int width, int precision){
		fmt.format("%s %s円", this.name, this.price);
	}
	
	@Override
	public String toString(){
		return String.format("%s", this);
	}
}
