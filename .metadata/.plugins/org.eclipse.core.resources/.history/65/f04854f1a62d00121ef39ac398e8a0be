package main;

import java.math.BigInteger;

public final class Yen implements Comparable<Yen>{
	private final BigInteger amount;
	
	public Yen(int amount){
		this.amount= BigInteger.valueOf(amount);
	}
	
	public Yen(long amount){
		this.amount= BigInteger.valueOf(amount);
	}
	
	public Yen(String amount){
		this.amount= new BigInteger(amount);
	}
	
	Yen(BigInteger amount){
		this.amount= amount;
	}
	
	public static Yen zero(){
		return new Yen(0);
	}
	
	public int intValue(){
		return this.amount.intValue();
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
		return this.amount.toString(10);
	}
}
