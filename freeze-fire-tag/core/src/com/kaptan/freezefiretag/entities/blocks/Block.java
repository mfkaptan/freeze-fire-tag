package com.kaptan.freezefiretag.entities.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.kaptan.freezefiretag.entities.Board;
import com.kaptan.freezefiretag.util.Status;


/* Every *Block class must extend from this class */
public abstract class Block extends Actor implements BlockInterface, Poolable
{
    /* Reference to the board */
    protected final Board board;
    /* Holds the texture and geometry */
    protected Sprite block;
    /* Input Listener */
    protected InputListener inputListener;
    /* Whether if selected by user */
    protected boolean selected = false;
    /* Status */
    protected Status status;

    protected Block(Texture texture, final Board board)
    {
        this.board = board;
        block = new Sprite(texture);
        /* Since our map has 1/32f units */
        block.setSize(1, 1);
        /* For clicking */
        setBounds(0, 0, 1, 1);
        setOrigin(1, 1);
        block.setOrigin(1, 1);
        setX(1);
        setY(1);

        inputListener = new InputListener()
        {
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button)
            {
                /* Double clicking will unselect*/
                if(selected)
                    unselect();
                else
                    select();

                return true;
            }
        };
        addListeners();
    }

    private void addListeners()
    {
        addListener(inputListener);
    }

    /* Every block should have a select method */
    public abstract void select();

    /* Every block should have an unselect method */
    public abstract void unselect();

    /* Set current position of geometry */
    @Override
    public void setPosition(float x, float y)
    {
        super.setPosition(x, y);
        block.setPosition(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        block.draw(batch);
    }
}
