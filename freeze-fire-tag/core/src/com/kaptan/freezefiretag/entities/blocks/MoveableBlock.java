package com.kaptan.freezefiretag.entities.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kaptan.freezefiretag.entities.Board;
import com.kaptan.freezefiretag.util.Direction;
import com.kaptan.freezefiretag.util.Status;


public abstract class MoveableBlock extends Block
{
    /* Current tile's previous status */
    protected Status previousStatus = Status.EMPTY;
    /* Current direction */
    protected Direction direction = Direction.NONE;
    /* Block's range tiles */
    public Group rangeGroup;
    /* Selected range */
    public RangeBlock selectedRange;

    protected MoveableBlock(Texture texture, Board board, Status status)
    {
        super(texture, board);
        rangeGroup = new Group();
        this.status = status;
        setTouchable(Touchable.disabled);
    }

    @Override
    public void setPosition(float x, float y)
    {
        /* When leaving a tile, first set it back to its previousStatus */
        board.setTileStatus(getX(), getY(), previousStatus);
        super.setPosition(x, y);
        /* Then save the current tiles status */
        previousStatus = board.getTileStatus(x, y);
        /* Finally set current tile's status as appropriate status */
        board.setTileStatus(x, y, status);
    }

    protected void showRange()
    {
        board.showRange(this);
        selected = true;
    }

    protected void clearRange()
    {
        selectedRange = null;
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

    /* Move to selected tile */
    /* Clear the range after moving */
    public void moveToSelected()
    {
        float x = selectedRange.getX(), y = selectedRange.getY();
        clearRange();
        setDirection(x, y);
        setPosition(x, y);
    }

    @Override
    public void reset()
    {
        direction = Direction.NONE;
        previousStatus = Status.EMPTY;
    }
}
