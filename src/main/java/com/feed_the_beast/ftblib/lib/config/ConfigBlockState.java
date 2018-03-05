package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class ConfigBlockState extends ConfigValue
{
	public static final String ID = "blockstate";

	private IBlockState value;

	public ConfigBlockState()
	{
		this(CommonUtils.AIR_STATE);
	}

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
	public Object getValue()
	{
		return getBlockState();
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
	public void fromJson(JsonElement o)
	{
		value = CommonUtils.AIR_STATE;

		if (o.isJsonPrimitive())
		{
			value = Block.REGISTRY.getObject(new ResourceLocation(o.getAsString())).getDefaultState();
		}

		setBlockState(value);
	}

	@Override
	public JsonElement getSerializableElement()
	{
		return new JsonPrimitive(Block.REGISTRY.getNameForObject(getBlockState().getBlock()).toString());
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
}