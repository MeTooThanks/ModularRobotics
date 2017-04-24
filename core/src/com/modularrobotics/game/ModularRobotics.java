package com.modularrobotics.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.ArrayList;

public class ModularRobotics extends ApplicationAdapter implements InputProcessor {
	ModelBatch batch;
	PerspectiveCamera camera;
	ArrayList<Cube> container;
	ArrayList<Target> targets;
	ArrayList<Module> modules;
	Environment environment;
	FirstPersonCameraController cameraController;
	Logic logic;
	int selecting;
	Cube selectorCube;
	int cubeSize;
	int fileSel = 0;
	ModelBuilder modelBuilder;
	Model envModel, modModel, tarModel;
	
	@Override
	public void create () {
		cubeSize = 5;
		batch = new ModelBatch();
		camera = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(10f, 10f, 10f);
		camera.lookAt(0, 0, 0);
		camera.near = 1f;
		camera.far = 300f;
		camera.update();
		
		
		modelBuilder = new ModelBuilder();
		envModel = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
                new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                Usage.Position | Usage.Normal);
		modModel = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                Usage.Position | Usage.Normal);
		tarModel = modelBuilder.createBox(cubeSize, cubeSize, cubeSize,
                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
                Usage.Position | Usage.Normal);
		
		container = new ArrayList<Cube>();
		targets = new ArrayList<Target>();
		modules = new ArrayList<Module>();
		
		createGrid.createNewGrid(20, 20, cubeSize, container);
		
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight,
				0.4f, 0.4f, 0.4f, 1f));		
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -
				-1f, -0.8f, -0.2f));
		
		cameraController = new FirstPersonCameraController(camera);
		cameraController.setVelocity(30);
		Gdx.input.setInputProcessor(new InputMultiplexer (this, cameraController));
		
		logic = new Logic(container, targets, modules, camera, cubeSize);
		selecting = -1;
		
		selectorCube = SelectorCube.createSelectorCube(cubeSize);
		
	}

	@Override
	public void render () {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		batch.begin(camera);

		selectorCube = logic.refreshSelector(selectorCube, Gdx.input.getX(), Gdx.input.getY());
		
		container.forEach(cube -> batch.render(cube, environment));
		targets.forEach(target -> batch.render(target, environment));
		modules.forEach(module -> batch.render(module, environment));
		
		batch.render(selectorCube, environment);
		cameraController.update();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
	
	@Override
	public boolean keyDown(int keycode) { 
		if (keycode == Input.Keys.W) {
			cameraController.keyDown(keycode);
		}
		
		if (keycode == Input.Keys.A) {
			cameraController.keyDown(keycode);
		}
		
		if (keycode == Input.Keys.S) {
			cameraController.keyDown(keycode);
		}
		
		if (keycode == Input.Keys.D) {
			cameraController.keyDown(keycode);
		}
		
		//inserts Cube object
		if (keycode == Input.Keys.SPACE) {
			logic.placeObject(selectorCube);
		}
		
		//removes Cube object
		if (keycode == Input.Keys.BACKSPACE) {
			logic.removeObject();
		}
		
		//cycles through objects: cube, module, target
		if (keycode == Input.Keys.C) {
			selectorCube = SelectorCube.cycleMode(selectorCube);
		}
		
		//inits AI
		if (keycode == Input.Keys.N) {
			logic.nextStep();
		}
				return true;
	}
	
	@Override
	public boolean keyTyped(char character) {
		return true;
	}
	
	
	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.W) {
			cameraController.keyUp(keycode);
		}
		
		if (keycode == Input.Keys.A) {
			cameraController.keyUp(keycode);
		}
		
		if (keycode == Input.Keys.S) {
			cameraController.keyUp(keycode);
		}
		
		if (keycode == Input.Keys.D) {
			cameraController.keyUp(keycode);
		}
		if (keycode == Input.Keys.NUM_1) {
			fileSel = 1;
		}
		if (keycode == Input.Keys.NUM_2) {
			fileSel = 2;
		}
		if (keycode == Input.Keys.NUM_3) {
			fileSel = 3;
		}
		if (keycode == Input.Keys.NUM_4) {
			fileSel = 4;
		}
		if (keycode == Input.Keys.NUM_5) {
			fileSel = 5;
		}
		if (keycode == Input.Keys.K) {
			LoadSave.save(container, modules, targets, fileSel);
		}
		if (keycode == Input.Keys.L) {
			LoadSave.load(fileSel, envModel, modModel, tarModel);
			if (LoadSave.environment != null) {
				container.clear();
				for (int i = 0; i < LoadSave.environment.size(); i++) {
					container.add(LoadSave.environment.get(i));
				}
			}
			if (LoadSave.modules != null) {
				modules.clear();
				for (int i = 0; i < LoadSave.modules.size(); i++) {
					modules.add(LoadSave.modules.get(i));
				}
			}
			if (LoadSave.targets != null) {
				targets.clear();
				for (int i = 0; i < LoadSave.targets.size(); i++) {
					targets.add(LoadSave.targets.get(i));
				}
			}
			

			logic = new Logic(container, targets, modules, camera, cubeSize);
			LoadSave.environment = null;
			LoadSave.modules = null;
			LoadSave.targets = null;
		}
			
		return true;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return true;
	}
	
	@Override
	public boolean scrolled(int amount) {
		return true;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		selecting = logic.getObject(screenX, screenY);
		return true;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		cameraController.touchDragged(screenX, screenY, pointer);
		return true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(selecting >= 0) {
			if (selecting == logic.getObject(screenX, screenY)) {
			logic.setSelected(selecting);
		}
	}
		return true;
}
}

