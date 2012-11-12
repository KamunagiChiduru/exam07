package vendingmachine;

import com.google.common.collect.ImmutableList;

public class Simulator{
	static class VendingMachineSelectionSequence implements Sequence{
		@Override
		public Sequence next(){
			System.out.println("購入する自動販売機を選択してください。");
			System.out.print("[1:ドリンク 2:終了]>");
			int i= selectIdx(1, 2);
			
			if(i == 1){
				System.out.println("ドリンクの自動販売機です。");
				return new OperationSelectionSequence();
			}
			return null;
		}
	}
	
	static class OperationSelectionSequence implements Sequence{
		@Override
		public Sequence next(){
			System.out.println("操作を選択してください。");
			System.out.print("[1:コインを入れる 2:買う 3:コインを戻す 4:終了]>");
			int i= selectIdx(1, 4);
			
			switch(i){
				case 1:
					return new CoinSelectionSequence();
				case 2:
					return new ProductSelectionSequence();
				case 3:
				case 4:
					return null; // payback
			}
			
			return this;
		}
	}
	
	static class CoinSelectionSequence implements Sequence{
		@Override
		public Sequence next(){
			System.out.println("入れるコイルを選択してください。");
			System.out.print("[1:500円 2:100円 3:50円 4:10円]>");
			int i= selectIdx(1, 4);
			
			
			return null;
		}
	}
	
	public static void main(String[] args){
		// 自販機選択
		Scene scene= new VendingMachineSelection(DEFAULT_IOMANAGER, AVAILABLE_VENDING_MACHINES);
		
		while(scene != null){
			scene= scene.start();
		}
		
		// 買い物結果表示
	}
	
	private static final IOManager DEFAULT_IOMANAGER= null;
	private static final ImmutableList<VendingMachine> AVAILABLE_VENDING_MACHINES = null;
}
