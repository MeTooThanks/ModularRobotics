package com.modularrobotics.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;


public class Target extends Cube {
	
		int openConnections;
		
	    public Target(Model model, float x, float y, float z) {
	        super(model,x ,y, z);
	    }
	    
	    public void calculateOpenConnections(ArrayList<Vector3> allLocations) {
	    	allLocations.forEach(loc -> {
	    		Vector3 delta = position.sub(loc);
	    		if (delta.len() <= 1) {
	    			openConnections++;
	    		}
	    	});
	    	
	    }
	    
	    public int getOpenConnections() {
	    	return openConnections;
	    }
}