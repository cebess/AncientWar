package com.cebess.ancientwar;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.cebess.ancientwar.screens.*;
import java.util.Stack;

public class AncientWar extends Game {

	public static MainMenuScreen gameMainMenu;
	private AppPreferences preferences;
	//world screen parameters
	public static final int WORLD_WIDTH = 1024;
	public static final int WORLD_HEIGHT = 768;
	public static final String TITLE = "Ancient War";
	public SpriteBatch batch;
	private Stack <Screen> screens;


	//#todo the recognition of UI events needs to be rethought since com.cebess.ancientwar.screen touches were passing through to other screens
	//#todo a macro control (charge, retreat...) is needed on the main battle com.cebess.ancientwar.screen
	//#todo sound options for both background and conflicts need to be identified and a preferences dialog is required for those that don't want that sound effects
	//#todo skins fix on preferences
	//#todo issue with com.cebess.ancientwar.screen landscape change on first com.cebess.ancientwar.screen
	@Override
	public void create() {
		batch = new SpriteBatch();
		screens = new Stack<Screen>();
		gameMainMenu = new MainMenuScreen(this);
		push(gameMainMenu); // put the screen on the top of the stack
 		preferences = new AppPreferences(); // this is where the games preferences are stored

	}

	@Override
	public void dispose() {
		gameMainMenu.dispose();
	}

	public void closeGameScreen() {
		setScreen(gameMainMenu);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		Screen activeScreen = getScreen();
		activeScreen.resize(width, height);
	}

	public AppPreferences getPreferences(){
		return this.preferences;
	}

	//#todo add the code to handle the input transfer of control
	public void push(Screen screen){
		screens.push(screen);
		setScreen(screen);
	}
	public void pop() {
		screens.pop();
		setScreen(screens.peek());
	}
	public void set(Screen screen) {
		screens.pop();
		screens.push(screen);
		setScreen(screen);
	}
}
