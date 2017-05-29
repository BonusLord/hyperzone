package com.bonuslord.hyper.math;

import static java.lang.Math.*;

public class HZMath {

	public static double asinh(double x) { 
		return Math.log(x + Math.sqrt(x*x + 1.0)); 
	} 

	public static double acosh(double x) { 
		return Math.log(x + Math.sqrt(x*x - 1.0)); 
	} 

	public static double atanh(double x) { 
		return 0.5*Math.log( (x + 1.0) / (x - 1.0)); 
	} 
	
	public static boolean epEq(double a, double b) {
		return epEq(a, b, 0.00000001);
	}
	
	public static boolean epEq(double a, double b, double ep) {
		return abs(a-b) <= ep;
	}
}
