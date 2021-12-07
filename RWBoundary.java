/**
 * Title: RWBoundary.java
 * Function:
 *  Simple class to hold a boundary
 * History:     
 *  Date         Author              Changes
 *  10.24.2021   lghoward@uwm.edu    initial
**/

package edu.uwm.cs790.assignment9;

import java.awt.Point;

/**
 * Simple wrapper to represent a square boundary.
 * 
 * @author lonny
 */
	
public class RWBoundary {

    public final int bound;
    public final Point center;

    /**
     * 
     * Constructor sets boundary
     * Sets square boundary to confine randomWalk movement
     * 
     * @param bound integer representing the boundaries
     */
    public RWBoundary(int bound) {
        this(bound, new Point(0,0));
    }
    
    public RWBoundary(int bound, Point p) {
        this.bound = bound;
        this.center = new Point(p.x, p.y);
    }
    
    public int getRightLimit() {
    	return center.x + bound;
    }
    
    public int getLeftLimit() {
    	return center.x - bound;
    }
    
    
    public int getTopLimit() {
    	return center.y + bound;
    }
    
    public int getBottomLimit() {
    	return center.y - bound;
    }
}
