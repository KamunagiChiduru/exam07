package vendingmachine;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

public class IOManager{
	public static IOManager getInstance(){
		return INSTANCE;
	}
	
	public <T> T select(String msg, Collection<T> candidates){
		ImmutableList<T> ordered= ImmutableList.copyOf(candidates);
		
		System.out.println(msg);
		for(int i= 0; i < ordered.size(); i++){
			System.out.print(String.format("%d:%s ", i, ordered.get(i).toString()));
		}
		
		return ordered.get(0);
	}
	
	private static final IOManager INSTANCE= new IOManager();
	private IOManager(){}
}