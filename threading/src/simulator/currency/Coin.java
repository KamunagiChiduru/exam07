package simulator.currency;

import java.util.Formattable;
import java.util.Formatter;

import simulator.util.Yen;

public class Coin implements Formattable{
	private final Yen amt;
	
	Coin(Yen amt){
		this.amt= amt;
	}

	public Yen getAmount(){
		return this.amt;
	}

	@Override
	public int hashCode(){
		final int prime= 31;
		int result= 1;
		result= prime * result + ((amt == null) ? 0 : amt.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		Coin other= (Coin)obj;
		if(amt == null){
			if(other.amt != null) return false;
		}
		else if( !amt.equals(other.amt)) return false;
		return true;
	}
	
	@Override
	public String toString(){
		return String.format("%s", this);
	}

	@Override
	public void formatTo(Formatter formatter, int flags, int width, int precision){
		formatter.format(this.amt.toString());
	}
}
