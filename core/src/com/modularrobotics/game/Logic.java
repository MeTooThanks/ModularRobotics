package com.modularrobotics.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.modularrobotics.game.AI.AiHandler;
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
	int selectingCube;
	int selectingTarget;
	int selectingModule;
	int cubeSize;
	Material defaultMatCube;
	Material defaultMatTarget;
	Material defaultMatModule;
	Material selectingMat;
	Plane plane;
	ObjectMode mode;
	AiHandler AI;
	
	public Logic(ArrayList<Cube> initEnvironment, ArrayList<Target> initTarget, ArrayList<Module> initModule, PerspectiveCamera initCamera, int initCubeSize) {
		environment = initEnvironment;
		targets = initTarget;
		modules = initModule;
		camera = initCamera;
		position = new Vector3();
		selecting = -1;
		selectingCube = -1;
		selectingTarget = -1;
		selectingModule = -1; 
		cubeSize = initCubeSize;
		defaultMatCube = new Material();
		defaultMatTarget = new Material();
		defaultMatModule = new Material();
		selectingMat = new Material();
		mode = ObjectMode.CUBE;
		
		selectingMat.set(ColorAttribute.createDiffuse(Color.ORANGE));
		defaultMatCube.set(ColorAttribute.createDiffuse(Color.GRAY));
		defaultMatTarget.set(ColorAttribute.createDiffuse(Color.BLUE));
		defaultMatModule.set(ColorAttribute.createDiffuse(Color.RED));
		
		Vector3 one = new Vector3(1, 0, 0);
		Vector3 two = new Vector3(0, 0, 1);
		Vector3 three = new Vector3(1, 0, 1);
		
		plane = new Plane(one, two, three);
	}
	
	public enum ObjectMode {
		MODULE, CUBE, TARGET;
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
				mode = ObjectMode.CUBE;
			}
		}
		
		for(int i = 0; i < targets.size(); i++) {
			Target temp = targets.get(i);
			temp.transform.getTranslation(position);
			position.add(temp.center);
			
			float dist2 = ray.origin.dst2(position);
			if (distance >= 0f && dist2 > distance)
				continue;
			if(Intersector.intersectRaySphere(ray, position, temp.radius, null)) {
				selecting = i;
				distance = dist2;
				mode = ObjectMode.TARGET;
			}
			
		}
		
		for(int i = 0; i < modules.size(); i++) {
			Module temp = modules.get(i);
			temp.transform.getTranslation(position);
			position.add(temp.center);
			
			float dist2 = ray.origin.dst2(position);
			if (distance >= 0f && dist2 > distance)
				continue;
			if(Intersector.intersectRaySphere(ray, position, temp.radius, null)) {
				selecting = i;
				distance = dist2;
				mode = ObjectMode.MODULE;
			}
		}
		return selecting;
	}


	public void setSelected(int value) {
		if (mode == ObjectMode.CUBE) {
			if (selectingCube == value) {
				return;
			}
			
			if (selectingCube >= 0 ) {
				Material mat = null;
				if (selectingCube >= 0) {
					mat = environment.get(selectingCube).materials.get(0);
					mat.clear();
					mat.set(defaultMatCube);
				}
				
				if (selectingTarget >= 0) {
					mat = targets.get(selectingTarget).materials.get(0);
					mat.clear();
					mat.set(defaultMatTarget);
				}
				
				if (selectingModule >= 0) {
					mat = modules.get(selectingModule).materials.get(0);
					mat.clear();
					mat.set(defaultMatModule);
				}
			}
			
			selectingCube = value;
			//selectingModule = -1;
			//selectingTarget = -1;
			
			if (selectingCube >= 0 && mode == ObjectMode.CUBE) {
				Material mat = environment.get(selectingCube).materials.get(0);
				
				defaultMatCube.clear();
				defaultMatCube.set(mat);
				mat.clear();
				mat.set(selectingMat);
				
				if (selectingTarget >= 0) {
					mat = targets.get(selectingTarget).materials.get(0);
					mat.clear();
					mat.set(defaultMatTarget);
				}
				
				if (selectingModule >= 0) {
					mat = modules.get(selectingModule).materials.get(0);
					mat.clear();
					mat.set(defaultMatModule);
				}
			}
		}
		
		if (mode == ObjectMode.TARGET) {
			if (selectingTarget == value) {
				return;
			}
			
			if (selectingTarget >= 0) {
				Material mat = null;
				if (selectingCube >= 0) {
					mat = environment.get(selectingCube).materials.get(0);
					mat.clear();
					mat.set(defaultMatCube);
				}
				
				if (selectingTarget >= 0) {
					mat = targets.get(selectingTarget).materials.get(0);
					mat.clear();
					mat.set(defaultMatTarget);
				}
				
				if (selectingModule >= 0) {
					mat = modules.get(selectingModule).materials.get(0);
					mat.clear();
					mat.set(defaultMatModule);
				}
			}
			
			selectingTarget = value;
			//selectingModule = -1;
		//	selectingCube = -1;
			
			if (selectingTarget >= 0 && mode == ObjectMode.TARGET) {
				Material mat = targets.get(selectingTarget).materials.get(0);
				
				defaultMatTarget.clear();
				defaultMatTarget.set(mat);
				mat.clear();
				mat.set(selectingMat);
				
				if (selectingCube >= 0) {
					mat = environment.get(selectingCube).materials.get(0);
					mat.clear();
					mat.set(defaultMatCube);
				}
				
				if (selectingModule >= 0) {
					mat = modules.get(selectingModule).materials.get(0);
					mat.clear();
					mat.set(defaultMatModule);
				}
			}
		}
		
		if (mode == ObjectMode.MODULE) {
			if (selectingModule == value) {
				return;
			}
			
			if (selectingModule >= 0) {
				Material mat = null;
				if (selectingCube >= 0) {
					mat = environment.get(selectingCube).materials.get(0);
					mat.clear();
					mat.set(defaultMatCube);
				}
				
				if (selectingTarget >= 0) {
					mat = targets.get(selectingTarget).materials.get(0);
					mat.clear();
					mat.set(defaultMatTarget);
				}
				
				if (selectingModule >= 0) {
					mat = modules.get(selectingModule).materials.get(0);
					mat.clear();
					mat.set(defaultMatModule);
				}
			}
			
			selectingModule = value;
			//selectingTarget = -1;
			//selectingCube = -1;
			
			if (selectingModule >= 0 && mode == ObjectMode.MODULE) {
				Material mat = modules.get(selectingModule).materials.get(0);
				
				defaultMatModule.clear();
				defaultMatModule.set(mat);
				mat.clear();
				mat.set(selectingMat);
				
				if (selectingCube >= 0) {
					mat = environment.get(selectingCube).materials.get(0);
					mat.clear();
					mat.set(defaultMatCube);
				}
				
				if (selectingTarget >= 0) {
					mat = targets.get(selectingTarget).materials.get(0);
					mat.clear();
					mat.set(defaultMatTarget);
				}
			}
		}
	}
	
	public Cube refreshSelector(Cube oldSelectorCube, float screenX, float screenY) {
		Ray ray = camera.getPickRay(screenX, screenY);
		Vector3 directionVector = new Vector3();
		Intersector.intersectRayPlane(ray, plane, directionVector);
		
		float x = directionVector.x - directionVector.x % cubeSize;
		float y = getPlane(oldSelectorCube);
		float z = directionVector.z - directionVector.z % cubeSize;
        
		Cube selectorCube = new Cube(oldSelectorCube.model, x, y, z);
		return selectorCube;
	}
	
	public void placeObject(Cube oldSelectorCube) {
		
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
	
	public void removeObject() {
			if (mode == ObjectMode.CUBE && selectingCube >= 0) {
				environment.remove(selectingCube);
				selectingCube = -1;
			}
			
			if (mode == ObjectMode.TARGET && selectingTarget >= 0) {
				targets.remove(selectingTarget);
				selectingTarget = -1;
			}
			
			if (mode == ObjectMode.MODULE && selectingModule >= 0) {
				modules.remove(selectingModule);
				selectingModule = -1;
			}
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
	
	public Model createModelWithColor(Color chosenColor) {
		ModelBuilder modelBuilder = new ModelBuilder();
        Model model = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
                new Material(ColorAttribute.createDiffuse(chosenColor)),
                Usage.Position | Usage.Normal);
        
        return model;
	}
	
	public void nextStep() {
		if (AI == null) {
			AI = new AiHandler(environment, modules, targets); 
		}
		AI.nextStep();
	}
}