package com.modularrobotics.game.AI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {
	PriorityQueue<PathfindingNode> pq;
	PathfindingNode start, target;
	ArrayList<PathfindingNode> possibleTargets;
	ArrayList<PathfindingNode> graph;
	
	public ArrayList<PathfindingNode> getPath(PathfindingNode initStart,
					PathfindingNode initTarget,
					PathfindingGraphLayers initGraph) {
		start = initStart;
		target = initTarget;
		start.cumulatedDistance = 0;
		ArrayList<PathfindingNode> graphNodes = new ArrayList<PathfindingNode>();
		
		for (PathfindingGraph layer : initGraph.layers) {
			for (PathfindingNode nodes : layer.nodes) {
				graphNodes.add(nodes);
			}
		}
		graph = graphNodes;
		System.out.println("graph size: " +graph.size());
		possibleTargets = new ArrayList<PathfindingNode>();
		for (PathfindingNode node : graph) {
			if (target == null)
				System.out.println("isNull!");
			if (node.hasPosition(target.position)) {
				possibleTargets.add(node);
			}
		}
		System.out.println(graph.size());
		pq = new PriorityQueue<PathfindingNode>(graph.size(), new Comparator<PathfindingNode>() {
			@Override
			public int compare(PathfindingNode node1, PathfindingNode node2) {
				if (node1.cumulatedDistance < node2.cumulatedDistance)
					return -1;
				if (node2.cumulatedDistance < node1.cumulatedDistance)
					return 1;
				else
					return 0;
			}
		});
		for (PathfindingNode node : graph) {
			pq.add(node);
		}
		System.out.println("pqs: " +pq.size());
		ArrayList<PathfindingNode> finishedPile = new ArrayList<PathfindingNode>();
		boolean bla = true;
		while(!pq.isEmpty() && bla) {
			PathfindingNode current = pq.remove();
			finishedPile.add(current);

			for (PathfindingNode neighbor : current.allNeighbors()) {
				if (pq.contains(neighbor) && neighbor.cumulatedDistance > current.cumulatedDistance-1) {
					if(!pq.remove(neighbor)) {
						bla = !bla;
						break;
						
					}
					neighbor.cumulatedDistance = current.cumulatedDistance+1;
					pq.add(neighbor);
					neighbor.setPrevious(current);
				}
			}
			if (possibleTargets.contains(current)) {
				break;
			}
		}

		graph.forEach(node -> node.resetDistance());
		
	//	System.out.println("paths: " +path.size());
		ArrayList<PathfindingNode> path = new ArrayList<PathfindingNode>();
		for (int i = finishedPile.size()-1; i >= 0; i--) {
			System.out.println("path: " +finishedPile.get(i).position.toString());
		}
		PathfindingNode reverseCurrent = finishedPile.get(finishedPile.size()-1);
		if (reverseCurrent.previous == null)
			return null;
		
		reverseCurrent.isTargetSeat = true;
		while(reverseCurrent != null) {
			path.add(reverseCurrent);
			System.out.println("dg: " +reverseCurrent.position.toString());
			reverseCurrent = reverseCurrent.previous;
		}
		
		ArrayList<PathfindingNode> finalPath = new ArrayList<PathfindingNode>();
		for (int i = path.size()-1; i >= 0; i--) {
			finalPath.add(path.get(i));
		}
		for (PathfindingNode node : target.allNeighbors()) {

			System.out.println("targetneih: " +node.position.toString());
		}
		for (int i = 0; i < finishedPile.size(); i++) {
			finishedPile.get(i).previous = null;
		}
		
		if (!finalPath.get(0).hasPosition(start.position))
			return null;
		System.out.println("target pos: " +target.position.toString());
		
		return finalPath;
	}
	
	
}
