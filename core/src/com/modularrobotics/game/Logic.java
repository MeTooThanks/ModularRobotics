package com.modularrobotics.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;


public class Logic {
	
	ArrayList<Cube> environment;
	ArrayList<Target> targets;
	ArrayList<Module> modules;
	PerspectiveCamera camera;
	Vector3 position;
	int selecting;
	int selecting2;
	Material defaultMat;
	Material selectingMat;
	Plane plane;
	int cubeSize;
	
	public Logic(ArrayList<Cube> initEnvironment, ArrayList<Target> initTarget, ArrayList<Module> initModule, PerspectiveCamera initCamera, int initCubeSize) {
		environment = initEnvironment;
		targets = initTarget;
		modules = initModule;
		camera = initCamera;
		cubeSize = initCubeSize;
		position = new Vector3();
		selecting = -1;
		selecting2 = -1;
		defaultMat = new Material();
		selectingMat = new Material();
		
		selectingMat.set(ColorAttribute.createDiffuse(Color.ORANGE));
		defaultMat.set(ColorAttribute.createDiffuse(Color.GRAY));
		
		Vector3 one = new Vector3(1, 0, 0);
		Vector3 two = new Vector3(0, 0, 1);
		Vector3 three = new Vector3(1, 0, 1);
		
		plane = new Plane(one, two, three);


	}
	
	public int getObject(float screenX, float screenY) {
		Ray ray = camera.getPickRay(screenX, screenY);
		selecting = -1;
		float distance = -1;
		
		for(int i = 0; i < environment.size(); i++) {
			Cube temp = environment.get(i);
			temp.transform.getTranslation(position);
			position.add(temp.center);
			
			float dist2 = ray.origin.dst2(position);
			if (distance >= 0f && dist2 > distance)
				continue;
			if(Intersector.intersectRaySphere(ray, position, temp.radius, null)) {
				selecting = i;
				distance = dist2;
			}
		}	
		return selecting;
	}


	public void setSelected(int value) {
		if (selecting2 == value)
			return;
		
		if (selecting2 >= 0) {
			Material mat = environment.get(selecting2).materials.get(0);
			mat.clear();
			mat.set(defaultMat);
		}
		
		selecting2 = value;
		
		if (selecting2 >= 0) {
			Material mat = environment.get(selecting2).materials.get(0);
			defaultMat.clear();
			defaultMat.set(mat);
			mat.clear();
			mat.set(selectingMat);
		}
	}
	
	public Cube refreshSelector(Cube oldSelectorCube, float screenX, float screenY) {
		Ray ray = camera.getPickRay(screenX, screenY);
		Vector3 directionVector = new Vector3();
		Intersector.intersectRayPlane(ray, plane, directionVector);
		
		float x = directionVector.x - directionVector.x % cubeSize;
		float y = getPlane(oldSelectorCube);
		float z = directionVector.z - directionVector.z % cubeSize;
        
		Cube selectorCube = new Cube(SelectorCube.model, x, y, z);
		return selectorCube;
	}
	
	public void placeCube(Cube oldSelectorCube) {
		
		Vector3 position = new Vector3();
		oldSelectorCube.transform.getTranslation(position);

		if (SelectorCube.mode == SelectorCube.PlacementMode.CUBE) {
			
			ModelBuilder modelBuilder = new ModelBuilder();
	        Model model = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
	                new Material(ColorAttribute.createDiffuse(Color.GRAY)),
	                Usage.Position | Usage.Normal);
	    
	        
	     environment.add(new Cube(model, position.x, position.y, position.z));
		}
		
		if (SelectorCube.mode == SelectorCube.PlacementMode.TARGET) {
			
			ModelBuilder modelBuilder = new ModelBuilder();
	        Model model = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
	                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
	                Usage.Position | Usage.Normal);
	    
	        
	        targets.add(new Target(model, position.x, position.y, position.z));
	     
		}
		
		if (SelectorCube.mode == SelectorCube.PlacementMode.MODULE) {
			
				ModelBuilder modelBuilder = new ModelBuilder();
		        Model model = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
		                new Material(ColorAttribute.createDiffuse(Color.RED)),
		                Usage.Position | Usage.Normal);
		    
		        
		     modules.add(new Module(model, position.x, position.y, position.z));
			}
	}
	
	public void removeCube(int value) {
		environment.remove(value);
	}
	
	
	public float getPlane(Cube oldSelectorCube) {
		
		Vector3 position = new Vector3();
		oldSelectorCube.transform.getTranslation(position);
		
		Ray ray = new Ray(position, new Vector3(0, -1, 0));
		
		Vector3 positionObj = new Vector3();
		float height = 0;	
		float distance = -1;
		for(int i = environment.size() - 1; i >= 0; i--) {
			Cube temp = environment.get(i);
			temp.transform.getTranslation(positionObj);
			positionObj.add(temp.center);
			
			float dist2 = ray.origin.dst2(positionObj);
			if (distance >= 0f && dist2 > distance)
				continue;
			if(Intersector.intersectRaySphere(ray, positionObj, temp.radius, null)) {
				distance = dist2;
				height = positionObj.y + cubeSize;
			}	
		}
		for(int i = targets.size() - 1; i >= 0; i--) {
			Target temp = targets.get(i);
			temp.transform.getTranslation(positionObj);
			positionObj.add(temp.center);
			
			float dist2 = ray.origin.dst2(positionObj);
			if (distance >= 0f && dist2 > distance)
				continue;
			if(Intersector.intersectRaySphere(ray, positionObj, temp.radius, null)) {
				distance = dist2;
				if (positionObj.y + cubeSize > height)
					height = positionObj.y + cubeSize;
			}	
		}
		
		for(int i = modules.size() - 1; i >= 0; i--) {
			Module temp = modules.get(i);
			temp.transform.getTranslation(positionObj);
			positionObj.add(temp.center);
			
			float dist2 = ray.origin.dst2(positionObj);
			if (distance >= 0f && dist2 > distance)
				continue;
			if(Intersector.intersectRaySphere(ray, positionObj, temp.radius, null)) {
				distance = dist2;
				if (positionObj.y + cubeSize > height)
					height = positionObj.y + cubeSize;
			}	
		}
		
		return height;
		
	}
	
}