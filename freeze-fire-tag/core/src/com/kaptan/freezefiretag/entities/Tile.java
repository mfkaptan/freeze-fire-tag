package com.kaptan.freezefiretag.entities;

import com.kaptan.freezefiretag.util.Status;


public class Tile
{
    private Status status;
    public int posX;
    public int posY;

    public Tile(int x, int y)
    {
        status = Status.EMPTY;
        posX = x + 1;
        posY = y + 1;
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
