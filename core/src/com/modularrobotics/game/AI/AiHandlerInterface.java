package com.modularrobotics.game;

import java.util.ArrayList;

public interface AiHandlerInterface {
	ArrayList<PathfindingNode> pollPath(PathfindingNode start, PathfindingNode target);
}
