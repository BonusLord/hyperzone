package com.bonuslord.hyper.model;

import java.util.ArrayList;
import java.util.List;

public class HZWorld {

	private HZActor player;
	private List<HZActor> actors = new ArrayList<>();
	
	public List<HZActor> getActors() {
		return actors;
	}
	
	public HZActor getPlayer() {
		return player;
	}
	
	public void setPlayer(HZActor player) {
		this.player = player;
	}
	
	public void addActor(HZActor actor) {
		actors.add(actor);
	}
	
	public void removeActor(HZActor actor) {
		actors.remove(actor);
	}
}
