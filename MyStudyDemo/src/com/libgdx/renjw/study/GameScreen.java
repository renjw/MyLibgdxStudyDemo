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
	 * 障碍物图层
	 */
	private MapLayer blockMapLayer;
	/**
	 * 障碍物
	 */
	private RectangleMapObject blockMapObject;

	/**
	 * 障碍物矩形
	 */
	private Rectangle blockRectangle;

	/**
	 * 人物矩形
	 */
	private Rectangle playerRectangle;

	/*
	 * 人物
	 */
	WalkingActor playActor;

	/*
	 * 屏幕的宽高
	 */
	int w, h;

	/**
	 * 相机的X坐标
	 */
	int viewportX = 0;

	/*
	 * 地图的像素宽度
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

		// 获取障碍物图层以及障碍物
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
		// 计算地图的宽高
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
	 * 移动精灵到某个位置
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
		// 刚开始的时候就移动精灵到一个位置
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
//			playActor.setState(STATE.RIGHT);//设置精灵的状态
			viewportX += 30;
			if (viewportX >= mapPixelWidth - w / 2) {
				viewportX = mapPixelWidth - w / 2;
			}
			camera.position.x = viewportX;

			// 当相机移动到最右边时在移动精灵
			if (camera.position.x == mapPixelWidth - w / 2) {
				playActor.setX(playActor.getX()+10);
			}

			// 当精灵到达屏幕最右端时，停止
			if (playActor.getX() >= w - playActor.getWidth()) {
				playActor.setX(w - playActor.getWidth());
			}
			break;
		case Input.Keys.LEFT:
//			playActor.setState(STATE.LEFT);//设置精灵的状态
			viewportX -= 30;
			if (viewportX <= w / 2) {
				viewportX = w / 2;
			}
			camera.position.x = viewportX;

			// 相机只能移动到最左端
			if (camera.position.x == w / 2) {
				playActor.setX(playActor.getX()-10);
			}
			// 精灵不能移出地图的最左端
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
