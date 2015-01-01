package com.kaptan.freezefiretag;

import com.badlogic.gdx.Game;
import com.kaptan.freezefiretag.screens.GameScreen;
import com.kaptan.freezefiretag.screens.MenuScreen;


public class FreezeFireTag extends Game
{
    public GameScreen gameScreen;
    public MenuScreen menuScreen;

    @Override
    public void create()
    {
        gameScreen = new GameScreen(this);
        menuScreen = new MenuScreen(this, gameScreen.stage.getBatch());
        setScreen(menuScreen);
    }

    @Override
    public void render()
    {
        super.render();
    }
}
