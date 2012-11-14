package simulator.io;

import java.util.Collection;
import java.util.Formattable;

public interface Appender{
	<T extends Formattable> T select(String msg, Collection<T> candidates);
	Layout getLayout();
	void setLayout(Layout layout);
	void write(String format, Object...params);
	void writeln(String format, Object...params);
}
