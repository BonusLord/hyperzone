package com.bonuslord.hyper.coords;

import com.bonuslord.hyper.model.HZGeoInfo;

/**
 * Represents a coordinate in the azimuthal projection about the origin (polar coord (0,0))
 * of the hyperbolic plane.
 */
public class HZAziCoord {
	public double x;
	public double y;
	
	public HZAziCoord(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public HZPolarCoord toPolar() {
		return new HZPolarCoord(Math.atan2(y, x), Math.sqrt(x*x+y*y));
	}
}
