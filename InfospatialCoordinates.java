/**
 * Title: InfospatialCoordinates.java
 * Function:
 * Holds infospatial coordinates.
 * Based on details in Cyberspatial Mechanics (IEEE TRANSACTIONS ON SYSTEMS, MAN, AND CYBERNETICS—PART B: CYBERNETICS, VOL. 38, NO. 3, JUNE 2008)
 * History:     
 *  Date         Author              Changes
 *  09.29.2021   lghoward@uwm.edu    initial
**/

package edu.uwm.basecso;

/**
 * Represents a location in information space using a (g,s,a) coordinate system.
 *
 * This could be used to represent an IPv6 address where the "g" coordinate is the
 * global network address, "s" is the subnet, and "a" is the particular device address.
 *
 * @author lonny
 */
public final class InfospatialCoordinates {
    final private long g, s, a;

    /**
     * Constructor that takes the needed coordinates.
     * 
     * Since the class is final, any changes to coordinates must be done by creating a new instance.
     * 
     * @param g a long value such as the global network address portion of an IPv6 address
     * @param s a long value such as the subnet address portion of an IPv6 address
     * @param a a long value such as the device address portion of an IPv6 address
     */
    public InfospatialCoordinates(long g, long s, long a) {
        this.g = g;
        this.s = s;
        this.a = a;
    }

    public long getG() {
        return g;
    }

    public long getS() {
        return s;
    }

    public long getA() {
        return a;
    }
    
    
}
