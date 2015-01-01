package com.kaptan.freezefiretag.entities;

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
    private final String FIRE = "FireTurn";
    private final String ICE = "IceTurn";
    private final int oriX = 1, oriY = 1;
    /* Temp */
    private RangeBlock tempRange;
    private Array<Tile> neighbours;

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
        turn = Turn.fire;
    }

    @Override
    public void act(float delta)
    {
        if(turn.hasChanged)
        {
            switch(turn)
            {
                case fire:
                    mapLayers.get(FIRE).setVisible(true);
                    mapLayers.get(ICE).setVisible(false);
                    break;
                case ice:
                    mapLayers.get(ICE).setVisible(true);
                    mapLayers.get(FIRE).setVisible(false);
                    break;
            }
            turn.hasChanged = false;
        }
    }

    public Turn getTurn()
    {
        return turn;
    }

    private void switchTurn()
    {
        switch(turn)
        {
            case fire:
                turn = Turn.ice;
                break;
            case ice:
                turn = Turn.fire;
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
            if(t.getStatus() == Status.empty || t.getStatus() == Status.free)
            {
                tempRange = blockPools.rangePool.obtain();
                tempRange.init(blk, t.posX, t.posY);
                setTileStatus(tempRange.getX(), tempRange.getY(), Status.range);
                blk.rangeGroup.addActor(tempRange);
            }
        }

        if(getTile(blk.getX(), blk.getY()).getStatus() == Status.ice)
        {
            float[] mv = Direction.move2tiles(blk.getX(),
                                              blk.getY(),
                                              blk.getDirection());

            if(mv[0] >= oriX && mv[0] <= boardCols && mv[1] >= oriY
               && mv[1] <= boardRows)
            {
                tempRange = blockPools.rangePool.obtain();
                tempRange.init(blk, mv[0], mv[1]);
                setTileStatus(tempRange.getX(), tempRange.getY(), Status.range);
                blk.rangeGroup.addActor(tempRange);
            }
        }
        rangeGroup.addActor(blk.rangeGroup);
    }

    public void clearRange(MoveableBlock blk)
    {
        for(Actor r : blk.rangeGroup.getChildren())
        {
            blockPools.rangePool.free((RangeBlock) r);
            setTileStatus(r.getX(), r.getY(), Status.empty);
        }
        blk.rangeGroup.clearChildren();
        rangeGroup.removeActor(blk.rangeGroup);
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
    }

    public void addFire(float x, float y)
    {
        FireBlock fire = blockPools.firePool.obtain();
        fire.setPosition(x, y);
        blockGroup.addActor(fire);
    }

    /* Debugging purposes */
    public void boardToString()
    {
        for(int row = 0; row < boardRows; row++)
        {
            for(int col = 0; col < boardCols; col++)
            {
                System.out.print(tiles[row][col].status.toString() + " ");
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
