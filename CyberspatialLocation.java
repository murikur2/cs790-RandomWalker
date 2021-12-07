/**
 * Title: CyberspatialLocation.java
 * Function:
 * Initial implementation of a Cyberspatial location using the GeospatialCoordinates, InfospatialCoordinates, and SociospatialCoordinates.
 * Based on details in Cyberspatial Mechanics (IEEE TRANSACTIONS ON SYSTEMS, MAN, AND CYBERNETICS—PART B: CYBERNETICS, VOL. 38, NO. 3, JUNE 2008)
 * History:     
 *  Date         Author              Changes
 *  09.29.2021   lghoward@uwm.edu    copied implementation for first assignment
**/

package edu.uwm.basecso;

/**
 * Holds the GeospatialCoordinates, InfospatialCoordinates, and SociospatialCoordinates
 * that make up a Cyberspatial location.
 * 
 * @author Lonny Howard - lghoward@uwm.edu
 * @version 0.1.0
 */
public final class CyberspatialLocation {
    private final GeospatialCoordinates geospatialCoordinates;
    private final InfospatialCoordinates infospatialCoordinates;
    private final SociospatialCoordinates sociospatialCoordinates;

    /**
     * Constructor passing in the 3 components.
     * @param g GeospatialCoordinates
     * @param i InfospatialCoordinates
     * @param s SociospatialCoordinates
     */
    public CyberspatialLocation(GeospatialCoordinates g, InfospatialCoordinates i, SociospatialCoordinates s) {
        this.geospatialCoordinates = g;
        this.infospatialCoordinates = i;
        this.sociospatialCoordinates = s;
    }

    /**
     * Getter for GeospatialCoordinates
     * @return GeospatialCoordinates
     */
    public GeospatialCoordinates getGeospatialCoordinates() {
        return geospatialCoordinates;
    }

    /**
     * Getter for InfospatialCoordinates.
     * @return InfospatialCoordinates
     */
    public InfospatialCoordinates getInfospatialCoordinates() {
        return infospatialCoordinates;
    }

    /**
     * Getter for SociospatialCoordinates.
     * @return SociospatialCoordinates
     */
    public SociospatialCoordinates getSociospatialCoordinates() {
        return sociospatialCoordinates;
    }
    
    
}
