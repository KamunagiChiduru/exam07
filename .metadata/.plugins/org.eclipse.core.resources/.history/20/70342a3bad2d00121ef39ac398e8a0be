package simulator.currency;

import simulator.io.Formattable;
import simulator.util.Yen;

public enum Coin implements Formattable{
	TEN(10), 
	FIFTY(50), 
	HUNDRED(100), 
	FIVE_HUNDRED(500), 
	;
	
	private final int amt;
	private Coin(int amt){
		this.amt= amt;
	}

	@Override
	public String getDisplayText(){
		return String.format("%d円", this.amt);
	}
	
	public Yen getAmount(){
		return new Yen(this.amt);
	}
}
