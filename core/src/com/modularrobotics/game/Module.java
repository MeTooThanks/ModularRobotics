package com.modularrobotics.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;


public class Module extends Cube {

	public ArrayList<Vector3> path;
	public ArrayList<Vector3> originalPath;
	Model modModel;
	public boolean isConstrained;
	
	    public Module(Model model, float x, float y, float z) {
	        super(model,x ,y, z);
	        modModel = model;
	    }
	    
	    public void setOriginalPath(ArrayList<Vector3> initOriginalPath) {
	    	originalPath = initOriginalPath;
	    }
	    
	    public void setPath(ArrayList<Vector3> initPath) {
	    	path = initPath;
	    }
}