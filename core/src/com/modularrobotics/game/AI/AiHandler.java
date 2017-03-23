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
	ArrayList<Vector3> allPaths;

	//needs to fetch all nodes
	public AiHandler(ArrayList<Cube> initEnvironment,
			ArrayList<Module> initModules,
			ArrayList<Target> initTargets) {
		environment = initEnvironment;
		modules = initModules;
		targets = initTargets;
		ProcedureProcessor.createProcedureProcessing(modules, targets);
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
		possibleNeighbors.add(new Vector3(0,0,5));
		possibleNeighbors.add(new Vector3(0,0,-5));
		possibleNeighbors.add(new Vector3(-5,0,0));
		possibleNeighbors.add(new Vector3(5,0,0));
		possibleNeighbors.add(new Vector3(0,5,0));
		//possibleNeighbors.add(new Vector3(5,0,5));
		//possibleNeighbors.add(new Vector3(5,0,-5));
		//possibleNeighbors.add(new Vector3(-5,0,-5));
		//possibleNeighbors.add(new Vector3(-5,0,5));
		possibleNeighbors.add(new Vector3(0,5,5));
		possibleNeighbors.add(new Vector3(5,5,0));
		possibleNeighbors.add(new Vector3(0,5,-5));
		possibleNeighbors.add(new Vector3(-5,5,0));
		
		ArrayList<PathfindingNode> nodes = new ArrayList<PathfindingNode>();
		//checks if adjacent nodes are unoccupied
		for (Cube env : environment) {
			for (Vector3 vector : possibleNeighbors) {
				Vector3 envPosition = new Vector3();
				env.transform.getTranslation(envPosition);
				//System.out.println("pos: " +envPosition.toString());
				envPosition.add(vector);

				//System.out.println("pos after: " +envPosition.toString());
				nodes.add(new PathfindingNode(envPosition.x, envPosition.y, envPosition.z, 0));
			}
		}
		for (Cube env : environment) {
			Vector3 envPos = new Vector3(env.position.x, env.position.y, env.position.z);
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).hasPosition(envPos)) {
					PathfindingNode nodeToRemove = nodes.get(i);
					nodes.remove(nodeToRemove);
					i--;
				}
			}
		}
		System.out.println(nodes.size());
		for (Module mod : modules) {
				Vector3 modPosition = new Vector3();
				mod.transform.getTranslation(modPosition);
				nodes.add(new PathfindingNode(modPosition.x, modPosition.y, modPosition.z, 0));
				/*
				for (Cube cubes : environment) {
					Vector3 position = new Vector3();
					cubes.transform.translate(position);
					if (position.sub(modPosition).len() == 1) {
						System.out.println("f: "+nodes.size());
						nodes.remove(nodes.size()-1);
					}
				}*/
			
		}
		//System.out.println(nodes.size());
		for (Target target : targets) {
			Vector3 targetPosition = new Vector3();
			target.transform.getTranslation(targetPosition);
			PathfindingNode targetNode = new PathfindingNode(targetPosition.x, targetPosition.y, targetPosition.z, 0);
			targetNode.isTargetSeat = true;
			nodes.add(targetNode);
		}
		//System.out.println(nodes.size());
		
		for (PathfindingNode node : nodes) {
			pgLayers.layers.get(0).add(node);
		}
		pgLayers.layers.get(0).connectNodes();
	}
	
	public void nextStep() {
		modules.forEach(module -> {
			module.checkConstrainedness(modules, allPaths);
		});
		ArrayList<PathfindingNode> pathNodes = new ArrayList<PathfindingNode>();
		ProcedureProcessor.createPairing();
		ArrayList<Target> targetsToBeProcessed = ProcedureProcessor.targetsInOrder;
		ArrayList<Module> modulesToBeProcessed = ProcedureProcessor.modulesInOrder;
		if (targetsToBeProcessed.size() > 0) {
		System.out.println("mTBP: " +modulesToBeProcessed.get(0).position);
		
		for (int i = 0; i < targetsToBeProcessed.size(); i++) {
			PathfindingNode targetNode = null;
			PathfindingNode moduleNode = null;
			for (PathfindingNode node : pgLayers.layers.get(0).nodes) {
				//System.out.println("HAS POSISHUN: " +node.position.toString());
				System.out.println("node: " +node.position);
				if (node.hasPosition(modulesToBeProcessed.get(i).position)) {
					System.out.println("node: " +node.position);
					moduleNode = node;
					break;
				}
			}
			for (PathfindingNode node : pgLayers.layers.get(0).nodes) {
				if (node.hasPosition(targetsToBeProcessed.get(i).position)) {
				//	System.out.println("HAS POSISHUN DUUUUH");
					targetNode = node;
					break;
				}
			}
			pathNodes = dijkstra.getPath(moduleNode, targetNode, pgLayers);
			
			ArrayList<Vector3> pathInVectors = new ArrayList<Vector3>();
			if (pathNodes != null) {
				for (PathfindingNode node : pathNodes) {
					pathInVectors.add(node.position);
				}
				modulesToBeProcessed.get(i).setPath(pathInVectors);
			} else
				modulesToBeProcessed.get(i).setPath(null);
		}
		
		}
		advancePosition();
		if (targetsToBeProcessed.size() > 0)
				pgLayers.project(pathNodes);
		//TO DO: RECOMPRESS PATH
	}
	
	public void advancePosition() {
		/*System.out.println("IM IN");
		for (int k = 0; k < modules.get(0).path.size(); k++) {
			System.out.println("path now: " +modules.get(0).path.get(k).toString());
		}*/
		for (int i = 0; i < modules.size(); i++) {
			if (modules.get(i).path != null && modules.get(i).path.size() > 0) {
				Vector3 newPosition = modules.get(i).path.get(0);
				ArrayList<Vector3> reducedPath = new ArrayList<Vector3>();
				for (int k = 1; k < modules.get(i).path.size(); k++) {
					reducedPath.add(modules.get(i).path.get(k));
				}
				Module tempMod = new Module(modules.get(i).model, newPosition.x, newPosition.y, newPosition.z);
				tempMod.isConstrained = modules.get(i).isConstrained;
				tempMod.setPath(reducedPath);
				modules.set(i, tempMod);
			}
		}
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
