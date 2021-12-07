/**
 * Title: RWDirectives.java
 * Function:
 *  Simple class to direct a Random Walker
 * History:     
 *  Date         Author              Changes
 *  10.24.2021   lghoward@uwm.edu    initial
**/

package edu.uwm.cs790.assignment9;

/**
 * Indicates whether movement in a particular direction is allowed.
 * 
 * @author lonny
 */
public class RWDirectives {

    public final boolean isNorthAllowed;
    public final boolean isSouthAllowed;
    public final boolean isEastAllowed;
    public final boolean isWestAllowed;
    
    /**
	* Sets directives for all directions, allowNorth, allowSouth, allowEast, allowWest
	* Can modify the directions in which randomWalk is allowed to move.
	* 
	* @param allowNorth boolean that indicates if travel is allowed north
	* @param allowSouth boolean that indicates if travel is allowed north
	* @param allowEast boolean that indicates if travel is allowed north
	* @param allowWest boolean that indicates if travel is allowed north
	*/
    public RWDirectives(boolean allowNorth, boolean allowSouth, boolean allowEast, boolean allowWest) {
        this.isNorthAllowed = allowNorth;
        this.isSouthAllowed = allowSouth;
        this.isEastAllowed = allowEast;
        this.isWestAllowed = allowWest;
    }
    
}
