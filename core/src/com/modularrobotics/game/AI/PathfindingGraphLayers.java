package com.modularrobotics.game.AI;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

public class PathfindingGraphLayers {
	ArrayList<PathfindingGraph> layers;
	
	public PathfindingGraphLayers() {
		layers = new ArrayList<PathfindingGraph>();
		layers.add(new PathfindingGraph());
	}
	
	public void resetDistances() {
		layers.forEach(layer -> {
			layer.nodes.forEach(node -> node.setDistance(0));
		});
	}
}