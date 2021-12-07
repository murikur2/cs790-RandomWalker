/**
 * Title: GeospatialCoordinates.java
 * Function:
 * Holds geospatial coordinates.
 * Based on details in Cyberspatial Mechanics (IEEE TRANSACTIONS ON SYSTEMS, MAN, AND CYBERNETICS—PART B: CYBERNETICS, VOL. 38, NO. 3, JUNE 2008)
 * History:     
 *  Date         Author              Changes
 *  09.29.2021   lghoward@uwm.edu    initial
**/

package edu.uwm.basecso;

/**
 * Represents a location in geographic space using an (x,y,z) coordinate system.
 * 
 * The class is neutral as to what the coordinates represent.
 * For example, they could be longitude, latitude, and azimuth or
 * a representation of a simple 2D or 3D drawing surface.
 *
 * @author lonny
 */
public final class GeospatialCoordinates {
    final private long x, y, z;

    /**
     * Constructor that takes the needed coordinates.
     * 
     * Since the class is final, any changes to coordinates must be done by creating a new instance.
     * 
     * @param x a long value representing the x coordinate of a position
     * @param y a long value representing the y coordinate of a position
     * @param z a long value representing the z coordinate of a position
     */
    public GeospatialCoordinates(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public long getZ() {
        return z;
    }
    

}
