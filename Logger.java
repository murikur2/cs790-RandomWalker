/**
 * Title: Logger.java
 * Function:
 *  Simple class to hand logging.
 * History:     
 *  Date         Author              Changes
 *  11.03.2021   lghoward@uwm.edu    initial
**/
package edu.uwm.cs790.assignment9;

/**
 * Simple class to handle logging.
 * 
 * @author lonny
 *
 */
public class Logger {
	private static boolean isEnabled = true;

	public static boolean isEnabled() {
		return isEnabled;
	}

	public static void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}
	
	public static void log(String message) {
		if (isEnabled) {
			StdOut.println(Thread.currentThread().getName() + ": " + message);
		}
	}
}
