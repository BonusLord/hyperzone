package com.bonuslord.hyper.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.time.ZoneOffset;
import java.util.List;

import javax.swing.JPanel;

import com.bonuslord.hyper.control.HZControls;
import com.bonuslord.hyper.coords.HZAziCoord;
import com.bonuslord.hyper.coords.HZPolarCoord;
import com.bonuslord.hyper.model.HZActor;
import com.bonuslord.hyper.model.HZWorld;

public class HyperView {

	private HZWorld world;
	private HyperPanel panel;
	private HZControls controls;
	
	private double unitsPerPixel = 0.02;
	private HZAziCoord upperLeft = new HZAziCoord(0, 0);
	
	private boolean playerCentric = true;
	
	public HyperView(HZWorld world) {
		this.world = world;
		
		panel = new HyperPanel();
		
		panel.setFocusable(true);
		
		panel.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (controls == null) return;
				controls.handleRelease(e);
				
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					playerCentric = !playerCentric;
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (controls == null) return;
				controls.handlePress(e);
			}
		});
		
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				double x = e.getX();
				double y = e.getY();
				HZAziCoord aziPos = screenToAzi(x, y);
				if (e.getButton() == MouseEvent.BUTTON1) {
					controls.handleLeftClick(aziPos);
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					controls.handleRightClick(aziPos);
				}
			}
		});
	}
	
	public void setControls(HZControls controls) {
		this.controls = controls;
	}

	public void setUpperLeft(HZAziCoord upperLeft) {
		this.upperLeft = upperLeft;
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	private HZAziCoord screenToAzi(double screenX, double screenY) {
		double aziX = screenX*unitsPerPixel + upperLeft.x;
		double aziY = screenY*unitsPerPixel + upperLeft.y;
		
		return new HZAziCoord(aziX, aziY);
	}
	
	private class HyperPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		private void paintRings(Graphics2D g2) {
			g2.setColor(Color.GREEN);
			
			double xOffset = -upperLeft.x;
			double yOffset = -upperLeft.y;
			
			double ringSpacing = 1;
			
			for (double radius=ringSpacing; radius < ringSpacing*50; radius += ringSpacing) {
				int rr = (int)(radius/unitsPerPixel);
				double x = (-radius+xOffset)/unitsPerPixel;
				double y = (-radius+yOffset)/unitsPerPixel;
				g2.drawOval((int)x, (int)y, rr*2, rr*2);
			}
				
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;
			List<HZActor> actors = world.getActors();
			
			g2.setBackground(Color.BLACK);
			g2.clearRect(0, 0, getWidth(), getHeight());
			
			g2.setColor(Color.RED);
			g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
			
			paintRings(g2);
			
			double xOffset = -upperLeft.x;
			double yOffset = -upperLeft.y;
			
			g2.setColor(Color.WHITE);
			
			//g2.setStroke(new BasicStroke(3));
			
			HZPolarCoord absPlayerPos = world.getPlayer().position;
			HZPolarCoord negatedAbsPlayerPos = new HZPolarCoord(absPlayerPos.angle+Math.PI, absPlayerPos.distance);
			
			int x = 0;
			for (HZActor actor : actors) {
				HZPolarCoord actPos = actor.position;
				List<HZPolarCoord> points = actor.getRelativeParts();
				
				int[] xs = new int[points.size()];
				int[] ys = new int[points.size()];
				
				for (int i=0; i<points.size(); i++) {
					HZPolarCoord p = points.get(i);
					HZPolarCoord absPos = actPos.add(p);
					
					HZPolarCoord finalRenderPos;
					if (playerCentric) {
						HZPolarCoord playerCentricPos = HZPolarCoord.redefineOrigin(absPos, absPlayerPos);
						finalRenderPos = playerCentricPos;
					} else {
						finalRenderPos = absPos;
					}
					
					HZAziCoord aziPos = finalRenderPos.toAzi();
					
					int finalX = (int)Math.round((aziPos.x + xOffset)/unitsPerPixel);
					int finalY = (int)Math.round((aziPos.y + yOffset)/unitsPerPixel);
					
					
					//g2.setColor(new Color(b, b, b));
					//g2.fillRect(finalX-1, finalY-1, 3, 3);
					
					xs[i] = finalX;
					ys[i] = finalY;
					
					//String xxx = String.valueOf(i);
					//g2.drawChars(xxx.toCharArray(), 0, xxx.length(), finalX, finalY);
				}
				x++;
				
				g2.setColor(actor.color);
				g2.setStroke(new BasicStroke((float)actor.thickness));
				
				g2.drawPolygon(xs, ys, points.size());
			}
			
			
			
		}
		
	}
}
