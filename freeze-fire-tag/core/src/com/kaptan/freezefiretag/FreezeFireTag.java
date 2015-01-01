package com.kaptan.freezefiretag;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.kaptan.freezefiretag.screens.GameScreen;
import com.kaptan.freezefiretag.screens.MenuScreen;


public class FreezeFireTag extends Game
{
    public GameScreen gameScreen;
    public MenuScreen menuScreen;
    public BitmapFont font;

    @Override
    public void create()
    {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/second.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 2;
        font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();

        gameScreen = new GameScreen(this);
        menuScreen = new MenuScreen(this, gameScreen.stage.getBatch());
        setScreen(menuScreen);
    }

    @Override
    public void render()
    {
        super.render();
    }

    @Override
    public void dispose()
    {
        font.dispose();
        gameScreen.dispose();
        menuScreen.dispose();
    }
}
