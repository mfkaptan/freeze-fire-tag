package com.kaptan.freezefiretag.entities;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.kaptan.freezefiretag.entities.blocks.Block;
import com.kaptan.freezefiretag.entities.blocks.FireBlock;
import com.kaptan.freezefiretag.entities.blocks.IceBlock;
import com.kaptan.freezefiretag.entities.blocks.MoveableBlock;
import com.kaptan.freezefiretag.entities.blocks.RangeBlock;
import com.kaptan.freezefiretag.entities.blocks.pools.BlockPools;
import com.kaptan.freezefiretag.util.Constants;
import com.kaptan.freezefiretag.util.Direction;
import com.kaptan.freezefiretag.util.Status;
import com.kaptan.freezefiretag.util.Turn;


public class Board extends Actor
{
    /* Board's tiled map */
    private TiledMap map;
    private TiledMapRenderer renderer;
    private MapLayers mapLayers;
    /* Tile array which holds every tile */
    private Tile[][] tiles;
    /* A BlockPool for every block */
    private BlockPools blockPools;
    /* Add range group separately for cleaning up easily */
    private Group blockGroup, rangeGroup;
    /* Width x Height */
    private int boardCols, boardRows;
    private Turn turn;
    private final int oriX = 1, oriY = 1;
    /* Temp */
    private RangeBlock tempRange;
    private FireBlock tempFire;
    private Array<Tile> neighbours = new Array<Tile>();
    private Array<FireBlock> frozenBlocks = new Array<FireBlock>();
    private Array<IceBlock> iceBlocks = new Array<IceBlock>();

    /**
     * Holds board information
     * 
     * @param width
     *            Column number of the board
     * @param height
     *            Row number of the board
     * @param fg
     *            Reference to foreground
     * @param camera
     *            Camera of the stage
     */
    public Board(int width, int height, Group fg, Camera camera)
    {
        this.boardCols = width;
        this.boardRows = height;
        setOrigin(1, 1);

        /* Add range group to fg first */
        rangeGroup = new Group();
        fg.addActor(rangeGroup);

        /* Add block group to fg */
        blockGroup = new Group();
        fg.addActor(blockGroup);

        loadMap(camera, "t1.tmx");
        createBoard();
        blockPools = new BlockPools(this);
        neighbours = new Array<Tile>();
        turn = Turn.FIRE;
    }

    @Override
    public void act(float delta)
    {
        if(turn.hasChanged)
        {
            switch(turn)
            {
                case FIRE:
                    mapLayers.get(Constants.FIRE).setVisible(true);
                    mapLayers.get(Constants.ICE).setVisible(false);
                    break;
                case ICE:
                    mapLayers.get(Constants.ICE).setVisible(true);
                    mapLayers.get(Constants.FIRE).setVisible(false);
                    break;
            }
            turn.hasChanged = false;

            Iterator<FireBlock> iter = frozenBlocks.iterator();
            while(iter.hasNext())
            {
                tempFire = iter.next();
                /* Search all neighbors */
                for(Tile t : getNeighbours(tempFire.getX(), tempFire.getY()))
                {
                    if(t.getStatus() == Status.FIRE)
                    {
                        /* If there is a fire neighbor, unfreeze*/
                        tempFire.unfreeze();
                        setTileStatus(tempFire.getX(),
                                      tempFire.getY(),
                                      Status.FIRE);
                        /* Remove it from the list*/
                        iter.remove();
                        break;
                    }
                }
            }

            /* Freeze blocks */
            /* Search all neighbors */
            for(IceBlock iceBlock : iceBlocks)
            {
                for(Tile t : getNeighbours(iceBlock.getX(), iceBlock.getY()))
                {
                    if(t.getStatus() == Status.FIRE)
                    {
                        /* If there is a fire neighbor, freeze it*/
                        tempFire = (FireBlock) getBlock(t.posX, t.posY);
                        tempFire.freeze();
                        t.setStatus(Status.FROZEN);
                        /* Add it to frozenblocks */
                        frozenBlocks.add(tempFire);
                    }
                }
            }
            // debugLog();
        }
    }

    public void makeMove()
    {
        moveAndClear();
        switchTurn();
    }

    public Turn getTurn()
    {
        return turn;
    }

    public void switchTurn()
    {
        switch(turn)
        {
            case FIRE:
                turn = Turn.ICE;
                break;
            case ICE:
                turn = Turn.FIRE;
                break;
        }
        turn.hasChanged = true;
    }

