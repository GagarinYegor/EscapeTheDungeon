package com.escapethedungeon.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.escapethedungeon.EscapeTheDungeon;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new EscapeTheDungeon(), config);
		config.width = EscapeTheDungeon.SCR_WIDTH;
		config.height = EscapeTheDungeon.SCR_HEIGHT;
	}
}
