package com.modularrobotics.game.AI;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;
import com.modularrobotics.game.Cube;
import com.modularrobotics.game.Module;
import com.modularrobotics.game.Target;

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
		dijkstra = new Dijkstra();
	}
	
	//initially, ALL modules calculate their adjacent nodes for the graph!
	//other modules are NOT obstacles! They get removed in the next step!
	//Important!!
	public void constructGraph() {
		singleGraphLayer = pgLayers.layers.get(0);
		ArrayList<Vector3> possibleNeighbors = new ArrayList<Vector3>();
		possibleNeighbors.add(new Vector3(0,0,1));
		possibleNeighbors.add(new Vector3(0,0,-1));
		possibleNeighbors.add(new Vector3(-1,0,0));
		possibleNeighbors.add(new Vector3(1,0,0));
		possibleNeighbors.add(new Vector3(0,1,0));
		possibleNeighbors.add(new Vector3(1,0,1));
		possibleNeighbors.add(new Vector3(1,0,-1));
		possibleNeighbors.add(new Vector3(-1,0,-1));
		possibleNeighbors.add(new Vector3(-1,0,1));
		possibleNeighbors.add(new Vector3(0,1,1));
		possibleNeighbors.add(new Vector3(1,1,0));
		possibleNeighbors.add(new Vector3(0,1,-1));
		possibleNeighbors.add(new Vector3(-1,1,0));
		
		ArrayList<PathfindingNode> nodes = new ArrayList<PathfindingNode>();
		//checks if adjacent nodes are unoccupied
		for (Cube env : environment) {
			for (Vector3 vector : possibleNeighbors) {
				Vector3 envPosition = new Vector3();
				env.transform.getTranslation(envPosition);
				envPosition.add(vector);
				nodes.add(new PathfindingNode(envPosition.x, envPosition.y, envPosition.z, 0));
				for (Cube cubes : environment) {
					Vector3 position = new Vector3();
					cubes.transform.translate(position);
					if (position.epsilonEquals(envPosition, 0)) {
					nodes.remove(nodes.size()-1);
					}
				}
			}
		}
		for (Module mod : modules) {
			for (Vector3 vector : possibleNeighbors) {
				Vector3 modPosition = new Vector3();
				mod.transform.getTranslation(modPosition);
				modPosition.add(vector);
				nodes.add(new PathfindingNode(modPosition.x, modPosition.y, modPosition.z, 0));
				for (Cube cubes : environment) {
					Vector3 position = new Vector3();
					cubes.transform.translate(position);
					if (position.epsilonEquals(modPosition, 0)) {
						nodes.remove(nodes.size()-1);
					}
				}
			}
		}
		for (Target target : targets) {
			Vector3 targetPosition = new Vector3();
			target.transform.getTranslation(targetPosition);
			nodes.add(new PathfindingNode(targetPosition.x, targetPosition.y, targetPosition.z, 0));
		}
		
		pgLayers.layers.get(0).connectNodes();
	}
	
	
	/*
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
	*/
}
