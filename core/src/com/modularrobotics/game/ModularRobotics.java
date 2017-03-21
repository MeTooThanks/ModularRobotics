package com.modularrobotics.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;

import java.util.ArrayList;

public class ModularRobotics extends ApplicationAdapter implements InputProcessor {
	ModelBatch batch;
	PerspectiveCamera camera;
	ArrayList<Cube> container;
	Environment environment;
	FirstPersonCameraController cameraController;
	Logic logic;
	int selecting;
	Cube selectorCube;
	int cubeSize;
	
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
		
		container = new ArrayList<Cube>();
		createGrid.createNewGrid(20, 20, cubeSize, container);
		
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight,
				0.4f, 0.4f, 0.4f, 1f));		
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -
				-1f, -0.8f, -0.2f));
		
		cameraController = new FirstPersonCameraController(camera);
		Gdx.input.setInputProcessor(new InputMultiplexer (cameraController, this));
		
		logic = new Logic(container, camera, cubeSize);
		selecting = -1;
		
		selectorCube = SelectorCube.createSelectorCube(cubeSize);
	
	}

	@Override
	public void render () {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		batch.begin(camera);

		//insert commands to draw here
		container.forEach(cube -> batch.render(cube, environment));
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
		return true;
	}
	
	@Override
	public boolean keyTyped(char character) {
		return true;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		return true;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		selectorCube = logic.refreshSelector(selectorCube, screenX, screenY);
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
