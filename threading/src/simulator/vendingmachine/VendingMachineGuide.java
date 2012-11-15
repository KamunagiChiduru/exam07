package simulator.vendingmachine;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import simulator.customer.Customer;

import com.google.common.collect.ImmutableList;

public class VendingMachineGuide{
	private static interface Message{
		String PLEASE_SELECT_VENDING_MACHINE= "自動販売機を選んでください。";
	}
	
	public VendingMachineGuide(){}
	
	public void comeNewCustomer(Customer newCustomer){
		// ここでは異なる種類の自販機を選択肢としておきたいため、ワイルドカード指定
		ImmutableList<? extends VendingMachine<?>> machineList= ImmutableList.of( //
				new DrinkVendingMachine(newCustomer.getIOManager()), //
				ReturnTicketVendingMachine.INSTANCE //
				);
		
		ExecutorService service= Executors.newCachedThreadPool();
		
		try{
			while(true){
				VendingMachine<?> selected= newCustomer.getIOManager() //
						.select(Message.PLEASE_SELECT_VENDING_MACHINE, machineList);
				
				if(selected == ReturnTicketVendingMachine.INSTANCE){
					break;
				}
				
				Future<Customer> future= service.submit(selected);
				
				selected.comeNewCustomer(newCustomer);
				
				Customer customer= future.get();
				
				customer.showResult();
			}
		}
		catch(InterruptedException | ExecutionException e){
			e.printStackTrace();
		}
		finally{
			service.shutdown();
		}
	}
}
