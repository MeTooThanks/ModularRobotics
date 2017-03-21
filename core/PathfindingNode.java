package com.modularrobotics.game;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

public class PathfindingNode {
	Vector3 position;
	int layer;
	ArrayList<PathfindingNode> neighbors;
	int cumulatedDistance;
	boolean isTargetSeat;
	
	public PathfindingNode(float x, float y, float z, int initLayer) {
		position = new Vector3(x, y, z);
		neighbors = new ArrayList<PathfindingNode>();
		layer = initLayer;
	}
	
	public int getDistance() {
		return cumulatedDistance;
	}
	
	public void setDistance(int value) {
		cumulatedDistance = value;
	}
	
	public void addDistance(int value) {
		cumulatedDistance += value;
	}
	
	public PathfindingNode cloneNode() {
		PathfindingNode clonedNode = new PathfindingNode(position.x, position.y, position.z, layer+1);
		return clonedNode;
	}
	
	public int getLayer() {
		return layer;
	}
	
	public void setLayer(int setLayer) {
		layer = setLayer;
	}
	
	public void addNeighbor(PathfindingNode neighbor) {
		neighbors.add(neighbor);
	}
	
	public void isTarget(boolean setTarget) {
		isTargetSeat = setTarget;
	}
	
	public PathfindingNode neighbor(int i) {
		return neighbors.get(i);
	}
	
	public ArrayList<PathfindingNode> allNeighbors() {
		return neighbors;
	}
	
	public void removeNeighbor(int i) {
		neighbors.remove(i);
	}
	
	public Vector3 position() {
		return position;
	}
	
	public boolean equals(PathfindingNode otherNode) {
		if (this != otherNode
				&& position.x == otherNode.position().x 
				&& position.x == otherNode.position().y 
				&& position.x == otherNode.position().z) {
			return true;
		} else
			return false;
	}
	
	public boolean hasPosition(Vector3 positionToCheck) {
		if (position.x == positionToCheck.x
				&& position.y == positionToCheck.y
				&& position.z == positionToCheck.z) {
			return true;
		}
		return false;
	}
}
