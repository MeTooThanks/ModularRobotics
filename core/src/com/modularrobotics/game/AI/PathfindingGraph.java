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
			for (PathfindingNode secondNode : nodes) {
				if (secondNode.hasPosition(firstNode.position().add(new Vector3(0,0,1)))) {
					if (!firstNode.allNeighbors().contains(secondNode))
						firstNode.addNeighbor(secondNode);
					if (!secondNode.allNeighbors().contains(firstNode))
						secondNode.addNeighbor(firstNode);
				}
				if (secondNode.hasPosition(firstNode.position().add(new Vector3(-1,0,0)))) {
					if (!firstNode.allNeighbors().contains(secondNode))
						firstNode.addNeighbor(secondNode);
					if (!secondNode.allNeighbors().contains(firstNode))
						secondNode.addNeighbor(firstNode);
				}
				if (secondNode.hasPosition(firstNode.position().add(new Vector3(1,0,0)))) {
					if (!firstNode.allNeighbors().contains(secondNode))
						firstNode.addNeighbor(secondNode);
					if (!secondNode.allNeighbors().contains(firstNode))
						secondNode.addNeighbor(firstNode);
				}
				if (secondNode.hasPosition(firstNode.position().add(new Vector3(0,0,-1)))) {
					if (!firstNode.allNeighbors().contains(secondNode))
						firstNode.addNeighbor(secondNode);
					if (!secondNode.allNeighbors().contains(firstNode))
						secondNode.addNeighbor(firstNode);
				}
				if (secondNode.hasPosition(firstNode.position().add(new Vector3(0,1,1)))) {
					if (!firstNode.allNeighbors().contains(secondNode))
						firstNode.addNeighbor(secondNode);
					if (!secondNode.allNeighbors().contains(firstNode))
						secondNode.addNeighbor(firstNode);
				}
			}
		}
	}
	
	private void mergeNodes() {
		ArrayList<PathfindingNode> toRemove = new ArrayList<PathfindingNode>();
		nodes.forEach(node -> {
			nodes.forEach(node2 -> {
				if (node.equals(node2)) {
					toRemove.add(node2);
				}
			});
		});
		toRemove.forEach(removeNode -> nodes.remove(removeNode));
	}
}
