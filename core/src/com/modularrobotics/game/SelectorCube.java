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
	public static Cube createSelectorCube(int initCubeSize) {
		cubeSize = initCubeSize;
		
		return createSelectorCube(Color.GREEN, Vector3.Zero);
	}
	
	public static Cube cycleMode(Cube oldSelectorCube) {
		Vector3 position = new Vector3();
		oldSelectorCube.transform.getTranslation(position);

		if (mode == PlacementMode.CUBE) {
			mode = PlacementMode.MODULE;
			return createSelectorCube(Color.RED, position);
		} else if (mode == PlacementMode.MODULE) {
			mode = PlacementMode.TARGET;
			return createSelectorCube(Color.BLUE, position);
		} else {
			mode = PlacementMode.CUBE;
			return createSelectorCube(Color.GREEN, position);
		}
	}
	
	public static Cube createSelectorCube(Color chosenColor, Vector3 position) {
		ModelBuilder modelBuilder = new ModelBuilder();
        Model model = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
                new Material(ColorAttribute.createDiffuse(chosenColor)),
                Usage.Position | Usage.Normal);
	
        Cube selectorCube  = new Cube(model, position.x, position.y, position.z);
        return selectorCube;
	}
}
