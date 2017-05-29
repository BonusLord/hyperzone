package com.bonuslord.hyper.coords;

import com.bonuslord.hyper.math.HZMath;
import com.bonuslord.hyper.model.HZGeoInfo;
import static java.lang.Math.*;
import static com.bonuslord.hyper.math.HZMath.*;

public class HZPolarCoord {
	public double angle; //radians
	public double distance;
	
	public HZPolarCoord(double angle, double distance) {
		if (distance < 0) {
			distance *= -1;
			angle += Math.PI;
		}
		while (angle < 0) {
			angle += Math.PI*2;
		}
		while (angle > Math.PI*2) {
			angle -= Math.PI*2;
		}
		
		this.angle = angle;
		this.distance = distance;
	}

	private static double solveForSide(double b, double c, double x) {
		double q1 = sinh((b-c)/2);
		double q2 = sin(x/2);
		
		double barf = cosh(b)*cosh(c) - sinh(b)*sinh(c)*cos(x);
		double distCalc1 = acosh(barf);
		double newDist = distCalc1;
		
		if (Double.isNaN(newDist)) {
			double distCalc2 = asinh(sqrt(q1*q1+sinh(b)*sinh(c)*q2*q2));
			newDist = distCalc2;
		}
		
		return newDist;
	}
	
	private static double solveForAngle(double a, double b, double c, boolean flip, double otherAngle) {
		double t1 = sinh(a/2);
		double t2 = sinh((b-c)/2);
		
		double numer = t1*t1 - t2*t2;
		double deno = sinh(c)*sinh(b);
		double ratioSqrt = sqrt(numer/deno);
		double y = asin(ratioSqrt)*2;
		
		double newAng = otherAngle-y;
		
		if (flip) {
			newAng = otherAngle+y;
		}
		
		return newAng;
	}
	
	public static HZPolarCoord redefineOrigin(HZPolarCoord toRedefine, HZPolarCoord newOrigin) {
		double c = newOrigin.distance;
		double b = toRedefine.distance;
	
		if (b == 0) {
			return new HZPolarCoord(newOrigin.angle, -newOrigin.distance);
		}
		if (c == 0) {
			return new HZPolarCoord(toRedefine.angle, toRedefine.distance);
		}
		
		double angDelta = newOrigin.angle - toRedefine.angle;
		boolean flip = (angDelta < 0 && angDelta > -Math.PI) || (angDelta > Math.PI && angDelta < Math.PI*2);
		
		//flip = false;
		
//		if (angDelta < 0) angDelta = Math.abs(angDelta-PI);
//		else angDelta += PI;
		
		if (epEq(angDelta, 0) || epEq(angDelta, 2*Math.PI) || epEq(angDelta, -2*Math.PI)) {
			return new HZPolarCoord(newOrigin.angle, toRedefine.distance - newOrigin.distance);
		} else if (epEq(angDelta, Math.PI) || epEq(angDelta, -Math.PI)) {
			return new HZPolarCoord(newOrigin.angle+Math.PI, newOrigin.distance + toRedefine.distance);
		}
		
		double newDist = solveForSide(b, c, angDelta);
		
		double newAng = solveForAngle(b, newDist, c, !flip, newOrigin.angle);
		
		newAng += Math.PI;
		
		if (Double.isNaN(newAng)) {
			newAng = newOrigin.angle;
		}
		
		if (Double.isNaN(newDist)) {
			newDist = newOrigin.distance;
		}
		
		return new HZPolarCoord(newAng, newDist);
	}
	
	public HZPolarCoord sub(HZPolarCoord toRedefine) {
		double c = distance;
		double b = toRedefine.distance;
	
		if (b == 0) {
			return new HZPolarCoord(angle, distance);
		}
		if (c == 0) {
			return new HZPolarCoord(toRedefine.angle, toRedefine.distance);
		}
		
		double angDelta = angle - toRedefine.angle;
		boolean flip = (angDelta < 0 && angDelta > -Math.PI) || (angDelta > Math.PI && angDelta < Math.PI*2);
		
		//flip = false;
		
//		if (angDelta < 0) angDelta = Math.abs(angDelta-PI);
//		else angDelta += PI;
		
		if (epEq(angDelta, 0) || epEq(angDelta, 2*Math.PI) || epEq(angDelta, -2*Math.PI)) {
			return new HZPolarCoord(angle, toRedefine.distance - distance);
		} else if (epEq(angDelta, Math.PI) || epEq(angDelta, -Math.PI)) {
			return new HZPolarCoord(angle+Math.PI, distance + toRedefine.distance);
		}
		
		double newDist = solveForSide(b, c, angDelta);
		
		double newAng = solveForAngle(b, newDist, c, !flip, angle);
		
		newAng += Math.PI;
		
		if (Double.isNaN(newAng)) {
			newAng = angle;
		}
		
		if (Double.isNaN(newDist)) {
			newDist = distance;
		}
		
		return new HZPolarCoord(newAng, newDist);
	}
	
	public HZPolarCoord add(HZPolarCoord other) {
		double c = distance;
		double b = other.distance;
	
		if (b == 0) {
			return new HZPolarCoord(angle, distance);
		}
		if (c == 0) {
			return new HZPolarCoord(other.angle, other.distance);
		}
		
		double angDelta = angle - other.angle;
		boolean flip = (angDelta < 0 && angDelta > -Math.PI) || (angDelta > Math.PI && angDelta < Math.PI*2);
		
		//flip = false;
		
		if (angDelta < 0) angDelta = Math.abs(angDelta-PI);
		else angDelta += PI;
		
		if (epEq(angDelta, 0) || epEq(angDelta, 2*Math.PI) || epEq(angDelta, -2*Math.PI)) {
			return new HZPolarCoord(angle, distance - other.distance);
		} else if (epEq(angDelta, Math.PI) || epEq(angDelta, -Math.PI)) {
			return new HZPolarCoord(angle, distance + other.distance);
		}
		
		double newDist = solveForSide(b, c, angDelta);
		
		double newAng = solveForAngle(b, newDist, c, flip, angle);
		
		if (Double.isNaN(newAng)) {
			newAng = angle;
		}
		
		if (Double.isNaN(newDist)) {
			newDist = distance;
		}
		
		return new HZPolarCoord(newAng, newDist);
	}
	
	public HZPolarCoord negate() {
		return new HZPolarCoord(angle, -distance);
	}
	
	public HZAziCoord toAzi() {
		double x = distance * cos(angle);
		double y = distance * sin(angle);
		
		return new HZAziCoord(x, y);
	}
	
	@Override
	public String toString() {
		
		return "("+distance+","+Math.toDegrees(angle)+")";
	}
}
