package com.bonuslord.hyper.control;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import com.bonuslord.hyper.coords.HZAziCoord;
import com.bonuslord.hyper.coords.HZPolarCoord;
import com.bonuslord.hyper.model.HZActor;
import com.bonuslord.hyper.model.HZWorld;

public class HZControls {

	private HZEngine engine;
	private Map<Integer, Boolean> pressedMap = new HashMap<>();

	public HZControls(HZEngine engine) {
		this.engine = engine;
	}

	public void handlePress(KeyEvent kev) {
		int key = kev.getKeyCode();
		pressedMap.put(key, true);
	}
	
	public void handleRelease(int keyCode) {
		pressedMap.put(keyCode, false);
	}
	
	public void handleRelease(KeyEvent kev) {
		int key = kev.getKeyCode();
		pressedMap.put(key, false);
	}
	
	public boolean isPressed(int keyCode) {
		Boolean ret = pressedMap.get(keyCode);
		return ret == null ? false : ret;
	}
	
	public void handleRightClick(HZAziCoord clickPos) {
		HZPolarCoord playerRelataivePos = clickPos.toPolar();
		HZWorld world = engine.getWorld();
		HZActor player = world.getPlayer();
		
		HZPolarCoord absPos = HZPolarCoord.redefineOrigin(playerRelataivePos, player.position.negate());
		
		System.out.println("Jumping player to new pos based on click pos of: "+playerRelataivePos);
		
		engine.submitTask(()->player.position = absPos);
		
	}
	
	public void handleLeftClick(HZAziCoord clickPos) {
		HZPolarCoord playerRelataivePos = clickPos.toPolar();
		HZWorld world = engine.getWorld();
		HZPolarCoord absPos = HZPolarCoord.redefineOrigin(playerRelataivePos, world.getPlayer().position.negate());
		
		HZActor clickie = HZActor.createCircleish(0.1, 128);
		clickie.setPosition(absPos);
		
		engine.submitTask(()->world.addActor(clickie));
		
		Runnable remover = ()->{
			try {Thread.sleep(1000);} catch (InterruptedException e) {}
			engine.submitTask(()->world.removeActor(clickie));
		};
		
		new Thread(remover).start();
		
	}
	
	
}
