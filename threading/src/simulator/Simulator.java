package simulator;

import simulator.customer.Customer;
import simulator.vendingmachine.VendingMachineGuide;

public class Simulator{
	public static void main(String[] args){
		VendingMachineGuide guide= new VendingMachineGuide();
		
		guide.comeNewCustomer(new Customer());
	}
}
