package guava.immutablecollection;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public final class SpeedCheck{
	private static final int ntrials= 10000;
	
	public static void main(String[] args){
		PrintStream origin= System.out;
		System.setOut(new DevNull(System.out));
		
		for(int i= 0; i < 10; ++i)
			Runtime.getRuntime().gc();
		
		final long mut_time= mutableList();
		final long imm_time= immutableList();
		System.setOut(origin);
		System.out.println(String.format(
				"time required: \n" +
						"immutable list: %s [s]\n" +
						"mutable list:   %s [s]\n",
				numberFormat(imm_time),
				numberFormat(mut_time)));
	}
	
	private static String numberFormat(long val){
		return DecimalFormat.getNumberInstance().format((double)val * 1e-9);
	}
	
	private static long immutableList(){
		final long start= System.nanoTime();
		ImmutableList<Integer> l= ImmutableList.of();
		for(int i= 0; i < ntrials; ++i){
			System.out.println(l);
			l= ImmutableList.copyOf(Iterables.concat(l, ImmutableList.of(i)));
		}
		final long end= System.nanoTime();
		
		return end - start;
	}
	
	private static long mutableList(){
		final long start= System.nanoTime();
		List<Integer> l= Lists.newArrayList();
		for(int i= 0; i < ntrials; ++i){
			System.out.println(l);
			l= Lists.newArrayList(Iterables.concat(l, Lists.newArrayList(i)));
		}
		final long end= System.nanoTime();
		
		return end - start;
	}
	
	private static final class DevNull extends PrintStream{
		public DevNull(OutputStream out){
			super(out);
		}
		
		@Override
		public void flush(){}
		
		@Override
		public void close(){}
		
		@Override
		public boolean checkError(){
			return false;
		}
		
		@Override
		protected void setError(){}
		
		@Override
		protected void clearError(){}
		
		@Override
		public void write(int b){}
		
		@Override
		public void write(byte[] buf, int off, int len){}
		
		@Override
		public void print(boolean b){}
		
		@Override
		public void print(char c){}
		
		@Override
		public void print(int i){}
		
		@Override
		public void print(long l){}
		
		@Override
		public void print(float f){}
		
		@Override
		public void print(double d){}
		
		@Override
		public void print(char[] s){}
		
		@Override
		public void print(String s){}
		
		@Override
		public void print(Object obj){}
		
		@Override
		public void println(){}
		
		@Override
		public void println(boolean x){}
		
		@Override
		public void println(char x){}
		
		@Override
		public void println(int x){}
		
		@Override
		public void println(long x){}
		
		@Override
		public void println(float x){}
		
		@Override
		public void println(double x){}
		
		@Override
		public void println(char[] x){}
		
		@Override
		public void println(String x){}
		
		@Override
		public void println(Object x){}
		
		@Override
		public PrintStream printf(String format, Object... args){
			return this;
		}
		
		@Override
		public PrintStream printf(Locale l, String format, Object... args){
			return this;
		}
		
		@Override
		public PrintStream format(String format, Object... args){
			return this;
		}
		
		@Override
		public PrintStream format(Locale l, String format, Object... args){
			return this;
		}
		
		@Override
		public PrintStream append(CharSequence csq){
			return this;
		}
		
		@Override
		public PrintStream append(CharSequence csq, int start, int end){
			return this;
		}
		
		@Override
		public PrintStream append(char c){
			return this;
		}
		
	}
}