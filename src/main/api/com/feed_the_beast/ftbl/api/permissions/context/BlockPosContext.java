package com.feed_the_beast.ftbl.api.permissions.context;

import com.google.common.base.Preconditions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import javax.annotation.Nullable;

public class BlockPosContext extends PlayerContext
{
    private final BlockPos blockPos;
    private IBlockState blockState;
    private EnumFacing facing;

    public BlockPosContext(EntityPlayer ep, BlockPos pos, @Nullable IBlockState state, @Nullable EnumFacing f)
    {
        super(ep);
        blockPos = Preconditions.checkNotNull(pos, "BlockPos can't be null in BlockPosContext!");
        blockState = state;
        facing = f;
    }

    public BlockPosContext(EntityPlayer ep, ChunkPos pos)
    {
        this(ep, new BlockPos((pos.chunkXPos << 4) + 8, 0, (pos.chunkZPos << 4) + 8), null, null);
    }

    @Override
    @Nullable
    public <T> T get(ContextKey<T> key)
    {
        if(key.equals(ContextKeys.POS))
        {
            return (T) blockPos;
        }
        else if(key.equals(ContextKeys.BLOCK_STATE))
        {
            if(blockState == null)
            {
                blockState = getWorld().getBlockState(blockPos);
            }

            return (T) blockState;
        }
        else if(key.equals(ContextKeys.FACING))
        {
            return (T) facing;
        }

        return super.get(key);
    }

    @Override
    protected boolean covers(ContextKey<?> key)
    {
        return key.equals(ContextKeys.POS) || key.equals(ContextKeys.BLOCK_STATE) || (facing != null && key.equals(ContextKeys.FACING));
    }
}