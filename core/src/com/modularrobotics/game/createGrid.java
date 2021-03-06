package com.modularrobotics.game;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class createGrid {
	static Model model;
	  
    public static void createNewGrid(float width, float length, float cubeSize, ArrayList<Cube> container) {
      
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
                new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                Usage.Position | Usage.Normal);

        for(float w = 0; w < width*cubeSize; w = w + cubeSize){
            for (float l = 0; l < length*cubeSize; l = l + cubeSize) {
            	Vector3 cubePosition = new Vector3(l, 0, w);
                container.add(new Cube(model, cubePosition.x, cubePosition.y, cubePosition.z));
            }
        }
    }
}

