package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.ConfigValueProvider;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 30.08.2016.
 */
public class PropertyBlockState extends PropertyBase
{
    public static final String ID = "blockstate";

    @ConfigValueProvider(ID)
    public static final IConfigValueProvider PROVIDER = () -> new PropertyBlockState(Blocks.AIR.getDefaultState());

    private IBlockState value;

    public PropertyBlockState(IBlockState state)
    {
        value = state;
    }

    @Override
    public String getID()
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
    public NBTBase serializeNBT()
    {
        return new NBTTagShort((short) getInt());
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        setBlockState(Block.getStateById(((NBTPrimitive) nbt).getShort() & 0xFFFF));
    }

    @Override
    public void fromJson(JsonElement o)
    {
        value = Blocks.AIR.getDefaultState();

        if(o.isJsonPrimitive())
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
    public void writeData(ByteBuf data, boolean extended)
    {
        data.writeShort(getInt());
    }

    @Override
    public void readData(ByteBuf data, boolean extended)
    {
        setBlockState(Block.getStateById(data.readUnsignedShort()));
    }
}