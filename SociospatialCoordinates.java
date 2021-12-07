/**
 * Title: SociospatialCoordinates.java
 * Function:
 * Holds sociospatial coordinates.
 * Based on details in Cyberspatial Mechanics (IEEE TRANSACTIONS ON SYSTEMS, MAN, AND CYBERNETICS—PART B: CYBERNETICS, VOL. 38, NO. 3, JUNE 2008)
 * History:     
 *  Date         Author              Changes
 *  09.29.2021   lghoward@uwm.edu    initial
**/

package edu.uwm.basecso;


/**
 * Represents a location in sociological space using an (f,p,c) coordinate system.
 * 
 * The coordinates could be used for anything, but generally should represent some sort
 * of role in a federated system where "f" represents a specific federation, "p" represents
 * a position in the horizontal production chain, and "c" represents the position in the 
 * vertical chain of command.
 *
 * @author lonny
 */
public final class SociospatialCoordinates {
    final private long f, p, c;

    /**
     * Constructor that takes the needed coordinates.
     * 
     * Since the class is final, any changes to coordinates must be done by creating a new instance.
     * 
     * @param f a long value representing a federation, business unit, etc.
     * @param p a long value representing a position in the horizontal production chain
     * @param c a long value representing a position in the vertical chain of command
     */
    public SociospatialCoordinates(long f, long p, long c) {
        this.f = f;
        this.p = p;
        this.c = c;
    }

    public long getF() {
        return f;
    }

    public long getP() {
        return p;
    }

    public long getC() {
        return c;
    }
    
}
