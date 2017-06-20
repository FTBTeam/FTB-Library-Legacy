package com.feed_the_beast.ftbl.lib.tile;

import com.feed_the_beast.ftbl.lib.math.BlockDimPos;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nullable;

public class TileBase extends TileEntity
{
	private boolean isDirty = true;
	private IBlockState currentState;

	protected void writeData(NBTTagCompound nbt, EnumSaveType type)
	{
	}

	protected void readData(NBTTagCompound nbt, EnumSaveType type)
	{
	}

	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt = super.writeToNBT(nbt);
		writeData(nbt, EnumSaveType.SAVE);
		return nbt;
	}

	@Override
	public final void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		readData(nbt, EnumSaveType.SAVE);
	}

	@Override
	@Nullable
	public final SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbt = super.writeToNBT(new NBTTagCompound());
		writeData(nbt, EnumSaveType.NET_UPDATE);
		nbt.removeTag("id");
		nbt.removeTag("x");
		nbt.removeTag("y");
		nbt.removeTag("z");
		return nbt.hasNoTags() ? null : new SPacketUpdateTileEntity(pos, 0, nbt);
	}

	@Override
	public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readData(pkt.getNbtCompound(), EnumSaveType.NET_UPDATE);
		onUpdatePacket();
	}

	@Override
	public final NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = super.getUpdateTag();
		writeData(nbt, EnumSaveType.NET_FULL);
		nbt.removeTag("id");
		return nbt;
	}

	@Override
	public final void handleUpdateTag(NBTTagCompound tag)
	{
		readData(tag, EnumSaveType.NET_FULL);
		onUpdatePacket();
	}

	public void onUpdatePacket()
	{
		markDirty();
	}

	protected boolean notifyBlock()
	{
		return true;
	}

	protected boolean updateComparator()
	{
		return false;
	}

	@Override
	public void onLoad()
	{
		isDirty = true;
	}

	@Override
	public void markDirty()
	{
		isDirty = true;
	}

	public final void checkIfDirty()
	{
		if (isDirty)
		{
			sendDirtyUpdate();
			isDirty = false;
		}
	}

	@Override
	public final Block getBlockType()
	{
		return getBlockState().getBlock();
	}

	protected void sendDirtyUpdate()
	{
		updateContainingBlockInfo();

		if (world != null)
		{
			world.markChunkDirty(pos, this);

			if (notifyBlock())
			{
				world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 255);
			}

			if (updateComparator() && getBlockType() != Blocks.AIR)
			{
				world.updateComparatorOutputLevel(pos, getBlockType());
			}
		}
	}

	@Override
	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		currentState = null;
	}

	public IBlockState createState(IBlockState state)
	{
		return state;
	}

	public IBlockState getBlockState()
	{
		if (currentState == null)
		{
			if (world == null)
			{
				return Blocks.AIR.getDefaultState();
			}

			currentState = createState(world.getBlockState(getPos()));
		}

		return currentState;
	}

	public final boolean isServerSide()
	{
		return world != null && !world.isRemote;
	}

	public void notifyNeighbors()
	{
		world.notifyNeighborsOfStateChange(getPos(), getBlockType(), false);
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