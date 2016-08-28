package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IPackMode;
import com.feed_the_beast.ftbl.api.ISharedData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.util.LMStringUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IJsonSerializable;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class SharedData implements ISharedData, IJsonSerializable
{
    private final Side side;
    private IPackMode currentMode;
    private UUID worldID;

    public SharedData(Side s)
    {
        side = s;
    }

    @Override
    public Side getSide()
    {
        return side;
    }

    @Override
    public IPackMode getMode()
    {
        if(currentMode == null)
        {
            currentMode = side.isClient() ? new PackMode("default") : FTBLibAPI.get().getPackModes().getDefault();
        }

        return currentMode;
    }

    @Override
    public UUID getUUID()
    {
        if(worldID == null || (worldID.getLeastSignificantBits() == 0L && worldID.getMostSignificantBits() == 0L))
        {
            worldID = UUID.randomUUID();
        }

        return worldID;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("M", getMode().getID());
        nbt.setLong("ID_M", getUUID().getMostSignificantBits());
        nbt.setLong("ID_L", getUUID().getLeastSignificantBits());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        currentMode = new PackMode(nbt.getString("M"));
        worldID = new UUID(nbt.getLong("ID_M"), nbt.getLong("ID_L"));
    }

    public final int setMode(String mode)
    {
        IPackMode m = FTBLibAPI.get().getPackModes().getRawMode(mode);

        if(m == null)
        {
            return 1;
        }
        if(m.equals(getMode()))
        {
            return 2;
        }

        currentMode = m;
        return 0;
    }

    @Override
    public void fromJson(JsonElement json)
    {
        JsonObject group = json.getAsJsonObject();
        worldID = group.has("world_id") ? LMStringUtils.fromString(group.get("world_id").getAsString()) : null;
        currentMode = group.has("mode") ? FTBLibAPI.get().getPackModes().getMode(group.get("mode").getAsString()) : FTBLibAPI.get().getPackModes().getDefault();
    }

    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = new JsonObject();
        o.add("world_id", new JsonPrimitive(LMStringUtils.fromUUID(getUUID())));
        o.add("mode", new JsonPrimitive(getMode().getID()));
        return o;
    }
}