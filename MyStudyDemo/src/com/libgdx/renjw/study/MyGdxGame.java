package com.libgdx.renjw.study;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {
	SpriteBatch batch;
	Texture img;

	public static GameScreen gameScreen;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("data/libgdx.png");
		gameScreen = new GameScreen(this);

		setScreen(gameScreen);

	}

}
