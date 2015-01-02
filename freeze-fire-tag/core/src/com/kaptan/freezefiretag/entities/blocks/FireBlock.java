package com.kaptan.freezefiretag.entities.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kaptan.freezefiretag.entities.Board;
import com.kaptan.freezefiretag.util.Status;
import com.kaptan.freezefiretag.util.Turn;


/* The main actor which tries to escape from IceBlocks */
public class FireBlock extends MoveableBlock
{
    public FireBlock(Texture texture, Board board)
    {
        super(texture, board, Status.FIRE);
    }

    @Override
    public void act(float delta)
    {
        if(board.getTurn() == Turn.FIRE)
        {
            setTouchable(Touchable.enabled);
        }
        else
        {
            setTouchable(Touchable.disabled);
        }
    }

    /* Override abstract select*/
    @Override
    public void select()
    {
        showRange();
    }

    /* Override abstract unselect*/
    @Override
    public void unselect()
    {
        clearRange();
    }
}
