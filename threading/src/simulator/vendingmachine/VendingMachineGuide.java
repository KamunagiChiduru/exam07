package simulator.vendingmachine;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import simulator.customer.Customer;

import com.google.common.collect.ImmutableList;

public class VendingMachineGuide{
	public VendingMachineGuide(){}
	
	public void comeNewCustomer(Customer newCustomer){
		// ここでは異なる種類の自販機を選択肢としておきたいため、ワイルドカード指定
		ImmutableList<? extends VendingMachine<?>> machineList= ImmutableList.of( //
				new DrinkVendingMachine(newCustomer.getIOManager()) //
				);
		
		VendingMachine<?> selected= newCustomer.getIOManager().select("選べ、自販機を。", machineList);
		
		ExecutorService service= Executors.newCachedThreadPool();
		
		Future<Customer> future= service.submit(selected);
		
		selected.comeNewCustomer(newCustomer);
		
		try{
			Customer customer= future.get();
			
			customer.showResult();
		}
		catch(InterruptedException | ExecutionException e){
			e.printStackTrace();
		}
		finally{
			service.shutdown();
		}
	}
}
