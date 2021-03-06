package com.kaptan.freezefiretag.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kaptan.freezefiretag.FreezeFireTag;
import com.kaptan.freezefiretag.entities.Board;
import com.kaptan.freezefiretag.entities.Button;


public class GameScreen implements Screen
{
    private final FreezeFireTag game;
    public Stage stage;
    private Board board;
    private Skin skin;
    private Stage hud;
    /* Buttons */
    private Button readyButton;
    Table table = new Table();
    Group bg = new Group();
    Group fg = new Group();

    /*
     * The game screen. This class will be rendered during the game
     */
    public GameScreen(FreezeFireTag freezeFireTag)
    {
        /* Reference to the game */
        this.game = freezeFireTag;
        /* Stage that will handle the actors */
        stage = new Stage(new FitViewport(27, 16));
        hud = new Stage();
        // hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(),
        // Gdx.graphics.getHeight());

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(0, hud);
        inputMultiplexer.addProcessor(1, stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        /* Add backgroung group to stage */
        stage.addActor(bg);
        /* Add foreground group to stage */
        stage.addActor(fg);

        /* Board as an actor */
        board = new Board(20, 14, fg, stage.getCamera());
        /* Add board to background */
        bg.addActor(board);
        board.addIce(10, 7);
        board.addIce(11, 8);

        board.addFire(7, 3);
        board.addFire(14, 3);
        board.addFire(4, 4);
        board.addFire(17, 4);
        board.addFire(18, 7);
        board.addFire(3, 8);
        board.addFire(4, 11);
        board.addFire(17, 11);
        board.addFire(7, 12);
        board.addFire(14, 12);

        hud.getCamera().viewportWidth = Gdx.graphics.getWidth();
        hud.getCamera().viewportHeight = Gdx.graphics.getHeight();

        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        readyButton = new Button("Ready", skin, board);
        table.setPosition(Gdx.graphics.getWidth() * 0.9f,
                Gdx.graphics.getHeight() * 0.1f);
        table.add(readyButton).width(Gdx.graphics.getWidth() * 0.1f)
                .height(Gdx.graphics.getHeight() * 0.1f);
        hud.addActor(table);
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

        hud.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, true);
        hud.getViewport().update(width, height, true);
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
        skin.dispose();
        stage.dispose();
        hud.dispose();
        board.dispose();
    }
}
