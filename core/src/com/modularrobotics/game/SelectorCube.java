package com.modularrobotics.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class SelectorCube {
	
	public static enum PlacementMode {
		MODULE, CUBE, TARGET;
	}
	
	static PlacementMode mode = PlacementMode.CUBE;
	static float cubeSize = 5;
	
	static ModelBuilder modelBuilder = new ModelBuilder();
	static Model model = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
            new Material(ColorAttribute.createDiffuse(Color.GREEN)),
            Usage.Position | Usage.Normal);
	
	public static Cube createSelectorCube(int cubeSize) {
		
		ModelBuilder modelBuilder = new ModelBuilder();
        Model model = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                Usage.Position | Usage.Normal);
        
		Cube initSelectorCube = new Cube(model, 0, 0, 0);
		
		return initSelectorCube;
	}
	
	public static Cube cycleMode(Cube oldSelectorCube) {
		
		Vector3 position = new Vector3();
		oldSelectorCube.transform.getTranslation(position);
		float cubeSize = oldSelectorCube.bounds.getHeight();

		if (mode == PlacementMode.CUBE) {
			mode = PlacementMode.MODULE;
			
			
			ModelBuilder modelBuilder = new ModelBuilder();
	        model = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
	                new Material(ColorAttribute.createDiffuse(Color.RED)),
	                Usage.Position | Usage.Normal);
	        
			Cube selectorCube  = new Cube(model, position.x, position.y, position.z);
			return selectorCube;
			
		}
		if (mode == PlacementMode.MODULE) {
			mode = PlacementMode.TARGET;
			
			ModelBuilder modelBuilder = new ModelBuilder();
	        model = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
	                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
	                Usage.Position | Usage.Normal);
	        
			Cube selectorCube  = new Cube(model, position.x, position.y, position.z);
			return selectorCube;
			
		}
		if (mode == PlacementMode.TARGET) {
			mode = PlacementMode.CUBE;
			
			ModelBuilder modelBuilder = new ModelBuilder();
	        model = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
	                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
	                Usage.Position | Usage.Normal);
	        
			Cube selectorCube  = new Cube(model, position.x, position.y, position.z);
			return selectorCube;
			
		}
		
		return null;
		
	}
}
