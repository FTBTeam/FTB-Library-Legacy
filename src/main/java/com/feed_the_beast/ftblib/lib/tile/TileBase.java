package com.feed_the_beast.ftblib.lib.tile;

import com.feed_the_beast.ftblib.lib.math.BlockDimPos;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileBase extends TileEntity implements IWorldNameable
{
	private boolean isDirty = true;
	private IBlockState currentState;
	public boolean destroyedByCreativePlayer = false;

	protected abstract void writeData(NBTTagCompound nbt, EnumSaveType type);

	protected abstract void readData(NBTTagCompound nbt, EnumSaveType type);

	@Override
	public String getName()
	{
		return getDisplayName().getFormattedText();
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	@Nonnull
	public ITextComponent getDisplayName()
	{
		return new TextComponentTranslation(getBlockType().getUnlocalizedName() + ".name");
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
		onUpdatePacket(EnumSaveType.NET_UPDATE);
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
		onUpdatePacket(EnumSaveType.NET_FULL);
	}

	public void onUpdatePacket(EnumSaveType type)
	{
		markDirty();
	}

	protected boolean notifyBlock()
	{
		return true;
	}

	public boolean updateComparator()
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

	public boolean shouldDrop()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeData(nbt, EnumSaveType.SAVE);
		return !nbt.hasNoTags();
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

	@Override
	public final int getBlockMetadata()
	{
		return getBlockState().getBlock().getMetaFromState(getBlockState());
	}

	protected void sendDirtyUpdate()
	{
		updateContainingBlockInfo();

		if (world != null)
		{
			world.markChunkDirty(pos, this);

			if (notifyBlock())
			{
				CommonUtils.notifyBlockUpdate(world, pos, getBlockState());
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

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		updateContainingBlockInfo();
		return oldState.getBlock() != newSate.getBlock();
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
				return CommonUtils.AIR_STATE;
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

	public NBTTagCompound createItemData()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound nbt1 = new NBTTagCompound();
		writeData(nbt1, EnumSaveType.SAVE);

		if (!nbt1.hasNoTags())
		{
			nbt.setTag("BlockEntityTag", nbt1);
		}

		return nbt;
	}
}