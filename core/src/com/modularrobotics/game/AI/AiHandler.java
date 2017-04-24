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
		ProcedureProcessor.createProcedureProcessing(modules, initTargets);
		pgLayers = new PathfindingGraphLayers();
		constructGraph();
		dijkstra = new Dijkstra();
	}
	
	public void constructGraph() {
		singleGraphLayer = pgLayers.layers.get(0);
		ArrayList<Vector3> possibleNeighbors = new ArrayList<Vector3>();
		possibleNeighbors.add(new Vector3(0,0,5));
		possibleNeighbors.add(new Vector3(0,0,-5));
		possibleNeighbors.add(new Vector3(-5,0,0));
		possibleNeighbors.add(new Vector3(5,0,0));
		possibleNeighbors.add(new Vector3(0,5,0));
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
				envPosition.add(vector);
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
		}

		for (Target target : targets) {
			Vector3 targetPosition = new Vector3();
			target.transform.getTranslation(targetPosition);
			PathfindingNode targetNode = new PathfindingNode(targetPosition.x, targetPosition.y, targetPosition.z, 0);
			targetNode.isTargetSeat = true;
			nodes.add(targetNode);
		}
		
		for (PathfindingNode node : nodes) {
			pgLayers.layers.get(0).add(node);
		}
		pgLayers.layers.get(0).connectNodes();
	}
	
	public void nextStep() {
		ArrayList<PathfindingNode> pathNodes = new ArrayList<PathfindingNode>();
		ProcedureProcessor.createPairing();
		ArrayList<Target> targetsToBeProcessed = ProcedureProcessor.targetsInOrder;
		ArrayList<Module> modulesToBeProcessed = ProcedureProcessor.modulesInOrder;
		if (targetsToBeProcessed.size() > 0) {
		for (int i = 0; i < targetsToBeProcessed.size(); i++) {
			PathfindingNode targetNode = null;
			PathfindingNode moduleNode = null;
			for (PathfindingNode node : pgLayers.layers.get(0).nodes) {
				if (node.hasPosition(modulesToBeProcessed.get(i).position)) {
					moduleNode = node;
					break;
				}
			}
			for (PathfindingNode node : pgLayers.layers.get(0).nodes) {
				if (node.hasPosition(targetsToBeProcessed.get(i).position)) {
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
				modulesToBeProcessed.get(i).setOriginalPath(pathInVectors);
			} else
				modulesToBeProcessed.get(i).setPath(null);
		}
		
		}
		advancePosition();
	}
	
	public void advancePosition() {
		ArrayList<Vector3> busyPositions = new ArrayList<Vector3>();
		for (int i = 0; i < modules.size(); i++) {
			if (modules.get(i).path != null && modules.get(i).path.size() > 0) {
				Vector3 newPosition = modules.get(i).path.get(0);
				
				if (noModuleThere(newPosition, busyPositions)) {
					busyPositions.add(newPosition);
					modules.get(i).path.remove(0);
					
					Module tempMod = new Module(modules.get(i).model, newPosition.x, newPosition.y, newPosition.z);
					tempMod.setPath(modules.get(i).path);
					modules.set(i, tempMod);
				}
			}
		}
	}
	public boolean noModuleThere(Vector3 newPosition, ArrayList<Vector3> busyPositions) {
		for (int i = 0; i < busyPositions.size(); i++) {
			Vector3 currentBusyNode = busyPositions.get(i);
			if (newPosition.x == currentBusyNode.x
					&& newPosition.y == currentBusyNode.y
					&& newPosition.z == currentBusyNode.z)
				return false;
		}
		for (int i = 0; i < modules.size(); i++) {
			Vector3 tempPos = new Vector3();
			modules.get(i).transform.getTranslation(tempPos);
			if (tempPos == newPosition)
				return false;
		}
		return true;
	}
}
