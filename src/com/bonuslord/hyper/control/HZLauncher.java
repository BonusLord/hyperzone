package com.bonuslord.hyper.control;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

import com.bonuslord.hyper.coords.HZAziCoord;
import com.bonuslord.hyper.coords.HZPolarCoord;
import com.bonuslord.hyper.model.HZActor;
import com.bonuslord.hyper.model.HZWorld;
import com.bonuslord.hyper.ui.HyperView;

public class HZLauncher {

	
//	private void barfZone() {
//
//		Color[] colorz = new Color[]{
//			new Color(200, 10, 10),
//			new Color(200, 200, 10),
//			new Color(150, 55, 165),
//			new Color(2, 2, 165),
//		};
//		
//		for (int level=0; level<6; level += 2) {
//			int count = (int)Math.pow(2, level);
//			double angleInc = Math.PI*2 / count;
//			
//			for (int i=0; i<count; i++) {
//				HZActor a = HZActor.createCircleish(1+level/2.5, 128);
//				a.setPosition(new HZPolarCoord(i*angleInc, level));
//				world.addActor(a);
//				
//				a.color = colorz[level/2];
//			}
//		}
//	}
	
	public static void main(String[] args) {
		HZWorld world = new HZWorld();
		
		HZActor player = HZActor.createCircleish(0.2, 128);
		player.setPosition(new HZPolarCoord(0, 0));
		world.setPlayer(player);
		world.addActor(player);
		
		
		HZActor a = HZActor.createCircleish(0.5, 128);
		a.setPosition(new HZPolarCoord(0, 1));
		//world.addActor(a);
		
		a = HZActor.createCircleish(0.2, 128);
		a.setPosition(new HZPolarCoord(45, 1.5));
		//world.addActor(a);
		
		a = HZActor.createCircleish(0.8, 128);
		a.setPosition(new HZPolarCoord(-156, 0.8));
		//world.addActor(a);
		
		a = HZActor.createCircleish(1.5, 128);
		a.setPosition(new HZPolarCoord(Math.PI, 3));
		world.addActor(a);
		
		a = HZActor.createCircleish(1.5, 128);
		a.setPosition(new HZPolarCoord(0, 3));
		world.addActor(a);
		
		a = HZActor.createCircleish(1.5, 128);
		a.setPosition(new HZPolarCoord(Math.PI/2, 3));
		world.addActor(a);
		
		a = HZActor.createCircleish(1.5, 128);
		a.setPosition(new HZPolarCoord(-Math.PI/2, 3));
		world.addActor(a);
		
		a = HZActor.createCircleish(2, 128);
		Color absRingColor = new Color(100, 100, 250, 200);
		a.color = absRingColor;
		a.thickness = 2;
		world.addActor(a);
		
		a = HZActor.createCircleish(3, 128);
		a.color = absRingColor;
		a.thickness = 2;
		world.addActor(a);
		
		a = HZActor.createCircleish(4, 128);
		a.color = absRingColor;
		a.thickness = 2;
		world.addActor(a);
		
		a = HZActor.createCircleish(5, 512);
		a.color = absRingColor;
		a.thickness = 2;
		world.addActor(a);
		
		HyperView hv = new HyperView(world);
		
		JFrame frm = new JFrame("HyperZone");
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container content = frm.getContentPane();
		
		content.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		
		content.add(hv.getPanel(), gbc);
		
		int w = 1024;
		int h = 768;
		
		frm.setSize(1024, 768);
		
		//hv.setUpperLeft(new HZAziCoord(-w/2, -h/2));
		hv.setUpperLeft(new HZAziCoord(-7,-7));
		
		frm.setVisible(true);
		
		
		HZEngine engine = new HZEngine(world, hv);
		
	}
	
}
