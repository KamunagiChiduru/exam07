package simulator.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

public class ConsoleAppender implements Appender{
	public ConsoleAppender(DefaultLayout layout){
		
	}
	
	@Override
	public Layout getLayout(){
		return null;
	}
	
	@Override
	public void setLayout(Layout layout){
		
	}
	
	@Override
	public <T extends Formattable>T select(String msg, Collection<T> candidates){
		StringBuilder builder= new StringBuilder();
		ImmutableList<T> copyList= ImmutableList.copyOf(candidates);
		
		builder.append("[");
		for(int i= 0; i < copyList.size(); i++ ){
			if(i > 0){
				builder.append(" ");
			}
			builder.append(String.format("%d:%s", i + 1, copyList.get(i).getDisplayText()));
		}
		builder.append("]");
		builder.append(">");
		
		System.out.println(msg);
		System.out.print(builder.toString());
		
		BufferedReader reader= new BufferedReader(new InputStreamReader(System.in));
		
		try{
			final Range<Integer> validRange= Ranges.closed(1, copyList.size());
			Integer parseInput;
			
			do{
				String input= reader.readLine();
				parseInput= Integer.parseInt(input);
				
			}while( !validRange.apply(parseInput));
			
			return copyList.get(parseInput - 1);
		}
		catch(IOException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public void write(String format, Object... params){
		System.out.print(String.format(format, params));
	}

	@Override
	public void writeln(String format, Object... params){
		System.out.println(String.format(format, params));
	}
}
