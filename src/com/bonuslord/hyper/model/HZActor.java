package com.bonuslord.hyper.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.bonuslord.hyper.coords.HZPolarCoord;

public class HZActor {

	public Color color = Color.WHITE;
	public int thickness = 1;
	public HZPolarCoord position = new HZPolarCoord(0, 0);
	public List<HZPolarCoord> relativeParts = new ArrayList<>();
	
	public List<HZPolarCoord> getRelativeParts() {
		return relativeParts;
	}
	
	public HZActor(List<HZPolarCoord> relativeParts) {
		this.relativeParts = relativeParts;
	}
	
	public void setPosition(HZPolarCoord position) {
		this.position = position;
	}

	public static HZActor createCircleish(double radius, int pointCount) {
		double inc = (Math.PI*2) / pointCount;
		
		List<HZPolarCoord> coords = new ArrayList<>();
		
		for (int i=0; i<pointCount; i++) {
			double ang = inc*i;
			
			coords.add(new HZPolarCoord(ang, radius));
		}
		
		return new HZActor(coords);
	}
	
}
