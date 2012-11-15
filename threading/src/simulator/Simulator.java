package simulator;

import simulator.customer.Customer;
import simulator.vendingmachine.VendingMachineGuide;

public class Simulator{
	public static void main(String[] args){
		new VendingMachineGuide().comeNewCustomer(new Customer());
	}
}
