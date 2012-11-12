package vendingmachine;

public class CoinSelection implements Scene{
	private final IOManager iomanager;
	private final VendingMachine vendingMachine;
	private final Scene returnTo;
	
	public CoinSelection(IOManager iomanager, VendingMachine vendingMachine, Scene returnTo){
		this.iomanager= iomanager;
		this.vendingMachine= vendingMachine;
		this.returnTo= returnTo;
	}
	
	@Override
	public Scene start(){
		this.iomanager.select();
	}
}
