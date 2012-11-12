package something;

import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

public class CurrencyTest{
	@Test
	public void availableCurrencies(){
		Set<Currency> availables= Currency.getAvailableCurrencies();
		SortedSet<Currency> sorted= new TreeSet<Currency>(new Comparator<Currency>(){
			@Override
			public int compare(Currency o1, Currency o2){
				return o1.getCurrencyCode().compareTo(o2.getCurrencyCode());
			}
		});
		
		sorted.addAll(availables);
		for(Currency available : sorted)
			System.out.println(available);
	}
	
	@Test
	public void japaneseCurrency(){
		Currency cur= Currency.getInstance(Locale.JAPAN);
		
		System.out.println("japanese currency : " + cur);
	}
}
