package com.bonuslord.hyper.control;

import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.swing.SwingUtilities;

import com.bonuslord.hyper.coords.HZPolarCoord;
import com.bonuslord.hyper.model.HZWorld;
import com.bonuslord.hyper.ui.HyperView;

public class HZEngine {

	private HZControls controls = new HZControls(this);
	private HZWorld world;
	
	private HyperView view;
	
	private HZPolarCoord playerVelo = new HZPolarCoord(0, 0);
	
	private Timer gameLoopTimer;
	
	private ConcurrentLinkedDeque<Runnable> bonusTasks = new ConcurrentLinkedDeque<>();
	
	public HZEngine(HZWorld world, HyperView view) {
		this.world = world;
		this.view = view;
		
		view.setControls(controls);
		
		startGameLoop();
	}

	public HZWorld getWorld() {
		return world;
	}
	
	public void submitTask(Runnable task) {
		bonusTasks.addLast(task);
	}
	
	private void renderLoop() {
		view.getPanel().repaint();
		try {SwingUtilities.invokeAndWait(()->{});} catch (Exception e) {}
	}
	
	private void logicLoop() {
		
		boolean up = controls.isPressed(KeyEvent.VK_UP);
		boolean down = controls.isPressed(KeyEvent.VK_DOWN);
		boolean left = controls.isPressed(KeyEvent.VK_LEFT);
		boolean right = controls.isPressed(KeyEvent.VK_RIGHT);
		
		playerVelo.distance = 0.07;
		//playerVelo.distance = 2.5;
		if (up && left) {
			playerVelo.angle = Math.toRadians(-135);
		} else if (up && right) {
			playerVelo.angle = Math.toRadians(-45);
		} else if (down && left) {
			playerVelo.angle = Math.toRadians(135);
		} else if (down && right) {
			playerVelo.angle = Math.toRadians(45);
		} else if (up) {
			playerVelo.angle = Math.toRadians(-90);
		} else if (down) {
			playerVelo.angle = Math.toRadians(90);
		} else if (left) {
			playerVelo.angle = Math.toRadians(180);
		} else if (right) {
			playerVelo.angle = Math.toRadians(0);
		} else {
			playerVelo.distance = 0;
		}
		
		//controls.handleRelease(KeyEvent.VK_LEFT);
		
		HZPolarCoord pp = world.getPlayer().position;
		
		HZPolarCoord veloInPlayerSpace = playerVelo;
		
		HZPolarCoord worldOriginInPlayerSpace = new HZPolarCoord(pp.angle, -pp.distance);
		
		HZPolarCoord playerDestination = HZPolarCoord.redefineOrigin(veloInPlayerSpace, worldOriginInPlayerSpace);
		
		if (true) {
			world.getPlayer().position = playerDestination;
			return;
		}
		
		if (false) {
			playerVelo.distance /= 100;
			for (int i=0; i<100; i++) {
				world.getPlayer().position = world.getPlayer().position.add(playerVelo);
			}
		} else {
			world.getPlayer().position = pp.add(playerVelo);
		}
		//world.getPlayer().position = playerVelo.add(pp);
		
	}

	private void startGameLoop() {
		gameLoopTimer = new Timer(true);
		
		gameLoopTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					mainLoop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 25);
	}
	
	private void mainLoop() {
		renderLoop();
		logicLoop();
		bonusLoop();
	}
	
	private void bonusLoop() {
		while (!bonusTasks.isEmpty()) {
			bonusTasks.pollFirst().run();
		}
	}
	
	
}
