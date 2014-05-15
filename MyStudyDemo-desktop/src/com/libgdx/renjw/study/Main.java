package com.libgdx.renjw.study;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "MyStudyDemo";
		cfg.width = 480;
		cfg.height = 320;
		cfg.useGL20 = true;
		
		new LwjglApplication(new MyGdxGame(), cfg);
	}
}
