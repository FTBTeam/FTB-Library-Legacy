package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class PropertyBlockState extends PropertyBase
{
	public static final String ID = "blockstate";
	public static final IBlockState AIR_STATE = Blocks.AIR.getDefaultState();

	private IBlockState value;

	public PropertyBlockState()
	{
		this(AIR_STATE);
	}

	public PropertyBlockState(IBlockState state)
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

	@Nullable
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
		return getBlockState() != Blocks.AIR.getDefaultState();
	}

	@Override
	public int getInt()
	{
		return Block.getStateId(getBlockState());
	}

	@Override
	public IConfigValue copy()
	{
		return new PropertyBlockState(getBlockState());
	}

	@Override
	public void fromJson(JsonElement o)
	{
		value = Blocks.AIR.getDefaultState();

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
	public void writeData(ByteBuf data)
	{
		data.writeShort(getInt());
	}

	@Override
	public void readData(ByteBuf data)
	{
		setBlockState(Block.getStateById(data.readUnsignedShort()));
	}
}