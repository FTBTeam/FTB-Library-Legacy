package com.latmod.lib.config;

import com.feed_the_beast.ftbl.api.config.ConfigValueProvider;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by LatvianModder on 30.08.2016.
 */
public class PropertyBlockState extends PropertyBase
{
    public static final String ID = "blockstate";

    @ConfigValueProvider(ID)
    public static final IConfigValueProvider PROVIDER = () -> new PropertyBlockState(Blocks.AIR.getDefaultState());

    private IBlockState blockState;

    public PropertyBlockState(IBlockState state)
    {
        blockState = state;
    }

    public IBlockState getBlockState()
    {
        return blockState;
    }

    public void setBlockState(IBlockState state)
    {
        blockState = state;
    }

    @Override
    public String getID()
    {
        return ID;
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
        blockState = Blocks.AIR.getDefaultState();

        if(o.isJsonPrimitive())
        {
            blockState = Block.REGISTRY.getObject(new ResourceLocation(o.getAsString())).getDefaultState();
        }

        setBlockState(blockState);
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(Block.REGISTRY.getNameForObject(getBlockState().getBlock()).toString());
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
    public void writeData(DataOutput data, boolean extended) throws IOException
    {
        data.writeShort(getInt());
    }

    @Override
    public void readData(DataInput data, boolean extended) throws IOException
    {
        setBlockState(Block.getStateById(data.readUnsignedShort()));
    }
}