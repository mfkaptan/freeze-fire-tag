package com.kaptan.freezefiretag.entities.blocks.pools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;
import com.kaptan.freezefiretag.entities.Board;
import com.kaptan.freezefiretag.entities.blocks.FireBlock;
import com.kaptan.freezefiretag.entities.blocks.IceBlock;
import com.kaptan.freezefiretag.entities.blocks.RangeBlock;


public class BlockPools
{
    Texture iceTexture = new Texture(Gdx.files.internal("img/ice.png"));
    Texture rangeTexture = new Texture(Gdx.files.internal("img/range.png"));
    public static Texture fireTexture = new Texture(Gdx.files.internal("img/fire.png"));
    public static Texture frozenTexture = new Texture(Gdx.files.internal("img/frozen.png"));

    public final Pool<IceBlock> icePool;
    public final Pool<FireBlock> firePool;
    public final Pool<RangeBlock> rangePool;

    public BlockPools(final Board board)
    {
        icePool = new Pool<IceBlock>()
        {
            @Override
            protected IceBlock newObject()
            {
                return new IceBlock(iceTexture, board);
            }
        };

        firePool = new Pool<FireBlock>()
        {
            protected FireBlock newObject()
            {
                return new FireBlock(fireTexture, board);
            }
        };
        rangePool = new Pool<RangeBlock>()
        {
            protected RangeBlock newObject()
            {
                return new RangeBlock(rangeTexture, board);
            }
        };
    }

    public void dispose()
    {
        fireTexture.dispose();
        iceTexture.dispose();
        frozenTexture.dispose();
        rangeTexture.dispose();
    }
}
