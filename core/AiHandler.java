package com.modularrobotics.game;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

public class AiHandler implements AiHandlerInterface{
	PathfindingGraphLayers pgLayers;
	PathfindingGraph singleGraphLayer;
	Dijkstra dijkstra;
	ArrayList<Cube> environment;
	ArrayList<Target> targets;
	ArrayList<Module> modules;

	//needs to fetch all nodes
	public AiHandler(ArrayList<Cube> environment,
			ArrayList<Module> modules,
			ArrayList<Target> targets) {
		pgLayers = new PathfindingGraphLayers();
		constructGraph();
	}
	
	//initially, ALL modules calculate their adjacent nodes for the graph!
	//other modules are NOT obstacles! They get removed in the next step!
	//Important!!
	public void constructGraph() {
		singleGraphLayer = pgLayers.layers.get(0);
		
		//add nodes here
		
		pgLayers.layers.get(0).connectNodes();
	}
	
	public ArrayList<PathfindingNode> pollPath(PathfindingNode start, PathfindingNode target) {
		dijkstra = new Dijkstra();
		pgLayers.resetDistances();
		
		ArrayList<Vector3> unconstrainedModuleNodes = getUnconstrainedModuleNodes();
		
		pgLayers.removeUnconstrainedModuleNodes(unconstrainedModuleNodes);
		ArrayList<PathfindingNode> path = new ArrayList<PathfindingNode>();
		path = dijkstra.getPath(start, target, pgLayers);
		pgLayers.project(path);
		return path;
	}
	
	public ArrayList<Vector3> getUnconstrainedModuleNodes() {
		ArrayList<Vector3> unconstrainedModuleNodes = new ArrayList<Vector3>();
		for (Module module : modules) {
			if (!module.constrained) {
				//add unoccupied neighboring nodes here
			}
		}
		
		return unconstrainedModuleNodes;
	}
}
