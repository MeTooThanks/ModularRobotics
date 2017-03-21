package com.modularrobotics.game;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

public class PathfindingGraphLayers {
	ArrayList<PathfindingGraph> layers;
	
	public PathfindingGraphLayers() {
		layers = new ArrayList<PathfindingGraph>();
		layers.add(new PathfindingGraph());
	}
	
	public void cloneTopLayer() {
		//clones the nodes
		PathfindingGraph topLayer = layers.get(layers.size()-1);
		ArrayList<PathfindingNode> clonedNodes = new ArrayList<PathfindingNode>();
		topLayer.nodes.forEach(nodeToBeCloned -> {
			clonedNodes.add(nodeToBeCloned.cloneNode());
		});
		
		//clones the neighbor structure
		for (int i = 0; i < clonedNodes.size(); i++) {
			for (int k = 0; k < topLayer.nodes.get(i).allNeighbors().size(); k++) {
				clonedNodes.get(i).addNeighbor(
						clonedNodes.get(
								topLayer.nodes.indexOf(
										topLayer.nodes.get(i).allNeighbors().get(k))));
			}
		}
		
		//connects the old top layer with the new top layer
		for (int i = 0; i < topLayer.nodes.size(); i++) {
			topLayer.nodes.get(i).addNeighbor(clonedNodes.get(i));
		}
		
		//write the new layer into the data structure
		PathfindingGraph newLayer = new PathfindingGraph();
		newLayer.nodes = clonedNodes;
		layers.add(newLayer);
	}
	
	//projects the path into next layers
	//!!!!!
	//to be called IMMEDIATELY after a path for a module has been calculated!!
	//!!!!!
	public void project(ArrayList<PathfindingNode> path) {
		ArrayList<PathfindingNode> projectedPath = new ArrayList<PathfindingNode>();
		for (int i = 0; i < path.size(); i++) {
			projectedPath.add(path.get(i));
			//ends if next node is target (circumvents ugly checks for i being in scope)
			if (path.get(i+1).isTargetSeat) {
				projectedPath.add(path.get(i+1));
				break;
			}
			//projects if module didn't decide to wait at that node
			if (!(path.get(i).layer+1 == path.get(i+1).layer)) {
				int layerOfNodeToBeProjected = path.get(i).layer;
				int indexOfNodeToBeProjected = layers.get(layerOfNodeToBeProjected).nodes.indexOf(path.get(i));
				
				if (layerOfNodeToBeProjected+1 < layers.size()) {
					projectedPath.add(layers.get(layerOfNodeToBeProjected+1).nodes.get(indexOfNodeToBeProjected));
				} else {
					cloneTopLayer();
					projectedPath.add(layers.get(layerOfNodeToBeProjected+1).nodes.get(indexOfNodeToBeProjected));
				}
			}
		}
		
		deleteBusyNodes(projectedPath);
	}
	
	//deletes busy nodes from the graph (every node in projectedPath with an odd index or index 0)
	//detailing why this is the case is out of the scope of the commentary here
	public void deleteBusyNodes(ArrayList<PathfindingNode> projectedPath) {
		int layerOfCurrentNode = -1;
		
		for (int i = 0; i < projectedPath.size()-1; i++) {
			if ((i & 1) == 0) {
				PathfindingNode currentNode = projectedPath.get(i);
				layerOfCurrentNode = currentNode.layer;
				layers.get(layerOfCurrentNode).nodes.forEach(node -> {
					if (node.allNeighbors().contains(currentNode)) {
						node.allNeighbors().remove(currentNode);
					}
				});
				if (layerOfCurrentNode > 0) {
					layers.get(layerOfCurrentNode-1).nodes.forEach(node -> {
						if (node.allNeighbors().contains(currentNode)) {
							node.allNeighbors().remove(currentNode);
						}
					});
				}
				layers.get(layerOfCurrentNode).nodes.remove(currentNode);
			}
		}
		//deletes Target node from top layer and second top layer
		int layer = layers.size()-1;
		PathfindingNode targetNode = projectedPath.get(projectedPath.size()-1);
		Vector3 targetNodePosition = targetNode.position();
		
		for (PathfindingNode node : layers.get(layer-1).nodes) {
			if (node.hasPosition(targetNodePosition)) {
				targetNode = node;
				break;
			}
		}
		
		for (PathfindingNode node : layers.get(layer).nodes) {
			if (node.hasPosition(targetNodePosition)) {
				targetNode = node;
				break;
			}
		}
		
		for (int node = 0; node < layers.get(layer).nodes.size(); node++) {
			PathfindingNode currentNode = layers.get(layer).nodes.get(node);
			if (currentNode.allNeighbors().contains(targetNode)) {
				currentNode.allNeighbors().remove(targetNode);
			}
		}
		if (layers.get(layer).nodes.contains(targetNode))
			layers.get(layer).nodes.remove(targetNode);
	}
}
