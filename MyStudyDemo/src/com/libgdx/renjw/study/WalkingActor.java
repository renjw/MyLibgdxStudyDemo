package com.libgdx.renjw.study;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class WalkingActor extends Actor {

	private static final int ROW_COUNT = 5;
	private static final int COL_COUNT = 6;

	Texture texture;
	TextureRegion[] walkFrames;
	TextureRegion[] leftWalkFrames;
	
	Animation rightAnimation;
	Animation leftAnimation;

	float stateTime = 0;
	
	STATE mstate = STATE.RIGHT;
	
	enum STATE
	{
		LEFT,RIGHT
	}

	public WalkingActor() {
		// TODO Auto-generated constructor stub
		texture = new Texture("data/sprite-animation4.png");
		TextureRegion[][] tmpRegions = new TextureRegion[ROW_COUNT][COL_COUNT];
		tmpRegions = TextureRegion.split(texture, texture.getWidth()
				/ COL_COUNT, texture.getHeight() / ROW_COUNT);
		walkFrames = new TextureRegion[ROW_COUNT * COL_COUNT];
		leftWalkFrames = new TextureRegion[ROW_COUNT * COL_COUNT];

		int index = 0;
		for (int i = 0; i < ROW_COUNT; i++) {
			for (int j = 0; j < COL_COUNT; j++) {
				walkFrames[index++] = tmpRegions[i][j];
			}
		}

		rightAnimation = new Animation(0.3f, walkFrames);
		rightAnimation.setPlayMode(Animation.LOOP);

	}
	
	public void setState(STATE state)
	{
		this.mstate = state;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		
		stateTime += Gdx.graphics.getDeltaTime();
		
		TextureRegion region = new TextureRegion();
		switch (mstate) {
//		case LEFT:
//			region = leftAnimation.getKeyFrame(stateTime);
//			break;
		case RIGHT:
			region = rightAnimation.getKeyFrame(stateTime);
			break;
		default:
			break;
		}
		
		batch.draw(region, getX(), getY());
		
		super.draw(batch, parentAlpha);
	}

	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		super.clear();

		texture.dispose();
	}

}
