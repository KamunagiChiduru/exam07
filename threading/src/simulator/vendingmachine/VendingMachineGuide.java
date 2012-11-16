package simulator.vendingmachine;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import simulator.customer.Customer;

import com.google.common.collect.ImmutableList;

/**
 * 自動販売機をお客さんに選んでもらうための水先案内人。
 * @author E.Sekito
 * @since 2012/11/16
 */
public class VendingMachineGuide {
    /** メッセージ文字列 */
    private static interface Message {
        /** 自動販売機を選んでください。 */
        String PLEASE_SELECT_VENDING_MACHINE = "自動販売機を選んでください。";

        /** %sの自動販売機です。 */
        String INTRODUCE_SELECTED_MACHINE_FORMAT = "%sの自動販売機です。";
    }

    /**
     * コンストラクタ
     */
    public VendingMachineGuide() {
    }

    /**
     * 新しいお客さんを迎える。
     * @param newCustomer 新しいお客さん
     */
    public void comeNewCustomer( Customer newCustomer) {
        // ここでは異なる種類の自販機を選択肢としておきたいため、ワイルドカード指定
        ImmutableList<? extends VendingMachine<?>> machineList = ImmutableList.of( //
            new DrinkVendingMachine( newCustomer.getIOManager()), //
            ReturnTicketVendingMachine.INSTANCE //
            );

        ExecutorService service = Executors.newCachedThreadPool();

        try {
            while ( true) {
                VendingMachine<?> selected = newCustomer.getIOManager() //
                    .select( Message.PLEASE_SELECT_VENDING_MACHINE, machineList);

                if ( selected == ReturnTicketVendingMachine.INSTANCE) {
                    break;
                }

                Future<Customer> future = service.submit( selected);

                newCustomer.getIOManager() //
                    .writeln( Message.INTRODUCE_SELECTED_MACHINE_FORMAT, selected);
                selected.comeNewCustomer( newCustomer);

                Customer customer = future.get();

                customer.showResult();
            }
        }
        catch ( InterruptedException e) {
            e.printStackTrace();
        }
        catch ( ExecutionException e) {
            e.printStackTrace();
        }
        finally {
            service.shutdown();
        }
    }
}
