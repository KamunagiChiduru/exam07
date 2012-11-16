package simulator;

import simulator.customer.Customer;
import simulator.vendingmachine.VendingMachineGuide;

/**
 * 自動販売機シミュレーションのエントリポイント。
 * @author E.Sekitp
 * @since 2012/11/16
 */
public class Simulator {
    public static void main( String[] args) {
        VendingMachineGuide guide = new VendingMachineGuide();

        guide.comeNewCustomer( new Customer());
    }
}
