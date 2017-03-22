package com.feed_the_beast.ftbl.lib.tile;

import com.feed_the_beast.ftbl.lib.math.BlockDimPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nullable;

public class TileLM extends TileEntity
{
    private boolean isDirty = true;
    private IBlockState currentState;

    @Override
    public final NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        writeTileData(tag);
        return tag;
    }

    @Override
    public final void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        readTileData(tag);
    }

    @Override
    @Nullable
    public final SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public final NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeTileClientData(tag);
        return tag;
    }

    @Override
    public final void onDataPacket(NetworkManager m, SPacketUpdateTileEntity p)
    {
        handleUpdateTag(p.getNbtCompound());
    }

    @Override
    public final void handleUpdateTag(NBTTagCompound tag)
    {
        readTileClientData(tag);
        onUpdatePacket();
    }

    public void writeTileData(NBTTagCompound nbt)
    {
    }

    public void readTileData(NBTTagCompound nbt)
    {
    }

    public void writeTileClientData(NBTTagCompound nbt)
    {
    }

    public void readTileClientData(NBTTagCompound nbt)
    {
    }

    protected boolean rerenderBlock()
    {
        return false;
    }

    protected boolean updateComparator()
    {
        return false;
    }

    protected void onUpdatePacket()
    {
        if(rerenderBlock())
        {
            IBlockState state = getBlockState();
            world.notifyBlockUpdate(pos, state, state, 7);
        }
    }

    @Override
    public void onLoad()
    {
        isDirty = true;
    }

    @Override
    public void onChunkUnload()
    {
    }

    @Override
    public void markDirty()
    {
        isDirty = true;
    }

    public final void checkIfDirty()
    {
        if(isDirty)
        {
            sendDirtyUpdate();
            isDirty = false;
        }
    }

    protected void sendDirtyUpdate()
    {
        if(world != null && !world.isRemote)
        {
            updateContainingBlockInfo();
            world.markChunkDirty(getPos(), this);

            /*
            if(updateComparator() && getBlockType() != Blocks.AIR)
            {
                worldObj.updateComparatorOutputLevel(getPos(), getBlockType());
            }
            */
        }
    }

    @Override
    public void updateContainingBlockInfo()
    {
        super.updateContainingBlockInfo();
        currentState = null;
    }

    public void onNeighborChange()
    {
        updateContainingBlockInfo();
    }

    public IBlockState getBlockState()
    {
        if(currentState == null)
        {
            currentState = world.getBlockState(getPos());
        }

        return currentState;
    }

    public final boolean isServerSide()
    {
        return world != null && !world.isRemote;
    }

    public void notifyNeighbors()
    {
        world.notifyBlockOfStateChange(getPos(), getBlockType());
    }

    public void playSound(SoundEvent event, SoundCategory category, float volume, float pitch)
    {
        world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, event, category, volume, pitch);
    }

    public BlockDimPos getDimPos()
    {
        return new BlockDimPos(pos, hasWorld() ? world.provider.getDimension() : 0);
    }
}