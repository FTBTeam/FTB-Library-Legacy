package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class ConfigBlockState extends ConfigValue
{
	public static final String ID = "blockstate";

	private IBlockState value;

	public ConfigBlockState(IBlockState state)
	{
		value = state;
	}

	@Override
	public String getName()
	{
		return ID;
	}

	public IBlockState getBlockState()
	{
		return value;
	}

	public void setBlockState(IBlockState state)
	{
		value = state;
	}

	@Override
	public String getString()
	{
		return getBlockState().toString();
	}

	@Override
	public boolean getBoolean()
	{
		return getBlockState() != CommonUtils.AIR_STATE;
	}

	@Override
	public int getInt()
	{
		return Block.getStateId(getBlockState());
	}

	@Override
	public ConfigBlockState copy()
	{
		return new ConfigBlockState(getBlockState());
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String key)
	{
		nbt.setString(key, Block.REGISTRY.getNameForObject(getBlockState().getBlock()).toString());
		//TODO: Blockstate properties
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String key)
	{
		String id = nbt.getString(key);
		setBlockState(id.isEmpty() ? CommonUtils.AIR_STATE : Block.REGISTRY.getObject(new ResourceLocation(id)).getDefaultState());
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeBlockState(getBlockState());
	}

	@Override
	public void readData(DataIn data)
	{
		setBlockState(data.readBlockState());
	}

	@Override
	public void setValueFromOtherValue(ConfigValue value)
	{
		if (value instanceof ConfigBlockState)
		{
			setBlockState(((ConfigBlockState) value).getBlockState());
		}
		else
		{
			super.setValueFromOtherValue(value);
		}
	}
}