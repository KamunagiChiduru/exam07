package simulator.product;

import java.util.Formattable;

import simulator.util.Yen;

public interface Product extends Formattable{
	String getName();
	
	Yen getPrice();
}
