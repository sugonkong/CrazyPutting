package com.crazy_putting.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.crazy_putting.game.MyCrazyPutting;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = MyCrazyPutting.WIDTH;
		config.height = MyCrazyPutting.HEIGHT;
		config.title = MyCrazyPutting.TITLE;
		new LwjglApplication(new MyCrazyPutting(), config);
	}
}
