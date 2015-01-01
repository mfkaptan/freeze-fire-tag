package com.kaptan.freezefiretag.entities.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.kaptan.freezefiretag.entities.Board;
import com.kaptan.freezefiretag.util.Direction;
import com.kaptan.freezefiretag.util.Status;


public abstract class MoveableBlock extends Block
{
    /* Current tile's previous status */
    public Status previousStatus = Status.empty;
    /* Current direction */
    private Direction direction = Direction.NONE;
    /* Block's range tiles */
    public Group rangeGroup;

    public MoveableBlock(Texture texture, Board board)
    {
        super(texture, board);
        rangeGroup = new Group();
    }

    @Override
    public void setPosition(float x, float y)
    {
        /* When leaving a tile, first set it back to its previousStatus */
        board.setTileStatus(getX(), getY(), previousStatus);
        super.setPosition(x, y);
        /* Then save the current tiles status */
        previousStatus = board.getTileStatus(getX(), getY());
    }

    protected void showRange()
    {
        board.showRange(this);
        selected = true;
    }

    protected void clearRange()
    {
        board.clearRange(this);
        selected = false;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public void setDirection(float x, float y)
    {
        direction = Direction.calculateDirection(getX(), getY(), x, y);
    }

    /* Move to specified coordinates and set the direction */
    /* Clear the range after moving */
    public void moveTo(float x, float y)
    {
        setDirection(x, y);
        setPosition(x, y);
        clearRange();
    }

    @Override
    public void reset()
    {
        direction = Direction.NONE;
        previousStatus = Status.empty;
    }
}
