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
	PerspectiveCamera camera;
	Vector3 position;
	int selecting;
	int selecting2;
	Material defaultMat;
	Material selectingMat;
	Plane plane;
	int cubeSize;
	
	public Logic(ArrayList<Cube> initEnvironment, PerspectiveCamera initCamera, int initCubeSize) {
		environment = initEnvironment;
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
		
		//moves green cube in steps of 5(cubeSize)
		float x = directionVector.x - directionVector.x % cubeSize;
		float y = getPlane(oldSelectorCube);
		float z = directionVector.z - directionVector.z % cubeSize;
		
		ModelBuilder modelBuilder = new ModelBuilder();
        Model model = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                Usage.Position | Usage.Normal);
        
		Cube selectorCube = new Cube(model, x, y, z);
		return selectorCube;
	}
	
	public void placeCube(Cube oldSelectorCube) {
		
		Vector3 position = new Vector3();
		oldSelectorCube.transform.getTranslation(position);

		
		ModelBuilder modelBuilder = new ModelBuilder();
	        Model model = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
	                new Material(ColorAttribute.createDiffuse(Color.GRAY)),
	                Usage.Position | Usage.Normal);
	        
	     environment.add(new Cube(model, position.x, position.y, position.z));
	}
	
	public void removeCube(int value) {
		environment.remove(value);
	}
	
	
	public float getPlane(Cube oldSelectorCube) {
		
		Vector3 position = new Vector3();
		oldSelectorCube.transform.getTranslation(position);
		
		Ray ray = new Ray(position, new Vector3(0, -1, 0));
		
		Vector3 positionEnvCube = new Vector3();
				
		float distance = -1;
		for(int i = environment.size() - 1; i >= 0; i--) {
			Cube temp = environment.get(i);
			temp.transform.getTranslation(positionEnvCube);
			positionEnvCube.add(temp.center);
			
			float dist2 = ray.origin.dst2(positionEnvCube);
			if (distance >= 0f && dist2 > distance)
				continue;
			if(Intersector.intersectRaySphere(ray, positionEnvCube, temp.radius, null)) {
				distance = dist2;
				return positionEnvCube.y + cubeSize;
			}	
		}
		
		return 0;
		
	}
}