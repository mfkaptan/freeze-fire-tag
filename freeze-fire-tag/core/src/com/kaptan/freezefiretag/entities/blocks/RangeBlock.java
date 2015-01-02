package com.kaptan.freezefiretag.entities.blocks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kaptan.freezefiretag.entities.Board;
import com.kaptan.freezefiretag.util.Status;


public class RangeBlock extends Block
{
    /* Save the origin Block */
    MoveableBlock origin;
    public static final Color chosenColor = new Color(0.1f, 0.8f, 0.1f, 0.8f);
    public static Color originColor;

    public RangeBlock(Texture texture, final Board board)
    {
        super(texture, board);
        block.setAlpha(150);
        originColor = block.getColor();
    }

    public void init(MoveableBlock b, float x, float y)
    {
        setPosition(x, y);
        origin = b;
        setVisible(true);
    }

    /* Override abstract unselect*/
    @Override
    public void select()
    {
        cleanUpAll();

        board.setTileStatus(getX(), getY(), Status.SELECTED);
        selected = true;
        block.setColor(chosenColor);
        origin.selectedRange = this;
    }

    /* Override abstract unselect*/
    @Override
    public void unselect()
    {
        cleanUpAll();

        board.setTileStatus(getX(), getY(), Status.EMPTY);
        selected = false;
        block.setColor(originColor);
        origin.selectedRange = null;
    }

    /* Call cleanUp() on all rangeGroup members of origin block*/
    public void cleanUpAll()
    {
        for(Actor r : origin.rangeGroup.getChildren())
        {
            ((RangeBlock) r).cleanUp();
        }
    }

    public MoveableBlock getOriginBlock()
    {
        return origin;
    }

    public void cleanUp()
    {
        selected = false;
        board.setTileStatus(getX(), getY(), Status.EMPTY);
        block.setColor(originColor);
    }

    @Override
    public void reset()
    {
        cleanUp();
        origin = null;
    }
}
