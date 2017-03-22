package com.modularrobotics.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;


public class Module extends Cube {

	ArrayList<Vector3> path;
	Model modModel;
	public boolean isConstrained;
	
	    public Module(Model model, float x, float y, float z) {
	        super(model,x ,y, z);
	        modModel = model;
	    }
	    
	    public boolean checkConstrainedness(ArrayList<Module> allModules, ArrayList<Vector3> allPaths) {
	    	isConstrained = false;
	    	allModules.forEach(module -> {
	    		Vector3 delta = module.position.sub(this.position);
	    		if (delta.x == 0 && delta.y == 1 && delta.z == 0) {
	    			isConstrained = true;
	    		}
	    	});
	    	if (isConstrained == false) {
	    		allPaths.forEach(pathPoint -> {
	    			Vector3 delta = pathPoint.sub(position);
	    			if (delta.len() == 1 && delta.y != -1) {
	    				isConstrained = true;
	    			}
	    		});
	    	}
	    	return isConstrained;
	    }
	    
	    public void setPath(ArrayList<Vector3> initPath) {
	    	path = initPath;
	    }
}