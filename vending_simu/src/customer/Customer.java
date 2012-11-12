package customer;

import java.util.Collection;

import vendingmachine.Operation;
import vendingmachine.VendingMachine;

public interface Customer {
	VendingMachine selectVendingMachine(Collection<VendingMachine> availables);
	Operation selectOperation(Collection<Operation> availables);
}
