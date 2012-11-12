package vendingmachine;

import java.math.BigInteger;

public final class Yen implements Comparable<Yen>{
	private final BigInteger amount;
	
	public Yen(int amount){
		this.amount= BigInteger.valueOf(amount);
	}
	
	@Override
	public int hashCode(){
		final int prime= 31;
		int result= 1;
		result= prime * result + ((amount == null) ? 0 : amount.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		Yen other= (Yen)obj;
		if(amount == null){
			if(other.amount != null) return false;
		}
		else if( !amount.equals(other.amount)) return false;
		return true;
	}
	
	@Override
	public String toString(){
		return String.format("%d yen", this.amount.toString(10));
	}

	public Yen(long amount){
		this.amount= BigInteger.valueOf(amount);
	}
	
	public Yen(String amount){
		try{
			this.amount= new BigInteger(amount);
		}
		catch(NumberFormatException e){
			throw new IllegalArgumentException(e);
		}
	}
	
	Yen(BigInteger amount){
		this.amount= amount;
	}
	
	public long longValue(){
		return this.amount.longValue();
	}
	
	public Yen add(Yen rhs){
		return new Yen(this.amount.add(rhs.amount));
	}
	
	public Yen subtract(Yen rhs){
		return new Yen(this.amount.subtract(rhs.amount));
	}
	
	@Override
	public int compareTo(Yen o){
		return this.amount.compareTo(o.amount);
	}
	
	
}