    private void loadMap(Camera camera, String mapName)
    {
        map = new TmxMapLoader().load(mapName);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.BLOCK_SIZE);
        renderer.setView((OrthographicCamera) camera);
        mapLayers = map.getLayers();
    }

    /* Create each tile and save it to tiles array. */
    private void createBoard()
    {
        tiles = new Tile[boardRows][boardCols];
        for(int row = 0; row < boardRows; row++)
        {
            for(int col = 0; col < boardCols; col++)
            {
                /*
                 * Since opengl's (0,0) is bottom left and out Tile[][]'s (0,0)
                 * is top left, we have to convert row (y) values as this.
                 * Ex: for Tile[3][3]:
                 * 
                 * [0][0] [0][1] [0][2]    [2][0] [2][1] [2][2]
                 * [1][0] [1][1] [1][2] is [1][0] [1][1] [1][2]
                 * [2][0] [2][1] [2][2]    [0][0] [0][1] [0][2]
                 *
                 * So [2][1] should be [3-2-1][1]
                 **/
                tiles[row][col] = new Tile(col, boardRows - row - 1);
            }
        }
    }

    private Tile getTile(float col, float row)
    {
        // boardRows -(row - oriY)) - 1 = boardRows - row
        return tiles[(int) (boardRows - row)][(int) (col - oriX)];
    }

    public Status getTileStatus(float col, float row)
    {
        return getTile(col, row).getStatus();
    }

    public void setTileStatus(float col, float row, Status status)
    {
        getTile(col, row).setStatus(status);
    }

    private Block getBlock(float col, float row)
    {
        for(Actor a : blockGroup.getChildren())
        {
            if(a.getX() == col && a.getY() == row)
                return (Block) a;
        }
        return null;
    }

    /*** Range Functions ***/

    private Array<Tile> getNeighbours(float col, float row)
    {
        neighbours.clear();
        int startPosX = (int) (col == oriX ? col : col - 1);
        int startPosY = (int) (row == oriY ? row : row - 1);
        int endPosX = (int) (col == boardCols ? col : col + 1);
        int endPosY = (int) (row == boardRows ? row : row + 1);

        for(int rowNum = startPosY; rowNum <= endPosY; rowNum++)
        {
            for(int colNum = startPosX; colNum <= endPosX; colNum++)
            {
                // All the neighbors will be grid[rowNum][colNum]
                neighbours.add(getTile(colNum, rowNum));
            }
        }

        return neighbours;
    }

    public void showRange(MoveableBlock blk)
    {
        for(Tile t : getNeighbours(blk.getX(), blk.getY()))
        {
            if(t.getStatus() == Status.EMPTY)
            {
                tempRange = blockPools.rangePool.obtain();
                tempRange.init(blk, t.posX, t.posY);
                setTileStatus(tempRange.getX(), tempRange.getY(), Status.RANGE);
                blk.rangeGroup.addActor(tempRange);
            }
        }

        if(getTile(blk.getX(), blk.getY()).getStatus() == Status.ICE)
        {
            float[] mv = Direction.move2tiles(blk.getX(),
                                              blk.getY(),
                                              blk.getDirection());

            if(mv[0] >= oriX && mv[0] <= boardCols && mv[1] >= oriY
               && mv[1] <= boardRows)
            {
                tempRange = blockPools.rangePool.obtain();
                tempRange.init(blk, mv[0], mv[1]);
                setTileStatus(tempRange.getX(), tempRange.getY(), Status.RANGE);
                blk.rangeGroup.addActor(tempRange);
            }
        }
        rangeGroup.addActor(blk.rangeGroup);
    }

    /**
     * Clears a MoveableBlock's range
     * 
     * @param blk
     *            MoveableBlock's range will be removed
     */
    public void clearRange(MoveableBlock blk)
    {
        for(Actor r : blk.rangeGroup.getChildren())
        {
            blockPools.rangePool.free((RangeBlock) r);
        }
        blk.rangeGroup.clearChildren();
        rangeGroup.removeActor(blk.rangeGroup);
    }

    /**
     * Clears a MoveableBlock's range except the selected one.
     * Sets the MoveableBlock's selectedRange
     * 
     * @param blk
     *            MoveableBlock that is going to move
     */
    public void clearExceptSelected(MoveableBlock blk)
    {
        Iterator<Actor> iter = blk.rangeGroup.getChildren().iterator();
        while(iter.hasNext())
        {
            tempRange = (RangeBlock) iter.next();
            if(tempRange.isSelected())
            {
                blk.selectedRange = tempRange;
            }
            else
            {
                blockPools.rangePool.free(tempRange);
                iter.remove();
            }
        }
    }

    private void moveAndClear()
    {
        for(Actor r : blockGroup.getChildren())
        {
            clearRange((MoveableBlock) r);
            if(((MoveableBlock) r).selectedRange != null)
            {
                ((MoveableBlock) r).moveToSelected();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        batch.end();
        renderer.render();
        batch.begin();
    }

    public void addIce(float x, float y)
    {
        IceBlock ice = blockPools.icePool.obtain();
        ice.setPosition(x, y);
        blockGroup.addActor(ice);
        iceBlocks.add(ice);
    }

    public void addFire(float x, float y)
    {
        FireBlock fire = blockPools.firePool.obtain();
        fire.setPosition(x, y);
        blockGroup.addActor(fire);
    }

    /* Debugging purposes */
    public void debugLog()
    {
        for(int row = 0; row < boardRows; row++)
        {
            for(int col = 0; col < boardCols; col++)
            {
                switch(tiles[row][col].getStatus())
                {
                    case EMPTY:
                        System.out.print("- ");
                        break;
                    case ICE:
                        System.out.print("I ");
                        break;
                    case FIRE:
                        System.out.print("F ");
                        break;
                    case FROZEN:
                        System.out.print("0 ");
                        break;
                    case RANGE:
                        System.out.print("_ ");
                        break;
                    case SELECTED:
                        System.out.print("= ");
                        break;
                    default:
                        break;
                }
            }
            System.out.println();
        }
    }

    public void dispose()
    {
        map.dispose();
        blockPools.dispose();
        neighbours.clear();
    }

}
