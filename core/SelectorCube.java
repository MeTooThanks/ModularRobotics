package com.modularrobotics.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class SelectorCube {
	
	public static Cube createSelectorCube(int cubeSize) {
	
		
		ModelBuilder modelBuilder = new ModelBuilder();
        Model model = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                Usage.Position | Usage.Normal);
        
		Cube initSelectorCube = new Cube(model, 0, 0, 0);
		
		return initSelectorCube;
	}
}
