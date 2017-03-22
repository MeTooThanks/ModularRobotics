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
					ArrayList<PathfindingNode> initGraph) {
		start = initStart;
		target = initTarget;
		graph = initGraph;
		
		possibleTargets = new ArrayList<PathfindingNode>();
		for (PathfindingNode node : graph) {
			if (node.hasPosition(target.position)) {
				possibleTargets.add(node);
			}
		}
		
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
		
		ArrayList<PathfindingNode> finishedPile = new ArrayList<PathfindingNode>();
		while(!pq.isEmpty()) {
			PathfindingNode current = pq.poll();
			finishedPile.add(current);
			
			for (PathfindingNode neighbor : current.allNeighbors()) {
				if (neighbor.cumulatedDistance + 1 > current.cumulatedDistance) {
					pq.remove(neighbor);
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
		ArrayList<PathfindingNode> path = new ArrayList<PathfindingNode>();
		PathfindingNode reverseCurrent = path.get(path.size()-1);
		while(reverseCurrent != null) {
			path.add(reverseCurrent);
			reverseCurrent = reverseCurrent.previous;
		}
		
		return path;
	}
	
	
}
