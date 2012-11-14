package simulator.io;

import java.util.Collection;

public interface Appender{
	<T> T select(String msg, Collection<T> candidates);
	Layout getLayout();
	void setLayout(Layout layout);
	void write(String format, Object...params);
	void writeln(String format, Object...params);
}
