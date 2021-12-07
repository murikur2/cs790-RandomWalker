/**
 * Title: CSOPayload.java
 * Function:
 * Initial implementation of a CSOPayload containing the information needed for a CSO to act on. The type is generic in order to allow implementation specific data.
 * Based on details in Cyberspatial Mechanics (IEEE TRANSACTIONS ON SYSTEMS, MAN, AND CYBERNETICS—PART B: CYBERNETICS, VOL. 38, NO. 3, JUNE 2008)
 * History:     
 *  Date         Author              Changes
 *  09.29.2021   lghoward@uwm.edu    initial
**/

package edu.uwm.basecso;

import java.util.Date;

/**
 * Implementation of a CSOPayload containing the information needed for a CSO to act on. 
 * The type is generic in order to allow implementation specific data.
 * 
 * @author Lonny Howard - lghoward@uwm.edu
 * @version 0.1.0
 * @param <T> The type of the order. It is expected to be sent to a CSO that will know how to handle.
 */
public final class CSOPayload<T> {
    public final T order;
    public final long quality;
    public final Date issuedTime;
    public final Date completionTime;

    /**
     * Constructor
     * @param order Should be the same type as the generic parameter for the class. Intended to be CSO specific.
     * @param quality long
     * @param issuedTime Date
     * @param completionTime Date
     */
    public CSOPayload(T order, long quality, Date issuedTime, Date completionTime) {
        this.order = order;
        this.quality = quality;
        this.issuedTime = issuedTime;
        this.completionTime = completionTime;
    }

}
