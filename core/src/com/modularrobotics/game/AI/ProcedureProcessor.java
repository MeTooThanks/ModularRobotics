package com.modularrobotics.game.AI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Queue;
import com.modularrobotics.game.Module;
import com.modularrobotics.game.Target;

public class ProcedureProcessor {
	static Queue<Module> moduleQ = new Queue<Module>();
	static PriorityQueue<Target> targetPQ;
	static ArrayList<Module> modules;
	static ArrayList<Target> targets;
	static Module constrainedModule;
	static ArrayList<Module> toBePaired;
	static ArrayList<Module> modulesInOrder;
	static ArrayList<Target> targetsInOrder;
	
	public static void createProcedureProcessing(ArrayList<Module> initModules, ArrayList<Target> initTargets) {
		modules = initModules;
		targets = initTargets;
		modules.forEach(module -> {
			moduleQ.addLast(module);
		});
		System.out.println("ts: " +targets.size());
		targetPQ = new PriorityQueue<Target>(targets.size(),
				Comparator.comparingInt(t ->  t.getOpenConnections()));

		
		Module tempModule = moduleQ.removeFirst();
		
		Queue<Module> unconstrainedModules = new Queue<Module>();
		
		if (!tempModule.isConstrained) {
			unconstrainedModules.addFirst(tempModule);
		}
		while (tempModule.isConstrained && moduleQ.size > 0) {
			unconstrainedModules.addFirst(moduleQ.removeFirst());
			tempModule = moduleQ.first();
		}
		
		while (unconstrainedModules.size > 0) {
			moduleQ.addFirst(unconstrainedModules.removeFirst());
		}
		
		targets.forEach(target -> {
			targetPQ.add(target);
		});
	}
	
	public static void createPairing() {
		toBePaired = new ArrayList<Module>();
		int encounteredConstrained = 0;
		while (moduleQ.size > 0 && encounteredConstrained < 2) {
			Module evaluatedModule = moduleQ.removeFirst();
			if (evaluatedModule.isConstrained) {
				if (evaluatedModule == constrainedModule) {
					encounteredConstrained++;
				}
				moduleQ.addLast(evaluatedModule);
			} else {
				toBePaired.add(evaluatedModule);
			}
		}
		targetsInOrder = new ArrayList<Target>();
		for (int i = 0; i < toBePaired.size(); i++) {
			targetsInOrder.add(targetPQ.poll());
		}
		
		modulesInOrder = new ArrayList<Module>();
		
		for (Target target : targetsInOrder) {
			float distance = Integer.MAX_VALUE;
			Module paired = null;
			for (Module module : toBePaired) {
				Vector3 modPos = new Vector3(module.position.x, module.position.y, module.position.z);
				if (Math.abs((modPos.sub(target.position)).len()) < distance) {
					paired = module;
					distance = modPos.len();
				}
			}
			toBePaired.remove(paired);
			modulesInOrder.add(paired);
		}
	}
}
