/**
 * Title: Messageable.java
 * Function:
 * Simple interface for to representing conforming objects can receive a CSOMessage
 * Based on details in Cyberspatial Mechanics (IEEE TRANSACTIONS ON SYSTEMS, MAN, AND CYBERNETICS—PART B: CYBERNETICS, VOL. 38, NO. 3, JUNE 2008)
 * History:     
 *  Date         Author              Changes
 *  10.24.2021   lghoward@uwm.edu    initial
**/

package edu.uwm.basecso;

/**
 * Simple interface for representing that a conforming object can receive a CSOMessage
 * and also require them to have an identifier.
 * 
 * @author lonny
 */
public interface Messageable {
	/**
	 * Returns an identifier that should be unique for each conforming object within the application.
	 * 
	 * @return long identifier
	 */
	public long getIdentifier();
	
    /**
     * Implementations will handle the passed in message as needed.
     * 
     * @param message a CSOMessage to handle
     * @return boolean whether the message was handled successfully
     */
    public boolean receiveMessage(CSOMessage message);
}
