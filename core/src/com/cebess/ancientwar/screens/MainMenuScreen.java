package com.cebess.ancientwar.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.cebess.ancientwar.AncientWar;

public class MainMenuScreen implements Screen{

    private AncientWar gsm;
    private Stage stage;

    public MainMenuScreen(AncientWar gsm) {
        this.gsm = gsm;
        /// create stage and set it as input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false); // change to false when design is finalized
        stage.addActor(table);
        int stageWidth = stage.getViewport().getScreenWidth();
        int stageHeight = stage.getViewport().getScreenHeight();

        Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

        //create buttons
        TextButton newGame = new TextButton("New Game", skin);
        TextButton preferences = new TextButton("Preferences", skin);
        TextButton exit = new TextButton("Exit", skin);

        //add buttons to table
        //set up the defaults
        table.columnDefaults(0).width(Math.round(stageWidth*0.8));
        long cellHeight = Math.round((stageHeight*0.8)/5); // where 5 is the number of buttons + 2;
        float fontScale = stageHeight/(cellHeight*3);
        table.row().height(cellHeight);
        newGame.getLabel().setFontScale(fontScale, fontScale);
        table.add(newGame);
        table.row().height(cellHeight);
        table.row().pad(10, 0,  10, 0);
        preferences.getLabel().setFontScale(fontScale, fontScale);
        table.add(preferences);
        table.row().height(cellHeight);
        exit.getLabel().setFontScale(fontScale, fontScale);
        table.add(exit);

        // create button listeners
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameFieldScreen gameFieldScreen;
                gameFieldScreen = new GameFieldScreen(gsm);
                gsm.push(gameFieldScreen);
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PreferencesScreen preferencesScreen = new PreferencesScreen(gsm);
                gsm.push(preferencesScreen);
            }
        });

    }

    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // change the stage's viewport when the screen size is changed
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // dispose of assets when not needed anymore
        stage.dispose();
    }

}