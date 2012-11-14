package simulator.io;

import java.util.Collection;

public interface Appender{
	<T> T select(String msg, Collection<T> candidates);
	void write(String format, Object...params);
	void writeln(String format, Object...params);
}
