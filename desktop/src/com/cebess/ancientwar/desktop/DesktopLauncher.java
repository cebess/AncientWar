package com.cebess.ancientwar.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cebess.ancientwar.AncientWar;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = AncientWar.TITLE;
		config.width = AncientWar.WORLD_WIDTH;
		config.height = AncientWar.WORLD_HEIGHT;
		new LwjglApplication(new AncientWar(), config);
	}
}
