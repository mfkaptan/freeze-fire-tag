package com.kaptan.freezefiretag.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kaptan.freezefiretag.FreezeFireTag;
import com.kaptan.freezefiretag.entities.Board;


public class GameScreen implements Screen
{
    private final FreezeFireTag game;
    public Stage stage;
    private Board board;
    Group bg = new Group();
    Group fg = new Group();

    // private InputMultiplexer inputMultiplexer = new InputMultiplexer();

    /*
     * The game screen. This class will be rendered during the game
     */
    public GameScreen(FreezeFireTag freezeFireTag)
    {
        /* Reference to the game */
        this.game = freezeFireTag;
        /* Stage that will handle the actors */
        stage = new Stage(new FitViewport(27, 16));
        Gdx.input.setInputProcessor(stage);
        /* Add backgroung group to stage */
        stage.addActor(bg);
        /* Add foreground group to stage */
        stage.addActor(fg);

        /* Board as an actor */
        board = new Board(20, 14, fg, stage.getCamera());
        /* Add board to background */
        bg.addActor(board);
        board.addFire(1, 2);
        board.addFire(2, 8);
        board.addIce(3, 5);
        board.addFire(3, 11);
        board.addFire(9, 6);
        board.addFire(10, 10);
        board.addFire(11, 4);
        board.addFire(13, 12);
        board.addFire(17, 13);
        board.addFire(19, 2);
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        // stage.getViewport().update(width, height, true);
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
        stage.dispose();
        board.dispose();
    }
}
