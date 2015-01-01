package com.kaptan.freezefiretag.entities.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kaptan.freezefiretag.entities.Board;
import com.kaptan.freezefiretag.util.Status;
import com.kaptan.freezefiretag.util.Turn;


public class FireBlock extends MoveableBlock
{

    public FireBlock(Texture texture, Board board)
    {
        super(texture, board);
    }

    @Override
    public void setPosition(float x, float y)
    {
        super.setPosition(x, y);
        /* Finally set current tiles status as fire */
        board.setTileStatus(getX(), getY(), Status.fire);
    }

    @Override
    public void act(float delta)
    {
        if(board.getTurn() == Turn.fire)
        {
            setTouchable(Touchable.enabled);
        }
        else
        {
            setTouchable(Touchable.disabled);
        }
    }

    @Override
    public void select()
    {
        showRange();
    }

    @Override
    public void unselect()
    {
        clearRange();
    }
}
