package com.kaptan.freezefiretag.entities.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kaptan.freezefiretag.entities.Board;
import com.kaptan.freezefiretag.util.Status;
import com.kaptan.freezefiretag.util.Turn;


/* The main actor which tries to "freeze" FireBlocks */
public class IceBlock extends MoveableBlock
{
    public IceBlock(Texture texture, Board board)
    {
        super(texture, board);
    }

    @Override
    public void setPosition(float x, float y)
    {
        super.setPosition(x, y);
        /* Finally set current tiles status as ice */
        board.setTileStatus(x, y, Status.ice);
    }

    @Override
    public void act(float delta)
    {
        if(board.getTurn() == Turn.ice)
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
