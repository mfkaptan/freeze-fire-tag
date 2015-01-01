package com.kaptan.freezefiretag.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.kaptan.freezefiretag.FreezeFireTag;


public class MenuScreen implements Screen
{
    private Texture welcome;
    private final Batch batch;
    private final FreezeFireTag game;

    public MenuScreen(FreezeFireTag game, Batch batch)
    {
        this.game = game;
        this.batch = batch;
        welcome = new Texture(Gdx.files.internal("img/welcome.jpg"));
    }

    @Override
    public void show()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(welcome, 0, 0);
        batch.end();

        if(Gdx.input.justTouched())
        {
            dispose();
            game.setScreen(game.gameScreen);
        }
    }

    @Override
    public void resize(int width, int height)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose()
    {
        welcome.dispose();
    }

}
