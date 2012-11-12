
public class Simulator{
	public static void main(String[] args){
		Customer customer= new Customer();
		VendingMachine vendingMachine= new VendingMachine();
		
		State state= new State();
		
		while(true){
			state.action(customer, vendingMachine);
			state= state.getNextState();
		}
	}
}