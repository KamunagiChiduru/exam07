package vendingmachine;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StockSlot<E extends Product>{
	private final Queue<E> queue;
	
	public StockSlot(){
		this.queue= new ConcurrentLinkedQueue<>();
	}
	
	public E poll(){
		return this.queue.poll();
	}
	
	public void add(E elm){
		this.queue.offer(elm);
	}
	
	public boolean isEmpty(){
		return this.queue.isEmpty();
	}
}
