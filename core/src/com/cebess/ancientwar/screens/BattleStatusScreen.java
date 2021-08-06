package com.cebess.ancientwar.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cebess.ancientwar.AncientWar;

public class BattleStatusScreen implements Screen {

    private AncientWar gsm;
    private Stage stage;
    private final int numberOfColumns = 8;
    private final int numberOfRows = 5;

    private int[][][] battleStatus;

    public BattleStatusScreen(AncientWar gsm) {
        this.gsm = gsm;
        /// create stage and set it as input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    public void setBattleStatus(int[][][] status) {
        battleStatus = status;
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

        //set up the defaults for column width and height
//        table.columnDefaults(0).width(Math.round(stageWidth*0.5));
//       table.columnDefaults(1).width(Math.round(stageWidth*0.5));
//        long cellHeight = Math.round((stageHeight*0.8)/(numberOfRows+2));
        long cellHeight = Math.round((stageHeight*0.8)/(numberOfRows));
 //       float fontScale = stageHeight/(cellHeight); // this might need to change?!?
        float fontScale = 0.5f;
         table.row().height(cellHeight);
        //create the labels
        Label leftSideLabel = new Label("Left", skin);
        Label rightSideLabel = new Label ("Right", skin);
        Label blankLabel = new Label ("    ", skin);
        Label archerLabel = new Label ("Archer", skin);
        Label warriorLabel = new Label ("Warrior", skin);
        Label knightLabel = new Label ("Knight", skin);
        Label liveLabel1 = new Label ("Live", skin);
        Label retiredLabel1 = new Label ("Retired", skin);
        Label deadLabel1 = new Label ("Dead", skin);
        Label liveLabel2 = new Label ("Live", skin);
        Label retiredLabel2 = new Label ("Retired", skin);
        Label deadLabel2 = new Label ("Dead", skin);

        table.row(); // this is my placeholder row
        table.add().width(160);
        table.add().width(140);
        table.add().width(140);
        table.add().width(140);
        table.add().width(50);
        table.add().width(140);
        table.add().width(140);
        table.add().width(140);

        table.row();
        table.add();
        table.add();
        leftSideLabel.setFontScale(fontScale, fontScale);
        table.add(leftSideLabel).center();
        table.add();
        table.add();
        table.add();
        rightSideLabel.setFontScale(fontScale, fontScale);
        table.add(rightSideLabel).center();

        table.row();
        table.add();
        liveLabel1.setFontScale(fontScale, fontScale);
        table.add(liveLabel1).center();
        retiredLabel1.setFontScale(fontScale, fontScale);
        table.add(retiredLabel1).center();
        deadLabel1.setFontScale(fontScale, fontScale);
        table.add(deadLabel1).center();
        table.add();
        liveLabel2.setFontScale(fontScale, fontScale);
        table.add(liveLabel2).center();
        retiredLabel2.setFontScale(fontScale, fontScale);
        table.add(retiredLabel2).center();
        deadLabel2.setFontScale(fontScale, fontScale);
        table.add(deadLabel2).center();
        table.row();

        archerLabel.setFontScale(fontScale, fontScale);
        table.add(archerLabel).width(100);
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();

        table.row();

        warriorLabel.setFontScale(fontScale, fontScale);
        table.add(warriorLabel).width(100);
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();

        table.row();
        knightLabel.setFontScale(fontScale, fontScale);
        table.add(knightLabel).width(100);
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();

        //create buttons
        TextButton continueButton = new TextButton("Continue", skin);

        //add the button
        table.row().pad(10, 0,  10, 0);
 //       continueButton.getLabel().setFontScale(fontScale, fontScale);
        stage.addActor(continueButton);
 //       table.add(continueButton).colspan(4);

        // create button listeners
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gsm.pop();
                dispose();
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