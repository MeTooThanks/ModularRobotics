package com.modularrobotics.game;


import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Cube extends ModelInstance {

    Vector3 center;
    BoundingBox bounds;
    Vector3 dimensions;
    float radius;

    public Cube(Model model, float x, float y, float z) {
        super(model,x ,y, z);

        center = new Vector3();
        bounds = new BoundingBox();
        dimensions = new Vector3();
        calculateBoundingBox(bounds);

        bounds.getCenter(center);
        bounds.getDimensions(dimensions);

        radius = dimensions.len()/2f;
    }
}