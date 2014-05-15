package com.libgdx.renjw.study;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.libgdx.renjw.study.WalkingActor.STATE;

public class GameScreen implements Screen, InputProcessor {

	public MyGdxGame myGdxGame;

	public TiledMap map;
	public Stage stage;
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer render;

	/**
	 * �ϰ���ͼ��
	 */
	private MapLayer blockMapLayer;
	/**
	 * �ϰ���
	 */
	private RectangleMapObject blockMapObject;

	/**
	 * �ϰ������
	 */
	private Rectangle blockRectangle;

	/**
	 * �������
	 */
	private Rectangle playerRectangle;

	/*
	 * ����
	 */
	WalkingActor playActor;

	/*
	 * ��Ļ�Ŀ��
	 */
	int w, h;

	/**
	 * �����X����
	 */
	int viewportX = 0;

	/*
	 * ��ͼ�����ؿ��
	 */
	int mapPixelWidth;
	int mapPixelHeight;

	float playerWidth = 0;
	float playerHeight = 0;

	public GameScreen(MyGdxGame game) {
		myGdxGame = game;

		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		viewportX = w / 2;

		Gdx.app.log("renjw", "w= " + w + ", h= " + h);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);

		map = new TmxMapLoader().load("data/level.tmx");
		render = new OrthogonalTiledMapRenderer(map);

		// ��ȡ�ϰ���ͼ���Լ��ϰ���
		blockMapLayer = map.getLayers().get("objLayout");
		if (null != blockMapLayer) {
			blockMapObject = (RectangleMapObject) blockMapLayer.getObjects()
					.get("blockObj");
			if (null != blockMapObject) {
				blockRectangle = blockMapObject.getRectangle();
				blockRectangle.set(blockRectangle.getX() / 32f,
						blockRectangle.getY() / 32f,
						blockRectangle.getWidth() / 32f,
						blockRectangle.getHeight() / 32f);
				Gdx.app.log("renjw", "block rectangle is (" + blockRectangle.x
						+ " , " + blockRectangle.y + ")(width = "
						+ blockRectangle.getWidth() + ",height= "
						+ blockRectangle.getHeight());
			}
		}
		// �����ͼ�Ŀ��
		MapProperties prop = map.getProperties();
		int mapWidth = prop.get("width", Integer.class);
		int mapHeight = prop.get("height", Integer.class);
		int tilePixelWidth = prop.get("tilewidth", Integer.class);
		int tilePixelHeight = prop.get("tileheight", Integer.class);
		mapPixelWidth = mapWidth * tilePixelWidth;
		mapPixelHeight = mapHeight * tilePixelHeight;

		Gdx.app.log("renjw", "width= " + mapPixelWidth + ", height= "
				+ mapPixelHeight);

		playActor = new WalkingActor();
		stage = new Stage();
		stage.addActor(playActor);

		Gdx.input.setInputProcessor(stage);
		Gdx.input.setInputProcessor(this);
	}

	/**
	 * �ƶ����鵽ĳ��λ��
	 * 
	 * @param actor
	 * @param x
	 * @param y
	 */
	public void MoveActorTo(Actor actor, float x, float y) {
		MoveToAction moveToAction = new MoveToAction();
		moveToAction.setX(x);
		moveToAction.setY(y);
		moveToAction.setDuration(1);
		moveToAction.setActor(actor);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		render.setView(camera);
		render.render();

		stage.act();
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		// �տ�ʼ��ʱ����ƶ����鵽һ��λ��
		MoveActorTo(playActor, 300, playActor.getY());
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Input.Keys.RIGHT:
//			playActor.setState(STATE.RIGHT);//���þ����״̬
			viewportX += 30;
			if (viewportX >= mapPixelWidth - w / 2) {
				viewportX = mapPixelWidth - w / 2;
			}
			camera.position.x = viewportX;

			// ������ƶ������ұ�ʱ���ƶ�����
			if (camera.position.x == mapPixelWidth - w / 2) {
				playActor.setX(playActor.getX()+10);
			}

			// �����鵽����Ļ���Ҷ�ʱ��ֹͣ
			if (playActor.getX() >= w - playActor.getWidth()) {
				playActor.setX(w - playActor.getWidth());
			}
			break;
		case Input.Keys.LEFT:
//			playActor.setState(STATE.LEFT);//���þ����״̬
			viewportX -= 30;
			if (viewportX <= w / 2) {
				viewportX = w / 2;
			}
			camera.position.x = viewportX;

			// ���ֻ���ƶ��������
			if (camera.position.x == w / 2) {
				playActor.setX(playActor.getX()-10);
			}
			// ���鲻���Ƴ���ͼ�������
			if (playActor.getX() <= 0) {
				playActor.setX(0);
			}
			break;

		default:
			break;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
