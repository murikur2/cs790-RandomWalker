/**
 * Title: CSOMessage.java
 * Function:
 * Initial implementation of a CSOMessage containing the information needed to be sent to a CSO (i.e. an order to act on).
 * Based on details in Cyberspatial Mechanics (IEEE TRANSACTIONS ON SYSTEMS, MAN, AND CYBERNETICS—PART B: CYBERNETICS, VOL. 38, NO. 3, JUNE 2008)
 * History:     
 *  Date         Author              Changes
 *  09.29.2021   lghoward@uwm.edu    initial
**/

package edu.uwm.basecso;

import java.util.ArrayList;
import java.util.Date;

/**
 * Implementation of a CSOMessage containing the information needed to be sent to a CSO (such as an order to act on).
 * 
 * @author Lonny Howard - lghoward@uwm.edu
 * @version 0.1.0
 */
public final class CSOMessage {
    public final Messageable fromCSO;
    public final Messageable toCSO;
    public final ArrayList<InfospatialCoordinates> selectors;
    public final CSOPayload<?> payload;
    public final Date timeSent;

    /**
     * A container providing a payload for a CSO to act on as well as various metadata.
     * 
     * @param fromCSO long identifier of the CSO issuing the message
     * @param toCSO long identifier of the CSO being messaged
     * @param selectors array of InfospatialCoordinates(???)
     * @param payload CSOPayload contains the CSO typed order
     * @param timeSent Date time message is being sent
     */
    public CSOMessage(Messageable fromCSO, Messageable toCSO, ArrayList<InfospatialCoordinates> selectors, CSOPayload<?> payload, Date timeSent) {
        this.fromCSO = fromCSO;
        this.toCSO = toCSO;
        this.selectors = selectors;
        this.payload = payload;
        this.timeSent = timeSent;
    }

}
