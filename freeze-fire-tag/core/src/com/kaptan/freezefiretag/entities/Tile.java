package com.kaptan.freezefiretag.entities;

import com.kaptan.freezefiretag.util.Status;


public class Tile
{
    Status status;
    private int boardX;
    private int boardY;
    public int posX;
    public int posY;

    public Tile(int boardX, int boardY)
    {
        status = Status.empty;
        this.boardX = boardX;
        this.boardY = boardY;
        posX = boardX + 1;
        posY = boardY + 1;
    }

    public int getBoardY()
    {
        return boardY;
    }

    public int getBoardX()
    {
        return boardX;
    }

    public void setStatus(Status s)
    {
        status = s;
    }

    public Status getStatus()
    {
        return status;
    }
}
