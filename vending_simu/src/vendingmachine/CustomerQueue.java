package vendingmachine;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import customer.Customer;

public class CustomerQueue{
	private final Queue<Customer> queue;
	
	public CustomerQueue(){
		this.queue= new ConcurrentLinkedQueue<Customer>();
	}
}
