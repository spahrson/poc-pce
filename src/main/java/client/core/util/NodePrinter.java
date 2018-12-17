package client.core.util;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * NodePrinter is a simple class that helps to visualize a tree based on node object.
 * 
 * PoC - PCE
 * @author Ramon Spahr (ramon.spahr@gmail.com)
 */
public class NodePrinter {

	public static void print(Node<?> node) {
		NodePrinter.print(node, System.out);
	}

	public static void print(Node<?> node, PrintStream out) {
		print(node, 0, out);
	}

	private static void print(Node<?> node, int level, PrintStream out) {
		StringBuilder line = new StringBuilder();
		int i=0;
		line.append("|");
		for(; i<level-1; i++) {
			line.append("-");
		}
		if(level>0) line.append("-");
		if(!node.getChildren().isEmpty()) line.append("+");
		else line.append("+");
		/*for(i=0; i<level; i++) {
			line.append("-");
		}*/
		line.append(" "+node.toString());
		out.print(line.toString());
		out.println(" ("+node.getChildren().size()+"|"+dataSize(node.getData())+")");
		level++;
		printData(node.getData(), level, out);
		for(Node<?> n : node.getChildren()) {
			print(n, level, out);
		}
		
	}
	
	public static void printData(Object o, int level, PrintStream out) {
		if(o instanceof Collection) 
			for(Object x : (Collection<?>) o) {
				printDataLine(x.toString(), level, out);
			}
		else if(o instanceof Map)
			for(Entry<?, ?> x : ((Map<?,?>) o).entrySet()) {
				try {
					printDataLine(x.getKey().toString()+":"+x.getValue().toString(), level, out);
				} catch(NullPointerException e) {
					printDataLine(x.getKey().toString()+":<not printable>", level, out);
				}
			}
		else if(o != null) printDataLine(o.toString(), level, out);
		else printDataLine("<empty>", level, out);
	}
	
	public static void printDataLine(String content, int level, PrintStream out) {
		StringBuilder line = new StringBuilder();
		line.append("|");
		for(int i=0; i<level; i++) {
			line.append(" ");
		}
		line.append("    [");
		line.append(content);
		line.append("]");
		out.println(line.toString());
	}

	public static int dataSize(Object o) {
		if(o instanceof Collection) return ((Collection<?>) o).size();
		else if(o instanceof Map) return ((Map<?,?>) o).size();
		else if(o != null) return 1;
		else return 0;
	}
}