package simulator.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigInteger;

public final class Yen implements Comparable<Yen>{
	private final BigInteger amount;
	
	public static Yen zero(){
		return new Yen(0);
	}
	
	public static Yen one(){
		return new Yen(1);
	}
	
	public static Yen five(){
		return new Yen(5);
	}
	
	public static Yen ten(){
		return new Yen(10);
	}
	
	public static Yen fifty(){
		return new Yen(50);
	}
	
	public static Yen oneHundred(){
		return new Yen(100);
	}
	
	public static Yen fiveHundred(){
		return new Yen(500);
	}
	
	public static Yen oneThousand(){
		return new Yen(1000);
	}
	
	public static Yen twoThousand(){
		return new Yen(2000);
	}
	
	public static Yen fiveThousand(){
		return new Yen(5000);
	}
	
	public static Yen tenThousand(){
		return new Yen(10000);
	}
	
	public static Yen of(int amount){
		return new Yen(amount);
	}
	
	public static Yen of(long amount){
		return new Yen(amount);
	}
	
	public static Yen of(String amount) throws NullPointerException{
		return new Yen(amount);
	}
	
	public Yen(int amount){
		this.amount= BigInteger.valueOf(amount);
	}
	
	public Yen(long amount){
		this.amount= BigInteger.valueOf(amount);
	}
	
	public Yen(String amount) throws NullPointerException, NumberFormatException{
		this.amount= new BigInteger(checkNotNull(amount));
	}
	
	Yen(BigInteger amount) throws NullPointerException{
		this.amount= checkNotNull(amount);
	}
	
	public Yen add(Yen rhs) throws NullPointerException{
		return new Yen(this.amount.add(checkNotNull(rhs).amount));
	}
	
	public Yen subtract(Yen rhs) throws NullPointerException{
		return new Yen(this.amount.subtract(checkNotNull(rhs).amount));
	}
	
	@Override
	public int compareTo(Yen o) throws NullPointerException{
		return this.amount.compareTo(checkNotNull(o).amount);
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
