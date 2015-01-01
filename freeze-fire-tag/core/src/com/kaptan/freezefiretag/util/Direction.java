package com.kaptan.freezefiretag.util;

public enum Direction
{
    NONE(0, 0), // ///// ..
    UP(0, 1), // /////// ^^
    UPRIGHT(1, 1), // // ^>
    RIGHT(1, 0), // //// >>
    DOWNRIGHT(1, -1), // v>
    DOWN(0, -1), // //// vv
    DOWNLEFT(-1, -1), // <v
    LEFT(-1, 0), // //// <<
    UPLEFT(-1, 1); // // <^

    final int dx;
    final int dy;

    Direction(int dx, int dy)
    {
        this.dx = dx;
        this.dy = dy;
    }

    public static float[] move1tile(float fromX, float fromY, Direction to)
    {
        fromX += to.dx;
        fromY += to.dy;
        return new float[] { fromX, fromY };
    }

    public static float[] move2tiles(float fromX, float fromY, Direction to)
    {
        fromX += to.dx + to.dx;
        fromY += to.dy + to.dy;
        return new float[] { fromX, fromY };
    }

    public static Direction calculateDirection(float fromX, float fromY,
                                               float toX, float toY)
    {
        String d = "";
        if(toY > fromY)
        {
            d += "UP";
        }
        else if(toY < fromY)
        {
            d += "DOWN";
        }

        if(toX > fromX)
        {
            d += "RIGHT";
        }
        else if(toX < fromX)
        {
            d += "LEFT";
        }

        return Direction.valueOf(d);
    }
}
