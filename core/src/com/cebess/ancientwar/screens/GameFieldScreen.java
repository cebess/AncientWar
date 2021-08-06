package com.cebess.ancientwar.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cebess.ancientwar.*;

public class GameFieldScreen implements Screen {

    //screen
    private OrthographicCamera camera;
    private Viewport viewport;
    private AncientWar gsm;

    //graphics
    private Texture background;

    public static TextureAtlas swordTextureAtlas;
    public static TextureAtlas spearTextureAtlas;
    public static TextureAtlas bowTextureAtlas;

    //timing
    private long lastTime;
    private final long sleepTime = 150;

    static final String[] backgrounds= new String[] {
            "desert background1",
            "desert background2",
            "desert background3",
            "field background1",
            "field background2",
            "wooded1",
            "wooded2"};

    //class variables
    private Field myField;
    //#todo add scoring information on the top of the screen?
    public GameFieldScreen(AncientWar gsm) {
        lastTime = System.currentTimeMillis()*1000;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(AncientWar.WORLD_WIDTH,AncientWar.WORLD_HEIGHT,camera);
        this.gsm = gsm;

        background = new Texture(backgrounds[MathUtils.random(0, backgrounds.length-1)] + ".jpg");

        swordTextureAtlas = new TextureAtlas("walking man with sword.txt");
        spearTextureAtlas = new TextureAtlas("walking man with spear.txt");
        bowTextureAtlas = new TextureAtlas("walking man with bow.txt");
        myField = new Field();
        // create some test people for the field
        // create left side
        BattleFormation leftFormation = new BattleFormation(MathUtils.random(0, 2)+MathUtils.random(0, 2),MathUtils.random(0, 2)+MathUtils.random(0, 2),MathUtils.random(0, 2)+MathUtils.random(0, 2),"row",BattleFormation.FillMethod.CenterOut);
        // create right side
        BattleFormation rightFormation = new BattleFormation(MathUtils.random(0, 2)+MathUtils.random(0, 2),MathUtils.random(0, 2)+MathUtils.random(0, 2),MathUtils.random(0, 2)+MathUtils.random(0, 2),leftFormation.formationNames[MathUtils.random(0, 5)],BattleFormation.FillMethod.CenterOut);

        myField.formationAdder(leftFormation,true);
        myField.formationAdder(rightFormation,false);

    }

    @Override
    public void render(float delta) {
        //update movement section
        myField.action();
        //render section
        gsm.batch.begin();
        gsm.batch.draw(background,0,0,AncientWar.WORLD_WIDTH,AncientWar.WORLD_HEIGHT);
        myField.draw(gsm.batch);
        gsm.batch.end();
        try {
            Thread.sleep(sleepTime);
        }
        catch (InterruptedException e) {
            System.out.println("Interrupted Exception Caught"+ e);
        }
        //check to see if anyone is left
        if (myField.getBattleComplete()) {
            //we are done with this screen
            //#todo switch to the who won screen.
            BattleStatusScreen battleStatusScreenScreen = new BattleStatusScreen(gsm);
            int [][][] statusInfo = myField.battleStatus();
            battleStatusScreenScreen.setBattleStatus(statusInfo);
            gsm.set(battleStatusScreenScreen);
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true);
        gsm.batch.setProjectionMatrix(camera.combined);
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
    public void show() {
        // this is where we would start music
        //rainMusic.play();

    }

    @Override
    public void dispose() {
        swordTextureAtlas.dispose();
        spearTextureAtlas.dispose();
        bowTextureAtlas.dispose();
        background.dispose();

    }
}
