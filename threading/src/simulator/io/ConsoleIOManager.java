package simulator.io;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

public class ConsoleIOManager implements IOManager{
	/** 定数宣言のためのインタフェイス */
	private static interface Message{
		String ILLEGAL_INPUT_ERROR= "ERROR> %d以上%d以下の数字を入力してください。%n";
		String CANDIDATE_BOX= "[%s]";
		String A_CANDIDATE= "%d:%s";
		String PROMPT_SYMBOL= ">";
		String CANDIDATE_SEPARATOR= " ";
	}
	
	public ConsoleIOManager(){}
	
	@Override
	public <T>T select(String msg, Collection<T> candidates){
		ImmutableList<T> copyList= ImmutableList.copyOf(candidates);
		
		this.writeln(msg);
		this.write(Message.CANDIDATE_BOX, this.formatCandidates(0, copyList));
		
		try{
			BufferedReader reader= new BufferedReader(new InputStreamReader(System.in));
			
			Range<Integer> validRange= Ranges.closed(1, copyList.size());
			
			while(true){
				try{
					this.write(Message.PROMPT_SYMBOL);
					String input= reader.readLine();
					
					// XXX: もっと細かく入力チェックするなら、BigIntegerあたりでStringをパースし、
					// 細かくビット数等を見ればよいのだが、オーバースペックだと思われる。
					int parseInput= Integer.parseInt(input);
					
					if(validRange.apply(parseInput)){ return copyList.get(parseInput - 1); }
					
					this.write( //
							Message.ILLEGAL_INPUT_ERROR, //
							validRange.lowerEndpoint(), //
							validRange.upperEndpoint() //
					);
				}
				catch(NumberFormatException e){
					this.write( //
							Message.ILLEGAL_INPUT_ERROR, //
							validRange.lowerEndpoint(), //
							validRange.upperEndpoint() //
					);
				}
			}
		}
		catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	
	private String formatCandidates(int idx, ImmutableList<?> candidates){
		checkArgument(idx >= 0 && candidates != null);
		if( !(idx < candidates.size())){ return null; }
		
		String candidate= String.format(Message.A_CANDIDATE, idx + 1, candidates.get(idx));
		
		Joiner joiner= Joiner.on(Message.CANDIDATE_SEPARATOR).skipNulls();
		
		return joiner.join(candidate, this.formatCandidates(idx + 1, candidates));
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
