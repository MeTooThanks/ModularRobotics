package com.modularrobotics.game.AI;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

public class PathfindingGraph {
	ArrayList<PathfindingNode> nodes;
	ArrayList<PathfindingNode> takenRegularNodes;
	ArrayList<PathfindingNode> takenTargetSeats;
	
	public PathfindingGraph() {
		nodes  = new ArrayList<PathfindingNode>();
		takenRegularNodes = new ArrayList<PathfindingNode>();
		takenTargetSeats = new ArrayList<PathfindingNode>();
	}
	
	public void add(PathfindingNode node) {
		nodes.add(node);
	}
	
	public PathfindingNode node(int i) {
		return nodes.get(i);
	}
	
	public PathfindingGraph createNewLayer() {
		return null;
	}
	
	public ArrayList<PathfindingNode> getSingleGraph() {
		return nodes;
	}
	
	//to be called ONCE at the initial creation of the graph
	//merges duplicate nodes and connects them to neighboring nodes
	public void connectNodes() {
		mergeNodes();
		
		for (PathfindingNode firstNode : nodes) {
			Vector3 firstNodePosition = new Vector3(firstNode.position.x, firstNode.position.y, firstNode.position.z);
			for (PathfindingNode secondNode : nodes) {
				firstNodePosition = new Vector3(firstNode.position.x, firstNode.position.y, firstNode.position.z);
				if (secondNode.hasPosition(firstNodePosition.add(new Vector3(0,0,5)))) {
					if (!firstNode.allNeighbors().contains(secondNode))
						firstNode.addNeighbor(secondNode);
					if (!secondNode.allNeighbors().contains(firstNode))
						secondNode.addNeighbor(firstNode);
				}
				firstNodePosition = new Vector3(firstNode.position.x, firstNode.position.y, firstNode.position.z);
				if (secondNode.hasPosition(firstNode.position().add(new Vector3(-5,0,0)))) {
					if (!firstNode.allNeighbors().contains(secondNode))
						firstNode.addNeighbor(secondNode);
					if (!secondNode.allNeighbors().contains(firstNode))
						secondNode.addNeighbor(firstNode);
				}
				firstNodePosition = new Vector3(firstNode.position.x, firstNode.position.y, firstNode.position.z);
				if (secondNode.hasPosition(firstNode.position().add(new Vector3(5,0,0)))) {
					if (!firstNode.allNeighbors().contains(secondNode))
						firstNode.addNeighbor(secondNode);
					if (!secondNode.allNeighbors().contains(firstNode))
						secondNode.addNeighbor(firstNode);
				}
				firstNodePosition = new Vector3(firstNode.position.x, firstNode.position.y, firstNode.position.z);
				if (secondNode.hasPosition(firstNode.position().add(new Vector3(0,0,-5)))) {
					if (!firstNode.allNeighbors().contains(secondNode))
						firstNode.addNeighbor(secondNode);
					if (!secondNode.allNeighbors().contains(firstNode))
						secondNode.addNeighbor(firstNode);
				}
				firstNodePosition = new Vector3(firstNode.position.x, firstNode.position.y, firstNode.position.z);
				if (secondNode.hasPosition(firstNode.position().add(new Vector3(0,5,5)))) {
					if (!firstNode.allNeighbors().contains(secondNode))
						firstNode.addNeighbor(secondNode);
					if (!secondNode.allNeighbors().contains(firstNode))
						secondNode.addNeighbor(firstNode);
				}
				firstNodePosition = new Vector3(firstNode.position.x, firstNode.position.y, firstNode.position.z);
				if (secondNode.hasPosition(firstNode.position().add(new Vector3(0,-5,0)))) {
					if (!firstNode.allNeighbors().contains(secondNode))
						firstNode.addNeighbor(secondNode);
					if (!secondNode.allNeighbors().contains(firstNode))
						secondNode.addNeighbor(firstNode);
				}
			}
		}
		
		for (PathfindingNode node : nodes) {
			Vector3 posOfThisNode = new Vector3(node.position.x, node.position.y, node.position.z);
			for (int i = 0; i < node.allNeighbors().size(); i++) {
				PathfindingNode neighbor = node.allNeighbors().get(i);
				Vector3 posOfOtherNode = new Vector3(neighbor.position.x, neighbor.position.y, neighbor.position.z);
				posOfThisNode.sub(posOfOtherNode);
				if (posOfThisNode.y == 5) {
					deleteAllNeighborsWithoutNodeUnder(node);
					break;
				}
			}
		}
	} 
	
	public void deleteAllNeighborsWithoutNodeUnder(PathfindingNode nodeToClean) {
		ArrayList<PathfindingNode> levitatingNeighbors = new ArrayList<PathfindingNode>();
		for (PathfindingNode node : nodeToClean.allNeighbors()) {
			
			Vector3 posOfThisNode = new Vector3(node.position.x, node.position.y, node.position.z);
			for (PathfindingNode nodeNeighbors : node.allNeighbors()) {
				Vector3 posOfOtherNode = new Vector3(nodeNeighbors.position.x, nodeNeighbors.position.y, nodeNeighbors.position.z);
				posOfThisNode.sub(posOfOtherNode);
				if (posOfThisNode.y == 5) {
					levitatingNeighbors.add(node);
					break;
				}
			}
		}
		
		levitatingNeighbors.forEach(node -> {
			nodeToClean.neighbors.remove(node);
		});
	}
	
	public boolean isNodeInList(ArrayList<PathfindingNode> list, PathfindingNode nodeToCheck) {
		if (list.size() > 0)
			for (PathfindingNode node : list) {
				if (node.hasPosition(nodeToCheck.position))
					return true;
			}
		return false;
	}
	
	private void mergeNodes() {
		ArrayList<PathfindingNode> toKeep = new ArrayList<PathfindingNode>();

		System.out.println("nodes size before: " +toKeep.size());
		for (int i = 0; i < nodes.size(); i++) {
			if (!isNodeInList(toKeep, nodes.get(i))) {
				toKeep.add(nodes.get(i));
			}
		}
		System.out.println("nodes size after: " +toKeep.size());
		nodes = toKeep;
	}
}
